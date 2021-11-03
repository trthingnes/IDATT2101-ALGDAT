package T8_compression

import java.io.File
import java.lang.IllegalStateException

fun main() {
    val c = Compressor()
    val compress = true

    if(compress) c.compress(File("diverse.txt"), File("diverse.c.txt"))
    else c.decompress(File("diverse.c.txt"), File("diverse.d.txt"))
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