package hashtable

import kotlin.math.abs

class LinearHashTable(maxSize : Int) : AbstractHashTable(maxSize) {
    override fun add(n : Int) : Boolean {
        val hash = multiHash(n)

        for (i in array.indices) {
            val probe = (hash + i) % size

            if (array[probe] == null) {
                array[probe] = n
                filled++
                return true
            }

            collisions++
        }

        return false
    }

    override fun find(n : Int) : Boolean {
        val hash = multiHash(n)

        for (i in array.indices) {
            val probe = (hash + i) % size

            if (array[probe] == n) {
                return true
            }
            else if (array[probe] == null) {
                return false
            }
        }

        return false
    }
}

class QuadraticHashTable(maxSize : Int) : AbstractHashTable(maxSize) {
    private val k1 = 3
    private val k2 = 5

    override fun add(n : Int) : Boolean {
        val hash = multiHash(n)

        for (i in array.indices) {
            val probe = (hash + ((k1 + k2 * i) * i)) % size

            if (array[probe] == null) {
                array[probe] = n
                filled++
                return true
            }

            collisions++
        }

        return false
    }

    override fun find(n : Int) : Boolean {
        val hash = multiHash(n)

        for (i in array.indices) {
            val probe = (hash + ((k1 + k2 * i) * i)) % size

            if (array[probe] == n) {
                return true
            }
            else if (array[probe] == null) {
                return false
            }
        }

        return false
    }
}

class DoubleHashTable(maxSize : Int) : AbstractHashTable(maxSize) {
    override fun add(n : Int) : Boolean {
        val hash1 = h1(n)
        val hash2 = h2(n)

        for (i in array.indices) {
            val probe = abs((hash1 + i*hash2) % size)

            if (array[probe] == null) {
                array[probe] = n
                this.filled++
                return true
            }

            collisions++
        }

        return false
    }

    override fun find(n : Int) : Boolean {
        val hash1 = h1(n)
        val hash2 = h2(n)

        for (i in array.indices) {
            val probe = (hash1 + i*hash2) % size

            if (array[probe] == n) {
                return true
            }
            else if (array[probe] == null) {
                return false
            }
        }

        return false
    }

    private fun h1(n : Int) : Int {
        return n % size
    }

    private fun h2(n : Int) : Int {
        return (n % (size - 1)) + 1
    }
}