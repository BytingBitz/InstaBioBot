package au.jamal.instabiobot.instagram

import au.jamal.instabiobot.utilities.Log
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class InstagramInterface(session: BrowserManager) {

    private val wait: WebDriverWait = session.wait

    fun getUsernameElement(): WebElement {
        return getElement(By::cssSelector, "input[name='username']")
    }

    fun getPasswordElement(): WebElement {
        return getElement(By::cssSelector, "input[name='password']")
    }

    fun getLoginElement(): WebElement {
        return getElement(By::xpath, "//button[@type='submit']")
    }

    fun getBioElement(): WebElement {
        return getElement(By::cssSelector, "textarea[id='pepBio']")
    }

    fun getUpdateElement(): WebElement {
        return getElement(By::xpath, "//*[contains(text(), 'Submit')]")
    }

    fun getBioTextAttribute(): String {
        val bioElement = getBioElement()
        return getAttribute(bioElement, "value")
    }

    fun getUpdateAriaAttribute(): String {
        val updateElement = getUpdateElement()
        return getAttribute(updateElement, "aria-disabled")
    }

    private fun getElement(selector: (String) -> By, expression: String): WebElement {
        try {
            val locator: By = selector(expression)
            return wait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
            )
        } catch (e: Exception) {
            Log.alert("Failed to access element: [$selector, $expression]")
            throw IllegalStateException("Failed to get element...", e)
        }
    }

    private fun getAttribute(element: WebElement, attribute: String): String {
        try {
            return element.getAttribute(attribute) ?: throw IllegalStateException("Null attribute")
        } catch (e: Exception) {
            Log.alert("Failed to access attribute [$attribute]")
            Log.dump(element)
            throw IllegalStateException("Failed to get attribute...", e)
        }
    }

}