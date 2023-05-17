import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class FractionTest {

    @Test
    fun toFractionFormat() {
        val firstExpected = "1/2"
        val secondExpected = "1&1/2"
        val thirdExpected = "-3/4"

        val firstResult = Fraction.toFractionFormat(0.5)
        val secondResult = Fraction.toFractionFormat(1.5)
        val thirdResult = Fraction.toFractionFormat(-0.75)

        assertEquals(firstExpected, firstResult)
        assertEquals(secondExpected, secondResult)
        assertEquals(thirdExpected, thirdResult)
    }

    @Test
    fun plus() {
        val expected = "3&1/2"
        val result = calculate("2&3/8 + 9/8")
        assertEquals(expected, Fraction.toFractionFormat(result))
    }

    @Test
    fun minus() {
        val expected = "-1/4"
        val result = calculate("1&3/4 - 2")
        assertEquals(expected, Fraction.toFractionFormat(result))
    }

    @Test
    fun times() {
        val expected = "1&7/8"
        val result = calculate("1/2 * 3&3/4")
        assertEquals(expected, Fraction.toFractionFormat(result))
    }

    @Test
    fun div() {
        val expected = "15"
        val result = calculate("3&3/4 / 1/4")
        assertEquals(expected, Fraction.toFractionFormat(result))
    }
}