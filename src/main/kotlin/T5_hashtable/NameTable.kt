package T5_hashtable

import java.io.File

fun main() {
    val hs = HashTable(149)
    val lines = File("./src/main/kotlin/nametable/names.txt").readLines()

    println("Found ${lines.size} names to add.\n")

    for (line in lines) {
        hs.add(line)
    }

    println("\nAdded ${hs.n} names and got ${hs.collision} collisions (${hs.collision*100/hs.n} %). The load factor is ${hs.getAlpha()}")
    println("Found me in table: ${hs.find("Tobias Rødahl Thingnes") != null}")
    println("Found Ola Nordmann in table: ${hs.find("Ola Nordmann") != null}")
}

class HashNode<T>(var value : T, var next : HashNode<T>? = null)

class HashTable(size : Int) {
    private val array = Array<HashNode<String>?>(size) { null }

    var n = 0
    var collision = 0

    private fun hash(value : String) : Int {
        var sum = 0
        var factor = 0

        // This way of weighting removes the need for .pow(x,y).
        for (c in value) {
            sum += c.code
            sum = (sum * ++factor) % array.size
        }

        return sum
    }

    fun add(value : String) {
        val hash = hash(value)

        if (array[hash] == null) {
            array[hash] = HashNode(value)
        }
        else {
            var node = array[hash]

            while (node?.next != null) {
                node = node.next
            }

            println("$value collided with ${node?.value}")

            node?.next = HashNode(value)
            collision++
        }

        n++
    }

    fun find(value : String) : HashNode<String>? {
        val hash = hash(value)
        var node = array[hash] ?: return null

        while (node.value != value) {
            node = node.next ?: return null
        }

        return node
    }

    fun getAlpha() : Float {
        return n.toFloat() / array.size.toFloat()
    }
}

