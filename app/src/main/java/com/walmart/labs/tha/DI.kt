package com.walmart.labs.tha

import android.app.Application
import android.content.Context
import com.walmart.labs.tha.api.testserver.ProductRepository
import com.walmart.labs.tha.api.testserver.ProductRepositoryImpl
import com.walmart.labs.tha.api.testserver.data.ProductFactory
import com.walmart.labs.tha.api.testserver.data.TestServerDatabase
import com.walmart.labs.tha.api.testserver.net.TestServerWebService
import okhttp3.ConnectionPool

class DI private constructor(
        private val application: Application,
        private val config: AppConfig = AppConfig()
) {

    private val connectionPool by lazy {
        ConnectionPool()
    }
    private val testServerDb by lazy {
        TestServerDatabase.getInstance(application)
    }

    private val webService: TestServerWebService by lazy {
        TestServerWebService.getService(
                config.domain,
                connectionPool
        )
    }

    private val productFactory by lazy { ProductFactory(config.domain) }

    val productRepo: ProductRepository by lazy {
        ProductRepositoryImpl(testServerDb, webService, productFactory)
    }

    companion object {
        @Volatile private var INSTANCE: DI? = null
        @Synchronized
        fun getInstance(application: Application): DI {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            return synchronized(this) {
                val tempInstance2 = INSTANCE
                if (tempInstance2 != null) {
                    tempInstance2
                } else {
                    INSTANCE = DI(application)
                    INSTANCE
                }
            } as DI
        }
    }
}

fun Context.di(): DI = DI.getInstance(this.applicationContext as Application)
