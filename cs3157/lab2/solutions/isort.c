
/*
 * isort: sorting an integer array
 *
 * Note that this is not a complete solution for isort.
 * It doesn't do everything the problem asked you to do.
 * The main purpose of this code is to show you how to use qsort().
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

static int compare_ascending(const void *x, const void *y)
{
    return *(int *)x - *(int *)y;
}

int main()
{
    int n;
    scanf("%d", &n);
    int *p = malloc(n * sizeof(int));
    
    // uncomment the following line to generate different sequence of
    // random numbres:
    //
    // srandom(time(NULL));

    // fill the array with random integers
    int i;
    for (i = 0; i < n; i++)
        p[i] = random();

    // print the array
    printf("original array:\n");
    for (i = 0; i < n; i++)
        printf("%d ", p[i]);
    printf("\n");

    // sort the array using qsort library function whose prototype
    // looks like this:
    //
    //   void qsort(void *base, size_t nmemb, size_t size,
    //              int (*compar)(const void *, const void *)
    //             );
    //
    qsort(p, n, sizeof(p[0]), &compare_ascending);
    
    // print the sorted array
    printf("sorted array:\n");
    for (i = 0; i < n; i++)
        printf("%d ", p[i]);
    printf("\n");

    free(p);
    return 0;
}

