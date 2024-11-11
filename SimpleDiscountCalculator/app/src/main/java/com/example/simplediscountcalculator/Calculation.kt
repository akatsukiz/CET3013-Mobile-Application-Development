package com.example.simplediscountcalculator

object Calculation {
    fun calculatePercentOffDiscountedPrice(data: DiscountData) {
        data.saveAmount = data.salesPrice * (data.discount / 100.0)
    }
    fun calculateFixedAmountDiscountedPrice(data: DiscountData) {
        data.saveAmount = data.discount.toDouble()
    }
    fun calculateTaxAndTotalAmount (data: DiscountData) {
        data.taxAmount = data.salesPrice * (data.tax / 100.0)
        data.totalAmount = data.salesPrice - data.saveAmount + data.taxAmount
    }
}
