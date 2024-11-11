package com.example.simplediscountcalculator

import android.text.InputFilter
import android.text.Spanned

class RangeInputFilter(private val minValue: Int, private val maxValue: Int) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toInt()
            if (isInRange(input, minValue, maxValue)) {
                return null  // Input is between 0 and 100, then accept
            }
        } catch (nfe: NumberFormatException) {
            // Input is not a valid integer
        }
        return ""  // Input is not between 0 and 100, then reject
    }

    private fun isInRange(value: Int, minValue: Int, maxValue: Int): Boolean {
        return value in minValue..maxValue
    }
}

class DoubleRangeInputFilter(private val minValue: Double, private val maxValue: Double) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toDouble()
            if (isInRange(input, minValue, maxValue)) {
                return null  // Input is between 0.0 and 10000000.0, then accept
            }
        } catch (nfe: NumberFormatException) {
            // Input is not a valid double
        }
        return ""  // Input is not between 0.0 and 10000000.0, then reject
    }

    private fun isInRange(value: Double, minValue: Double, maxValue: Double): Boolean {
        return value in minValue..maxValue
    }
}

