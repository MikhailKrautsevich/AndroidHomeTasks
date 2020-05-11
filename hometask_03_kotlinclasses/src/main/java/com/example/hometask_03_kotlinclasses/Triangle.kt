package com.example.hometask_03_kotlinclasses

import kotlin.math.sqrt

open class Triangle(side1: Int.Companion, side2: Int.Companion, side3: Int.Companion) : Figure() {
    open fun getSquare(side1: Int, side2: Int, side3: Int): Double {
        val halfperimeter : Double = (side1 + side2 + side3)/2.0
        val square : Double = sqrt(halfperimeter*(halfperimeter-side1)*(halfperimeter-side2)*(halfperimeter-side3))
        return square
    }
}