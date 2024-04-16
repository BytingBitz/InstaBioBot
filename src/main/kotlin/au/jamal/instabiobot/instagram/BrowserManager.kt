package au.jamal.instabiobot.instagram

import au.jamal.instabiobot.control.SessionController
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

class BrowserManager() {

    private val options: ChromeOptions = configureOptions()
    private val timeout: Duration = Duration.ofSeconds(SessionController.config.timeoutSeconds)
    val browser: WebDriver = startBrowser(options)
    val wait = WebDriverWait(browser, timeout)

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

    private fun configureOptions(): ChromeOptions {
        val options = ChromeOptions()
        if (SessionController.config.debugMode) {
            options.addArguments("--headless")
            options.addArguments("--disable-logging")
            options.setExperimentalOption("excludeSwitches", listOf("enable-automation"))
            java.util.logging.Logger.getLogger("org.openqa.selenium").level = Level.OFF
        } else {
            Log.warn("Running in debug mode...")
        }
        return options
    }

    private fun startBrowser(options: ChromeOptions): WebDriver {
        val browser: WebDriver = try {
            if (SessionController.config.productionMode) {
                val remoteDriver = RemoteWebDriver(URI.create("http://selenium:4444/wd/hub").toURL(), options)
                Log.status("Started production browser session")
                remoteDriver
            } else {
                val localDriver = ChromeDriver(options)
                Log.warn("Running in local mode...")
                localDriver
            }
        } catch (e: Exception) {
            Log.alert("Failed to start browser...")
            Log.error(e)
            exitProcess(0)
        }
        browser.manage().timeouts().implicitlyWait(timeout)
        return browser
    }

}