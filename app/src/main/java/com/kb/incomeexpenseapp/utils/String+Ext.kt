package com.kb.incomeexpenseapp.utils


fun String.appendCurrency(): String{
    return (CURRENCY.plus(this))
}