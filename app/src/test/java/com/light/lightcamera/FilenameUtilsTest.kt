package com.light.lightcamera

import android.content.SharedPreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FilenameUtilsTest {

    private class FakeSharedPreferences(
        private val map: Map<String, Any?>
    ) : SharedPreferences {
        override fun getString(key: String?, defValue: String?): String? {
            return map[key] as? String ?: defValue
        }
        override fun getStringSet(key: String?, defValues: Set<String>?): Set<String>? = TODO()
        override fun getAll(): MutableMap<String, *> = TODO()
        override fun getInt(key: String?, defValue: Int): Int = TODO()
        override fun getLong(key: String?, defValue: Long): Long = TODO()
        override fun getFloat(key: String?, defValue: Float): Float = TODO()
        override fun getBoolean(key: String?, defValue: Boolean): Boolean = TODO()
        override fun contains(key: String?): Boolean = map.containsKey(key)
        override fun edit(): SharedPreferences.Editor = TODO()
        override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) = TODO()
        override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) = TODO()
    }

    @Test
    fun getDateFormatPattern_returnsPresetPatterns() {
        val presets = mapOf(
            "MM-dd-yyyy-HH-mm-ss" to "MM-dd-yyyy-HH-mm-ss",
            "MM_dd_yyyy_HH-mm-ss" to "MM_dd_yyyy_HH-mm-ss",
            "MM.dd.yyyy.HH.mm.ss" to "MM.dd.yyyy.HH.mm.ss",
            "dd-MM-yyyy-HH-mm-ss" to "dd-MM-yyyy-HH-mm-ss",
            "dd_MM_yyyy_HH-mm-ss" to "dd_MM_yyyy_HH-mm-ss",
            "dd.MM.yyyy.HH.mm.ss" to "dd.MM.yyyy.HH.mm.ss",
            "yyyymmdd_hhmmss" to "yyyyMMdd_HHmmss",
            "yyyy_MM_dd_HH_mm_ss_SSS" to "yyyy_MM_dd_HH_mm_ss_SSS",
            "year_month_day_hour_minute_second_ms" to "yyyy_MM_dd_HH_mm_ss_SSS"
        )

        for ((setting, expectedPattern) in presets) {
            val prefs = FakeSharedPreferences(mapOf("filename_date_format" to setting))
            assertEquals("Expected pattern for $setting", expectedPattern, FilenameUtils.getDateFormatPattern(prefs))
        }
    }

    @Test
    fun getDateFormatPattern_returnsDefaultPattern_whenDefaultOrUnknown() {
        val prefsDefault = FakeSharedPreferences(mapOf("filename_date_format" to "default"))
        assertEquals(FilenameUtils.DEFAULT_PATTERN, FilenameUtils.getDateFormatPattern(prefsDefault))

        val prefsUnknown = FakeSharedPreferences(mapOf("filename_date_format" to "some_unknown_setting"))
        assertEquals(FilenameUtils.DEFAULT_PATTERN, FilenameUtils.getDateFormatPattern(prefsUnknown))
    }



    @Test
    fun getFormattedFilename_defaultsToImgPrefix() {
        val prefs = FakeSharedPreferences(mapOf("filename_date_format" to "yyyymmdd_hhmmss"))
        val filename = FilenameUtils.getFormattedFilename(prefs)
        assertTrue(filename.startsWith("IMG_"))
    }

    @Test
    fun getFormattedFilename_includesPrefix() {
        val prefs = FakeSharedPreferences(mapOf("filename_date_format" to "yyyymmdd_hhmmss"))
        val filename = FilenameUtils.getFormattedFilename(prefs, prefix = "ScreenRecord_")
        assertTrue(filename.startsWith("ScreenRecord_"))
    }
}
