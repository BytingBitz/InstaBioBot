package au.jamal.instabiobot.instagram

import au.jamal.instabiobot.utilities.DelayControl
import au.jamal.instabiobot.utilities.Log
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDateTime

const val INSTAGRAM_URL = "https://www.instagram.com/"
const val INSTAGRAM_SETTINGS_URL = "https://www.instagram.com/accounts/edit/"
const val SELENIUM_TIMEOUT = 10L

class InstagramSession (production: Boolean, debug: Boolean) {

    private val session: BrowserManager = BrowserManager(production, debug)
    private val sessionWait: WebDriverWait = WebDriverWait(
        session.browser, Duration.ofSeconds(SELENIUM_TIMEOUT)
    )

    fun login() {
        val (username, password) = LoginSecrets.envLoad()
        session.browser.get(INSTAGRAM_URL)
        DelayControl.sleep(5, 10)
        val usernameInput = getElement(By::cssSelector, "input[name='username']")
        val passwordInput = getElement(By::cssSelector, "input[name='password']")
        val loginButton = getElement(By::xpath, "//button[@type='submit']")
        usernameInput.sendKeys(username)
        passwordInput.sendKeys(password)
        loginButton.click()
        DelayControl.sleep(5, 10)
        accessSettings() // Verifies login
        Log.status("Login successful at ${LocalDateTime.now()}")
    }

    fun getCurrentBio (): String {
        accessSettings()
        val bioElement = getElement(By::cssSelector, "textarea[id='pepBio']")
        val bioText = bioElement.getAttribute("value")
        Log.status("Current bio text: [$bioText] at ${LocalDateTime.now()}")
        return bioText
    }

    fun updateBio (newBioText: String) {
        accessSettings()
        val bioElement = getElement(By::cssSelector, "textarea[id='pepBio']")
        val updateButton = getElement(By::xpath, "//*[contains(text(), 'submit')]")
        bioElement.clear()
        bioElement.sendKeys(newBioText)
        updateButton.click()
        // TODO: Verify bio update
        Log.status("Updated bio text: [$newBioText] at ${LocalDateTime.now()}")
    }

    fun end() {
        Log.warn("Killing Selenium session...")
        session.end()
    }

    private fun getElement(selector: (String) -> By, expression: String): WebElement {
        try {
            val locator = selector(expression)
            return sessionWait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
            )
        } catch (e: Exception) {
            Log.alert("Failed to access element: [$selector, $expression]")
            throw IllegalStateException("Invalid DOM references...")
        }
    }

    private fun accessSettings() {
        if (session.browser.currentUrl != INSTAGRAM_SETTINGS_URL) {
            session.browser.get(INSTAGRAM_SETTINGS_URL)
            DelayControl.sleep(2, 5)
            if (session.browser.currentUrl != INSTAGRAM_SETTINGS_URL) {
                Log.alert("Failed to access settings")
                throw IllegalStateException("Session login issue detected...")
            }
        }
    }

}