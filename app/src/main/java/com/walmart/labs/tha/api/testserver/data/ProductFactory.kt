package com.walmart.labs.tha.api.testserver.data

import com.walmart.labs.tha.api.testserver.net.ProductPageResponse
import com.walmart.labs.tha.api.testserver.net.ProductResponse

/**
 * Factory class for converting net response to data layer models
 */
class ProductFactory(private val imageBaseUrl: String) {

    fun convert(response: ProductPageResponse): List<Product> {
        val firstIndex = (response.pageNumber - 1) * response.pageSize
        return response.products.mapIndexed { index, productResponse ->
            convert(productResponse, (firstIndex + index).toLong())
        }.toList()
    }

    private fun convert(product: ProductResponse, index: Long): Product {
        return Product(
                index = index,
                productId = product.productId,
                productName = product.productName,
                shortDescription = product.shortDescription,
                longDescription = product.longDescription,
                price = product.price,
                productImage = "$imageBaseUrl${product.productImage}",
                reviewRating = product.reviewRating,
                reviewCount = product.reviewCount,
                inStock = product.inStock
        )
    }
}