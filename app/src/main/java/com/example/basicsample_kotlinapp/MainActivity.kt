package com.example.basicsample_kotlinapp

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import java.util.Calendar


class MainActivity : Activity() {

    private val TAG = "MainActivity"

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var nameText: EditText

    private lateinit var dobPicker: DatePicker

    private lateinit var emailText: EditText

    private var emailValidator = EmailValidator()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameText = findViewById(R.id.userNameInput)
        dobPicker = findViewById(R.id.dateOfBirthInput)
        emailText = findViewById(R.id.emailInput)

        emailText.addTextChangedListener(emailValidator)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferencesHelper = SharedPreferencesHelper(sharedPreferences)

        populateUi()

    }

    private fun populateUi() {

        val sharedPreferenceEntry = sharedPreferencesHelper.getPersonalInfo()
        nameText.setText(sharedPreferenceEntry.name)
        val dateOfBirth = sharedPreferenceEntry.dateOfBirth
        dobPicker.init(dateOfBirth.get(Calendar.YEAR), dateOfBirth.get(Calendar.MONTH),
            dateOfBirth.get(Calendar.DAY_OF_MONTH), null)
        emailText.setText(sharedPreferenceEntry.email)

    }

    fun onSaveClick(@Suppress("UNUSED_PARAMETER") view: View) {

        if (!emailValidator.isValid) {

        emailText.error = "Invalid email"

            Log.w(TAG, "Not saving personal information: Invalid email")
            return

        }

        val name = nameText.text.toString()
        val dateOfBirth = Calendar.getInstance()
        dateOfBirth.set(dobPicker.year, dobPicker.month, dobPicker.dayOfMonth)
        val email = emailText.text.toString()

        val sharedPreferenceEntry = SharedPreferenceEntry(name, dateOfBirth, email)

        val isSuccess = sharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry)

        if (isSuccess) {
            Toast.makeText(this, "Personal information saved", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Personal information saved")
        } else {
            Log.e(TAG, "Failed to write personal information to SharedPreferences")
        }
    }

    fun onRevertClick(@Suppress("UNUSED_PARAMETER") view: View) {

        populateUi()
        Toast.makeText(this, "Personal information reverted", Toast.LENGTH_LONG).show()
        Log.i(TAG, "Personal information reverted")

    }
}