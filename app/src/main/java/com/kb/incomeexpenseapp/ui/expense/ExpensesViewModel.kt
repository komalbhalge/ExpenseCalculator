package com.kb.incomeexpenseapp.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExpensesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is expenses Fragment, \nTODO: display expnese list"
    }
    val text: LiveData<String> = _text
}