package com.walmart.labs.tha.api.testserver.net

import com.google.common.truth.Truth.assertThat
import com.walmart.labs.tha.testutils.io.AssetReader
import com.walmart.labs.tha.testutils.io.BaseMoshiParsingTest
import org.junit.Before
import org.junit.Test

class ProductPageResponseTest: BaseMoshiParsingTest() {

    companion object {
        private const val RESOURCE_PRODUCT_PAGE_200 = "mock_walmartproducts_1_1_200.json"
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
        assertThat(parsedPayload).isNotNull()
        parsedPayload?.run {
            assertThat(totalProducts).isEqualTo(224)
            assertThat(pageNumber).isEqualTo(1)
            assertThat(pageSize).isEqualTo(1)
            assertThat(code).isEqualTo(200)
            assertThat(products).isNotNull()
            assertThat(products).isNotEmpty()
            assertThat(products).hasSize(1)
        }
    }
}