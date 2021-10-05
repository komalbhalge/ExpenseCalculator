package com.kb.piechart.data.db

import android.content.Context
import androidx.room.*
import com.kb.piechart.data.db.dao.AppDao
import com.kb.piechart.data.db.entities.Expense
import com.kb.piechart.data.db.entities.Income
import com.kb.piechart.utils.Converters


@Database(entities = [Expense::class, Income::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): AppDao

    companion object {
        private var dbINSTANCE: AppDatabase? = null

        fun getAppDB(context: Context): AppDatabase {
            if (dbINSTANCE == null) {
                dbINSTANCE = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "com.kb.income-expenditure"
                ).allowMainThreadQueries()
                    .build()
            }
            return dbINSTANCE!!
        }
    }
}