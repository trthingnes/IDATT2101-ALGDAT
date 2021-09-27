package hashtable

fun main() {
    val m = 100
    val array = getUniqueRandomArray(m)
}

fun getUniqueRandomArray(m : Int) : Array<Int> {
    val array = Array(m) { 0 }

    for (i in (1..m)) {
        array[i] = array[i-1] + (1..500).random()
    }
    array.shuffle()

    return array
}

class LinearHashTable(val size : Int) : HashTable(size) {
    fun hash() {

    }
}