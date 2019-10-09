package com.walmart.labs.tha.ui.controllers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import com.walmart.labs.tha.api.testserver.ProductRepository
import com.walmart.labs.tha.api.testserver.data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel that provides a [LiveData] that emits [PagedList]s of[Product]s which can be used to populate a
 * [PagedListAdapter].
 *
 * @param repo [ProductRepository] source from which to load data
 * @param firstPage initial page to load
 * @param pageSize size of the page to load
 */
class ProductListViewModel(repo: ProductRepository, pageSize: Int, firstPage: Int)
    : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main

    val products: LiveData<PagedList<Product>>

    init {
        val callback = PageLoadingBoundaryCallback(coroutineContext, repo, pageSize, firstPage)
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setMaxSize(224)
                .setPrefetchDistance(pageSize * 2)
                .build()
        products = LivePagedListBuilder(repo.createProductsDataSource(), config)
                .setBoundaryCallback(callback)
                .build()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancel()
    }

    /**
     * Factory class for creating a ProductListViewModel
     *
     * @param firstPage initial page to load
     * @param pageSize size of the page to load
     * @param repository [ProductRepository] source from which to load data
     */
    class Factory(
            private val repository: ProductRepository,
            private val pageSize: Int,
            private val firstPage: Int
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass != ProductListViewModel::class.java) {
                throw RuntimeException("Incorrect use of ProductListViewModel.Factory, likely cause: Static import of wrong Factory class")
            }
            @Suppress("UNCHECKED_CAST") // Exception thrown if T not ProductListViewModel.
            return ProductListViewModel(repository, pageSize, firstPage) as T
        }
    }
}
