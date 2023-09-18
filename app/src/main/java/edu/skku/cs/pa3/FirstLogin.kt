package edu.skku.cs.pa3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class FirstLogin : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    fun writeNewUser(userId: String, username: String, age: Int, gender: String, height: Double, weight: Double) {
        val user = User(username, age, gender, height, weight)
        database.child("users").child(userId).setValue(user)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_login)

        val et_username = findViewById<EditText>(R.id.edittext_username)
        val et_age = findViewById<EditText>(R.id.edittext_age)
        val et_height = findViewById<EditText>(R.id.edittext_height)
        val et_weight = findViewById<EditText>(R.id.edittext_weight)
        val cb_male = findViewById<CheckBox>(R.id.checkBox_male)
        val switch_female = findViewById<CheckBox>(R.id.checkBox_female)
        val btn_confirm = findViewById<Button>(R.id.button_confirm2)

        btn_confirm.setOnClickListener {
            val username = et_username.getText().toString()
            val age = et_age.getText().toString()
            val height = et_height.getText().toString()
            val weight = et_weight.getText().toString()

            val hasNull = listOf(username, age, height, weight).any {it.isNullOrEmpty()}

            if(hasNull) {
                Toast.makeText(applicationContext, "Please fill out every blank forms!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(cb_male.isChecked&&switch_female.isChecked) {
                Toast.makeText(applicationContext, "Please check only one gender!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(!cb_male.isChecked&&!switch_female.isChecked) {
                Toast.makeText(applicationContext, "Please check at least one gender!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender: String
            val intage = age.toInt()
            val doubleheight = height.toDouble()
            val doubleweight = weight.toDouble()

            gender = if(cb_male.isChecked) {
                "male"
            } else "female"

            val currentUser = FirebaseAuth.getInstance().currentUser
            val userid = currentUser?.uid.toString()

            database = FirebaseDatabase.getInstance().reference

            writeNewUser(userid, username, intage, gender, doubleheight, doubleweight)

            val intent = Intent(this, MainPage::class.java).apply{}
            startActivity(intent)
        }
    }
}