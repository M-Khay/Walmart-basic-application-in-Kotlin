package com.walmart.labs.tha.api.testserver.net

import com.squareup.moshi.JsonClass

/**
 * Example: "{
        "productId":"003e3e6a-3f84-43ac-8ef3-a5ae2db0f80e",
        "productName":"Ellerton TV Console",
        "shortDescription":"<p><span style=\"color:#FF0000;\"><b>White Glove Delivery Included</b></span></p>\n\n<ul>\n\t<li>Excellent for the gamer, movie enthusiest, or interior decorator in your home</li>\n\t<li>Built-in power strip for electronics</li>\n\t<li>Hardwood solids and cherry veneers</li>\n</ul>\n",
        "longDescription":"<p>The Ellerton media console is well-suited for today&rsquo;s casual lifestyle. Its elegant style and expert construction will make it a centerpiece in any home. Soundly constructed, the Ellerton uses hardwood solids &amp; cherry veneers elegantly finished in a rich dark cherry finish. &nbsp;With ample storage for electronics &amp; media, it also cleverly allows for customization with three choices of interchangeable door panels.</p>\n",
        "price":"$949.00",
        "productImage":"/images/image2.jpeg",
        "reviewRating":2,
        "reviewCount":1,
        "inStock":true
    }"
 */
@JsonClass(generateAdapter = true)
data class ProductResponse(
    val productId: String = "",
    val productName: String = "",
    val shortDescription: String = "",
    val longDescription: String = "",
    val price: String = "",
    val productImage: String = "",
    val reviewRating: Float = -1.0f,
    val reviewCount: Int = 0,
    val inStock: Boolean = true
) {
    companion object{

    }
}