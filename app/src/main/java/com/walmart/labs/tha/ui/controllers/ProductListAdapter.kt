package com.walmart.labs.tha.ui.controllers

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.walmart.labs.tha.R
import com.walmart.labs.tha.api.testserver.data.Product
import java.lang.Exception

/**
 * Paged List adapter that will display a List of [Product] in it's list form.
 */
class ProductListAdapter(
        private val itemClickListener: (View, Int, String) -> Unit
) : PagedListAdapter<Product, ProductListAdapter.ProductViewHolder>(ProductItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.onBind(item, position, itemClickListener)
        }
    }

    class ProductViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.list_item_product, parent, false)) {

        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val productRating: RatingBar = itemView.findViewById(R.id.productRating)
        private val productRatingText: TextView = itemView.findViewById(R.id.productRatingText)
        private val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        private val productInStock: TextView = itemView.findViewById(R.id.productInStock)

        fun onBind(product: Product, position: Int, itemClickListener: (View, Int, String) -> Unit) {

            val context = itemView.context
            itemView.setOnClickListener { itemClickListener.invoke(productImage, position, productImage.transitionName) }

            productName.text = product.productName

            if (product.reviewCount > 0) {
                productRatingText.visibility = View.VISIBLE
                productRatingText.text = context.getString(R.string.rating_text, product.reviewCount)
                productRating.visibility = View.VISIBLE
                productRating.numStars = 5
                productRating.rating = product.reviewRating
            } else {
                productRatingText.visibility = View.GONE
                productRating.visibility = View.GONE
            }

            productPrice.text = product.price

            if (product.inStock) {
                productInStock.setText(R.string.in_stock)
                productInStock.setTextColor(ContextCompat.getColor(context, R.color.colorPositive))
            } else {
                productInStock.setText(R.string.out_of_stock)
                productInStock.setTextColor(ContextCompat.getColor(context, R.color.colorNegative))
            }

            productImage.transitionName = product.index.toString()
            Picasso.get()
                    .load(product.productImage)
                    .placeholder(R.drawable.image_loading_placeholder)
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(productImage, object : Callback{
                        override fun onSuccess() {
                        }

                        override fun onError(e: Exception?) {
                            Log.v("ProductListAdapter", "Loading image failed: "+ e?.message)
                        }

                    })
            Log.v("ProductListAdapter", "Loading image: ${product.productImage}")
        }
    }

    class ProductItemCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
