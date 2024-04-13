package au.jamal.instabiobot

import au.jamal.instabiobot.control.SessionController

fun main() {
    SessionController.mainSessionLoop(production = false, debug = true)
} // TODO: implement provision of arguments and shutdown signal