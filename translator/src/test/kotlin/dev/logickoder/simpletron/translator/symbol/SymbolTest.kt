package dev.logickoder.simpletron.translator.symbol

import dev.logickoder.simpletron.translator.Subroutine
import org.junit.Assert.assertEquals
import org.junit.Test

class SymbolTest {

    @Test
    fun stringToSymbol() {
        val subroutine = Subroutine(Subroutine.DEFAULT, emptyList())
        assertEquals("10".toSymbol(subroutine), Symbol.Constant(10f))
        assertEquals("10.23".toSymbol(subroutine), Symbol.Constant(10.23f))
        assertEquals("v".toSymbol(subroutine), Symbol.Variable("v", subroutine))
        assertEquals("hE".toSymbol(subroutine), Symbol.Variable("hE", subroutine))
    }

    @Test
    fun stringToDataType() {
        assertEquals("'Hello'".toDataType(), DataType.String("Hello"))
        assertEquals("\"Hello\"".toDataType(), DataType.String("Hello"))
        assertEquals("10".toDataType(), DataType.Number(10))
        assertEquals("10 + 2".toDataType(), DataType.Number(12))
        assertEquals(
            "[10, 20, 'Hello', [50, '50', ['10', [],],],]".toDataType(), DataType.Array(
                listOf(
                    DataType.Number(10),
                    DataType.Number(20),
                    DataType.String("Hello"),
                    DataType.Array(
                        listOf(
                            DataType.Number(50),
                            DataType.String("50"),
                            DataType.Array(
                                listOf(
                                    DataType.String("10"),
                                    DataType.Array(emptyList())
                                )
                            )
                        )
                    ),
                )
            )
        )
    }
}