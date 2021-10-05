package com.kb.incomeexpenseapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.datepicker.MaterialDatePicker
import com.kb.incomeexpenseapp.R
import com.kb.incomeexpenseapp.databinding.FragmentHomeBinding
import com.kb.incomeexpenseapp.utils.DATE_FORMATTER
import com.kb.incomeexpenseapp.utils.EMPTY_BALANCE
import com.kb.incomeexpenseapp.utils.appendCurrency
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()
    private var currentDate: Long? = null

    //Default selection for Overview will be month(1)
    private var selectedFilter = 1
    private var selectedMonth = 0
    private var selectedDay = ""
    private var selectedYear = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Decorate Quote
        val quoteString =
            resources.getString(R.string.quote1) + "<b>" + resources.getString(R.string.quote2) + "</b> "
        binding.quote.setText(HtmlCompat.fromHtml(quoteString, HtmlCompat.FROM_HTML_MODE_LEGACY))

        setUpFilters()
        setUpData()
        return root
    }

    private fun setUpFilters() {
        //Set up Overview type filter
        val filter = resources.getStringArray(R.array.filters)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.filter_item, filter)
        binding.filterDropdown.setAdapter(arrayAdapter)

        binding.filterDropdown.setOnItemClickListener { _, _, position, l ->
            selectedFilter = position
            setFiltersVisibility(selectedFilter)
        }
        //Set up Date picker
        binding.btnSelectDayYear.setOnClickListener { showDatePicker() }

        //Set up Month picker
        val months = resources.getStringArray(R.array.months)

        //Set current month by default
        val localDate = LocalDate.now()
        binding.monthDropdown.setText(localDate.month.toString(), false)

        val monthsAdapter = ArrayAdapter(requireContext(), R.layout.filter_item, months)
        binding.monthDropdown.setAdapter(monthsAdapter)
        binding.monthDropdown.setOnItemClickListener { adapterView, view, position, l ->
            //Since months start from 1 and position starts from 0, just add +1 to current position
            //position + 1: is the selected month
            selectedMonth = position + 1
            binding.yearFilter.visibility = View.VISIBLE
            if (selectedYear != 0) {
                viewModel.getIncomesForMonth(month = selectedMonth, year = selectedYear)
            }
        }
        //Set up Year picker
        val years = resources.getStringArray(R.array.years)
        binding.yearDropdown.setText(years[0], false)
        val yearsAdapter = ArrayAdapter(requireContext(), R.layout.filter_item, years)
        binding.yearDropdown.setAdapter(yearsAdapter)

        binding.yearDropdown.setOnItemClickListener { adapterView, view, position, l ->
            selectedYear = years[position].toInt()
            if (selectedMonth != 0) {
                viewModel.getIncomesForMonth(month = selectedMonth, year = selectedYear)
            } else {
                viewModel.getIncomesForYear(selectedYear)
            }
        }
    }

    fun setUpData() {
        with(viewModel) {
            with(viewLifecycleOwner) {
                //Get current month's data by default
                val localDate = LocalDate.now()
                selectedMonth = localDate.monthValue
                selectedYear = localDate.year
                getIncomesForMonth(selectedMonth, selectedYear)

                monthIncomes.observe(this) {
                    getExpensesForMonth(selectedMonth, selectedYear)
                }
                dayIncomes.observe(this) {
                    getExepnsesByDay(selectedDay)
                }
                yearIncomes.observe(this) {
                    getExpensesForYear(selectedYear)
                }
                dayExpenses.observe(this) {
                    updatePieChart(
                        dayIncomes.value ?: EMPTY_BALANCE,
                        dayExpenses.value ?: EMPTY_BALANCE
                    )
                }
                monthExpenses.observe(this) {
                    updatePieChart(
                        monthIncomes.value ?: EMPTY_BALANCE,
                        monthExpenses.value ?: EMPTY_BALANCE
                    )
                }
                yearExpenses.observe(this) {
                    updatePieChart(
                        yearIncomes.value ?: EMPTY_BALANCE,
                        yearExpenses.value ?: EMPTY_BALANCE
                    )
                }
            }
        }

    }

    private fun updatePieChart(income: Double, expense: Double) {
        binding.tvExpenseAmount.text = expense.toString().appendCurrency()
        binding.tvIncomeAmount.text = income.toString().appendCurrency()
        binding.tvOverviewAmount.text = (income - expense).toString().appendCurrency()

        if (income == EMPTY_BALANCE && expense == EMPTY_BALANCE) {
            setUpPieChart(isEmpty = true, income = income, expense = expense)
        } else {
            setUpPieChart(isEmpty = false, income = income, expense = expense)
        }
    }

    private fun setUpPieChart(isEmpty: Boolean, income: Double, expense: Double) {
        val pieChartEntries = arrayListOf<PieEntry>()
        if (isEmpty) {
            pieChartEntries.add(PieEntry(100.0f, 0f))
        } else {
            pieChartEntries.add(PieEntry(expense.toFloat(), 0f))
            pieChartEntries.add(PieEntry(income.toFloat(), 1f))
        }
        binding.pieChart.animateXY(1000, 1000)

        val pieDataSet = PieDataSet(pieChartEntries, "This is pieChart")
        if (isEmpty) {
            pieDataSet.colors = listOf(
                ResourcesCompat.getColor(resources, R.color.app_bg, null)
            )
        } else {
            pieDataSet.colors = listOf(
                ResourcesCompat.getColor(resources, R.color.red, null),
                ResourcesCompat.getColor(resources, R.color.green, null)
            )
        }
        val pieData = PieData(pieDataSet)

        binding.pieChart.description = null

        binding.pieChart.legend.isEnabled = false

        binding.pieChart.isDrawHoleEnabled = false
        pieData.setDrawValues(false)
        binding.pieChart.data = pieData
    }

    private fun showDatePicker() {
        val selectedDateInMillis = currentDate ?: System.currentTimeMillis()

        MaterialDatePicker.Builder.datePicker().setSelection(selectedDateInMillis).build().apply {
            addOnPositiveButtonClickListener { dateInMillis -> onDateSelected(dateInMillis) }
        }.show(parentFragmentManager, MaterialDatePicker::class.java.canonicalName)
    }

    private fun onDateSelected(dateTimeStampInMillis: Long) {
        currentDate = dateTimeStampInMillis
        currentDate?.let {
            val dateTime: LocalDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
            val dateAsFormattedText: String =
                dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
            binding.btnSelectDayYear.text = dateAsFormattedText
            selectedDay = dateAsFormattedText
            viewModel.getIncomesByDay(selectedDay)
        }
    }

    private fun setFiltersVisibility(position: Int) {
        when (position) {
            DAY -> {
                binding.monthFilter.visibility = View.GONE
                binding.yearFilter.visibility = View.GONE
                binding.btnSelectDayYear.visibility = View.VISIBLE
            }
            MONTH -> {
                binding.btnSelectDayYear.visibility = View.GONE
                binding.monthFilter.visibility = View.VISIBLE
            }
            YEAR -> {
                //Undo month selection if have any
                selectedMonth = 0
                binding.btnSelectDayYear.visibility = View.GONE
                binding.monthFilter.visibility = View.GONE
                binding.yearFilter.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DAY = 0
        private const val MONTH = 1
        private const val YEAR = 2
    }
}