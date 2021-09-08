package quicksort

class ArrayVerifier {
    /**
     * Verifies that all numbers in an array are in order.
     */
    fun verify(a: Array<Int>) : Boolean {
        var prev = Integer.MIN_VALUE

        for (i in (0..a.lastIndex)) {
            if (a[i] < prev) {
                return false
            }

            prev = a[i]
        }

        return true
    }

    /**
     * Returns the sum of all the elements of an array.
     */
    fun checksum(a: Array<Int>) : Long {
        var sum : Long = 0

        for (i in (0..a.lastIndex)) {
            sum += a[i]
        }

        return sum
    }
}