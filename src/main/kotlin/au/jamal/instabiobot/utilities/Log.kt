package au.jamal.instabiobot.utilities

object Log {

    fun status(message: String) {
        printLog("[${colour("-", 32)}] ${colour(message, 32)}")
    }

    fun info(message: String) {
        printLog("[${colour("i", 34)}] ${colour(message, 34)}")
    }

    fun warn(message: String) {
        printLog("[${colour("*", 36)}] ${colour(message, 36)}")
    }

    fun alert(message: String) {
        printLog("[${colour("!", 31)}] ${colour(message, 31)}")
    }

    fun error(error: Throwable) {
        fun eString(error: Throwable): String {
            return "(${error::class.simpleName}) ${error.message}"
        }
        fun printErrors(error: Throwable?) {
            if (error == null) return
            printLog(colour("\t${eString(error)}", 31))
            printErrors(error.cause)
        }
        printLog("[${colour("e", 31)}] ${colour(eString(error), 31)}")
        printErrors(error.cause)
        trace(error)
    }

    fun dump(objectToDump: Any) {
        val content = objectToDump.toString()
        printLog("[${colour("d", 37)}] ${colour(content, 37)}")
    }

    private fun trace(error: Throwable) {
        val trace = error.stackTrace.joinToString("\n\t")
        printLog("[${colour("t", 31)}] ${colour(trace, 31)}")
    }

    private fun colour(text: String, colourCode: Int): String {
        return "\u001B[${colourCode}m$text\u001B[0m"
    }

    private fun printLog(text: String) {
        val cleanText = text.replace(Regex("[^\\p{Print}\\u001B\\n\\t]"), "")
        println(cleanText)
    }
}