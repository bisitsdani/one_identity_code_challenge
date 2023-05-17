import kotlin.math.absoluteValue

fun main() {
    val redStart = "\u001B[31m"
    val redEnd = "\u001B[0m"
    val exitText = "${redStart}exit$redEnd"
    println(
        "Welcome, please input an equation.\n" +
        "Mixed numbers should be represented by whole&numerator/denominator (ex. 3&1/4).\n" +
        "Legal operators are * (multiply), / (divide), + (add), - (subtract)\n" +
        "For example: 1/2 * 3&3/4\n" +
        "Type $exitText to stop the program"
    )
    var input: String?
    do {
        print("Please input an equation: ")
        input = readlnOrNull()
        input?.let {
            if(input != "exit") {
                try {
                    val result = calculate(it)
                    println("$input = ${Fraction.toFractionFormat(result)}")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }
        }
    } while (input != "exit")
}

fun calculate(input: String): Double {
    val regex = Regex("""^-?(\d+)?(&?(\d+)/(\d+))?\s{1}([+-/*])\s{1}-?(\d+)?(&?(\d+)/(\d+))?$""")
    if(regex.matches(input)) {
        val (first, operand, second) = input.split(" ")
        val firstFraction = Fraction(first)
        val secondFraction = Fraction(second)
        val result = when(operand) {
            "*" -> firstFraction * secondFraction
            "/" -> firstFraction / secondFraction
            "+" -> firstFraction + secondFraction
            "-" -> firstFraction - secondFraction
            else -> { throw IllegalArgumentException("Operator is not supported") }
        }
        return result
    } else throw IllegalArgumentException("Wrong format, please use the correct format (ex. 1/2 * 3&3/4)")

}

class Fraction(private val input: String) {

    private val doubleFormat: Double

    init {
        doubleFormat = toDouble()
    }

    private fun toDouble(): Double {
        val num = input.let {
            if(it.contains("&"))
                it.split("&").first().toDoubleOrNull()
            else
                null
        } ?: 0.0

        val temp = input.split("&")
        val fractionPart = temp.getOrNull(1) ?: input

        val fractionNumber = fractionPart.split("/")
            .map { it.toDouble() }
            .reduce { numerator, denominator -> numerator / denominator }

        return if(num < 0) num - fractionNumber else num + fractionNumber
    }

    operator fun plus(other: Fraction): Double {
        return doubleFormat + other.doubleFormat
    }

    operator fun minus(other: Fraction): Double {
        return doubleFormat - other.doubleFormat
    }

    operator fun times(other: Fraction): Double {
        return doubleFormat * other.doubleFormat
    }

    operator fun div(other: Fraction): Double {
        if(other.doubleFormat != 0.0) {
            return doubleFormat / other.doubleFormat
        } else throw ArithmeticException("Can't divide with zero")
    }

    companion object {
        fun toFractionFormat(num: Double): String {
            // First convert to numerator/denominator format (ex. 0.75 => 75/100)
            val whole = num.toInt()
            var numerator = num - whole
            var denominator = 1
            while (numerator % 1 != 0.0) {
                numerator *= 10
                denominator *= 10
            }

            val gcd = greatestCommonDivisor(numerator.toLong(), denominator.toLong())

            val fractionNumerator = numerator / gcd
            val fractionDenominator = denominator / gcd

            val isFractionDenominatorMinus = fractionDenominator < 0

            return if(fractionNumerator == 0.0) {
                "$whole"
            } else if (whole == 0) {
                // Bring minus to the front
                "${if(isFractionDenominatorMinus) "-" else ""}${fractionNumerator.toInt()}/${fractionDenominator.absoluteValue.toInt()}"
            } else {
                // Remove minus from denominator, because if it's minus then whole must be minus
                "$whole&${fractionNumerator.toInt()}/${fractionDenominator.absoluteValue.toInt()}"
            }
        }

        private fun greatestCommonDivisor(a: Long, b: Long): Long {
            return if(b == 0L) a else greatestCommonDivisor(b, a % b)
        }
    }

}