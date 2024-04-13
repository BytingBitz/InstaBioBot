package au.jamal.instabiobot.control

import au.jamal.instabiobot.utilities.Log
import org.yaml.snakeyaml.Yaml
import java.io.File

data class ConfigSettings(
    val production: Boolean = true,
    val debug: Boolean = false,
    val timeout: Long = 15
)

object ConfigHandler {

    private const val CONFIG_PATH = "config.yml"

    private val configFile = File(CONFIG_PATH)
    private val yaml = Yaml()

    fun loadConfig(): ConfigSettings {
        Log.status("Loading config...")
        if (!configFile.exists()) {
            Log.warn("No config file exists, generated default:")
            buildConfig()
        }
        val inputStream = configFile.inputStream()
        val settings = inputStream.use {
            val data = yaml.load(inputStream) as Map<String, Any>
            val production = data["production"] as Boolean
            val debug = data["debug"] as Boolean
            val timeout = (data["timeout"] as Number).toLong()
            ConfigSettings(production, debug, timeout)
        } // TODO: Add error handling on value loading
        Log.dump(settings)
        return settings
    }

    private fun buildConfig() {
        // TODO: Implement effective build
    }

}