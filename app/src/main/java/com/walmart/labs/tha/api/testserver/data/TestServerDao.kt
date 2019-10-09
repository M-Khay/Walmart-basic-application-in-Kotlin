package com.walmart.labs.tha.api.testserver.data

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
internal interface TestServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(products: List<Product>)

    @Query("SELECT * FROM ${Product.TABLE_NAME}")
    fun productsDataSource(): DataSource.Factory<Int, Product>

    @Query("DELETE FROM ${Product.TABLE_NAME}")
    fun clearProducts()
}