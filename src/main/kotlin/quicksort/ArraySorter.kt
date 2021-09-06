package quicksort

fun main() {
    val a = arrayOf(10, 5, 4, 2, 12, 200)
    ArraySorter().quicksort(a, 0, 5)
    for (num in a) {
        print("$num, ")
    }
}

class ArraySorter {
    /**
     * Sorts the given array using the quicksort algorithm.
     * @param a Array to sort.
     * @param l Index to sort from.
     * @param r Index to sort to.
     * @param pi (Optional) Index of pivot element. Uses median3sort if not present.
     */
    fun quicksort(a: Array<Int>, l: Int, r: Int, pi: Int? = null) {
        if(r - l > 2) {
            val pivot = split(a, l, r, pi)
            quicksort(a, l, pivot - 1)
            quicksort(a, pivot + 1, r)
        } else median3sort(a, l, r)
    }

    private fun split(a: Array<Int>, l: Int, r: Int, pi : Int?) : Int {
        val m = pi ?: median3sort(a, l, r) // Uses median3sort if nothing else is defined.
        val pivot = a[m]

        var il = l
        var ir = r - 1

        swap(a, m, r - 1)

        while (true) {
            while(a[++il] < pivot);
            while(a[--ir] > pivot);
            if(il >= ir) break
            swap(a, il, ir)
        }

        swap(a, il, r - 1)

        return il
    }

    private fun median3sort(a: Array<Int>, l: Int, r: Int) : Int {
        val m: Int = (l + r) / 2

        if(a[l] > a[m]) {
            swap(a, l, m)
        }

        if(a[m] > a[r]) {
            swap(a, m, r)

            if(a[l] > a[m]) {
                swap(a, l, m)
            }
        }

        return m
    }

    private fun swap(a: Array<Int>, v1: Int, v2: Int) {
        val temp = a[v1]
        a[v1] = a[v2]
        a[v2] = temp
    }
}