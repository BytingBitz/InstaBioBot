package au.jamal.instabiobot

fun main() {
    val session = BrowserManager(production = false, debug = true)
    session.browser.get("https://www.google.com")
    Log.status("Alive!")
    Thread.sleep(5 * 1000)
    session.end()
}