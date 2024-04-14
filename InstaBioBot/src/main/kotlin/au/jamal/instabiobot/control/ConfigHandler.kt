package au.jamal.instabiobot.control

import au.jamal.instabiobot.utilities.Log
import org.yaml.snakeyaml.Yaml
import java.io.File
import kotlin.system.exitProcess

data class ConfigSettings(
    val production: Boolean = true,
    val debug: Boolean = false,
    val timeout: Long = 15
)

object ConfigHandler {

    private const val CONFIG_PATH = "config.yml"

    private val configFile = File(CONFIG_PATH)
    private val configClass = ConfigSettings::class.java
    private val yaml = Yaml()

    fun loadSettings(): ConfigSettings {
        if (!configFile.exists()) {
            Log.warn("No config file exists, generated default:")
            buildConfig()
        }
        val settings = getConfig()
        Log.status("Loaded config:")
        Log.dump(settings)
        return settings
    }

    private fun buildConfig() {
        val yamlString = buildString {
            configClass.declaredFields.forEach { field ->
                field.isAccessible = true
                appendLine("${field.name}: ${field.get(ConfigSettings())}")
            }
        }
        configFile.writeText(yamlString)
    }

    private fun getConfig(): ConfigSettings {
        val inputStream = configFile.inputStream()
        inputStream.use {
            val data = yaml.load(inputStream) as Map<String, Any>
            val args = configClass.declaredFields.map { field ->
                val fieldName = field.name
                val fieldValue = data[fieldName] ?: field.get(ConfigSettings())
                fieldValue
            }.toTypedArray()
            return constructSettings(args, data)
        }
    }

    private fun constructSettings(args: Array<Any>, data: Map<String, Any>): ConfigSettings {
        try{
            val constructor = configClass.constructors.find { it.parameterCount == data.size }
                ?: throw IllegalArgumentException("No matching constructor found")
            return constructor.newInstance(*args) as ConfigSettings
        } catch (e: IllegalArgumentException) {
            Log.alert("Failed to build config, verify expected types:")
            val expectedTypes = getExpectedTypes()
            Log.dump(expectedTypes)
            Log.alert("Exiting session launch...")
            exitProcess(0)
        }
    }

    private fun getExpectedTypes(): String {
        val configFields = ConfigSettings::class.java.declaredFields
        val keyTypeArray = configFields.map { field ->
            field.name to field.type.simpleName
        }.toTypedArray()
        return keyTypeArray.contentToString()
    }

}