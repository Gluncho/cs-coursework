#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <search.h>

#define Nth(v, n) ((void *)((char *)v->elems + n * v->elem_size))

static int kInitialAllocation;
static const int defaultAlloc = 100;
static void VectorGrow(vector *v);

void VectorNew(vector *v, int elemSize, VectorFreeFunction freeFn, int initialAllocation)
{
    assert(elemSize > 0);
    assert(initialAllocation >= 0);
    if (initialAllocation == 0)
    {
        kInitialAllocation = defaultAlloc;
    }
    else
        kInitialAllocation = initialAllocation;
    v->log_len = 0;
    v->alloc_len = kInitialAllocation;
    v->elem_size = elemSize;
    v->freefn = freeFn;
    v->elems = malloc(kInitialAllocation * elemSize);
    assert(v->elems != NULL);
}

void VectorDispose(vector *v)
{
    if (v->freefn)
    {
        for (int i = 0; i < (v->log_len); i++)
        {
            v->freefn(Nth(v, i));
        }
    }
    free(v->elems);
}

int VectorLength(const vector *v)
{
    return v->log_len;
}

void *VectorNth(const vector *v, int position)
{
    assert(position >= 0 && position < v->log_len);
    return (void *)((char *)v->elems + position * v->elem_size);
}

void VectorReplace(vector *v, const void *elemAddr, int position)
{
    assert(position >= 0 && position < v->log_len);
    if (v->freefn)
        v->freefn(Nth(v, position));
    memcpy(Nth(v, position), elemAddr, v->elem_size);
}

void VectorInsert(vector *v, const void *elemAddr, int position)
{
    if (v->log_len == v->alloc_len)
        VectorGrow(v);
    v->log_len++;
    int newPos = position + 1;
    memmove(Nth(v, newPos), Nth(v, position), (v->log_len - 1 - position) * v->elem_size);
    memcpy(Nth(v, position), elemAddr, v->elem_size);
}

static void VectorGrow(vector *v)
{
    // v->alloc_len += kInitialAllocation;
    v->alloc_len *= 2;
    v->elems = realloc(v->elems, v->alloc_len * v->elem_size);
    assert(v->elems != NULL);
}

void VectorAppend(vector *v, const void *elemAddr)
{
    if (v->log_len == v->alloc_len)
        VectorGrow(v);
    memcpy(Nth(v, v->log_len), elemAddr, v->elem_size);
    v->log_len++;
}

void VectorDelete(vector *v, int position)
{
    assert(position >= 0 && position <= v->log_len);
    if (v->freefn != NULL)
        v->freefn(Nth(v, position));
    int newPos = position + 1;
    memmove(Nth(v, position), Nth(v, newPos), (v->log_len - position - 1) * v->elem_size);
    v->log_len--;
}

void VectorSort(vector *v, VectorCompareFunction compare)
{
    assert(compare);
    qsort(v->elems, v->log_len, v->elem_size, compare);
}

void VectorMap(vector *v, VectorMapFunction mapFn, void *auxData)
{
    assert(mapFn);
    for (int i = 0; i < (v->log_len); i++)
    {
        mapFn(Nth(v, i), auxData);
    }
}

static const int kNotFound = -1;
int VectorSearch(const vector *v, const void *key, VectorCompareFunction searchFn, int startIndex, bool isSorted)
{
    assert(searchFn);
    if (v->log_len == 0)
        return -1;
    assert(startIndex >= 0 && startIndex < v->log_len);
    assert(key != NULL);
    void *found = NULL;
    if (isSorted)
    {
        found = bsearch(key, VectorNth(v, startIndex), v->log_len, v->elem_size, searchFn);
    }
    else
    {
        for (int i = startIndex; i < v->log_len; i++)
        {
            if (searchFn(key, VectorNth(v, i)) == 0)
            {
                found = VectorNth(v, i);
                break;
            }
        }
    }
    if (found)
        return ((char *)found - (char *)v->elems) / v->elem_size;
    return kNotFound;
}
