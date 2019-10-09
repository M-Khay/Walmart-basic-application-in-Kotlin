package com.walmart.labs.tha.api.testserver

import androidx.paging.DataSource
import com.walmart.labs.tha.api.testserver.data.Product

/**
 * Interface to allow varied concrete implementations to be provided to consumers
 */
interface ProductRepository {

    /**
     * Load a page of products from remote data source
     *
     * @param pageToLoad page index
     * @param pageSize size of page
     */
    suspend fun loadPageOfProducts(pageToLoad: Int, pageSize: Int): List<Product>

    /**
     * Create a data source that returns pages of products keyed by page.
     */
    fun createProductsDataSource(): DataSource.Factory<Int, Product>
}
