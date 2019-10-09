package com.walmart.labs.tha.api.testserver.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.walmart.labs.tha.api.testserver.data.Product.Companion.TABLE_NAME
import kotlinx.android.parcel.Parcelize

/**
 * Domain layer Product object, database entity.
 */
@Parcelize
@Entity(tableName = TABLE_NAME)
data class Product(
    @PrimaryKey
        @ColumnInfo(name = "index")
        var index: Long,
    @ColumnInfo(name = "id")
        var productId: String,
    @ColumnInfo(name = "name")
        var productName: String,
    @ColumnInfo(name = "short_desc")
        var shortDescription: String,
    @ColumnInfo(name = "long_desc")
        var longDescription: String,
    @ColumnInfo(name = "price")
        var price: String,
    @ColumnInfo(name = "image")
        var productImage: String,
    @ColumnInfo(name = "rating")
        var reviewRating: Float,
    @ColumnInfo(name = "rating_count")
        var reviewCount: Int,
    @ColumnInfo(name = "product_id")
        var inStock: Boolean
) : Parcelable {
    companion object {
        const val TABLE_NAME = "product_table"
    }
}