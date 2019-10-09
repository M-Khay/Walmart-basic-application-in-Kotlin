package com.walmart.labs.tha.api.testserver.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit Web Service for interacting with TestServer
 */
interface TestServerWebService {

    @GET("/walmartproducts/{pageNumber}/{pageSize}")
    fun getProductList(
            @Path("pageNumber") pageNumber: Int,
            @Path("pageSize") pageSize: Int
    ): Deferred<ProductPageResponse>

    companion object {

        /**
         * Factory method for creating an instance of [TestServerWebService], allows injection of
         * [ConnectionPool] to be used with the RestClient created for the service
         */
        fun getService(baseUrl: String, connectionPool: ConnectionPool = ConnectionPool())
                : TestServerWebService {

            val okHttpClient = OkHttpClient.Builder()
                    .connectionPool(connectionPool)
                    .build()
            return getRetrofit(baseUrl, okHttpClient)
                    .create(TestServerWebService::class.java)
        }

        /**
         * Creates and configures a [Retrofit] object which can be used to build a [TestServerWebService]
         */
        private fun getRetrofit(baseUrl: String, restClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(restClient)
                    .addConverterFactory(getMoshiConverter())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
        }

        /**
         * Returns a [Converter.Factory] configured to parse the response objects returned
         * TestServer's endpoints.
         */
        private fun getMoshiConverter(): Converter.Factory {
            val moshi = Moshi.Builder().build()
            return MoshiConverterFactory.create(
                    Moshi.Builder()
                            .add(ProductResponse::class.java, ProductResponse.jsonAdapter(moshi))
                            .add(ProductPageResponse::class.java, ProductPageResponse.jsonAdapter(moshi))
                            .build()
            )
        }
    }
}