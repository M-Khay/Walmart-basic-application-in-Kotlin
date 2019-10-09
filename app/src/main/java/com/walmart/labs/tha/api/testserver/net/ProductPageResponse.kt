package com.walmart.labs.tha.api.testserver.net

import com.squareup.moshi.JsonClass

/**
 *  Response object from /walmartproducts/{pageNumber}/{pageSize}
 *  Example: "{
        "products":[
        // See [ProductResponse]
        ],
        "totalProducts":224,
        "pageNumber":1,
        "pageSize":1,
        "code":200
    }"
 */
@JsonClass(generateAdapter = true)
data class ProductPageResponse(
        val products: List<ProductResponse> = emptyList(),
        val totalProducts: Int = products.count(),
        val pageNumber: Int = 1,
        val pageSize: Int = 1,
        val code: Int = 200
) {
    companion object
}