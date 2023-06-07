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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.kursatmemis.gezilecek_yerler.adapters.TravelInfoAdapter
import com.kursatmemis.gezilecek_yerler.models.TravelInfo

class ProfileActivity : AppCompatActivity() {

    private lateinit var signOutButton: Button
    private lateinit var showFormButton: ImageButton
    private lateinit var travelInfoListView: ListView
    private val travelInfosDataSource = mutableListOf<TravelInfo>()
    private lateinit var database: DatabaseReference
    private lateinit var travelInfoAdapter: TravelInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bindViews()
        database = FirebaseDatabase.getInstance().reference
        travelInfoAdapter = TravelInfoAdapter(this, travelInfosDataSource)
        travelInfoListView.adapter = travelInfoAdapter

        signOutButton.setOnClickListener {
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
            val saveTravelInfoButton = formAlertDialog.findViewById<Button>(R.id.saveInfosButton)
            builder.setView(formAlertDialog)
            val alertDialog: AlertDialog = builder.create()
            saveTravelInfoButton.setOnClickListener {
                var title = titleEditText.text.toString()
                var city = cityEditText.text.toString()
                var note = noteEditText.text.toString()
                if (title.isEmpty() || city.isEmpty() || note.isEmpty()) {
                    title = "---"
                    city = "---"
                    note = "---"
                }
                val travelInfo = TravelInfo(title, city, note)
                saveTravelInfo(travelInfo)
                alertDialog.dismiss()
                Toast.makeText(this@ProfileActivity, "Başarıyla Kaydedildi!", Toast.LENGTH_SHORT)
                    .show()
            }
            alertDialog.show()
        }

        travelInfoListView.setOnItemLongClickListener { parent, view, position, id ->
            val item =
                travelInfosDataSource[position]
            val title = item.title
            val city = item.city
            val note = item.note
            val userId = Firebase.auth.currentUser?.uid

            val reference =
                FirebaseDatabase.getInstance().getReference("users/$userId/infos")

            reference.orderByChild("title").equalTo(title)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children) {
                            val cityValue = dataSnapshot.child("city").value as String?
                            val noteValue = dataSnapshot.child("note").value as String?

                            if (cityValue == city && noteValue == note) {
                                dataSnapshot.ref.removeValue()
                                    .addOnSuccessListener {
                                        Log.d("mKm-database", "Veri silindi.")
                                        travelInfosDataSource.remove(item)
                                        travelInfoAdapter.notifyDataSetChanged()
                                    }
                                    .addOnFailureListener { error ->
                                        Log.e("mKm-database", "Veri silinemedi: ${error.message}")
                                    }
                                break
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("mKm-database", "Veri silinemedi: ${error.message}")
                    }
                })

            true
        }

        val userId = Firebase.auth.currentUser?.uid

        database.child("users").child(userId!!).child("infos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    travelInfosDataSource.clear()
                    for (infoSnapshot in dataSnapshot.children) {
                        val travelInfo = infoSnapshot.getValue(TravelInfo::class.java)
                        travelInfo?.let { travelInfosDataSource.add(it) }
                    }
                    travelInfoAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("mKm - database", "loadPost:onCancelled", databaseError.toException())
                }
            })
    }

    private fun saveTravelInfo(travelInfo: TravelInfo) {
        val userId = Firebase.auth.currentUser?.uid
        database.child("users").child(userId!!).child("infos").push().setValue(travelInfo)
    }

    private fun bindViews() {
        signOutButton = findViewById(R.id.signOutButton)
        showFormButton = findViewById(R.id.showFormButton)
        travelInfoListView = findViewById(R.id.travelInfoListView)
    }
}
