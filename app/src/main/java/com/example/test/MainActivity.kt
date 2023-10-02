package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var input: TextView
    private lateinit var output: TextView
    private var canAddOperation = false
    private var canAddDecimal = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        input = findViewById(R.id.inputId)
        output = findViewById(R.id.outputId)

    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal)
                    input.append(view.text)
                canAddDecimal = false

            } else
                input.append(view.text)
            canAddOperation = true
        }
    }

    fun numberOperation(view: View) {
        if (view is Button && canAddOperation) {
            input.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }


    }

    fun equalAction(view: View) {
        output.text = calculateResult()
        input.text = ""

    }


    fun allClearAction(view: View) {
        input.text = ""
        output.text = ""
    }

    fun clearAction(view: View) {
        var length = input.length()
        if (length > 0)
            input.text = input.text.subSequence(0, length - 1)
    }


    private fun calculateResult(): String {
        val digitOperators = digitOperators()
        if (digitOperators.isEmpty())
            return ""
        val timesDivCal = timesDivCal(digitOperators)
        if (timesDivCal.isEmpty())
            return ""
        var result = addSubCal(timesDivCal)

        return result.toString()
    }

    private fun addSubCal(list: MutableList<Any>): Float {
        var result = list[0] as Float
        for (i in list.indices) {
            if (list[i] is Char && i != list.lastIndex) {
                val operator = list[i]

                var nextDigit = list[i + 1] as Float
                when (operator) {
                    '+' -> {
                        result += nextDigit
                    }

                    '-' -> {
                        result -= nextDigit
                    }

                    else -> {

                    }
                }
            }

        }

        return result
    }

    private fun timesDivCal(digitOperators: MutableList<Any>): MutableList<Any> {
        var list = digitOperators
        while (list.contains('X') || list.contains('/')) {

            list = calTimeDiv(list)
        }

        return list
    }

    private fun calTimeDiv(list: MutableList<Any>): MutableList<Any> {


        var newList = mutableListOf<Any>()
        var restartIndex = list.size
        for (i in list.indices) {
            if (list[i] is Char && i != list.lastIndex && i < restartIndex) {
                val operator = list[i]
                var prvDigit = list[i - 1] as Float
                var nextDigit = list[i + 1] as Float
                when (operator) {
                    'X' -> {
                        newList.add(prvDigit * nextDigit)
                        restartIndex = i + 1
                    }

                    '/' -> {
                        newList.add(prvDigit / nextDigit)
                        restartIndex = i + 1
                    }

                    else -> {
                        newList.add(prvDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex) {
                newList.add(list[i])
            }
        }

        return newList
    }

    private fun digitOperators(): MutableList<Any> {
        var list = mutableListOf<Any>()
        var currentDigit = ""
        for (char in input.text) {
            if (char.isDigit() || char == '.') {
                currentDigit += char
            } else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(char)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

}