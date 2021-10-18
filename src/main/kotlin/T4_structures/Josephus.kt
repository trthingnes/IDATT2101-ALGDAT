package T4_structures

fun main() {
    val n = 10
    val s = 4
    val j = Josephus()
    println("Used n=$n and s=$s and got ${j.getSurvivor(n, s)} as the survivor.")
}

// * Node to used make circular list
class Node<T>(var value : T, var next : Node<T>? = null)

class Josephus {
    fun getSurvivor(n : Int, s : Int) : Int {
        // * Make circular list.
        val first = Node(1)
        var previous = first
        for (i in (2..n)) {
            val node = Node(i)
            previous.next = node
            previous = node
        }
        previous.next = first

        // * Remove every s reference until only one remains.
        var current = previous

        for (round in (1 until n)) {
            for (step in (1 until s)) {
                current = current.next!!
            }
            current.next = current.next?.next
        }

        // * Return the value of the last node.
        return current.value
    }
}