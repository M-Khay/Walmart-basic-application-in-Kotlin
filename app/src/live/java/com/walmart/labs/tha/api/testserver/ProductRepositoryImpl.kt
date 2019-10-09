package com.walmart.labs.tha.api.testserver

import androidx.paging.DataSource
import com.walmart.labs.tha.api.testserver.data.Product
import com.walmart.labs.tha.api.testserver.data.ProductFactory
import com.walmart.labs.tha.api.testserver.data.TestServerDatabase
import com.walmart.labs.tha.api.testserver.net.TestServerWebService

/**
 * Concrete implementation of [ProductRepository] that will query the live [TestServerWebService]
 */
internal class ProductRepositoryImpl(
    private val db: TestServerDatabase,
    private val webService: TestServerWebService,
    private val productFactory: ProductFactory
) : ProductRepository {

    override suspend fun loadPageOfProducts(pageToLoad: Int, pageSize: Int): List<Product> {
        val request = webService.getProductList(pageToLoad, pageSize)
        val response = request.await()
        val newPage = productFactory.convert(response)
        db.getDao().insert(newPage)
        return newPage
    }

    override fun createProductsDataSource(): DataSource.Factory<Int, Product> {
        return db.getDao().productsDataSource()
    }
}
