package au.jamal.instabiobot.instagram

import au.jamal.instabiobot.control.SessionController
import io.github.cdimascio.dotenv.Dotenv

class CredentialManager() {

    private val dotenv: Dotenv = Dotenv.configure().ignoreIfMissing().load()
    val username = getSecret("USER")
    val password = getSecret("PASS")

    private fun getSecret(variable: String): String {
        return try {
            if (SessionController.config.productionMode) {
                System.getenv(variable) // docker-compose requires 's to block variable substitution
            } else {
                dotenv[variable].trim('\'') // local does no variable substitution, trim 's
            }
        } catch (e: Exception) {
            throw IllegalStateException("missing .env ['$variable']")
        }
    }

}