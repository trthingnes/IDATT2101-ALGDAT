package hashtable

import kotlin.math.round
import kotlin.math.sqrt

fun main() {
    val m = 127 // ! Must be prime for quadratic to work properly.
    val alpha = 0.8 // * Fill rate

    val array = getUniqueRandomArray(round(m*alpha).toInt())
    val lin = LinearHashTable(m)
    val qua = QuadraticHashTable(m)
    val dou = DoubleHashTable(m)

    array.forEach { lin.add(it) }
    println("Linear: Tried to add ${array.size}/$m elements. Filled ${lin.filled} after ${lin.collisions} collisions.")
    println("Got number out of table: ${lin.find(array.last())}\n")

    array.forEach { qua.add(it) }
    println("Quadratic: Tried to add ${array.size}/$m elements. Filled ${qua.filled} after ${qua.collisions} collisions.")
    println("Got number out of table: ${qua.find(array.last())}\n")

    array.forEach { dou.add(it) }
    println("Double: Tried to add ${array.size}/$m elements. Filled ${dou.filled} after ${dou.collisions} collisions.")
    println("Got number out of table: ${dou.find(array.last())}")
}

fun getUniqueRandomArray(m : Int) : Array<Int> {
    val array = Array(m) { 0 }

    array[0] = (1..500).random()
    for (i in (1 until m)) {
        array[i] = array[i-1] + (1..500).random()
    }
    array.shuffle()

    return array
}

abstract class HashTable(size : Int) {
    private val array = Array<Int?>(size) { null }
    private val a = (sqrt(5.0) - 1) / 2

    var filled = 0
    var collisions = 0

    internal fun multiHash(n : Int) : Int {
        return ( array.size * ( n*a - (n*a).toInt() ) ).toInt()
    }

    internal abstract fun probe(n: Int, i : Int) : Int

    fun add(n : Int) : Boolean {
        for (i in (0..array.lastIndex)) {
            val probe = probe(n, i)

            if (array[probe] == null) {
                array[probe] = n
                this.filled++
                return true
            }

            collisions++
        }

        return false
    }

    fun find(n : Int) : Boolean {
        for (i in (0..array.lastIndex)) {
            val probe = probe(n, i)

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