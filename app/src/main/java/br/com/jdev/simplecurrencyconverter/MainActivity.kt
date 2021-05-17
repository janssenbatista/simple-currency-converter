package br.com.jdev.simplecurrencyconverter

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.jdev.simplecurrencyconverter.databinding.ActivityMainBinding
import br.com.jdev.simplecurrencyconverter.enums.Currency
import br.com.jdev.simplecurrencyconverter.repositories.CurrencyConverterRepository
import kotlinx.coroutines.*
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: CurrencyConverterRepository
    private val numberFormat by lazy { NumberFormat.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Currency.values())
        repository = CurrencyConverterRepository()
        binding.apply {
            spnFrom.adapter = spinnerAdapter
            spnTo.adapter = spinnerAdapter
            btnConverter.setOnClickListener {
                if (edtValue.text.isBlank())
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.error_massege),
                        Toast.LENGTH_SHORT
                    ).show()
                else {
                    val from = spnFrom.selectedItem.toString()
                    val to = spnTo.selectedItem.toString()
                    if (from == to) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.same_values_message),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        tvResult.visibility = View.INVISIBLE
                        progressBar.visibility = View.VISIBLE
                        CoroutineScope(Dispatchers.Main).launch {
                            val convertedCurrencyValue: Double =
                                withContext(Dispatchers.IO)
                                { repository.convertCurrency(from, to) }
                            val value: Double = edtValue.text.toString().toDouble()
                            tvResult.text = numberFormat.format(value.times(convertedCurrencyValue))
                            progressBar.visibility = View.INVISIBLE
                            tvResult.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }


}