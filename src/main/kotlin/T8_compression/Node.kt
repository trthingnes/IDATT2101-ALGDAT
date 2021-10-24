package T8_compression

class Node(var value: Int?, var frequency: Int, var left: Node? = null, var right: Node? = null) {
    constructor(a: Node, b: Node) : this(null, a.frequency + b.frequency, a, b)
}