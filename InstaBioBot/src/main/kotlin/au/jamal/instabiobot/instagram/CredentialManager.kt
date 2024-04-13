package au.jamal.instabiobot.instagram

import io.github.cdimascio.dotenv.Dotenv

class CredentialManager {

    private val dotenv: Dotenv = Dotenv.configure().ignoreIfMissing().load()
    val username = getSecret("USER")
    val password = getSecret("PASS")

    private fun getSecret(variable: String): String {
        return dotenv[variable] ?: System.getenv(variable) ?: throw IllegalStateException(".env variable [$variable] not present")
    }

}