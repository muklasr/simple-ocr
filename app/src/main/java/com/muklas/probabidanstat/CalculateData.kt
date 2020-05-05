package com.muklas.probabidanstat

import kotlin.math.log10
import kotlin.math.pow

class CalculateData {
    companion object {
        fun countData(data: ArrayList<Double>): Double {
            var total = 0.0
            for (i in data) {
                total += i
            }
            return total
        }

        fun getMean(data: ArrayList<Double>): Double {
            val total = countData(data)
            val n = data.size
            return total / n
        }

        fun getMedian(data: ArrayList<Double>): Double {
            data.sort()
            val n = data.size
            return if (n % 2 == 0) {
                val medianIndex1 = n / 2
                val medianIndex2 = medianIndex1 + 1
                val median1 = data[medianIndex1]
                val median2 = data[medianIndex2]
                (median1 + median2) / 2
            } else {
                val medianIndex = ((n + 1) / 2)
                data[medianIndex - 1]
            }
        }

        fun getModus(data: ArrayList<Double>): Double {
            return 0.0
        }

        fun getRange(data: ArrayList<Double>): Double {
            data.sort()
            return data[data.size - 1] - data[0]
        }

        fun getClassCount(data: ArrayList<Double>): Double {
            val n = data.size
            val classCount = log10(n.toDouble()) * 3.3 + 1
            return classCount
        }

        fun getInterval(data: ArrayList<Double>): Double {
            return countData(data) / getClassCount(data)
        }

        fun getVariation(data: ArrayList<Double>): Double {
            var variation = 0.0
            var n = data.size
            for (i in data) {
                variation += (i - getMean(data)).pow(2)
            }
            return variation/(n-1)
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////
        fun showData(data: ArrayList<Double>): String {
            var listString = "Data = "
            for (i in data) {
                listString += "$i "
            }
            return listString
        }

        fun countDataString(data: ArrayList<Double>): String {
            var total = "Total = "
            for (i in data) {
                if (i != data[0]) total += "+"
                total += "$i"
            }
            total += " = " + countData(data)
            return total
        }

        fun getMeanString(data: ArrayList<Double>): String {
            val total = countData(data)
            val n = data.size
            return "Mean = $total/$n =" + getMean(data)
        }

        fun getMedianString(data: ArrayList<Double>): String {
            data.sort()
            val n = data.size
            return if (n % 2 == 0) {
                val medianIndex1 = n / 2
                val medianIndex2 = medianIndex1 + 1
                val median1 = data[medianIndex1]
                val median2 = data[medianIndex2]
                "($median1 + $median2) / 2 = " + getMedian(data)
            } else {
                val medianIndex = ((n + 1) / 2)
                "Data at index $medianIndex = " + getMedian(data)
            }
        }

        fun getRangeString(data: ArrayList<Double>): String {
            data.sort()
            return "Range = " + data[data.size - 1] + "-" + data[0] + " = " + getRange(data)
        }

        fun getClassCountString(data: ArrayList<Double>): String {
            val n = data.size
            return "Class count = 1+3.3log($n) = " + getClassCount(data) + " ~" + getClassCount(data).toInt()
        }

        fun getIntervalString(data: ArrayList<Double>): String {
            return "Interval = " + countData(data) + "/" + getClassCount(data) + " = " + getInterval(
                data
            )
        }

        fun getVariationString(data: ArrayList<Double>): String {
            var variation = ""
            val n = data.size
            for (i in data) {
                if (i != data[0]) variation += "+"
                variation += "($i - " + getMean(data) + ")^2"
            }
            return "Variation = " + variation + "/$n-1 = " + getVariation(data)
        }
    }
}