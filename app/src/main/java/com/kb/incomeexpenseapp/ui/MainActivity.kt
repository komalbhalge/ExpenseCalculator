package com.kb.incomeexpenseapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.kb.incomeexpenseapp.R
import com.kb.incomeexpenseapp.databinding.ActivityMainBinding
import com.kb.incomeexpenseapp.ui.home.HomeViewModel
import com.kb.incomeexpenseapp.utils.DATE_FORMATTER
import com.kb.incomeexpenseapp.utils.FabAnimation
import com.kb.piechart.data.db.entities.Expense
import com.kb.piechart.data.db.entities.Income
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    // to check whether sub FABs are visible or not
    var areAllFabsVisible: Boolean = false

    // TO add dummy income/expense
    private var currentDate: Long? = null
    private lateinit var selectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_incomes, R.id.navigation_expenses
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //init Selected date value
        selectedDate = LocalDate.now().toString()
        binding.date.setText(selectedDate)
        binding.date.setOnClickListener { showDatePicker() }
        initFab()
    }

    fun initFab() {
        FabAnimation.init(binding.addExpenseFab, binding.addExpenseText)
        FabAnimation.init(binding.addIncomefab, binding.addIncometext)
        binding.addFab.shrink()
        binding.addFab.setOnClickListener { view ->

            areAllFabsVisible = FabAnimation.rotateFab(view, !areAllFabsVisible)
            if (areAllFabsVisible) {
                binding.addIncomeExpenseLayout.visibility = View.VISIBLE
                FabAnimation.showIn(binding.addExpenseFab, binding.addExpenseText)
                FabAnimation.showIn(binding.addIncomefab, binding.addIncometext)
            } else {
                binding.addIncomeExpenseLayout.visibility = View.GONE
                FabAnimation.showOut(binding.addExpenseFab, binding.addExpenseText)
                FabAnimation.showOut(binding.addIncomefab, binding.addIncometext)
            }
        }
        binding.addExpenseFab.setOnClickListener { view ->
            //add Expense here
            addExpense()
            //Rotate back the add button and store the state
            areAllFabsVisible = FabAnimation.rotateFab(binding.addFab, false)
            FabAnimation.showOut(binding.addExpenseFab, binding.addExpenseText)
            FabAnimation.showOut(binding.addIncomefab, binding.addIncometext)
        }

        binding.addIncomefab.setOnClickListener { view ->
            //add Income here
            addIncome()
            //Rotate back the add button and store the state
            areAllFabsVisible = FabAnimation.rotateFab(binding.addFab, false)
            FabAnimation.showOut(binding.addExpenseFab, binding.addExpenseText)
            FabAnimation.showOut(binding.addIncomefab, binding.addIncometext)
        }
    }

    fun addExpense() {
        if (binding.amount.text.isNullOrEmpty() && binding.date.text.isNotEmpty()) {
            Toast.makeText(this, R.string.message_empty_data, Toast.LENGTH_LONG).show()
            return
        } else {
            val localDate = LocalDate.parse(selectedDate)
            val month = localDate.monthValue
            val year = localDate.year
            val amount = binding.amount.text.toString().toDouble()

            viewModel.insertExpense(
                Expense(
                    name = "Expense",
                    amount = amount,
                    day = localDate.toString(),
                    month = month,
                    year = year
                )
            )
            Toast.makeText(this, R.string.message_expense_added, Toast.LENGTH_LONG).show()
            binding.addIncomeExpenseLayout.visibility = View.GONE
        }
    }

    fun addIncome() {
        if (binding.amount.text.isNullOrEmpty() && binding.date.text.isNotEmpty()) {
            Toast.makeText(this, R.string.message_empty_data, Toast.LENGTH_LONG).show()
            return
        } else {
            val localDate = LocalDate.parse(selectedDate)
            val month = localDate.monthValue
            val year = localDate.year
            val amount = binding.amount.text.toString().toDouble()

            viewModel.insertIncome(
                Income(
                    name = "Income",
                    amount = amount,
                    day = localDate.toString(),
                    month = month,
                    year = year
                )
            )
            Toast.makeText(this, R.string.message_income_added, Toast.LENGTH_LONG).show()
            binding.addIncomeExpenseLayout.visibility = View.GONE
        }
    }

    private fun showDatePicker() {
        val selectedDateInMillis = currentDate ?: System.currentTimeMillis()

        MaterialDatePicker.Builder.datePicker().setSelection(selectedDateInMillis).build().apply {
            addOnPositiveButtonClickListener { dateInMillis -> onDateSelected(dateInMillis) }
        }.show(supportFragmentManager, MaterialDatePicker::class.java.canonicalName)
    }

    private fun onDateSelected(dateTimeStampInMillis: Long) {
        currentDate = dateTimeStampInMillis
        currentDate?.let {
            val dateTime: LocalDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
            val dateAsFormattedText: String =
                dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
            selectedDate = dateAsFormattedText
            binding.date.text = selectedDate
        }
    }

}