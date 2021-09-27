package arraysort

class ArraySorter {
    /**
     * Sorts the given array using the quicksort algorithm.
     * @param a Array to sort.
     * @param l Index to sort from.
     * @param r Index to sort to.
     */
    fun quicksort(a: Array<Int>, l: Int, r: Int, improved: Boolean = false) {
        // * Run improved check if this option is enabled.
        if(improved && l > 0 && r < a.lastIndex && a[l - 1] == a[r + 1]) return

        // * Run normal quicksort if improved check is not used.
        if(r - l > 2) {
            val pivot = split(a, l, r)
            quicksort(a, l, pivot - 1, improved)
            quicksort(a, pivot + 1, r, improved)
        }
        else median3sort(a, l, r)
    }

    private fun split(a: Array<Int>, l: Int, r: Int) : Int {
        val m = median3sort(a, l, r)
        val pivot = a[m]

        var il = l
        var ir = r - 1

        swap(a, m, r - 1)

        // * Move numbers around until all numbers are correctly placed.
        while (true) {
            while(a[++il] < pivot);
            while(a[--ir] > pivot);
            if(il >= ir) break
            swap(a, il, ir)
        }

        swap(a, il, r - 1)

        return il
    }

    /**
     * Compare first, last and middle values to see which one would be the best median.
     */
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