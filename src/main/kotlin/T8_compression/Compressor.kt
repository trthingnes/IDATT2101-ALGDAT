package T8_compression

import java.io.File
import java.lang.IllegalStateException

fun main() {
    val c = Compressor()
    c.compress(File("test.pdf"), File("test.c.pdf"))
    c.decompress(File("test.c.pdf"), File("test.d.pdf"))
}

class Compressor {
    fun compress(input: File, output: File) {
        val temp = File("temp")
        if(temp.exists()) throw IllegalStateException("Temporary file already exists. Please delete and try again.")

        LempelZiv().compress(input, temp)
        Huffman().compress(temp, output)

        temp.delete()
    }

    fun decompress(input: File, output: File) {
        val temp = File("temp")
        if(temp.exists()) throw IllegalStateException("Temporary file already exists. Please delete and try again.")

        Huffman().decompress(input, temp)
        LempelZiv().decompress(temp, output)

        temp.delete()
    }
}