package au.jamal.instabiobot

import au.jamal.instabiobot.control.ConfigHandler
import au.jamal.instabiobot.control.SessionController

fun main() {
    val config = ConfigHandler.loadSettings()
    SessionController.mainSessionLoop(config)
} // TODO: Handle browser session failures to start