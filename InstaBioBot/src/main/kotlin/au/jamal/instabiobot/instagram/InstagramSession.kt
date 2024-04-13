package au.jamal.instabiobot.instagram

import au.jamal.instabiobot.utilities.DelayControl
import au.jamal.instabiobot.utilities.Log
import org.openqa.selenium.WebElement
import java.time.LocalDateTime

class InstagramSession(production: Boolean, debug: Boolean) {

    private val session = BrowserManager(production, debug)
    private val sessionInterface = InstagramInterface(session)
    private val credentials = Credentials()

    fun login() {
        session.browser.get(Companion.INSTAGRAM_URL)
        DelayControl.sleep(5, 10)
        val usernameInput = sessionInterface.getUsernameElement()
        val passwordInput = sessionInterface.getPasswordElement()
        sendKeys(usernameInput, credentials.username)
        sendKeys(passwordInput, credentials.password)
        val loginButton = sessionInterface.getLoginElement()
        clickButton(loginButton)
        DelayControl.sleep(5, 10)
        accessSettings() // Verifies login
        Log.status("Login successful at ${LocalDateTime.now()}")
    }

    fun getCurrentBio(): String {
        accessSettings()
        val bioText = sessionInterface.getBioTextAttribute()
        Log.status("Got current bio text [$bioText] at ${LocalDateTime.now()}")
        return bioText
    }

    fun updateBio(newBioText: String) {
        accessSettings()
        val bioElement = sessionInterface.getBioElement()
        sendKeys(bioElement, newBioText)
        val updateButton = sessionInterface.getUpdateElement()
        clickButton(updateButton)
        DelayControl.sleep(5, 10)
        val updateButtonDisabled = sessionInterface.getUpdateAriaAttribute()
        if (updateButtonDisabled != "true") {
            Log.alert("Bio update to [$newBioText] failed at ${LocalDateTime.now()}")
            throw IllegalStateException("Instagram bio update failed...")
        }
        Log.status("Updated bio text: [$newBioText] at ${LocalDateTime.now()}")
    }

    fun end() {
        session.end()
    }

    private fun sendKeys(element: WebElement, key: String) {
        try {
            element.clear()
            element.sendKeys(key)
        } catch (e: Exception) {
            Log.alert("Failed to send keys to element")
            Log.dump(element)
            throw IllegalStateException("Failed to send keys...", e)
        }
    }

    private fun clickButton(element: WebElement) {
        try {
            element.click()
        } catch (e: Exception) {
            Log.alert("Failed to click element")
            Log.dump(element)
            throw IllegalStateException("Failed to click element...", e)
        }
    }

    private fun accessSettings() {
        if (session.browser.currentUrl != Companion.INSTAGRAM_SETTINGS_URL) {
            session.browser.get(Companion.INSTAGRAM_SETTINGS_URL)
            DelayControl.sleep(2, 5)
            if (session.browser.currentUrl != Companion.INSTAGRAM_SETTINGS_URL) {
                Log.alert("Failed to access settings")
                throw IllegalStateException("Session login issue...")
            }
        }
    }

    companion object {
        private const val INSTAGRAM_URL: String = "https://www.instagram.com/"
        private const val INSTAGRAM_SETTINGS_URL: String = "https://www.instagram.com/accounts/edit/"
    }

}