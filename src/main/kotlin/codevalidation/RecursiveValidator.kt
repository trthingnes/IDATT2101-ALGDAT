package codevalidation

class RecursiveValidator {
    private val opening = arrayOf('(', '{', '[')
    private val closing = arrayOf(')', '}', ']')

    fun validate(code : String) {
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