package com.example.basicsample_kotlinapp

import android.content.SharedPreferences
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {

    private val TEST_NAME = "Test name"
    private val TEST_EMAIL = "test@email.com"
    private val TEST_DATE_OF_BIRTH = Calendar.getInstance().apply { set(1980, 1, 1) }

    private lateinit var sharedPreferenceEntry: SharedPreferenceEntry
    private lateinit var mockSharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var mockBrokenSharedPreferencesHelper: SharedPreferencesHelper

    @Mock private lateinit var mockSharedPreferences: SharedPreferences
    @Mock private lateinit var mockBrokenSharedPreferences: SharedPreferences
    @Mock private lateinit var mockEditor: SharedPreferences.Editor
    @Mock private lateinit var mockBrokenEditor: SharedPreferences.Editor

    @Before fun initMocks() {

        sharedPreferenceEntry = SharedPreferenceEntry(TEST_NAME, TEST_DATE_OF_BIRTH, TEST_EMAIL)

        mockSharedPreferencesHelper = createMockSharedPreference()

        mockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference()

    }

    @Test fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        assertTrue(mockSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry))

        val savedEntry = mockSharedPreferencesHelper.getPersonalInfo()

        assertEquals(sharedPreferenceEntry.name, savedEntry.name)
        assertEquals(sharedPreferenceEntry.dateOfBirth, savedEntry.dateOfBirth)
        assertEquals(sharedPreferenceEntry.email, savedEntry.email)

    }

    @Test fun sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse()  {
        assertFalse(mockBrokenSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry))

    }

    private fun createMockSharedPreference(): SharedPreferencesHelper {

        given(mockSharedPreferences.getString(eq(SharedPreferencesHelper.KEY_NAME), anyString()))
            .willReturn(sharedPreferenceEntry.name)

        given(mockSharedPreferences.getString(eq(SharedPreferencesHelper.KEY_EMAIL), anyString()))
            .willReturn(sharedPreferenceEntry.email)


        given(mockSharedPreferences.getLong(eq(SharedPreferencesHelper.KEY_DOB), anyLong()))
            .willReturn(sharedPreferenceEntry.dateOfBirth.timeInMillis)


        given(mockEditor.commit()).willReturn(true)

        given(mockSharedPreferences.edit()).willReturn(mockEditor)

        return SharedPreferencesHelper(mockSharedPreferences)

    }

    private fun createBrokenMockSharedPreference(): SharedPreferencesHelper {

        given(mockBrokenEditor.commit()).willReturn(false)

        given(mockBrokenSharedPreferences.edit()).willReturn(mockBrokenEditor)

        return SharedPreferencesHelper(mockBrokenSharedPreferences)

    }
}