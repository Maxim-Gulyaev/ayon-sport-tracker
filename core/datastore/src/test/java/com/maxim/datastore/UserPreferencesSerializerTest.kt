package com.maxim.datastore

import androidx.datastore.core.CorruptionException
import com.maxim.datastore.data.AppLanguage
import com.maxim.datastore.data.DarkThemeConfig
import com.maxim.datastore.data.UserPreferences
import com.maxim.datastore.data.UserPreferences.getDefaultInstance
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UserPreferencesSerializerTest {

    private val serializer = UserPreferencesSerializer

    @Test
    fun defaultValue_contains_expected_defaults() {
        val default = serializer.defaultValue

        assertEquals(AppLanguage.SYSTEM, default.appLanguage)
        assertEquals(DarkThemeConfig.FOLLOW_BY_SYSTEM, default.darkThemeConfig)
    }


    @Test
    fun writing_and_reading_preferences_are_correct() = runTest {
        val expectedPrefs = UserPreferences.newBuilder()
            .setAppLanguage(AppLanguage.CHINESE)
            .setDarkThemeConfig(DarkThemeConfig.DARK)
            .build()

        val outputStream = ByteArrayOutputStream()

        expectedPrefs.writeTo(outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val actualPrefs = serializer.readFrom(inputStream)

        assertEquals(expectedPrefs, actualPrefs)
    }

    @Test(expected = CorruptionException::class)
    fun reading_corrupted_data_throws_CorruptionException() = runTest {
        serializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
    }
}