package au.jamal.instabiobot.control

import au.jamal.instabiobot.instagram.InstagramSession
import au.jamal.instabiobot.utilities.DelayControl
import au.jamal.instabiobot.utilities.Log
import au.jamal.instabiobot.utilities.TextConstructor
import java.time.LocalDate
import kotlin.system.exitProcess

object SessionController {

    private var failCount: Int = 1
    private const val CONSECUTIVE_FAIL_LIMIT: Int = 3
    private const val DAYS_TO_RESTART: Long = 9

    private fun bioUpdateHandler(session: InstagramSession) {
        val restartDate = LocalDate.now().plusDays(DAYS_TO_RESTART)
        Log.info("Calculated restart date: $restartDate")
        var currentBio: String = session.getCurrentBio()
        while (LocalDate.now() < restartDate) {
            val generatedBio: String = TextConstructor.buildBioText()
            if (currentBio != generatedBio) {
                session.updateBio(generatedBio)
                currentBio = generatedBio
            }
            DelayControl.sleep(1..2)
            failCount = 1
        }
        Log.status("Restarting session...")
    }

    fun mainSessionLoop(production: Boolean, debug: Boolean) {
        while (failCount <= CONSECUTIVE_FAIL_LIMIT) {
            val session = InstagramSession(production, debug)
            try {
                session.login()
                bioUpdateHandler(session)
            } catch (e: Exception) {
                Log.error(e)
                Log.alert("Session failed: $failCount/$CONSECUTIVE_FAIL_LIMIT")
                DelayControl.sleep(60..120)
                failCount += 1
            } finally {
                session.end()
            }
        }
        Log.alert("Exiting main session loop")
        exitProcess(0)
    }

}