package codevalidation

import java.io.File
import java.lang.IllegalStateException
import java.util.*


fun main() {
    fun getStringFromFile(path : String) : String {
        val sb = StringBuilder()
        File(path).forEachLine { sb.append(it) }
        return sb.toString()
    }

    val sfv = SourceFileValidator()
    // * val code = getStringFromFile("./Dummy.kt")
    // * sfv.validate(code)
    print("1: ")
    sfv.validate("{[](())")

    print("2: ")
    sfv.validate("{([()])}]")

    print("3: ")
    sfv.validate("(}")

    print("4: ")
    sfv.validate(" [ { ] }")

    print("5: ")
    sfv.validate("(){{}[[]][]}{()}")

    print("6: ")
    sfv.validate("int main(){ println(\"ok\");}")
}

class SourceFileValidator {
    private val opening = arrayOf('(', '{', '[')
    private val closing = arrayOf(')', '}', ']')
    private fun matching(opening : Char, closing : Char) : Boolean = (this.opening.indexOf(opening) == this.closing.indexOf(closing))

    fun validate(code : String) : Boolean {
        val stack = Stack<Char>()

        try {
            for (char in code) {
                if (char in opening) {
                    stack.add(char)
                }
                else if (char in closing && !matching(stack.pop(), char)) {
                    throw IllegalStateException("Found mismatching opening and closing parentheses")
                }
            }

            if(!stack.empty()) {
                throw IllegalStateException("Found opening parentheses without closing parentheses")
            }
        }
        catch(e : Exception) {
            println(e.message ?: "Found closing parentheses without opening parentheses")
            return false
        }

        println("No problems found")



        return true
    }

    fun validateWithRecursion(code : String) {
        var i = 0

        while (i < code.length) {
            if(code[i] in opening) {
                i = getClosingParenthesesIndex(code, i + 1, code[i])
            }
            else if(code[i] in closing) {
                throw IllegalArgumentException("Found closing parentheses before opening parentheses")
            }

            i++
        }
    }

    private fun getClosingParenthesesIndex(code : String, start : Int, type : Char) : Int {
        var i = start
        while (i < code.length && code[i] !in closing) {
            if (code[i] in opening) {
                i = getClosingParenthesesIndex(code, i + 1, code[i])
            }
            i++
        }

        if (i <= code.lastIndex && closing.indexOf(code[i]) == opening.indexOf(type)) {
            return i
        }

        throw IllegalArgumentException("Found parentheses mismatch")
    }
}