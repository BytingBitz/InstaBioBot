package au.jamal.instabiobot

fun main() {
    val instagramSession = InstagramSession(production = false, debug = true)
    try {
        instagramSession.login("test", "testtesttest")
        Log.status("Alive!")
        val bio = instagramSession.getCurrentBio()
    } catch (e: Exception) {
        Log.error(e.message ?: "No message...")
        Log.trace(e)
    } finally {
        instagramSession.end()
    }
}