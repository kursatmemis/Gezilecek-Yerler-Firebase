package com.kursatmemis.gezilecek_yerler

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gezilecek_yerler.R
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        bindViews()

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            showProgressBar(true)
            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        var title: String
        var message: String
        if (email.isEmpty() || password.isEmpty()) {
            title = "Tüm Alanlar Doldurulmalıdır"
            message = "Lütfen email ve şifre bilgilerini doldurunuz."
            createAlertDialog(title, message)
            showProgressBar(false)
        } else {
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Firebase.auth.signOut()
                        title = "İşlem Tamamlandı"
                        message =
                            "Başarılı bir şekilde kayıt oldunuz. Giriş sayfasına yönlendirilecektsiniz."
                        createAlertDialog(title, message, true)
                        showProgressBar(false)
                    } else {
                        when (val exception = task.exception) {

                            is FirebaseAuthUserCollisionException -> {
                                title = "Kullanılan Email"
                                message =
                                    "Bu email zaten bir kullanıcıya ait. Lütfen farklı bir email giriniz."
                                createAlertDialog(title, message)
                            }

                            is FirebaseAuthWeakPasswordException -> {
                                title = "Zayıf Şifre"
                                message =
                                    "Şifreni çok zayıf. Lütfen 6 hane ve daha yüksek haneli şifre giriniz."
                                createAlertDialog(title, message)
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                title = "Geçersiz Email"
                                message =
                                    "Geçersiz email girdiniz. Lütfen geçerli bir email giriniz."
                                createAlertDialog(title, message)
                            }

                            else -> {
                                title = "Hata"
                                message = "Kayıt sırasında bir hata oluştu. Hata:\n$exception"
                                createAlertDialog(title, message)
                            }
                        }
                        showProgressBar(false)
                    }
                }
        }
    }

    private fun createAlertDialog(title: String, message: String, goBack: Boolean = false) {
        val builder = AlertDialog.Builder(this@RegisterActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        if (goBack) {
            builder.setPositiveButton("Tamam") { dialog, which ->
                finish()
            }
        } else {
            builder.setPositiveButton("Tamam") { dialog, which ->
            }
        }
        builder.show()
    }

    private fun showProgressBar(flag: Boolean) {
        if (flag) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun bindViews() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        progressBar = findViewById(R.id.progressBar)
    }
}