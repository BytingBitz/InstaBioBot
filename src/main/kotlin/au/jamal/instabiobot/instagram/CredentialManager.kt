package au.jamal.instabiobot.instagram

import io.github.cdimascio.dotenv.Dotenv

class CredentialManager(production: Boolean) {

    private val dotenv: Dotenv = Dotenv.configure().ignoreIfMissing().load()
    val username = getSecret("USER", production)
    val password = getSecret("PASS", production)

    private fun getSecret(variable: String, production: Boolean): String {
        return try {
            if (production) {
                System.getenv(variable) // Note: getenv/dotenv use different parsers
            } else {
                (dotenv[variable]).trim('\'')
            }
        } catch (e: Exception) {
            throw IllegalStateException("missing .env ['$variable']")
        }
    }

}