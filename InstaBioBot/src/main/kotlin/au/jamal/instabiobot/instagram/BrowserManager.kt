package au.jamal.instabiobot.instagram

import au.jamal.instabiobot.utilities.Log
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URI
import java.time.Duration

class BrowserManager(production: Boolean, debug: Boolean, timeout: Long) {

    val browser: WebDriver
    val wait: WebDriverWait

    init {
        val options = ChromeOptions()
        Log.info("Starting Selenium: production [$production], debug [$debug]")
        if (!debug) {
            options.addArguments("--headless")
            options.addArguments("--disable-logging")
        } else {
            Log.warn("Running in debug mode...")
        }
        if (production) {
            browser = RemoteWebDriver(
                URI.create("http://selenium:4444/wd/hub").toURL(),
                options
            )
        } else {
            browser = ChromeDriver(options)
            Log.warn("Running in local mode...")
        }
        browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout))
        wait = WebDriverWait(browser, Duration.ofSeconds(timeout))
    }

    fun end() {
        Log.warn("Killing Selenium session...")
        try {
            browser.close()
            browser.quit()
            Log.status("Selenium session ended...")
        } catch (e: Exception) {
            Log.alert("Failed to kill selenium driver")
        }
    }

}