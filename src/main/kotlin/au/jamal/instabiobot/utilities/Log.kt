package au.jamal.instabiobot.utilities

object Log {

    fun status(message: String) {
        println("[${colour("-", 32)}] ${colour(message, 32)}")
    }

    fun info(message: String) {
        println("[${colour("i", 34)}] ${colour(message, 34)}")
    }

    fun warn(message: String) {
        println("[${colour("*", 36)}] ${colour(message, 36)}")
    }

    fun alert(message: String) {
        println("[${colour("!", 31)}] ${colour(message, 31)}")
    }

    fun error(error: Throwable) {
        println("[${colour("e", 31)}]".trimStart())
        fun printErrors(error: Throwable?) {
            if (error == null) return
            println(colour("(${error::class.simpleName}) ${error.message}", 31))
            printErrors(error.cause)
        }
        printErrors(error)
        trace(error)
    }

    fun dump(objectToDump: Any) {
        val content = objectToDump.toString()
        println("[${colour("d", 37)}] ${colour(content, 37)}")
    }

    private fun colour(text: String, colourCode: Int): String {
        return "\u001B[${colourCode}m$text\u001B[0m"
    }

    private fun trace(error: Throwable) {
        val trace = error.stackTrace.joinToString("\n")
        println("[${colour("t", 31)}]\n${colour(trace, 31)}".trimStart())
    }

}