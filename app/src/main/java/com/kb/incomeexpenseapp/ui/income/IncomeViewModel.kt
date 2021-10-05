package com.kb.incomeexpenseapp.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IncomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is income Fragment \nTODO: display income list"
    }
    val text: LiveData<String> = _text
}