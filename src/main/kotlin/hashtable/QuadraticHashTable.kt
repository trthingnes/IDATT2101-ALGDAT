package hashtable

class QuadraticHashTable(private val size : Int) : HashTable(size) {
    private val k1 = 3
    private val k2 = 5

    override fun probe(n : Int, i : Int) : Int {
        return (((k1 + k2 * i) * i) + multiHash(n)) % size
    }
}