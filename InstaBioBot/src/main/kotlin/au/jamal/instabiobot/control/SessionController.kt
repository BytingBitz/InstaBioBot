package au.jamal.instabiobot.control

import au.jamal.instabiobot.instagram.InstagramSession
import au.jamal.instabiobot.utilities.DelayControl
import au.jamal.instabiobot.utilities.Log
import au.jamal.instabiobot.utilities.TextConstructor
import java.time.LocalDate
import kotlin.system.exitProcess

object SessionController {

    private var failCount: Int = 1
    private const val CONSECUTIVE_FAIL_THRESHOLD = 1
    private const val DAYS_TO_RESTART = 9

    private fun updateHandler(session: InstagramSession) {
        val endDate = LocalDate.now().plusDays(DAYS_TO_RESTART.toLong())
        Log.info("Calculated end date: $endDate")
        var currentBio = session.getCurrentBio()
        while (LocalDate.now() <= endDate ) {
            val generatedBio = TextConstructor.buildText()
            if (currentBio != generatedBio) {
                session.updateBio(generatedBio)
                currentBio = generatedBio
            }
            DelayControl.sleep(1, 2)
        }
        Log.status("Restarting session...")
    }

    fun sessionLoop(production: Boolean, debug: Boolean) {
        while (failCount <= CONSECUTIVE_FAIL_THRESHOLD) {
            val session = InstagramSession(production, debug)
            try {
                session.login()
                updateHandler(session)
            } catch (e: Exception) {
                Log.error(e)
                Log.alert("Session failed: $failCount/3")
                DelayControl.sleep(60, 120)
                failCount += 1
            } finally {
                session.end()
            }
        }
        Log.alert("Exiting session controlling")
        exitProcess(0)
    }

}