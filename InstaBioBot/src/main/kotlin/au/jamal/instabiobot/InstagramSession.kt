package au.jamal.instabiobot

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import kotlin.random.Random

class InstagramSession (production: Boolean, debug: Boolean) {

    private val session: BrowserManager = BrowserManager(production, debug)
    private val sessionWait: WebDriverWait = WebDriverWait(session.browser, Duration.ofSeconds(10))
    private val randomGenerator = Random.Default

    fun login(username: String, password: String) {
        session.browser.get("https://www.instagram.com/")
        sleepDelay(5, 10)
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
        sleepDelay(5, 10)
        // Verify login, put status for login success
    }

    fun end() {
        Log.status("Killing Selenium session...")
        session.end()
    }

    private fun sleepDelay(minDuration: Int = 5, maxDuration: Int = 10) {
        val durationSeconds = randomGenerator.nextInt(minDuration, maxDuration + 1)
        val durationMillis = durationSeconds * 1000L
        Thread.sleep(durationMillis)
    }

}