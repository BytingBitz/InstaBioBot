package au.jamal.instabiobot.utilities

object Log {

    private fun colour(text: String, colourCode: Int): String {
        return "\u001B[${colourCode}m$text\u001B[0m"
    }

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

    fun error(message: String) {
        println("[${colour("e", 31)}]\n${colour(message, 31)}")
    }

    fun trace(errorTraceback: Throwable) {
        val trace = errorTraceback.stackTrace.joinToString("\n")
        println("[${colour("t", 31)}]\n${colour(trace, 31)}")
    }

    fun dump(objectToDump: Any) {
        val content = objectToDump.toString()
        println("[${colour("d", 37)}]\n${colour(content, 37)}")
    }

}