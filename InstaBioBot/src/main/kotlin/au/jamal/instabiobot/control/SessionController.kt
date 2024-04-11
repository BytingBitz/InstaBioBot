package au.jamal.instabiobot.control

import au.jamal.instabiobot.instagram.InstagramSession
import au.jamal.instabiobot.utilities.DelayControl
import au.jamal.instabiobot.utilities.Log
import au.jamal.instabiobot.utilities.TextConstructor

const val CONSECUTIVE_FAIL_THRESHOLD = 3

object SessionController {
    
    private var restart: Boolean = false
    private var failCount: Int = 1

    private fun updateHandler(session: InstagramSession) {
        while (true) {
            val currentBio = session.getCurrentBio()
            val generatedBio = TextConstructor.buildText()
            if (currentBio != generatedBio) {
                session.updateBio(generatedBio)
            }
            DelayControl.sleep(1, 2)
        }
    }

    fun sessionLoop(production: Boolean, debug: Boolean) {
        while (!restart && failCount <= CONSECUTIVE_FAIL_THRESHOLD) {
            val session = InstagramSession(production, debug)
            try {
                session.login()
                updateHandler(session)
            } catch (e: Exception) {
                session.end()
                Log.error(e)
                Log.alert("Session failed: $failCount/3")
                failCount += 1
                DelayControl.sleep(60, 120)
                continue
            } finally {
                session.end()
            }
        }
    }

    fun sessionRestart() {
        restart = true
    }

}