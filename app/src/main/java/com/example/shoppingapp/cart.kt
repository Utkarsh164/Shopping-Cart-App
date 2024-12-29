package com.example.shoppingapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class cart : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val cartItems = mutableListOf<Data>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.cartrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        loadCartData()
    }


private fun loadCartData() {
    val uid = auth.currentUser?.uid
    val emptyStateTextView = findViewById<TextView>(R.id.emptyStateTextView)

    uid?.let {
        databaseReference.child("User").child(it).child("products")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartItems.clear()


                    if (snapshot.exists() && snapshot.hasChildren()) {

                        emptyStateTextView.visibility = View.GONE
                        for (dataSnapshot in snapshot.children) {
                            val item = dataSnapshot.getValue(Data::class.java)
                            item?.let { cartItems.add(it) }
                        }

                        cartAdapter = CartAdapter(this@cart, cartItems)
                        recyclerView.adapter = cartAdapter
                    } else {

                        emptyStateTextView.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@cart, "Failed to load data", Toast.LENGTH_SHORT
                    ).show()
                    Log.e("CartActivity", error.message)
                }
            })
    }
}

}
