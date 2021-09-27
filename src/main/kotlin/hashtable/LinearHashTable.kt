package hashtable

class LinearHashTable(private val size : Int) : HashTable(size) {
    override fun probe(n : Int, i : Int) : Int {
        return (multiHash(n) + i) % size
    }
}