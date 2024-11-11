package com.example.simplediscountcalculator

data class DiscountData(
    var salesPrice:Double = 0.0,
    var discount:Int = 0,
    var tax:Int = 0,
    var totalAmount:Double = 0.0,
    var saveAmount:Double = 0.0,
    var taxAmount:Double = 0.0,
)
