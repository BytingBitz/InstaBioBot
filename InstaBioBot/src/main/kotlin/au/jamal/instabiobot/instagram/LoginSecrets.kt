package au.jamal.instabiobot.instagram

import io.github.cdimascio.dotenv.Dotenv

object LoginSecrets {

    fun envLoad(): Pair<String, String> {
        val dotenv = Dotenv.configure().ignoreIfMissing().load()
        val username: String = dotenv["USER"] ?: System.getenv("USER") ?:
            throw IllegalStateException(".env variable USER not present")
        val password: String = dotenv["PASS"] ?: System.getenv("PASS") ?:
            throw IllegalStateException(".env variable PASS not present")
        return Pair(username, password)
    }

}