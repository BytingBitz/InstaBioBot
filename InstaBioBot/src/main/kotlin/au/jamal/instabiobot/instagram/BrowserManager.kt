package au.jamal.instabiobot.instagram

import au.jamal.instabiobot.utilities.Log
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URI
import java.time.Duration
import java.util.logging.Level
import kotlin.system.exitProcess

class BrowserManager(production: Boolean, debug: Boolean, timeout: Long) {

    val browser: WebDriver
    val wait: WebDriverWait

    init {
        Log.info("Starting Selenium: production [$production], debug [$debug]")
        val options = configureOptions(debug)
        browser = startBrowser(options, production)
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

    private fun configureOptions(debug: Boolean): ChromeOptions {
        val options = ChromeOptions()
        if (!debug) {
            options.addArguments("--headless")
            options.addArguments("--disable-logging")
            options.setExperimentalOption("excludeSwitches", listOf("enable-automation"))
            java.util.logging.Logger.getLogger("org.openqa.selenium").level = Level.OFF
        } else {
            Log.warn("Running in debug mode...")
        }
        return options
    }

    private fun startBrowser(options: ChromeOptions, production: Boolean): WebDriver {
        val browser: WebDriver = try {
            if (production) {
                val remoteDriver = RemoteWebDriver(
                    URI.create("http://selenium:4444/wd/hub").toURL(),
                    options
                )
                Log.status("Started production browser session")
                remoteDriver
            } else {
                val localDriver = ChromeDriver(options)
                Log.warn("Running in local mode...")
                localDriver
            }
        } catch (e: Exception) {
            Log.alert("Failed to start production browser...")
            Log.error(e)
            exitProcess(0)
        }
        return browser
    }

}