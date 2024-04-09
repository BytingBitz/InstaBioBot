package au.jamal.instabiobot

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URI
import java.time.Duration

class BrowserManager(production: Boolean, debug: Boolean) {
    val browser: WebDriver

    init {
        val options = ChromeOptions()
        Log.info("Starting Selenium: production $production, debug $debug")
        if (!debug) {
            options.addArguments("--headless")
            options.addArguments("--disable-logging")
        } else {
            Log.alert("Running in debug mode...")
        }
        if (production) {
            browser = RemoteWebDriver(
                URI.create("http://selenium:4444/wd/hub").toURL(),
                options
            )
        } else {
            browser = ChromeDriver(options)
            Log.alert("Running in local mode...")
        }
        browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))
    }

    fun end() {
        browser.quit()
    }
}