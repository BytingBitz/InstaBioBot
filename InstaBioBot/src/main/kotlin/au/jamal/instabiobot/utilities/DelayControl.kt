package au.jamal.instabiobot.utilities

import kotlin.random.Random

object DelayControl {

    private val randomGenerator: Random = Random.Default

    fun sleep(minDuration: Int = 5, maxDuration: Int = 10) {
        try {
            val durationSeconds = randomGenerator.nextInt(minDuration, maxDuration + 1)
            val durationMillis = durationSeconds * 1000L
            Thread.sleep(durationMillis)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to sleep thread", e)
        }

    }

}