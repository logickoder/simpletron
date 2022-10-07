package dev.logickoder.simpletron.core.display

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.io.File
import java.nio.file.Files

class FileDisplayTest {

    @Before
    fun setup(){
        display = FileDisplay(file.path)
    }

    @Test
    fun writesAMessageToFile(){
        display.show(TEST_MESSAGE)
        assertEquals(TEST_MESSAGE, Files.readAllLines(file.toPath())[0])
    }

    @Test
    fun writesMultipleMessagesToFile(){
        val range = 1..10
        for(i in range)
            display.show("$TEST_MESSAGE${display.newline}")
        val lines = Files.readAllLines(file.toPath())
        assertEquals(10, lines.size)
        for (line in lines)
            assertEquals(TEST_MESSAGE, line)
    }

    @Test
    fun throwsAnExceptionWhenClosingDisplayAfterItHasBeenClosedAlready(){
        display.close()
        assertThrows(IllegalStateException::class.java) { display.close() }
    }

    @Test
    fun throwsAnExceptionWhenCallingShowWhenClosed(){
        display.close()
        assertThrows(IllegalStateException::class.java) { display.show(TEST_MESSAGE) }
    }

    companion object {
        private lateinit var display: Display
        private lateinit var file: File
        const val TEST_MESSAGE = "This is a file display test"

        @BeforeClass
        @JvmStatic fun createFile() {
            file = File(FileDisplayTest::class.java.simpleName)
        }

        @AfterClass
        @JvmStatic fun deleteFile(){
            try{
                display.close()
            }catch (_: IllegalStateException){}
            finally {
                //TODO: Find a way to delete this file later [File.deleteOnExit] doesn't work too
                file.delete()
            }
        }
    }
}