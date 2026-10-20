package com.light.lightcamera

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.Locale

object FilenameUtils {
    const val DEFAULT_PATTERN = "yyyy-MM-dd-HH-mm-ss-SSS"

    fun getDateFormatPattern(prefs: SharedPreferences): String {
        val setting = prefs.getString("filename_date_format", "default") ?: "default"
        return when (setting) {
            "MM-dd-yyyy-HH-mm-ss" -> "MM-dd-yyyy-HH-mm-ss"
            "MM_dd_yyyy_HH-mm-ss" -> "MM_dd_yyyy_HH-mm-ss"
            "MM.dd.yyyy.HH.mm.ss" -> "MM.dd.yyyy.HH.mm.ss"
            "dd-MM-yyyy-HH-mm-ss" -> "dd-MM-yyyy-HH-mm-ss"
            "dd_MM_yyyy_HH-mm-ss" -> "dd_MM_yyyy_HH-mm-ss"
            "dd.MM.yyyy.HH.mm.ss" -> "dd.MM.yyyy.HH.mm.ss"
            "yyyymmdd_hhmmss" -> "yyyyMMdd_HHmmss"
            "yyyy_MM_dd_HH_mm_ss_SSS", "year_month_day_hour_minute_second_ms" -> "yyyy_MM_dd_HH_mm_ss_SSS"
            else -> DEFAULT_PATTERN
        }
    }

    fun getFormattedFilename(prefs: SharedPreferences, prefix: String = "IMG_"): String {
        val pattern = getDateFormatPattern(prefs)
        val timestamp = System.currentTimeMillis()
        val formattedDate = try {
            SimpleDateFormat(pattern, Locale.US).format(timestamp)
        } catch (_: Exception) {
            SimpleDateFormat(DEFAULT_PATTERN, Locale.US).format(timestamp)
        }
        return prefix + formattedDate
    }

    fun getFormattedFilename(context: Context, prefix: String = "IMG_"): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return getFormattedFilename(prefs, prefix)
    }
}
