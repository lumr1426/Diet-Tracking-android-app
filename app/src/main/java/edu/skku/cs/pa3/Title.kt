package edu.skku.cs.pa3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Title : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.title)

        val et_email = findViewById<EditText>(R.id.edittext_useremail)
        val et_password = findViewById<EditText>(R.id.edittext_password)

        val btn_signup = findViewById<Button>(R.id.button_signup)
        val btn_login = findViewById<Button>(R.id.button_login)

        btn_signup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java).apply {}
            et_email.setText("")
            et_password.setText("")
            startActivity(intent)
        }

        btn_login.setOnClickListener {

            val email = et_email.getText().toString()
            val password = et_password.getText().toString()

            if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Please fill out email and password!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth = Firebase.auth

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Login Success!!",
                            Toast.LENGTH_SHORT,
                        ).show()

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val userid = currentUser?.uid.toString()
                        database = FirebaseDatabase.getInstance().reference

                        database.child("users").child(userid).child("firstlogin").addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val isFirstLogin = dataSnapshot.getValue(Boolean::class.java)
                                if (isFirstLogin == true) {
                                    database.child("users").child(userid).child("firstlogin").setValue(false)
                                    val first_intent = Intent(applicationContext, FirstLogin::class.java).apply{}
                                    startActivity(first_intent)
                                } else {
                                    val notfirst_intent = Intent(applicationContext, MainPage::class.java).apply{}
                                    startActivity(notfirst_intent)
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        })

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("tag", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }

    }
}