package com.kursatmemis.gezilecek_yerler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.gezilecek_yerler.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kursatmemis.gezilecek_yerler.adapters.InfoAdapter
import com.kursatmemis.gezilecek_yerler.models.Info

class ProfileActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var showFormButton: ImageButton
    private lateinit var infosListView: ListView
    private val infosDataSource = mutableListOf<Info>()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        button = findViewById(R.id.button)
        showFormButton = findViewById(R.id.showFormButton)
        infosListView = findViewById(R.id.infosListView)
        database = Firebase.database.reference

        button.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        showFormButton.setOnClickListener {
            val builder = AlertDialog.Builder(this@ProfileActivity)
            val formAlertDialog = layoutInflater.inflate(R.layout.form_alert_dialog, null)
            val titleEditText = formAlertDialog.findViewById<EditText>(R.id.titleEditText)
            val cityEditText = formAlertDialog.findViewById<EditText>(R.id.cityEditText)
            val noteEditText = formAlertDialog.findViewById<EditText>(R.id.noteEditText)
            val saveInfosButton = formAlertDialog.findViewById<Button>(R.id.saveInfosButton)
            builder.setView(formAlertDialog)
            val alertDialog: AlertDialog = builder.create()
            saveInfosButton.setOnClickListener {
                val title = titleEditText.text.toString()
                val city = cityEditText.text.toString()
                val note = noteEditText.text.toString()
                if (title.isEmpty() || city.isEmpty() || note.isEmpty()) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Kaydedilmedi! Bütün alanları doldurunuz!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else {
                    val info = Info(title, city, note)
                    saveInfo(info)
                    alertDialog.dismiss()
                    Toast.makeText(this@ProfileActivity, "Başarıyla Kaydedildi!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            alertDialog.show()
        }

        val infoAdapter = InfoAdapter(this, infosDataSource)
        infosListView.adapter = infoAdapter

        val userId = Firebase.auth.currentUser?.uid!!

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                infosDataSource.clear()
                dataSnapshot.child("users").child(userId).child("infos").children.forEach {
                    val info = it.getValue(Info::class.java)
                    infosDataSource.add(info!!)
                    infoAdapter.notifyDataSetChanged()
                    Log.w("mKm - x", info.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("mKm - database", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun saveInfo(info: Info) {
        val userId = Firebase.auth.currentUser?.uid!!
        database.child("users").child(userId).child("infos").push().setValue(info)
    }
}