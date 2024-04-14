package au.jamal.instabiobot.control

import au.jamal.instabiobot.instagram.InstagramSession
import au.jamal.instabiobot.utilities.Delay
import au.jamal.instabiobot.utilities.Log
import au.jamal.instabiobot.utilities.Bio
import java.time.LocalDate
import kotlin.system.exitProcess

object SessionController {

    private var failCount: Int = 1

    private fun bioUpdateHandler(session: InstagramSession, daysToRestart: Long) {
        val restartDate = LocalDate.now().plusDays(daysToRestart)
        Log.info("Calculated restart date: $restartDate")
        var currentBio: String = session.getCurrentBio()
        while (LocalDate.now() < restartDate) {
            val generatedBio: String = Bio.buildText()
            if (currentBio != generatedBio) {
                session.updateBio(generatedBio)
                currentBio = generatedBio
            }
            Delay.sleep(1..2)
            failCount = 1
        }
        Log.status("Restarting session...")
    }

    fun mainSessionLoop(config: ConfigSettings) {
        while (failCount <= config.failLimit) {
            val session = InstagramSession(config)
            try {
                session.login()
                bioUpdateHandler(session, config.restartInDays)
            } catch (e: Exception) {
                Log.error(e)
                Log.alert("Session failed: $failCount/${config.failLimit}")
                Delay.sleep(60..120)
                failCount += 1
            } finally {
                session.end()
            }
        }
        Log.alert("Exiting main session loop")
        exitProcess(0)
    }

}