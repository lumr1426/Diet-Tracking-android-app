package edu.skku.cs.pa3

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.`activity_signup`)

        val et_email = findViewById<EditText>(R.id.edittext_newemail)
        val et_password = findViewById<EditText>(R.id.edittext_age)
        val btn_confirm = findViewById<Button>(R.id.button_confirm)

        btn_confirm.setOnClickListener {
            val email = et_email.getText().toString()
            val password = et_password.getText().toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(
                    applicationContext,
                    "Please fill out email and password!!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth = Firebase.auth


            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Sign Up processed successfully!!",
                            Toast.LENGTH_SHORT,
                        ).show()

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val userid = currentUser?.uid.toString()
                        database = FirebaseDatabase.getInstance().reference
                        database.child("users").child(userid).child("firstlogin").setValue(true)
                        // return to title
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("tag", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            return@setOnClickListener
        }
    }
}
