package com.example.shoppingapp

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class MyAdaptar(
    val context: Activity,
    val productArrayList: List<Product>,
    private val onQuantityChanged: () -> Unit
) : RecyclerView.Adapter<MyAdaptar.MyViewHolder>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("User")

    class MyViewHolder(val itemview: View) : RecyclerView.ViewHolder(itemview) {
        val product_name = itemview.findViewById<TextView>(R.id.product_name)
        val brand = itemview.findViewById<TextView>(R.id.brand)
        var price = itemview.findViewById<TextView>(R.id.price)
        val img = itemview.findViewById<ImageView>(R.id.img)
        val rate = itemview.findViewById<TextView>(R.id.rating)

        val increment = itemview.findViewById<ImageView>(R.id.increment)
        val decrement = itemview.findViewById<ImageView>(R.id.decrement)
        val quantity = itemview.findViewById<TextView>(R.id.quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inf = LayoutInflater.from(parent.context).inflate(R.layout.dekhn, parent, false)
        return MyViewHolder(inf)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cur = productArrayList[position]


        holder.brand.text = cur.brand
        holder.product_name.text = cur.title
        holder.price.text = "$" + cur.price.toString()
        holder.rate.text = cur.rating.toString() + " Rating"
        Picasso.get().load(cur.thumbnail).into(holder.img)


        fetchQuantityFromFirebase(holder, cur)


        holder.increment.setOnClickListener {
            val currentQuantity = holder.quantity.text.toString().toInt()
            val newQuantity = currentQuantity + 1
            holder.quantity.text = newQuantity.toString()
            updateFirebase(cur, newQuantity)
            onQuantityChanged.invoke()
        }

        // Decrement button click
        holder.decrement.setOnClickListener {
            val currentQuantity = holder.quantity.text.toString().toInt()
            if (currentQuantity > 0) {
                val newQuantity = currentQuantity - 1
                holder.quantity.text = newQuantity.toString()
                updateFirebase(cur, newQuantity)
                onQuantityChanged.invoke()
            }
        }
    }

    override fun getItemCount(): Int {
        return productArrayList.size
    }

    private fun fetchQuantityFromFirebase(holder: MyViewHolder, product: Product) {
        val user = auth.currentUser
        user?.let {
            val uid = it.uid
            val productKey = product.title
            val userProductsRef = databaseReference.child(uid).child("products").child(productKey)

            userProductsRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val storedQuantity =
                        snapshot.child("quantity").getValue(String::class.java)?.toInt() ?: 0
                    holder.quantity.text = storedQuantity.toString()
                } else {
                    holder.quantity.text = "0"
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseError", e.message.toString())
            }
        }
    }

    private fun updateFirebase(product: Product, quantity: Int) {
        val user = auth.currentUser
        user?.let {
            val uid = it.uid
            val productKey = product.title
            val userProductsRef = databaseReference.child(uid).child("products").child(productKey)

            if (quantity > 0) {
                val data = Data(
                    productname = product.title,
                    brand = product.brand,
                    img = product.thumbnail,
                    id = product.id.toString(),
                    price = product.price.toString(),
                    quantity = quantity.toString()
                )
                userProductsRef.setValue(data)
            } else {
                userProductsRef.removeValue()
            }
        }
    }
}
