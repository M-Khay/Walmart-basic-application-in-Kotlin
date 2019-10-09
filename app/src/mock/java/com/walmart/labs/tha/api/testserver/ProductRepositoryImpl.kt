package com.walmart.labs.tha.api.testserver

import androidx.paging.DataSource
import com.walmart.labs.tha.api.testserver.data.Product
import com.walmart.labs.tha.api.testserver.data.ProductFactory
import com.walmart.labs.tha.api.testserver.data.TestServerDatabase
import com.walmart.labs.tha.api.testserver.net.TestServerWebService
import java.util.Random
import java.util.UUID

/**
 * Concrete implementation of [ProductRepository] that will query the live [TestServerWebService]
 */
internal class ProductRepositoryImpl(
    private val db: TestServerDatabase,
    private val webService: TestServerWebService,
    private val productFactory: ProductFactory
) : ProductRepository {

    private val rnd = Random()

    override suspend fun loadPageOfProducts(pageToLoad: Int, pageSize: Int): List<Product> {
        val newPage = getSomeProducts(pageToLoad, pageSize)
        db.getDao().insert(newPage)
        return newPage
    }

    override fun createProductsDataSource(): DataSource.Factory<Int, Product> {
        return db.getDao().productsDataSource()
    }

    private fun getSomeProducts(page: Int, pageSize: Int): List<Product> {
        val startIndex = (page * pageSize).toLong()
        return (0 until pageSize).map {
            Product(index = startIndex + it,
                    productId = UUID.randomUUID().toString(),
                    productName = "Unknown Product",
                    shortDescription = "Short Description",
                    longDescription = "Long description for a product that should be displayed across multiple lines",
                    price = "$20.00",
                    productImage = "http://d1ic4altzx8ueg.cloudfront.net/finder-us/wp-uploads/2017/01/walmart-logo-250x250.png",
                    reviewRating = rnd.nextFloat() * 5,
                    reviewCount = rnd.nextInt(300),
                    inStock = rnd.nextBoolean()
            )
        }.toList()
    }
}
