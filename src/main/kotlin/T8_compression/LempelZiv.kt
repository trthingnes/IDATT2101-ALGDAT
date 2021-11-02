package T8_compression

import java.io.*
import java.lang.IllegalStateException
import kotlin.math.abs

class LempelZiv {
    private var buffer = Buffer<Int>(32*1024)
    private var bytes = arrayListOf<Int>()

    fun compress(input: File, output: File) {
        // Read all bytes from file.
        DataInputStream(FileInputStream(input)).use {
            while(it.available() > 0) bytes.add(it.read())
        }

        val out = DataOutputStream(FileOutputStream(output))
        val skipped = arrayListOf<Int>()

        var i = 0
        while(i < bytes.size) {
            val pointer = findBestPointer(i)

            // If no good pointer was found.
            if((pointer == null) || (pointer.length == 0)) {
                skipped.add(bytes[i]) // Add this raw byte to the skipped bytes to be written later.
                buffer.add(bytes[i]) // Add this raw byte to the buffer.
                i++
            }
            else {
                // If a pointer could be made, we push this pointer instead.
                // Write previously skipped bytes to the file.
                if(skipped.isNotEmpty()) {
                    out.writeByte(skipped.size) // Write one positive byte to indicate length.
                    skipped.forEach { out.writeByte(it) } // Write all skipped bytes.
                }
                out.writeByte(pointer.length * -1) // Write one negative byte as length.
                out.writeShort(pointer.offset) // Write two bytes to indicate how far back to look.
                i += pointer.length // Skip the rest of the compressed segment.
            }
        }

        out.close()
        buffer.clear()
        bytes.clear()
    }

    /**
     * Compares the bytes list and the buffer starting at the given index, and finds the longest pointer.
     * If no pointer that saves space is found, this method returns null.
     */
    private fun findBestPointer(index: Int): Pointer? {
        TODO()
    }

    fun decompress(input: File, output: File) {
        val inp = DataInputStream(FileInputStream(input))
        val out = DataOutputStream(FileOutputStream(output))

        while(inp.available() > 0) {
            // Read new block
            val length = inp.readByte()

            // If the length is positive, the following is skipped bytes.
            if(length > 0) {
                // Write all the bytes directly to the output file.
                repeat(length.toInt()) {
                    // TODO: Verify that all + and - signs are correct when writing and reading. (Massive bug potential)
                    val byte = inp.read()
                    buffer.add(byte)
                    out.writeByte(byte)
                }
            }
            // If the length is negative, the following is a pointer.
            else if(length < 0) {
                val offset = inp.readShort()
                findBytesInBuffer(abs(length.toInt()), offset.toInt()).forEach { out.write(it) }
            }
            else throw IllegalStateException("Got zero as a block length. The file might be corrupted.")
        }

        out.close()
        bytes.clear()
        buffer.clear()
    }
}

/**
 * Takes the length and offset from a pointer and retrieves the bytes this corresponds to.
 */
private fun findBytesInBuffer(length: Int, offset: Int): ArrayList<Int> {
    TODO()
}

/**
 * Pointer is used to show where duplicate text was found.
 */
class Pointer(val length: Int, val offset: Int) {
    companion object {
        const val BYTES_LENGTH = 3
    }
}

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