package quicksort

fun main() {
    val a = arrayOf(10, 5, 4, 2, 12, 200)
    ArraySorter().quicksort(a, 0, 5)
    for (num in a) {
        print("$num, ")
    }
}

class ArraySorter {
    fun quicksort(a: Array<Int>, l: Int, r: Int) {
        if(r - l > 2) {
            val pivot = split(a, l, r)
            quicksort(a, l, pivot - 1)
            quicksort(a, pivot + 1, r)
        } else median3sort(a, l, r)
    }

    private fun split(a: Array<Int>, l: Int, r: Int) : Int {
        val m = median3sort(a, l, r)
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