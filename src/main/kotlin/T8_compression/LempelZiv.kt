package T8_compression

import java.io.*
import java.lang.IllegalStateException
import kotlin.math.abs

class LempelZiv {
    private var buffer = Buffer<Int>(32*1024)
    private var bytes = arrayListOf<Int>()

    /**
     * Uses Lempel Ziv encoding to compress the input file into the output file.
     */
    fun compress(input: File, output: File) {
        // Read all bytes from file.
        DataInputStream(FileInputStream(input)).use {
            while(it.available() > 0) bytes.add(it.readByte().toInt()) // Read signed bytes from file.
        }

        val out = DataOutputStream(FileOutputStream(output))
        val skipped = arrayListOf<Int>()

        var i = 0
        while(i < bytes.size) {
            val pointer = findBestPointer(i)

            // If no good pointer was found.
            if((pointer == null) || (pointer.length == 0)) {
                skipped.add(bytes[i]) // Add this unsigned byte to be written later.
                buffer.add(bytes[i]) // Add this unsigned byte to the buffer.
                i++
            }
            else {
                // If a pointer could be made, we push this pointer instead.
                // Write previously skipped bytes to the file.
                while(skipped.isNotEmpty()) {
                    out.writeShort(skipped.size) // Write one positive signed byte to indicate length.
                    skipped.forEach { out.writeByte(it)} // Write all skipped signed bytes.
                    skipped.clear() // Empty the list when the bytes are all written.
                }
                out.writeShort(pointer.length * -1) // Write one negative byte as length.
                out.writeShort(pointer.offset) // Write two bytes to indicate how far back to look.
                i += pointer.length // Skip the rest of the compressed segment.
            }
        }

        // Print the final skipped characters.
        if(skipped.isNotEmpty()) {
            out.writeShort(skipped.size) // Write one positive signed byte to indicate length.
            skipped.forEach { out.writeByte(it) } // Write all skipped signed bytes.
            skipped.clear() // Empty the list when the bytes are all written.
        }

        out.close()
        buffer.clear()
        bytes.clear()
    }

    /**
     * Decompresses a file compressed with Lempel Ziv encoding.
     */
    fun decompress(input: File, output: File) {
        val inp = DataInputStream(FileInputStream(input))
        val out = DataOutputStream(FileOutputStream(output))

        while(inp.available() > 0) {
            // Read new block
            val length = inp.readShort()

            // If the length is positive, the following is skipped bytes.
            if(length > 0) {
                // Write all the bytes directly to the output file.
                repeat(length.toInt()) {
                    val byte = inp.readByte()
                    buffer.add(byte.toInt())
                    out.writeByte(byte.toInt())
                }
            }
            // If the length is negative, the following is a pointer.
            else if(length < 0) {
                val offset = inp.readShort()

                // Write the bytes from the buffer to the file.
                findBytesInBuffer(abs(length.toInt()), offset.toInt()).forEach { out.writeByte(it) }
            }
            else throw IllegalStateException("Got zero as a block length. The file might be corrupted.")
        }

        inp.close()
        out.close()
        bytes.clear()
        buffer.clear()
    }

    /**
     * Compares the bytes list and the buffer starting at the given index, and finds the longest pointer.
     * If no pointer that saves space is found, this method returns null.
     */
    private fun findBestPointer(index: Int): Pointer? {
        val starts = arrayListOf<Int>()
        for (i in buffer.indices) {
            if(buffer[i] == bytes[index]) starts.add(i)
        }
        var best = Pointer(-1, -1)

        for(i in starts) {
            val length = findMatchLength(i, index)
            if(length > best.length) {
                best = Pointer(length, buffer.size - i)
            }
        }

        return if(best.length <= 4) null else best
    }

    /**
     * Takes the indices for both the buffer and index and finds the number of matching bytes in a row.
     */
    private fun findMatchLength(bufferIndex: Int, index: Int): Int {
        var bi = bufferIndex + 1
        var i = index + 1
        var length = 1

        while(bi < buffer.size && i < bytes.size && buffer[bi] == bytes[i] ) {
            length++
            bi++
            i++
        }

        return length
    }

    /**
     * Takes the length and offset from a pointer and retrieves the bytes this corresponds to.
     */
    private fun findBytesInBuffer(length: Int, offset: Int): ArrayList<Int> {
        val index = buffer.size - offset
        val result = arrayListOf<Int>()

        for(i in 0 until length) {
            result.add(buffer[i+index])
        }

        return result
    }
}

/**
 * Pointer is used to show where duplicate text was found.
 */
class Pointer(val length: Int, val offset: Int)

/**
 * Buffer is an ArrayList with a max size.
 * If the buffer is full, the first element will be removed to free up space.
 */
class Buffer<E>(private val maxSize: Int): ArrayList<E>() {
    override fun add(element: E): Boolean {
        if(size > maxSize - 1) removeFirst()
        return super.add(element)
    }
}