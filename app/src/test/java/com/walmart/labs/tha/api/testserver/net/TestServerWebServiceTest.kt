package com.walmart.labs.tha.api.testserver.net

import com.walmart.labs.tha.testutils.io.AssetReader
import com.walmart.labs.tha.testutils.net.MockWebServerTest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class TestServerWebServiceTest : MockWebServerTest() {

    lateinit var service: TestServerWebService

    @Before
    override fun before() {
        super.before()

        val url = server.url("/").toString()
        service = TestServerWebService.getService(url)
    }

    @After
    override fun after() {
        super.after()
    }

    @Test
    fun testMockedResponse() {
        val mockResponse = MockResponse()
                .setResponseCode(200)
                .setBody(AssetReader().loadResource("mock_walmartproducts_1_1_200.json"))
        server.enqueue(mockResponse)


        val call = service.getProductList(1, 1)
        val response: ProductPageResponse = runBlocking {
            call.await()
        }
        assertThat(response).isNotNull()
        assertThat(response.products).isNotEmpty
        assertThat(response.products[0]).isNotNull()
        assertThat(response.products[0].productId).isEqualTo("003e3e6a-3f84-43ac-8ef3-a5ae2db0f80e")
        assertThat(response.products[0].productName).isEqualTo("Ellerton TV Console")
        assertThat(response.products[0].shortDescription).isEqualTo("<p><span style=\"color:#FF0000;\"><b>White Glove Delivery Included</b></span></p>\n\n<ul>\n\t<li>Excellent for the gamer, movie enthusiest, or interior decorator in your home</li>\n\t<li>Built-in power strip for electronics</li>\n\t<li>Hardwood solids and cherry veneers</li>\n</ul>\n")
        assertThat(response.products[0].longDescription).isEqualTo("<p>The Ellerton media console is well-suited for today&rsquo;s casual lifestyle. Its elegant style and expert construction will make it a centerpiece in any home. Soundly constructed, the Ellerton uses hardwood solids &amp; cherry veneers elegantly finished in a rich dark cherry finish. &nbsp;With ample storage for electronics &amp; media, it also cleverly allows for customization with three choices of interchangeable door panels.</p>\n")
        assertThat(response.products[0].price).isEqualTo("\$949.00")
        assertThat(response.products[0].productImage).isEqualTo("/images/image2.jpeg")
        assertThat(response.products[0].reviewRating).isEqualTo(2.0f)
        assertThat(response.products[0].reviewCount).isEqualTo(1)
        assertThat(response.products[0].inStock).isTrue()
        assertThat(response.totalProducts).isEqualTo(224)
        assertThat(response.pageNumber).isEqualTo(1)
        assertThat(response.pageSize).isEqualTo(1)
        assertThat(response.code).isEqualTo(200)
    }
}