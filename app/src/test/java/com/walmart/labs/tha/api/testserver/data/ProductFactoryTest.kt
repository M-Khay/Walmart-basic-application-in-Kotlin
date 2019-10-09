package com.walmart.labs.tha.api.testserver.data

import com.google.common.truth.Truth.assertThat
import com.walmart.labs.tha.api.testserver.net.ProductPageResponse
import com.walmart.labs.tha.api.testserver.net.ProductResponse
import org.junit.Test

class ProductFactoryTest {

    companion object {
        private const val TEST_BASE_IMAGE_URL = "http://www.baseurl.com/"
        private const val PRODUCT_ID_PREFIX = "productId:"
        private const val PRODUCT_NAME_PREFIX = "productName:"
        private const val SHORT_DESC_PREFIX = "shortDesc:"
        private const val LONG_DESC_PREFIX = "longDesc:"
        private const val PRODUCT_IMAGE_PREFIX = "/images/image"
        private const val PRODUCT_IMAGE_SUFFIX = ".jpeg"
        private const val RATING_RANGE = 5.0f
    }

    @Test
    fun convert() {
        // GIVEN
        val pageResponse = getTestPageResponse(items = 10)
        val factory = ProductFactory(TEST_BASE_IMAGE_URL)

        // WHEN
        val convertedResult = factory.convert(pageResponse)

        // THEN
        assertThat(convertedResult).isNotNull()
        assertThat(convertedResult).hasSize(10)
        (0 until 10).forEach {
            assertThat(convertedResult[it]).isNotNull()
            verifyProductTemplate(convertedResult[it], it)
        }
    }

    @Test
    fun testPage1Indices() {
        // GIVEN
        val factory = ProductFactory(TEST_BASE_IMAGE_URL)
        listOf(5, 10, 15, 20, 25).forEach { pageSize -> // Page size parameters
            (1 until 5).forEach { page -> // Test multiple pages
                val pageResponse = getTestPageResponse(items = pageSize, page = page)
                val firstPageIndex = (page - 1) * pageSize

                // WHEN
                val convertedResult = factory.convert(pageResponse)

                // THEN
                assertThat(convertedResult).isNotNull()
                assertThat(convertedResult).hasSize(pageSize)

                (0 until pageSize).forEach {
                    assertThat(convertedResult[it]).isNotNull()
                    assertThat(convertedResult[it].index).isEqualTo(firstPageIndex + it)
                }
            }
        }
    }

    private fun verifyProductTemplate(product: Product, number: Int) {
        assertThat(product.productId).isEqualTo("$PRODUCT_ID_PREFIX$number")
        assertThat(product.productName).isEqualTo("$PRODUCT_NAME_PREFIX$number")
        assertThat(product.shortDescription).isEqualTo("$SHORT_DESC_PREFIX$number")
        assertThat(product.longDescription).isEqualTo("$LONG_DESC_PREFIX$number")
        assertThat(product.price).isEqualTo("$$number.00")
        assertThat(product.productImage).isEqualTo("$TEST_BASE_IMAGE_URL$PRODUCT_IMAGE_PREFIX$number$PRODUCT_IMAGE_SUFFIX")
        assertThat(product.reviewCount).isEqualTo(number)
        assertThat(product.inStock).isTrue()
    }

    private fun getTestPageResponse(items: Int = 5, page: Int = 1, total: Int = 30) = ProductPageResponse(
        products = getTestProducts(items),
        totalProducts = total,
        pageNumber = page,
        pageSize = items,
        code = 200
    )

    private fun getTestProducts(items: Int) = (0 until items).map {
        getTestProduct(it)
    }.toList()

    private fun getTestProduct(index: Int): ProductResponse {
        return ProductResponse (
            productId = "$PRODUCT_ID_PREFIX$index",
            productName = "$PRODUCT_NAME_PREFIX$index",
            shortDescription = "$SHORT_DESC_PREFIX$index",
            longDescription = "$LONG_DESC_PREFIX$index", // Ironically shorter than short description.
            price = "$$index.00",
            productImage = "$PRODUCT_IMAGE_PREFIX$index$PRODUCT_IMAGE_SUFFIX",
            reviewCount = index,
            reviewRating = indexToRating(index, RATING_RANGE),
            inStock = true
        )
    }

    private fun indexToRating(index: Int, range: Float): Float = (index.toFloat() % range)
}