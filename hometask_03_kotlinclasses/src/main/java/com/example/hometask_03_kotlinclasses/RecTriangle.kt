package com.example.hometask_03_kotlinclasses

class RecTriangle(side1: Int, side2: Int, side3: Int) : Triangle(Int, Int, Int) {

    override fun getSquare(side1: Int, side2: Int, side3: Int) : Double {
        var square : Double
        var cathet1 = 0
        var cathet2 = 0
        if (side1 < side3 && side2 <side3) {
            cathet1 = side1
            cathet2 = side2
        } else if (side2 < side1 && side3 < side1) {
            cathet1 = side2
            cathet2 = side3
        } else if (side1 < side2 && side3 < side2) {
            cathet1 = side1
            cathet2 = side3
        }
        square = 0.5*(cathet1 + cathet2)
        return square
    }
}