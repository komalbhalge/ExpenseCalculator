package com.kb.incomeexpenseapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kb.incomeexpenseapp.domain.room.RoomRepository
import com.kb.piechart.data.db.entities.Expense
import com.kb.piechart.data.db.entities.Income
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RoomRepository
) : ViewModel() {

    var dayExpenses = MutableLiveData<Double>()
    var dayIncomes = MutableLiveData<Double>()

    var monthExpenses = MutableLiveData<Double>()
    var monthIncomes = MutableLiveData<Double>()

    var yearExpenses = MutableLiveData<Double>()
    var yearIncomes = MutableLiveData<Double>()

    fun insertExpense(expense: Expense) {
        viewModelScope.launch(IO) {
            repository.insertExpense(expense)
        }
    }

    fun insertIncome(income: Income) {
        viewModelScope.launch(IO) {
            repository.insertIncome(income)
        }
    }

    fun getIncomesForMonth(month: Int, year: Int) {
        viewModelScope.launch(IO) {
            monthIncomes.postValue(repository.getIncomesForMonth(month, year))
        }
    }

    fun getExpensesForMonth(month: Int, year: Int) {
        viewModelScope.launch(IO) {
            monthExpenses.postValue(repository.getExpensesForMonth(month, year))
        }
    }

    fun getExpensesForYear(year: Int) {
        viewModelScope.launch(IO) {
            yearExpenses.postValue(repository.getExpensesForYear(year))
        }
    }

    fun getIncomesForYear(year: Int) {
        viewModelScope.launch(IO) {
            yearIncomes.postValue(repository.getIncomesForYear(year))
        }
    }

    fun getIncomesByDay(day: String) {
        viewModelScope.launch(IO) {
            dayIncomes.postValue(repository.getTotalIncomeOnDate(day))
        }
    }

    fun getExepnsesByDay(day: String) {
        viewModelScope.launch(IO) {
            dayExpenses.postValue(repository.getTotalExpenseByDate(day))
        }
    }
}