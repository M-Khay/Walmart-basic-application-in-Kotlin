package com.walmart.labs.tha.ui.controllers

import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.walmart.labs.tha.R
import com.walmart.labs.tha.api.testserver.data.Product
import java.lang.ref.WeakReference

/**
 * Paged List adapter that will display a List of [Product] in it's detailed page form.
 */
class ProductListDetailAdapter(itemLoadListener: () -> Unit) :
    PagedListAdapter<Product, ProductListDetailAdapter.ProductViewHolder>(ProductItemCallback()) {

    private var itemLoadListenerReference: WeakReference<() -> Unit>? = WeakReference(itemLoadListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.onBind(item, itemLoadListenerReference)
        }
    }

    fun clearItemLoadListener() {
        itemLoadListenerReference = null
    }

    class ProductViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
        layoutInflater.inflate(R.layout.list_item_product_detail, parent, false)
    ) {

        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        private val productInStock: TextView = itemView.findViewById(R.id.productInStock)
        private val productShortDescription: TextView = itemView.findViewById(R.id.productShortDescription)
        private val aboutLabel: TextView = itemView.findViewById(R.id.about_label)
        private val productLongDescription: TextView = itemView.findViewById(R.id.productLongDescription)
        private val productRating: RatingBar = itemView.findViewById(R.id.productRating)
        private val productRatingText: TextView = itemView.findViewById(R.id.productRatingText)
        private val addToCard: Button = itemView.findViewById(R.id.addToCard)

        fun onBind(product: Product, imageLoadListener: WeakReference<() -> Unit>?) {

            val context = itemView.context

            // Image
            if (imageLoadListener != null) { // While we have a listener, notify it
                val callback = object : Callback {
                    override fun onSuccess() {
                        imageLoadListener.get()?.invoke()
                    }

                    override fun onError(e: Exception?) {
                        Log.e(TAG, "Failed to load image: $e")
                    }
                }
                Picasso.get().load(product.productImage).into(productImage, callback)
            } else {
                Picasso.get().load(product.productImage).into(productImage)
            }
            productImage.transitionName = product.index.toString()
            productImage.contentDescription = context.getString(R.string.cont_desc_product_image, product.productName)

            // Name
            productName.text = product.productName

            // Description
            val shortDescHtml = HtmlCompat.fromHtml(
                product.shortDescription, HtmlCompat.FROM_HTML_MODE_LEGACY
            ).trim()
            productShortDescription.text = SpannableString(shortDescHtml)

            val longDescHtml = HtmlCompat.fromHtml(
                product.longDescription, HtmlCompat.FROM_HTML_MODE_LEGACY
            ).trim()
            val longDesc = SpannableString(longDescHtml)
            val longDescVis = if (longDesc.isEmpty()) {
                View.GONE
            } else {
                productLongDescription.text = longDesc
                View.VISIBLE
            }
            productLongDescription.visibility = longDescVis
            aboutLabel.visibility = longDescVis

            // Rating
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

            // Add item to cart
            addToCard.text = context.getString(R.string.add_to_cart, product.price)
            addToCard.setOnClickListener {
                // Delegate click here
                Toast.makeText(context, R.string.added_to_card, Toast.LENGTH_LONG).show()
            }
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

    companion object {
        const val TAG = "ProductDetailAdapter"
    }
}
