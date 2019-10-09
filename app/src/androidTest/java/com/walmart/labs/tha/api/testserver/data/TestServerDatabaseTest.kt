package com.walmart.labs.tha.api.testserver.data


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.LivePagedListBuilder
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.walmart.labs.tha.testutils.livedata.getFirst
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestServerDatabaseTest {

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: TestServerDao
    private lateinit var db: TestServerDatabase

    @Before
    fun init() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, TestServerDatabase::class.java)
                .build()
        assertThat(db).isNotNull()

        dao = db.getDao()
        assertThat(dao).isNotNull()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun testInsertGetDeleteAll() {
        // Given
        val products = mutableListOf<Product>()
        for(num in 0 until 10) {
            products.add(Product.create(num))
        }
        val liveData = LivePagedListBuilder(db.getDao().productsDataSource(), 10).build()

        dao.insert(products)
        assertThat(liveData.getFirst()).hasSize(10)

        dao.clearProducts()
        assertThat(liveData.getFirst()).hasSize(0)
    }

    private fun Product.Companion.create(num:Int):Product {
        return Product(
                index = num.toLong(),
                productId = num.toString(),
                productName = "productName$num",
                productImage = "productImage$num",
                shortDescription = "shortDesc$num",
                longDescription = "longDesc$num",
                price = "$num",
                reviewRating = 3.0f,
                reviewCount = 200,
                inStock = true
        )
    }
}