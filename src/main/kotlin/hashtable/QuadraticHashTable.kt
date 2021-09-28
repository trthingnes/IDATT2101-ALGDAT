package hashtable

class QuadraticHashTable(maxSize : Int) : HashTable(maxSize) {
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