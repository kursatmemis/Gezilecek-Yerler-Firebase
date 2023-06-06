package com.kursatmemis.gezilecek_yerler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.example.gezilecek_yerler.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var showFormButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        button = findViewById(R.id.button)
        showFormButton = findViewById(R.id.showFormButton)

        button.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        showFormButton.setOnClickListener {
            val formAlertDialog = layoutInflater.inflate(R.layout.form_alert_dialog, null)
            val titleEditText = formAlertDialog.findViewById<EditText>(R.id.titleEditText)
            val cityEditText = formAlertDialog.findViewById<EditText>(R.id.cityEditText)
            val noteEditText = formAlertDialog.findViewById<EditText>(R.id.noteEditText)
            val builder = AlertDialog.Builder(this@ProfileActivity)
            builder.setView(formAlertDialog)
            builder.show()
        }

    }
}