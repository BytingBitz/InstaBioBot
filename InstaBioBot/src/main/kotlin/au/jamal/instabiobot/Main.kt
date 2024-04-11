package au.jamal.instabiobot

import au.jamal.instabiobot.instagram.InstagramSession
import au.jamal.instabiobot.utilities.Log

fun main() {
    val instagramSession = InstagramSession(production = false, debug = true)
    try {
        instagramSession.login()
        val bio = instagramSession.getCurrentBio()
    } catch (e: Exception) {
        Log.error(e)
    } finally {
        instagramSession.end()
    }
}