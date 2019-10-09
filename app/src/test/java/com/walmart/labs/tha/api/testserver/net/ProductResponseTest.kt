package com.walmart.labs.tha.api.testserver.net

import com.google.common.truth.Truth
import com.walmart.labs.tha.testutils.io.AssetReader
import com.walmart.labs.tha.testutils.io.BaseMoshiParsingTest
import org.junit.Before
import org.junit.Test

class ProductResponseTest: BaseMoshiParsingTest() {

    companion object {
        private const val RESOURCE_PRODUCT_PAGE_200 = "mock_walmartproducts_1_1_200.json"
        private const val RESOURCE_PRODUCT_ID = "003e3e6a-3f84-43ac-8ef3-a5ae2db0f80e"
        private const val RESOURCE_PRODUCT_NAME = "Ellerton TV Console"
        private const val RESOURCE_SHORT_DESC = "<p><span style=\"color:#FF0000;\"><b>White Glove Delivery Included</b></span></p>\n" +
            "\n" +
            "<ul>\n" +
            "\t<li>Excellent for the gamer, movie enthusiest, or interior decorator in your home</li>\n" +
            "\t<li>Built-in power strip for electronics</li>\n" +
            "\t<li>Hardwood solids and cherry veneers</li>\n" +
            "</ul>\n"
        private const val RESOURCE_LONG_DESC = "<p>The Ellerton media console is well-suited for today&rsquo;s casual lifestyle. Its elegant style and expert construction will make it a centerpiece in any home. Soundly constructed, the Ellerton uses hardwood solids &amp; cherry veneers elegantly finished in a rich dark cherry finish. &nbsp;With ample storage for electronics &amp; media, it also cleverly allows for customization with three choices of interchangeable door panels.</p>\n"
        private const val RESOURCE_PRICE = "\$949.00"
        private const val RESOURCE_IMAGE = "/images/image2.jpeg"
        private const val RESOURCE_REVIEW_RATING = 2.0f
        private const val RESOURCE_REVIEW_COUNT = 1
        private const val RESOURCE_INSTOCK = true
    }

    @Before
    override fun setup() {
        super.setup()
    }

    @Test
    fun testDeserialization() {
        // GIVEN
        val testPayload = AssetReader().loadResource(RESOURCE_PRODUCT_PAGE_200)
        val adapter = ProductPageResponse.jsonAdapter(moshi)

        // WHEN
        val parsedPayload = adapter.fromJson(testPayload)

        // THEN
        Truth.assertThat(parsedPayload).isNotNull()
        parsedPayload?.run {
            Truth.assertThat(products).isNotNull()
            Truth.assertThat(products).isNotEmpty()
            Truth.assertThat(products).hasSize(1)
            products[0].run {
                Truth.assertThat(productId).isEqualTo(RESOURCE_PRODUCT_ID)
                Truth.assertThat(productName).isEqualTo(RESOURCE_PRODUCT_NAME)
                Truth.assertThat(shortDescription).isEqualTo(RESOURCE_SHORT_DESC)
                Truth.assertThat(longDescription).isEqualTo(RESOURCE_LONG_DESC)
                Truth.assertThat(price).isEqualTo(RESOURCE_PRICE)
                Truth.assertThat(productImage).isEqualTo(RESOURCE_IMAGE)
                Truth.assertThat(reviewRating).isEqualTo(RESOURCE_REVIEW_RATING)
                Truth.assertThat(reviewCount).isEqualTo(RESOURCE_REVIEW_COUNT)
                Truth.assertThat(inStock).isEqualTo(RESOURCE_INSTOCK)
            }
        }
    }
}