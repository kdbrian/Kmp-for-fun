package com.farmconnect.core.ui.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

object DateUtils {


    // Common formats
    const val FORMAT_FULL = "yyyy-MM-dd HH:mm:ss"          // 2025-08-22 14:35:07
    const val FORMAT_DATE_ONLY = "yyyy-MM-dd"              // 2025-08-22
    const val FORMAT_TIME_ONLY = "HH:mm:ss"                // 14:35:07
    const val FORMAT_READABLE = "dd MMM yyyy, hh:mm a"     // 22 Aug 2025, 02:35 PM
    const val FORMAT_DAY_NAME = "EEEE, dd MMM yyyy"        // Friday, 22 Aug 2025
    const val FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'" // 2025-08-22T14:35:07Z

    // Short / compact formats
    const val FORMAT_SHORT_DATE = "dd/MM/yy"               // 22/08/25
    const val FORMAT_SHORT_DATE_ALT = "MM/dd/yy"           // 08/22/25
    const val FORMAT_SHORT_TIME = "HH:mm"                  // 14:35
    const val FORMAT_SHORT_TIME_12H = "h:mm a"             // 2:35 PM
    const val FORMAT_MONTH_DAY = "MMM dd"                  // Aug 22
    const val FORMAT_DAY_MONTH = "dd MMM"                  // 22 Aug
    const val FORMAT_YEAR_SHORT = "yy"                     // 25
    const val FORMAT_MONTH_YEAR = "MMM yy"                 // Aug 25
    const val FORMAT_COMPACT = "yyMMddHHmm"                // 2508221435
    const val FORMAT_TIMESTAMP = "yyyyMMdd_HHmmss"         // 20250822_143507


    fun Long.toFormattedDate(format: String = FORMAT_MONTH_DAY, locale: Locale = Locale.getDefault()): String {
        val sdf = SimpleDateFormat(format, locale)
        return sdf.format(Date(this))
    }

    fun getLastFiveDays(): List<Long> {
        val today = LocalDate.now()
        val zoneId = ZoneId.systemDefault()

        return (0..4).map { daysAgo ->
            today.minusDays(daysAgo.toLong())
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli()
        }.reversed()
    }

}
