package au.jamal.instabiobot

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDateTime

const val INSTAGRAM_URL = "https://www.instagram.com/"
const val INSTAGRAM_SETTINGS_URL = "https://www.instagram.com/accounts/edit/"

class InstagramSession (production: Boolean, debug: Boolean) {

    private val session: BrowserManager = BrowserManager(production, debug)
    private val sessionWait: WebDriverWait = WebDriverWait(session.browser, Duration.ofSeconds(10))

    fun login(username: String, password: String) {
        session.browser.get(INSTAGRAM_URL)
        DelayControl.sleep(5, 10)
        val usernameInput = sessionWait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='username']"))
        )
        val passwordInput = sessionWait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='password']"))
        )
        usernameInput.sendKeys(username)
        passwordInput.sendKeys(password)
        val loginButton = sessionWait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@type='submit']"))
        )
        loginButton.click()
        DelayControl.sleep(5, 10)
        // Verify login
        Log.status("Login successful at ${LocalDateTime.now()}")
    }

    fun getCurrentBio (): String {
        navigateInstagramSettings()
        val bioElement = sessionWait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("textarea[id='pepBio']"))
        )
        val bioText = bioElement.getAttribute("value")
        Log.status("Current bio text: $bioText at ${LocalDateTime.now()}")
        return bioText
    }

    fun updateBio (newBioText: String) {
        navigateInstagramSettings()
        val bioElement = sessionWait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("textarea[id='pepBio']"))
        )
        bioElement.clear()
        bioElement.sendKeys(newBioText)
        val updateButton = sessionWait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'submit')]"))
        )
        updateButton.click()
        // Verify bio update
        Log.status("Updated bio text: $newBioText at ${LocalDateTime.now()}")
    }

    fun end() {
        Log.warn("Killing Selenium session...")
        session.end()
    }

    private fun navigateInstagramSettings() {
        if (session.browser.currentUrl != INSTAGRAM_SETTINGS_URL) {
            session.browser.get(INSTAGRAM_SETTINGS_URL)
            DelayControl.sleep(2, 5)
            if (session.browser.currentUrl != INSTAGRAM_SETTINGS_URL) {
                throw IllegalStateException("Failed to access settings...")
            }
        }
    }
}