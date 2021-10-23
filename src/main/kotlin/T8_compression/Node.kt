package T8_compression

class Node(var byte: Int?, var frequency: Int) {
    constructor(a: Node, b: Node) : this(null, a.frequency + b.frequency) {
        left = a
        right = b
    }

    var left: Node? = null
    var right: Node? = null
}