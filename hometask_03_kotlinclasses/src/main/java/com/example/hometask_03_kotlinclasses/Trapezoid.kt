package com.example.hometask_03_kotlinclasses

class Trapezoid(side1: Int, side2: Int, height: Int) : Figure() {
    fun getSquare (side1: Int, side2: Int, height: Int) = (side1+side2)*0.5*height
}