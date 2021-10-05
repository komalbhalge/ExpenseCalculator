package com.kb.incomeexpenseapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kb.piechart.data.db.AppDatabase
import com.kb.piechart.data.db.dao.AppDao
import com.kb.piechart.data.db.entities.Expense
import com.kb.piechart.data.db.entities.Income
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseInstrumentedTest {
    private lateinit var database: AppDatabase
    private lateinit var appDao: AppDao

    @After
    fun tearDown() {
        database.close()
    }

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            appContext, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        appDao = database.getDao()

    }

    @Test
    fun testExpensesByDay() {
        var expenseAmount = 0.0
        val expense = Expense(
            name = "Expense", amount = 13.0, day = "2021-09-16", month = 9, year = 2021
        )
        runBlocking {
            appDao.insertExpense(expense)
        }
        runBlocking {
            expenseAmount = appDao.getTotalExpenseOnDate("2021-09-16")
        }
        assertEquals(13.0, expenseAmount, 0.0)
    }

    @Test
    fun testIncomesByDay() {
        var incomeAmount = 0.0
        val income = Income(
            name = "Income", amount = 21.0, day = "2021-09-16", month = 9, year = 2021
        )
        runBlocking {
            appDao.insertIncome(income)
            appDao.insertIncome(income)
        }
        runBlocking {
            incomeAmount = appDao.getTotalIncomeOnDate("2021-09-16")
        }
        val expectedAmount = 42.0
        assertEquals(expectedAmount, incomeAmount, 0.0)
    }

    @Test
    fun getYearlyOverview() {
        var incomeAmount = 0.0
        var expenseAmount = 0.0
        val income = Income(
            name = "Income", amount = 10.0, day = "2021-09-16", month = 9, year = 2021
        )
        val expense = Expense(
            name = "Expense", amount = 11.0, day = "2021-09-16", month = 7, year = 2021
        )
        runBlocking {
            appDao.insertIncome(income)
            appDao.insertIncome(income)
            appDao.insertExpense(expense)
        }
        runBlocking {
            incomeAmount = appDao.getIncomesForYear(2021)
        }
        runBlocking {
            expenseAmount = appDao.getExpensesForYear(2021)
        }
        val expectedOverview = 9.0
        assertEquals(expectedOverview, (incomeAmount - expenseAmount), 0.0)
    }
}
