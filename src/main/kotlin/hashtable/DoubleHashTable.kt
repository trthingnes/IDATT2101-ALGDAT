package hashtable

class DoubleHashTable(private val size : Int) : HashTable(size) {
    override fun probe(n: Int, i: Int): Int {
        return (h1(n) + h2(n)*i) % size
    }

    private fun h1(n : Int) : Int {
        return n % size
    }

    private fun h2(n : Int) : Int {
        return (n % (size - 1)) + 1
    }
}