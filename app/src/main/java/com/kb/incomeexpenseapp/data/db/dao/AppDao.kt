package com.kb.piechart.data.db.dao

import androidx.room.*
import com.kb.piechart.data.db.entities.Expense
import com.kb.piechart.data.db.entities.Income
import java.sql.Date
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Insert
    suspend fun insertIncome(income: Income)

    @Query("SELECT COALESCE(SUM(amount),0) FROM expenses WHERE day = :date")
    suspend fun getTotalExpenseOnDate(date: String): Double

    @Query("SELECT COALESCE(SUM(amount),0) FROM incomes WHERE day = :date")
    suspend fun getTotalIncomeOnDate(date: String): Double

    @Query("SELECT COALESCE(SUM(amount),0) FROM expenses WHERE month =:month AND year = :year")
    suspend fun getExpensesForMonth(month: Int, year: Int): Double

    @Query("SELECT COALESCE(SUM(amount),0) FROM incomes  WHERE month =:month AND year = :year")
    fun getIncomesForMonth(month: Int, year: Int): Double

    @Query("SELECT COALESCE(SUM(amount),0) FROM expenses WHERE year = :year")
    suspend fun getExpensesForYear(year: Int): Double

    @Query("SELECT COALESCE(SUM(amount),0) FROM incomes WHERE  year = :year")
    suspend fun getIncomesForYear(year: Int): Double
}