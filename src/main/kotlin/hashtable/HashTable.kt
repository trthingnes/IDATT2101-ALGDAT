package hashtable

import kotlin.math.round
import kotlin.math.sqrt
import kotlin.reflect.typeOf
import kotlin.system.measureTimeMillis

fun mains() {
    val m = 15_485_863 // ! Must be prime for quadratic to work properly.
    val alpha = 0.8 // * Fill rate

    val array = getUniqueRandomArray(round(m*alpha).toInt())
    val lin = LinearHashTable(m)
    val qua = QuadraticHashTable(m)
    val dou = DoubleHashTable(m)

    array.forEach { lin.add(it) }
    println("Linear: Tried to add ${array.size}/$m elements. Filled ${lin.filled} after ${lin.collisions} collisions.")

    array.forEach { qua.add(it) }
    println("Quadratic: Tried to add ${array.size}/$m elements. Filled ${qua.filled} after ${qua.collisions} collisions.")

    array.forEach { dou.add(it) }
    println("Double: Tried to add ${array.size}/$m elements. Filled ${dou.filled} after ${dou.collisions} collisions.")
}

fun main() {
    val m = 15_485_863 // * Primtall over 10 millioner
    val alpha = 0.99

    val array = getUniqueRandomArray(round(m*alpha).toInt())

    val linTable = LinearHashTable(m)
    val linRuntime = measureTimeMillis {
        array.forEach { linTable.add(it) }
    }
    println("Linear: Filled $alpha of $m elements with a runtime of $linRuntime ms.")

    val quaTable = QuadraticHashTable(m)
    val quaRuntime = measureTimeMillis {
        array.forEach { quaTable.add(it) }
    }
    println("Quadratic: Filled $alpha of $m elements with a runtime of $quaRuntime ms.")

    val douTable = DoubleHashTable(m)
    val douRuntime = measureTimeMillis {
        array.forEach { douTable.add(it) }
    }
    println("Double: Filled $alpha of $m elements with a runtime of $douRuntime ms.")
}

fun getUniqueRandomArray(m : Int) : Array<Int> {
    val array = Array(m) { 0 }

    array[0] = (1..10).random()
    for (i in (1 until m)) {
        array[i] = array[i-1] + (1..10).random()
    }
    array.shuffle()

    return array
}

abstract class HashTable(val size : Int) {
    internal var array = Array<Int?>(size) { null }
    internal var filled = 0
    internal var collisions = 0

    private val a = (sqrt(5.0) - 1) / 2

    internal fun multiHash(n : Int) : Int {
        return ( array.size * ( n*a - (n*a).toInt() ) ).toInt()
    }

    internal abstract fun add(n : Int) : Boolean

    internal abstract fun find(n : Int) : Boolean

    fun clear() {
        array = Array(size) { null }
        filled = 0
        collisions = 0
    }
}