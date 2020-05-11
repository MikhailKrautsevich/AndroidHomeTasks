package com.example.hometask_03_kotlinclasses

class Rectangle(val width: Int , val length : Int) : Figure() {

    fun getSquare(width: Int, length: Int) = width*length
}