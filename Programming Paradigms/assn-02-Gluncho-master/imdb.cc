using namespace std;
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include "imdb.h"
#include <cstring>

const char *const imdb::kActorFileName = "actordata";
const char *const imdb::kMovieFileName = "moviedata";

imdb::imdb(const string& directory)
{
  const string actorFileName = directory + "/" + kActorFileName;
  const string movieFileName = directory + "/" + kMovieFileName;
  
  actorFile = acquireFileMap(actorFileName, actorInfo);
  movieFile = acquireFileMap(movieFileName, movieInfo);
}

bool imdb::good() const
{
  return !( (actorInfo.fd == -1) || 
	    (movieInfo.fd == -1) ); 
}

char* imdb::myBSearch(const char* key, const void* start, int (*cmpfn)(const char* c1, const char* c2)) const{
  int size= *(int*)(start);
  int* arr = (int*)start+1;
  int l=0,r=size-1;
  while(l<=r){
    int mid=l+(r-l)/2;
    char* infoStart = (char*)start+arr[mid];
    int cmp=cmpfn(infoStart,key);
    if(cmp>0){
     r=mid-1; 
    }else if(cmp<0){
      l=mid+1;
    }else return infoStart;
  }
  return NULL;
}

void imdb::addFilms(int sz, char* res, vector<film>& films) const {
  int* arr=(int*)res;
  int index=0;
  while(sz--){
    int offset=arr[index++];
    char* movies=(char*)movieFile;
    movies+=offset;
    string name="";
    while(*movies != '\0'){
      name+=(*movies);
      movies++;
    }
    movies++;
    int mLen=(int) *(unsigned char*)movies;
    film f={name,1900+mLen};
    films.push_back(f);
  }
}
// you should be implementing these two methods right here... 
bool imdb::getCredits(const string& player, vector<film>& films) const 
{
  const char* str=player.c_str();
  char* res=myBSearch((const char*)player.c_str(),actorFile,strcmp);
  char* saved=res;
  if(!res) return false;
  res+=player.size()+1;
  if((player.size() % 2) == 0) res++;
  short sz=*(short*)res;
  res+=2;
  if((res-saved)%4!=0)res+=2;
  addFilms(sz,res,films);
  return true; 
}

int cmpFilm(const char* f, const char* key){
  film keyFilm=*(film*)key;
  string title="";
  while(*f != '\0'){
    title+=(*f);
    f++;
  }
  f++;
  int flen=(int) *(unsigned char*)f;
  film arrFilm={title,1900+flen};
  if(arrFilm<keyFilm) return -1;
  if(arrFilm==keyFilm) return 0;
  return 1;
}

void imdb::addPlayers(short sz, char* res,vector<string>& players) const {
  int* arr=(int*)res;
  int index=0;
  while(sz--){
    int offset=arr[index++];
    char* names=(char*)actorFile;
    names+=offset;
    string name="";
    while(*names != '\0'){
      name+=(*names);
      names++;
    }
    players.push_back(name);
  }
}
bool imdb::getCast(const film& movie, vector<string>& players) const
{
  char* res=myBSearch((char*)&movie,movieFile,cmpFilm);
  char* saved=res;
  if(res == NULL) return false;
  int used=movie.title.size()+2;
  res+=used;
  if(used%2) res++;
  short sz=*(short*) res;
  res+=2;
  if((res-saved)%4!=0) res+=2;
  addPlayers(sz,res,players);
  return true;
}

imdb::~imdb()
{
  releaseFileMap(actorInfo);
  releaseFileMap(movieInfo);
}

// ignore everything below... it's all UNIXy stuff in place to make a file look like
// an array of bytes in RAM.. 
const void *imdb::acquireFileMap(const string& fileName, struct fileInfo& info)
{
  struct stat stats;
  stat(fileName.c_str(), &stats);
  info.fileSize = stats.st_size;
  info.fd = open(fileName.c_str(), O_RDONLY);
  return info.fileMap = mmap(0, info.fileSize, PROT_READ, MAP_SHARED, info.fd, 0);
}

void imdb::releaseFileMap(struct fileInfo& info)
{
  if (info.fileMap != NULL) munmap((char *) info.fileMap, info.fileSize);
  if (info.fd != -1) close(info.fd);
}
