package au.jamal.instabiobot.utilities

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object TextConstructor {

    // buildText called every ~2s, bio updates when return is not current text
    // NOTE: it is strongly recommended to not update more than once each hour

    fun buildText(): String {
        val currentDateTime = LocalDateTime.now(ZoneId.of("Australia/Queensland"))
        val hourFormatter = DateTimeFormatter.ofPattern("h a")
        val hour = currentDateTime.format(hourFormatter).lowercase(Locale.getDefault())
            .replaceFirst("^0".toRegex(), "")
        val dayOfWeek = currentDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        return "It is $hour on a $dayOfWeek..."
    }

}