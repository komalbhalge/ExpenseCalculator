package com.kb.incomeexpenseapp.domain.room

import com.kb.piechart.data.db.dao.AppDao
import com.kb.piechart.data.db.entities.Expense
import com.kb.piechart.data.db.entities.Income
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject constructor(private val appDao: AppDao) {

    suspend fun getExpensesForMonth(month: Int, year: Int): Double {
        return appDao.getExpensesForMonth(month, year)
    }

    suspend fun getIncomesForMonth(month: Int, year: Int): Double {
        return appDao.getIncomesForMonth(month, year)
    }

    suspend fun getExpensesForYear(year: Int): Double {
        return appDao.getExpensesForYear(year)
    }

    suspend fun getIncomesForYear(year: Int): Double {
        return appDao.getIncomesForYear(year)
    }

    suspend fun insertExpense(expense: Expense) {
        appDao.insertExpense(expense)
    }

    suspend fun insertIncome(income: Income) {
        appDao.insertIncome(income)
    }

    suspend fun getTotalIncomeOnDate(date: String): Double {
        return appDao.getTotalIncomeOnDate(date)
    }

    suspend fun getTotalExpenseByDate(date: String): Double {
        return appDao.getTotalExpenseOnDate(date)
    }
}