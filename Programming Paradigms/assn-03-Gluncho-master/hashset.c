#include "hashset.h"
#include <assert.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

void HashSetNew(hashset *h, int elemSize, int numBuckets,
				HashSetHashFunction hashfn, HashSetCompareFunction comparefn, HashSetFreeFunction freefn)
{
	assert(elemSize > 0);
	assert(numBuckets > 0);
	assert(hashfn && comparefn);
	h->elemSize = sizeof(vector*);
	h->numBuckets = numBuckets;
	h->hashfn = hashfn;
	h->comparefn = comparefn;
	h->freefn = freefn;
	h->numElements = 0;
	h->buckets = malloc(numBuckets * h->elemSize);
	for (int i = 0; i < numBuckets; i++)
	{
		h->buckets[i] = malloc(sizeof(vector));
		VectorNew(h->buckets[i], elemSize, freefn, 8);
	}
}

void HashSetDispose(hashset *h)
{
	for (int i = 0; i < h->numBuckets; i++)
	{
		VectorDispose(h->buckets[i]);
		free(h->buckets[i]);
	}
	free(h->buckets);
}

int HashSetCount(const hashset *h)
{
	return h->numElements;
}

void HashSetMap(hashset *h, HashSetMapFunction mapfn, void *auxData)
{
	assert(mapfn);
	for (int i = 0; i < h->numBuckets; i++)
	{
		VectorMap(h->buckets[i], mapfn, auxData);
	}
}

void HashSetEnter(hashset *h, const void *elemAddr)
{
	assert(elemAddr);
	int index = h->hashfn(elemAddr, h->numBuckets);
	assert(index >= 0 && index < h->numBuckets);
	int position = VectorSearch(h->buckets[index], elemAddr, h->comparefn, 0, false);
	if (position == -1)
	{
		VectorAppend(h->buckets[index], elemAddr);
	}
	else
	{
		VectorReplace(h->buckets[index], elemAddr, position);
	}
}

void *HashSetLookup(const hashset *h, const void *elemAddr)
{
	assert(elemAddr);
	int index = h->hashfn(elemAddr, h->numBuckets);
	assert(index >= 0 && index < h->numBuckets);
	int pos = VectorSearch(h->buckets[index],elemAddr,h->comparefn,0,false);
	if(pos != -1) return VectorNth(h->buckets[index],pos);
	return NULL;
}
