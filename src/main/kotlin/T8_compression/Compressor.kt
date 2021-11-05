package T8_compression

import java.io.File
import java.lang.IllegalStateException

fun main() {
    val c = Compressor()

    val compress = true
    val filename = "diverse"
    val fileext = "lyx"

    if(compress) c.compress(File("$filename.$fileext"), File("$filename.c.$fileext"))
    else c.decompress(File("$filename.c.$fileext"), File("$filename.d.$fileext"))
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