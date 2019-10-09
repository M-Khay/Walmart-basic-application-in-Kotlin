package com.walmart.labs.tha.api.testserver.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1, exportSchema = true)
internal abstract class TestServerDatabase : RoomDatabase() {

    abstract fun getDao(): TestServerDao

    companion object {
        private var INSTANCE: TestServerDatabase? = null

        fun getInstance(context: Context): TestServerDatabase {
            if (INSTANCE == null) {
                synchronized(TestServerDatabase::class.java) {
                    INSTANCE = create(context)
                }
            }
            return INSTANCE as TestServerDatabase
        }

        private fun create(context: Context): TestServerDatabase {
            return Room.databaseBuilder(context.applicationContext,
                    TestServerDatabase::class.java,
                    "test_server_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}