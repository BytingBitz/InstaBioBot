package au.jamal.instabiobot.utilities

import kotlin.random.Random
import kotlin.random.nextInt

object DelayControl {

    private val randomGenerator: Random = Random.Default

    fun sleep(range: IntRange) {
        try {
            val durationSeconds = randomGenerator.nextInt(range)
            val durationMillis = durationSeconds * 1000L
            Thread.sleep(durationMillis)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to sleep thread", e)
        }

    }

}