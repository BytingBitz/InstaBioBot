package au.jamal.instabiobot.instagram

import au.jamal.instabiobot.utilities.DelayControl
import au.jamal.instabiobot.utilities.Log
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDateTime

const val INSTAGRAM_URL: String = "https://www.instagram.com/"
const val INSTAGRAM_SETTINGS_URL: String = "https://www.instagram.com/accounts/edit/"
const val SELENIUM_TIMEOUT: Long = 10

class InstagramSession(production: Boolean, debug: Boolean) {

    private val session: BrowserManager = BrowserManager(production, debug)
    private val sessionWait: WebDriverWait = WebDriverWait(
        session.browser, Duration.ofSeconds(SELENIUM_TIMEOUT)
    )

    fun login() {
        val (username: String, password: String) = LoginSecrets.envLoad()
        session.browser.get(INSTAGRAM_URL)
        DelayControl.sleep(5, 10)
        val usernameInput = getElement(By::cssSelector, "input[name='username']")
        val passwordInput = getElement(By::cssSelector, "input[name='password']")
        sendKeys(usernameInput, username)
        sendKeys(passwordInput, password)
        val loginButton = getElement(By::xpath, "//button[@type='submit']")
        clickButton(loginButton)
        DelayControl.sleep(5, 10)
        accessSettings() // Verifies login
        Log.status("Login successful at ${LocalDateTime.now()}")
    }

    fun getCurrentBio(): String {
        accessSettings()
        val bioElement = getElement(By::cssSelector, "textarea[id='pepBio']")
        val bioText: String = getAttribute(bioElement, "value")
        Log.status("Got current bio text [$bioText] at ${LocalDateTime.now()}")
        return bioText
    }

    fun updateBio(newBioText: String) {
        accessSettings()
        val bioElement = getElement(By::cssSelector, "textarea[id='pepBio']")
        sendKeys(bioElement, newBioText)
        val updateButton = getElement(By::xpath, "//*[contains(text(), 'Submit')]")
        clickButton(updateButton)
        DelayControl.sleep(5, 10)
        val updateButtonState: String = getAttribute(updateButton, "aria-disabled")
        if (updateButtonState != "true") {
            Log.alert("Bio update to [$newBioText] failed at ${LocalDateTime.now()}")
            throw IllegalStateException("Instagram bio update failed...")
        }
        Log.status("Updated bio text: [$newBioText] at ${LocalDateTime.now()}")
    }

    fun end() {
        session.end()
    }

    private fun getElement(selector: (String) -> By, expression: String): WebElement {
        try {
            val locator: By = selector(expression)
            return sessionWait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
            )
        } catch (e: Exception) {
            Log.alert("Failed to access element: [$selector, $expression]")
            throw IllegalStateException("Failed to get element...", e)
        }
    }

    private fun getAttribute(element: WebElement, attribute: String): String {
        try {
            return requireNotNull(element.getAttribute(attribute))
        } catch (e: Exception) {
            Log.alert("Failed to access attribute [$attribute]")
            Log.dump(element)
            throw IllegalStateException("Failed to get attribute...", e)
        }
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
        if (session.browser.currentUrl != INSTAGRAM_SETTINGS_URL) {
            session.browser.get(INSTAGRAM_SETTINGS_URL)
            DelayControl.sleep(2, 5)
            if (session.browser.currentUrl != INSTAGRAM_SETTINGS_URL) {
                Log.alert("Failed to access settings")
                throw IllegalStateException("Session login issue...")
            }
        }
    }

}