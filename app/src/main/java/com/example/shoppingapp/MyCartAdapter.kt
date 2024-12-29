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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class CartAdapter(private val context: Activity, private val cartList: List<Data>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("User")

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val brand: TextView = itemView.findViewById(R.id.brand)
        val price: TextView = itemView.findViewById(R.id.price)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val image: ImageView = itemView.findViewById(R.id.img)
        val delete: TextView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.box, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]

        holder.productName.text = item.productname
        holder.brand.text = item.brand
        holder.price.text = "$${item.price}"
        holder.quantity.text = "Qty: ${item.quantity}"

        Picasso.get().load(item.img).into(holder.image)

        holder.delete.setOnClickListener {
            removeItem(item)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private fun removeItem(item: Data) {
        val uid = auth.currentUser?.uid
        uid?.let {
            val productRef = databaseReference.child(it).child("products").child(item.productname!!)
            productRef.removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show()
                    Log.e("CartAdapter", e.message.toString())
                }
        }
    }
}

