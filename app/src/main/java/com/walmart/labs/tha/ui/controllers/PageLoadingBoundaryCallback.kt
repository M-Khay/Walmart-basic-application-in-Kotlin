package com.walmart.labs.tha.ui.controllers

import android.util.Log
import android.util.SparseBooleanArray
import androidx.paging.PagedList
import com.walmart.labs.tha.api.testserver.ProductRepository
import com.walmart.labs.tha.api.testserver.data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Abstract common BoundaryCallback which loads a page of Products
 */
class PageLoadingBoundaryCallback(
        override val coroutineContext: CoroutineContext,
        private val repository: ProductRepository,
        private val pageSize: Int,
        firstPageNumber: Int = 1
) : PagedList.BoundaryCallback<Product>(), CoroutineScope {

    companion object {
        const val TAG = "PageLoadingBoundary"
    }

    private var currentPage = firstPageNumber

    override fun onZeroItemsLoaded() {
        tryLoadPage(currentPage)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Product) {
        tryLoadPage(++currentPage)
    }

    private fun tryLoadPage(pageToLoad: Int) {
        launch {
            try {
                Log.v(TAG, "pageToLoad: $pageToLoad, pageSize: $pageSize")
                withContext(Dispatchers.IO) {
                    repository.loadPageOfProducts(pageToLoad, pageSize)
                }
            } catch (e: Exception) {
                Log.e(TAG, "error occurred loading page", e)
            }
        }
    }
}