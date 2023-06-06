package com.kursatmemis.gezilecek_yerler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.gezilecek_yerler.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var goToRegisterActivityTextView: TextView
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

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            login(email, password)
            showProgressBar(true)
        }
    }

    private fun login(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this@LoginActivity,
                "Lütfen email ve şifre alanlarını doldurunuz.",
                Toast.LENGTH_SHORT
            ).show()
            showProgressBar(false)
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("mKm - Login", "signInWithEmail:success")
                        showProgressBar(false)
                        reload()
                    } else {
                        val taskFull = task.exception.toString()
                        val message = taskFull.substring(taskFull.indexOf(":")+1)
                        Toast.makeText(
                            this@LoginActivity,
                            message,
                            Toast.LENGTH_LONG
                        ).show()
                        showProgressBar(false)
                        Log.w("mKm - Login", "signInWithEmail:failure", task.exception)
                    }
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
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

    private fun showProgressBar(flag: Boolean) {
        if (flag) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun bindViews() {
        goToRegisterActivityTextView = findViewById(R.id.goToRegisterActivityTextView)
        loginButton = findViewById(R.id.loginButton)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        progressBar = findViewById(R.id.progressBar)
    }

}