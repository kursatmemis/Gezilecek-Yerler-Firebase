package com.kursatmemis.gezilecek_yerler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.gezilecek_yerler.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var resetPasswordButton: Button
    private lateinit var resetEmailEditText: EditText
    private lateinit var goLoginActivityButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        bindViews()

        resetPasswordButton.setOnClickListener {
            val email = resetEmailEditText.text.toString()
            sendResetEmail(email)
        }

        goLoginActivityButton.setOnClickListener {
            finish()
        }
    }

    private fun sendResetEmail(email: String) {
        var message: String

        if (email.isEmpty()) {
            message = "Lütfen bir email giriniz."
            showToastMessage(message)
        } else {
            Firebase.auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods
                        if (signInMethods.isNullOrEmpty()) {
                            message = "Bu e-posta adresiyle kayıtlı bir kullanıcı bulunmamaktadır."
                            showToastMessage(message)
                        } else {
                            Firebase.auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener { resetTask ->
                                    if (resetTask.isSuccessful) {
                                        message = "E-posta gönderildi."
                                    } else {
                                        message = "E-posta gönderilemedi.."
                                    }
                                    showToastMessage(message)
                                }
                        }
                    } else {
                        message = "E-posta adresi durumu alınamadı: ${task.exception}"
                        showToastMessage(message)
                    }
                }
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this@ResetPasswordActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun bindViews() {
        resetPasswordButton = findViewById(R.id.resetPasswordButton)
        resetEmailEditText = findViewById(R.id.resetEmailEditText)
        goLoginActivityButton = findViewById(R.id.goLoginActivityButton)
    }
}