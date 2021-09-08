package quicksort

import kotlin.system.measureTimeMillis

fun main() {
    val asort = ArraySorter()
    val averify = ArrayVerifier()

    // * Configuration
    var n = 2_000_000
    val factor = 2
    val runs = 3
    val min = 1
    val max = 100_000_000

    for (i in 1..runs) {
        // * Make two arrays with the same random content.
        val a = Array(n) { (min..max).random() }
        val b = a.copyOf()

        // * Get the checksum of the array before sorting.
        val checksumA = averify.checksum(a)
        val checksumB = averify.checksum(b)

        // * Sort the arrays and record the time spent.
        val runtimeA = measureTimeMillis {
            asort.quicksort(a, 0, a.lastIndex)
        }
        val runtimeB = measureTimeMillis {
            asort.quicksort(b, 0, b.lastIndex, improved = true)
        }

        // * Check arrays are correctly sorted and checksums match.
        val checksumCorrectA = checksumA == averify.checksum(a)
        val checksumCorrectB = checksumB == averify.checksum(b)
        val sortCorrectA = averify.verify(a)
        val sortCorrectB = averify.verify(b)

        // * Print the result of the run.
        println("Ran with n=$n, min=$min, max=$max and got runtime $runtimeA ms. Checks ${if(checksumCorrectA && sortCorrectA) "succeeded." else "failed."}")
        println("Ran improved with n=$n, min=$min, max=$max and got runtime $runtimeB ms. Checks ${if(checksumCorrectB && sortCorrectB) "succeeded." else "failed."}")

        // * Increase n for next run.
        n *= factor
    }
}