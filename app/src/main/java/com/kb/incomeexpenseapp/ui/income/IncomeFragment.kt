package com.kb.incomeexpenseapp.ui.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kb.incomeexpenseapp.databinding.FragmentIncomesBinding

class IncomeFragment : Fragment() {

    private lateinit var incomeViewModel: IncomeViewModel
    private var _binding: FragmentIncomesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        incomeViewModel =
            ViewModelProvider(this).get(IncomeViewModel::class.java)

        _binding = FragmentIncomesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textIncome
        incomeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}