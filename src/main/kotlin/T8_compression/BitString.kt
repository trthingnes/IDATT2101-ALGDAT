package T8_compression

class BitString(var length: Int = 0, var bits: Long = 0) {
    constructor(original: BitString) : this(original.length, original.bits)

    /**
     * Adds a zero bit to the end of this [BitString].
     */
    fun addZero(): BitString {
        // Shift one left.
        // Example: 1010 shl 1 -> 10100.
        bits = bits shl 1
        length++

        return this
    }

    /**
     * Adds a one bit to the end of this [BitString].
     */
    fun addOne(): BitString {
        // Shift one left and perform bitwise or with 1.
        // Example: 1010 shl 1 -> 10100, 10100 or 00001 -> 10101.
        bits = (bits shl 1) or 1
        length++

        return this
    }

    /**
     * Removes the first bit from this [BitString].
     */
    fun removeFirst(): Char {
        if(length == 0) throw NoSuchElementException("Cannot remove first bit of empty bit string")

        val first = if((bits shr length - 1) and 1 == 0L) '0' else '1'

        // Shift one right to eliminate last bit.
        // Example: 10100 shr 1 -> 1010.
        bits = bits and (1L shl length - 1) - 1
        length--

        return first
    }

    /**
     * Removes the last bit from this [BitString].
     */
    fun removeLast(): Char {
        if(length == 0) throw NoSuchElementException("Cannot remove last bit of empty bit string")

        val last = if(bits and 1 == 0L) '0' else '1'

        // Shift one right to eliminate last bit.
        // Example: 10100 shr 1 -> 1010.
        bits = bits shr 1
        length--

        return last
    }

    /**
     * Clears this [BitString].
     */
    fun clear(): BitString {
        length = 0
        bits = 0

        return this
    }

    /**
     * Add the other provided [BitString] to the end of this [BitString]
     */
    fun concat(other: BitString): BitString {
        // Return new bit string with length of both strings and first string added before the other.
        // Example: 100.concat(1011) -(shl 101.length)-> 100---- -(or ---1011)-> 1001011
        length += other.length
        bits = other.bits or (bits shl other.length)

        return this
    }

    /**
     * Returns an iterator of the chars in the [BitString].
     */
    operator fun iterator(): CharIterator {
        return toString().iterator()
    }

    /**
     * Returns string representation of [BitString].
     */
    override fun toString(): String {
        if(length == 0) return ""

        val result = StringBuilder()

        var current = 1L shl (length-1)
        while(current != 0L) {
            // Example: 1010 and 1000 != 0, 1010 and 100 == 0, 1010 and 10 != 0, 1010 and 1 == 0.
            result.append(if(bits and current == 0L) "0" else "1")
            current = current shr 1
        }

        return result.toString()
    }
}