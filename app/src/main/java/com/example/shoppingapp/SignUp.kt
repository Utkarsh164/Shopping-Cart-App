package com.example.shoppingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtName: EditText
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        mAuth=FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        edtName=findViewById(R.id.edt_name)
        btnSignUp = findViewById(R.id.btn_signup)

        btnSignUp.setOnClickListener {
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()
            val name=edtName.text.toString()
            if(email.isEmpty() || password.isEmpty() ||name.isEmpty())
            {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            else{
                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {
                        if(it.isSuccessful)
                        {
                            mDbRef= FirebaseDatabase.getInstance().reference

                            mDbRef.child("user").child(mAuth.currentUser?.uid!!).setValue(User(name,email,mAuth.currentUser?.uid!!))


                            Toast.makeText(this,"Registration Successful", Toast.LENGTH_SHORT).show()
                            val i=Intent(this,MainActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                        else
                        {
                            Toast.makeText(this,"Registration Failed :${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}

