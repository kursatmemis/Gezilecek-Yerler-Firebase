package com.kursatmemis.gezilecek_yerler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gezilecek_yerler.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var goToRegisterActivityTextView: TextView
    private lateinit var resetPasswordTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bindViews()
        auth = Firebase.auth

        goToRegisterActivityTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        resetPasswordTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            showProgressBar(true)
            login(email, password)
            closeKeyboard(it)
        }
    }

    private fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            val message = "Lütfen e-posta ve şifre alanlarını doldurunuz."
            showToastMessage(message)
            showProgressBar(false)
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("mKm - Login", "signInWithEmail:success")
                        showProgressBar(false)
                        reload()
                    } else {
                        val message: String = try {
                            throw task.exception!!
                        } catch (invalidEmailException: FirebaseAuthInvalidUserException) {
                            "Yanlış şifre veya e-posta adresi."
                        } catch (wrongPasswordException: FirebaseAuthInvalidCredentialsException) {
                            "Yanlış şifre veya e-posta adresi."
                        } catch (e: Exception) {
                            val taskFull = task.exception.toString()
                            "Error:\n${taskFull.substring(taskFull.indexOf(":") + 1)}"
                        }
                        showToastMessage(message)
                        showProgressBar(false)
                        Log.w("mKm - Login", "signInWithEmail:failure", task.exception)
                    }
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showProgressBar(flag: Boolean) {
        if (flag) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun bindViews() {
        goToRegisterActivityTextView = findViewById(R.id.goToRegisterActivityTextView)
        resetPasswordTextView = findViewById(R.id.resetPasswordTextView)
        loginButton = findViewById(R.id.loginButton)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        progressBar = findViewById(R.id.progressBar)
    }

}