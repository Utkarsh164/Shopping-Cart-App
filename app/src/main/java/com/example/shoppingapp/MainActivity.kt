package com.example.shoppingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var myAdaptar: MyAdaptar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var countTextView: TextView

    override fun onStart() {
        super.onStart()
        // Realtime listener for cart count
        setupRealtimeCartCountListener()
        fetchRecyclerViewData()
    }

    private fun setupRealtimeCartCountListener() {
        val userId = mAuth.currentUser?.uid
        userId?.let {
            databaseReference.child("User").child(it).child("products")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val count = snapshot.childrenCount.toInt()
                        if (count > 0) {
                            countTextView.visibility = TextView.VISIBLE
                            countTextView.text = count.toString()
                        } else {
                            countTextView.visibility = TextView.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("MainActivity", "Failed to fetch count: ${error.message}")
                    }
                })
        }
    }

    private fun fetchRecyclerViewData() {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiinterface::class.java)

        val retrofitData = retrofitBuilder.getProductData()
        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                val responseBody = response.body()
                val productList = responseBody?.products!!

                myAdaptar = MyAdaptar(this@MainActivity, productList) {
                    // Callback for quantity change to update count
                    setupRealtimeCartCountListener()
                }

                recyclerView.adapter = myAdaptar
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("MainActivity", "onFailure: ${t.message}")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        recyclerView = findViewById(R.id.list)
        countTextView = findViewById(R.id.count)

        val cartbtn = findViewById<ImageView>(R.id.cartIcon)
        cartbtn.setOnClickListener {
            val i = Intent(this, cart::class.java)
            startActivity(i)
        }

        fetchRecyclerViewData()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            mAuth.signOut()
            val intent = Intent(this@MainActivity, Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}
