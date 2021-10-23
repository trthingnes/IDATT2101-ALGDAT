package T8_compression

import java.io.*
import java.lang.IllegalStateException
import java.util.*
import kotlin.NoSuchElementException

fun main() {
    val h = Huffman()

    h.compress(File("./CV.pdf"), File("./CV.huff.pdf"))
    h.decompress(File("./CV.huff.pdf"), File("./CV.decoded.pdf"))

    FileInputStream(File("./CV.pdf")).use { inp ->
        FileInputStream(File("./CV.decoded.pdf")).use { out ->
            var counter = 0
            while(inp.available() > 0 && out.available() > 0) {
                val inpRead = inp.read()
                val outRead = out.read()
                if(inpRead != outRead) println("Byte $counter: $inpRead != $outRead")
                counter++
            }
        }
    }
}

class Huffman {
    /**
     * Uses Huffman encoding to compress the input file into the output file.
     */
    fun compress(input: File, output: File) {
        val frequencies = countFrequenciesInFile(input)
        val bitStrings = getBitStringsFromTree(getTreeFromFrequencies(frequencies))
        val outputBytes = arrayListOf<Int>()
        var padding = 0

        DataInputStream(FileInputStream(input)).use {
            val bitStringToWrite = BitString()

            // Read every byte from input file.
            while(it.available() > 0) {
                // Divide bits into bytes. Example: 101 11 100 0 1010 -> 10111100 01010...
                for(bit in bitStrings[it.read()]) {
                    when(bit) {
                        '0' -> bitStringToWrite.addZero()
                        '1' -> bitStringToWrite.addOne()
                    }

                    // If the string has length 8, push its byte value and reset it.
                    if(bitStringToWrite.length == 8) {
                        outputBytes.add(bitStringToWrite.bits.toInt())
                        bitStringToWrite.clear()
                    }
                }
            }

            //The file will probably not evenly split into bytes. Therefore, we pad the last byte.
            while(bitStringToWrite.length in (1..7)) {
                bitStringToWrite.addZero()
                padding++
            }
            outputBytes.add(bitStringToWrite.bits.toInt())
        }

        DataOutputStream(FileOutputStream(output)).use {
            // Write the frequencies to the output list (the tree is derived from this).
            frequencies.forEach { f -> it.writeInt(f) }

            // Write the number corresponding to the number of zeros used as padding.
            it.writeInt(padding)

            // Write all the encoded bytes to the list.
            outputBytes.forEach { b -> it.write(b) }
        }
    }

    /**
     * Decompresses a file compressed with Huffman encoding.
     */
    fun decompress(input: File, output: File) {
        val frequencies = Array(256) { 0 }
        val inputBytes = arrayListOf<Int>()
        val outputBytes = arrayListOf<Int>()
        val padding: Int

        DataInputStream(FileInputStream(input)).use {
            // Read all frequencies from file.
            for(i in frequencies.indices) {
                frequencies[i] = it.readInt()
            }

            // Read padding used for last byte from file.
            padding = it.readInt()

            // Read the rest of the file as bytes.
            while(it.available() > 0) {
                inputBytes.add(it.read())
            }
        }

        // Retrieve the tree after reading the frequencies from file.
        val root = getTreeFromFrequencies(frequencies)

        // If padding was added on compression, leave the last byte as a special case.
        val range = if(padding == 0) inputBytes.indices else 0 until inputBytes.lastIndex
        val bitString = BitString()
        for(i in range) {
            val byte = inputBytes[i]
            bitString.concat(BitString(8, byte.toLong()))

            while(bitString.length >= 24) {
                outputBytes.add(findByteInTree(root, bitString))
            }
        }

        // If the last byte has padding.
        if(padding > 0) {
            val byte = inputBytes.last()
            bitString.concat(BitString(8, byte.toLong()))
            repeat(padding) { bitString.removeLast() }

            while(bitString.length > 0) {
                try {
                    outputBytes.add(findByteInTree(root, bitString))
                } catch(e: NoSuchElementException) {
                    println("Encountered error while reading padded byte.")
                }
            }
        }

        DataOutputStream(FileOutputStream(output)).use {
            outputBytes.forEach { b -> it.write(b) }
        }
    }

    /**
     * Navigates the Huffman tree and finds a given byte.
     */
    private fun findByteInTree(root: Node, bitString: BitString): Int {
        // Descend the tree until character is reached.
        var currentNode = root
        while(currentNode.left != null && currentNode.right != null) {
            when(bitString.removeFirst()) {
                '0' -> {
                    currentNode = currentNode.left!!
                }
                '1' -> {
                    currentNode = currentNode.right!!
                }
            }
        }

        // Add the byte for the right character.
        return currentNode.byte ?: throw IllegalStateException("Reached leaf node that has no byte value.")
    }

    /**
     * Counts the frequency of bytes in a non-encoded file.
     */
    private fun countFrequenciesInFile(input: File): Array<Int> {
        val frequencies = Array(256) { 0 }

        // Read frequencies of all bytes in file.
        FileInputStream(input).use {
            while(it.available() > 0) {
                frequencies[it.read()]++
            }
        }

        return frequencies
    }

    /**
     * Makes a Huffman tree based on the given frequencies and returns the root.
     */
    private fun getTreeFromFrequencies(frequencies: Array<Int>): Node {
        // Create and fill random order node list.
        val nodes = arrayListOf<Node>()
        for (i in frequencies.indices) {
            if (frequencies[i] <= 0) continue
            nodes.add(Node(i, frequencies[i]))
        }

        // Add all nodes to priority queue based on frequency.
        val queue = PriorityQueue<Node>(256) { a, b -> a.frequency - b.frequency }
        queue.addAll(nodes)

        // Merge the two nodes with the lowest frequencies until only one is left.
        var root: Node? = null
        while (queue.size != 1) {
            root = Node(queue.poll(), queue.poll())
            queue.add(root)
        }

        // Return the root of the tree.
        return root ?: Node(null, 0)
    }

    /**
     * Gets an array of [BitString]s based on the tree defined by the provided root.
     */
    private fun getBitStringsFromTree(root: Node, string: BitString = BitString(), strings: Array<BitString> = Array(256) { BitString() }): Array<BitString> {
        root.left?.let { getBitStringsFromTree(it, BitString(string).addZero(), strings) }
        root.right?.let { getBitStringsFromTree(it, BitString(string).addOne(), strings) }

        if (root.left == null && root.right == null) {
            root.byte?.let { strings[it] = string }
        }

        return strings
    }
}