package com.example.simplediscountcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.simplediscountcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel:DiscountModel
    var isSalesPriceValid = false // For Sales Price Validation
    var isDiscountValid = false // For Discount Validation
    var isTaxValid = false // For Tax Validation
    var isTaxClear = false // To avoid the syncing problem between TaxWatcher and Seekbar Listener
    var isDiscountClear = false // To avoid the syncing problem between TaxWatcher and Seekbar Listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add the Action Bar at here
        setSupportActionBar(binding.toolbar)
        // Hide the default title as wanted to show customized title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this)[DiscountModel::class.java]

        // Set the default mode as Percent Off
        binding.radioPercentOff.isChecked = true
        // Set the default mode as With Tax
        binding.switchTax.isChecked = true

        // Set up Discount Slider max to allow sliding from 1 to 100
        binding.sliderDiscountPercent.min = 1
        binding.sliderDiscountPercent.max = 100
        // Set up Tax Slider max to allow sliding from 1 to 100
        binding.sliderTaxPercent.min = 1
        binding.sliderTaxPercent.max = 100

        // Set up the range for Sales Price input
        binding.textSalesPrice.filters = arrayOf(DoubleRangeInputFilter(0.0, 10000000.0))
        // Set up the range for Percent Off Discount for the first time as default mode = Percent Off
        binding.textDiscount.filters = arrayOf(RangeInputFilter(0, 100))
        // Set up the range for Tax input
        binding.textTax.filters = arrayOf(RangeInputFilter(0, 100))

        // Setup SeekBarChange Listener for DiscountPercent (Syncing the slider progress according to text)
        val discountSliderListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the EditText with the Slider progress
                if (!isDiscountClear) {
                    binding.textDiscount.setText(progress.toString())
                    binding.textDiscount.setSelection(binding.textDiscount.text.length)
                }
                /* Use isDiscountClear to fix the bug of syncing SeekerBar and EditTax so that
                * it will not appear 1 when user clear input as it is the min value of SeekerBar*/
                isDiscountClear = false
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }

        // Setup TextWatcher for Discount
        binding.textDiscount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                // Validate the discount input
                val discountInput = s.toString().trim()
                // If in Fixed Amount Mode
                if (binding.radioFixedAmount.isChecked) {
                    if (discountInput.toIntOrNull() == null) {
                        binding.textDiscount.error = getString(R.string.fixed_amount_error1)
                    }
                    else if (discountInput.toInt() <= 0) {
                        binding.textDiscount.error = getString(R.string.fixed_amount_error2)
                    }
                    else if ((isSalesPriceValid) && (discountInput.toDouble()
                                > binding.textSalesPrice.text.toString().toDoubleOrNull()!!)) {
                        binding.textDiscount.error = getString(R.string.fixed_amount_error3)
                    }
                    else {
                        binding.textDiscount.error = null
                    }
                }
                // If in Percent Off Mode
                else {
                    if (discountInput.toIntOrNull() == null) {
                        binding.textDiscount.error = getString(R.string.percent_off_error1)
                    }
                    else if (discountInput.toInt() <= 0) {
                        binding.textDiscount.error = getString(R.string.percent_off_error2)
                    }
                    else {
                        binding.textDiscount.error = null
                    }
                }
                // If no error = validate
                isDiscountValid = binding.textDiscount.error == null
                // Update the state of the calculate button
                updateCalculateButtonState()
            }
        })

        // Setup SeekBarChange Listener for Tax
        val taxSliderListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the EditText with the Slider progress
                if (!isTaxClear) {
                    binding.textTax.setText(progress.toString())
                    binding.textTax.setSelection(binding.textTax.text.length)
                }
                /* Use isTaxClear to fix the bug of syncing SeekerBar and EditTax so that
                * it will not appear 1 when user clear input as it is the min value of SeekerBar*/
                isTaxClear = false
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }

        // Setup TextWatcher for Tax
        val taxTextWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                    // Validate the tax input
                    val taxInput = s.toString().trim()
                    if (taxInput.toIntOrNull() == null) {
                        binding.textTax.error = getString(R.string.tax_error1)
                    }
                    else if (taxInput.toInt() <= 0) {
                        binding.textTax.error = getString(R.string.tax_error2)
                    }
                    else {
                        binding.textTax.error = null
                    }
                    // If no error = validate
                    isTaxValid = binding.textTax.error == null
                    // Update the state of the calculate button
                    updateCalculateButtonState()
            }
        }

        // Add the taxTextWatcher for the first time
        binding.textTax.addTextChangedListener(taxTextWatcher)

        // Set up an OnCheckedChangeListener for the Radio Button
        binding.groupMode.setOnCheckedChangeListener { _, checkedId ->
            // If is in Percent Off mode
            if (checkedId == R.id.radio_percent_off) {
                // Set up the range for Percent Off Discount input
                binding.textDiscount.filters = arrayOf(RangeInputFilter(0, 100))
                if ((binding.textDiscount.text.isNotEmpty())
                    && binding.textDiscount.text.toString().toIntOrNull()!! > 100) {
                    // If user entered > 100 in fixed amount and switch to percent off, set to 100
                    binding.textDiscount.setText("100")
                    binding.textDiscount.setSelection(binding.textDiscount.length())
                }
                if (binding.textDiscount.error != null) {
                    // Clear or change the error in case user switch mode when having error
                    if (binding.textDiscount.error.toString()
                        == getString(R.string.fixed_amount_error1)) {
                            binding.textDiscount.error = getString(R.string.percent_off_error1)
                        }
                        else if (binding.textDiscount.error.toString()
                        == getString(R.string.fixed_amount_error2)) {
                            binding.textDiscount.error = getString(R.string.percent_off_error2)
                        }
                        else {
                            binding.textDiscount.error = null
                        }
                    }

                if (binding.textDiscount.text.isNotEmpty() && binding.textDiscount.error == null) {
                    // If no error = validate
                    isDiscountValid = true
                    // Update the state of the calculate button
                    updateCalculateButtonState()
                }

                binding.textDiscountSymbol.text = "%"
                binding.sliderDiscountPercent.isVisible = true
                binding.buttonGroup.isVisible = false

                // Add the SeekBarChange Listener for discount input when percent off is checked
                binding.sliderDiscountPercent.setOnSeekBarChangeListener(discountSliderListener)

            } else { // If is in Fixed Amount mode
                // Set up the range for Fixed Amount Discount input
                binding.textDiscount.filters = arrayOf(RangeInputFilter(0, 10000000))
                binding.textDiscountSymbol.text = "$"
                binding.sliderDiscountPercent.isVisible = false
                binding.buttonGroup.isVisible = true
                // Remove SeekBarChange Listener when percent off is unchecked
                binding.sliderDiscountPercent.setOnSeekBarChangeListener(null)
                // Clear or change the error in case user switch mode when having error
                if (binding.textDiscount.error != null) {
                    if (binding.textDiscount.error.toString()
                        == getString(R.string.percent_off_error1)) {
                        binding.textDiscount.error = getString(R.string.fixed_amount_error1)
                    }
                    else if (binding.textDiscount.error.toString()
                        == getString(R.string.percent_off_error2)) {
                        binding.textDiscount.error = getString(R.string.fixed_amount_error2)
                    }
                    else {
                        binding.textDiscount.error = null
                    }
                }
                validateFixedAmountDiscount()
            }
        }

        // Add the discount SeekBarChange Listener for the first time
        binding.sliderDiscountPercent.setOnSeekBarChangeListener(discountSliderListener)
        // Add the tax SeekBarChange Listener for the first time
        binding.sliderTaxPercent.setOnSeekBarChangeListener(taxSliderListener)
        // Set up an OnCheckedChangeListener for the Tax Switch
        binding.switchTax.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If the switch is checked (with tax), enable the tax views
                binding.textTax.isEnabled = true
                binding.sliderTaxPercent.isEnabled = true
                // To clear the "0" after unchecking tax to show hint
                binding.textTax.text.clear()
                // Set the tax invalid back in case user did not input and switch from unchecked to checked
                isTaxValid = false
                // Update the state of the calculate button
                updateCalculateButtonState()
                // Add the TextWatcher and SeekBarChange Listener for tax input when tax is enabled
                binding.textTax.addTextChangedListener(taxTextWatcher)
                binding.sliderTaxPercent.setOnSeekBarChangeListener(taxSliderListener)

            } else {
                // If the switch is unchecked (without tax), disable the slider and edit text
                binding.textTax.isEnabled = false
                binding.sliderTaxPercent.isEnabled = false

                // Remove the TextWatcher and SeekBarChange Listener when tax is disabled
                binding.textTax.removeTextChangedListener(taxTextWatcher)
                binding.sliderTaxPercent.setOnSeekBarChangeListener(null)
                // Clear the tax input
                binding.textTax.setText("0")
                // Set the error to null
                binding.textTax.error = null
                // Set the Tax to valid since it is the value we set
                isTaxValid = true

                // Update the state of the calculate button
                updateCalculateButtonState()
            }
        }

        // Set up an OnClickedListener for the FixedAmount Button
        //implement 7 buttons clicking
        binding.buttonMinusTen.setOnClickListener(this)
        binding.buttonMinusFive.setOnClickListener(this)
        binding.buttonMinusOne.setOnClickListener(this)
        binding.buttonPlusOne.setOnClickListener(this)
        binding.buttonPlusFive.setOnClickListener(this)
        binding.buttonPlusTen.setOnClickListener(this)


        binding.textDiscount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString()
                isDiscountClear = inputText.isBlank()
                    // Try to parse the value as an integer
                    val intValue = inputText.toIntOrNull() ?: 0
                    // Clamp the value between 0 and 100
                    val clampedValue = intValue.coerceIn(1, 100)
                    // Update the Slider progress
                binding.sliderDiscountPercent.progress = clampedValue
            }
        })

        binding.textTax.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString()
                isTaxClear = inputText.isBlank()
                    // Try to parse the value as an integer
                    val intValue = inputText.toIntOrNull() ?: 0
                    // Clamp the value between 1 and 100
                    val clampedValue = intValue.coerceIn(1, 100)
                    // Update the Slider progress
                    binding.sliderTaxPercent.progress = clampedValue
            }
        })

        binding.textSalesPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                // Validate the price input
                    val priceInput = s.toString().trim()
                    if (priceInput.toDoubleOrNull() == null) {
                        binding.textSalesPrice.error = getString(R.string.sales_price_error1)
                    }
                    else if (priceInput.toDouble() <= 0)
                    {
                        binding.textSalesPrice.error = getString(R.string.sales_price_error2)
                    }
                    else {
                        binding.textSalesPrice.error = null
                    }
                    // If no error = validate
                    isSalesPriceValid = binding.textSalesPrice.error == null
                    // Update the state of the calculate button
                    updateCalculateButtonState()
                    if ((isSalesPriceValid) && (binding.textDiscount.text.isNotEmpty()) &&
                        (binding.radioFixedAmount.isChecked) &&
                        (priceInput.toDouble() < binding.textDiscount.text.toString().toDouble())) {
                        binding.textDiscount.error = getString(R.string.fixed_amount_error3)
                    }
                    else {
                        if (binding.textDiscount.error == getString(R.string.fixed_amount_error3)) {
                            binding.textDiscount.error = null
                            // If no error = validate
                            isDiscountValid = binding.textDiscount.error == null
                        }
                    }
                    // If no error = validate
                    isSalesPriceValid = binding.textSalesPrice.error == null
                    // Update the state of the calculate button
                    updateCalculateButtonState()
            }
        })

        // Setup OnClickListener for calculate button
        binding.buttonCalculate.setOnClickListener {
            val salesPrice = binding.textSalesPrice.text.toString().toDoubleOrNull() ?: 0.0
            val discount = binding.textDiscount.text.toString().toIntOrNull() ?: 0
            val tax = binding.textTax.text.toString().toIntOrNull() ?: 0

            val discountData = DiscountData(
                salesPrice = salesPrice,
                discount = discount,
                tax = tax
            )
            // If in Percent Off Mode
            if (binding.radioPercentOff.isChecked) {
                Calculation.calculatePercentOffDiscountedPrice(discountData)
            }
            // If in FixedAmount Mode
            else {
                Calculation.calculateFixedAmountDiscountedPrice(discountData)
            }
            // Calculate Tax Amount and Total Amount
            Calculation.calculateTaxAndTotalAmount(discountData)

            // Display the output in 2 decimal places with money symbol
            val saveAmount =  String.format("%.2f $", discountData.saveAmount)
            val taxAmount = String.format("%.2f $", discountData.taxAmount)
            val totalAmount = String.format("%.2f $", discountData.totalAmount)
            binding.textSaveAmount.text = saveAmount
            binding.textTaxAmount.text = taxAmount
            binding.textTotalAmount.text = totalAmount
        }
}

    private fun updateCalculateButtonState() {
        // Set the button to be enabled only when the input is valid
        binding.buttonCalculate.isEnabled = isSalesPriceValid && isDiscountValid && isTaxValid
    }

    private fun validateFixedAmountDiscount() {
        if ((isSalesPriceValid) && (binding.textDiscount.text.isNotEmpty())
            && ((binding.textDiscount.text.toString().toDoubleOrNull() ?: 0.0)
                    > (binding.textSalesPrice.text.toString().toDoubleOrNull() ?: 0.0))
        ) {
            binding.textDiscount.error = getString(R.string.fixed_amount_error3)
            // If no error = validate
            isDiscountValid = binding.textDiscount.error == null
        }
        // Update the state of the calculate button
        updateCalculateButtonState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //create the menu bar
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Toolbar item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear -> {
                clearAll()
            }

            R.id.menu_about -> {
                showDialog()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val SAVED_AMOUNT = "save_amount"
        const val TAX_AMOUNT = "tax_amount"
        const val TOTAL_PRICE = "total_amount"
    }

    // Clear all the input and output
    private fun clearAll() {
        binding.textSalesPrice.setText("")
        binding.textDiscount.setText("")
        binding.textSaveAmount.text = "0.00 $"
        binding.textTaxAmount.text = "0.00 $"
        binding.textTotalAmount.text = "0.00 $"
        // If with tax is unchecked, won't clear the "0"
        if(binding.switchTax.isChecked) {
            binding.textTax.setText("")
        }
    }

    // Save the discount information
    override fun onPause() {
        super.onPause()

        viewModel.saveAmount = binding.textSaveAmount.text.toString()
        viewModel.taxAmount = binding.textTaxAmount.text.toString()
        viewModel.totalAmount = binding.textTotalAmount.text.toString()
    }

    // Retrieve the discount information
    override fun onResume() {
        super.onResume()
        // Validate the tax input
        if (binding.textTax.text.isNotEmpty()) {
            val taxInput = binding.textTax.text.toString().trim()
            if (taxInput.toIntOrNull() == null || taxInput.toInt() <= 0) {
                binding.textTax.error = getString(R.string.tax_error1)
            } else {
                binding.textTax.error = null
            }
        }
        //recover the data for any configuration changes
        binding.textSaveAmount.text = viewModel.saveAmount
        binding.textTaxAmount.text = viewModel.taxAmount
        binding.textTotalAmount.text = viewModel.totalAmount
    }

    // Show App Information
    private fun showDialog() {
        val dialog = AboutDialog()
        dialog.show(supportFragmentManager, "123")
    }

    // Fixed Amount Button
    override fun onClick(v: View?) {
        val currentValue = binding.textDiscount.text.toString().toIntOrNull() ?: 0
        var newValue: Int
        val maxValue = 10000000
        val isMaxValue: Boolean = currentValue >= maxValue

        when (v?.id) {
            R.id.button_minus_ten -> {
                // Handle the -10 button click
                newValue = (currentValue - 10).coerceAtLeast(0)
                binding.textDiscount.setText(newValue.toString())
            }

            R.id.button_minus_five -> {
                // Handle the -5 button click
                newValue = (currentValue - 5).coerceAtLeast(0)
                binding.textDiscount.setText(newValue.toString())
            }

            R.id.button_minus_one -> {
                // Handle the -1 button click
                newValue = (currentValue - 1).coerceAtLeast(0)
                binding.textDiscount.setText(newValue.toString())
            }

            R.id.button_plus_one -> {
                // Handle the +1 button click
                if (!isMaxValue) {
                    newValue = currentValue + 1
                    if (newValue >= maxValue)
                    {
                        binding.textDiscount.setText(maxValue.toString())
                    }
                    else {
                        binding.textDiscount.setText(newValue.toString())
                    }
                }
            }

            R.id.button_plus_five -> {
                // Handle the +5 button click
                if (!isMaxValue) {
                    newValue = currentValue + 5
                    if (newValue >= maxValue)
                    {
                        binding.textDiscount.setText(maxValue.toString())
                    }
                    else {
                        binding.textDiscount.setText(newValue.toString())
                    }
                }
            }

            R.id.button_plus_ten -> {
                // Handle the +10 button click
                if(!isMaxValue) {
                    newValue = currentValue + 10
                    if (newValue >= maxValue)
                    {
                        binding.textDiscount.setText(maxValue.toString())
                    }
                    else {
                        binding.textDiscount.setText(newValue.toString())
                    }
                }
            }
        }
        validateFixedAmountDiscount()
        binding.textDiscount.setSelection(binding.textDiscount.text.length)
    }
}