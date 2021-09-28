package hashtable

class LinearHashTable(maxSize : Int) : HashTable(maxSize) {
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