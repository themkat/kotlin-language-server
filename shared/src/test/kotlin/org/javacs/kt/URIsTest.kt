package org.javacs.kt

import org.junit.Assert.assertEquals
import org.javacs.kt.util.parseURI
import org.junit.Test
import java.net.URI
import java.nio.file.Paths

class URIsTest {
    @Test
    fun `parseURI should work with different paths`() {
        assertEquals(
            URI.create("/home/ws%201"),
            parseURI("/home/ws 1")
        )

        assertEquals(
            URI.create("/home/ws-1"),
            parseURI("/home/ws-1")
        )

        assertEquals(
            URI.create("file:/home/ws%201"),
            parseURI("file:///home/ws%201")
        )

        assertEquals(
            URI.create("file:/home/ws%201"),
            parseURI("file%3A%2F%2F%2Fhome%2Fws%201")
        )
    }

	@Test
    fun `parseURI should work with special characters and work with Paths-get`() {
        var uriParsed = parseURI("file:///home/p%C3%A5l/Programming/Kotlin/%E5%A5%BD- projecttesting")
        assertEquals(
            URI.create("file:/home/pål/Programming/Kotlin/好-%20projecttesting"),
            uriParsed
        )
        Paths.get(uriParsed)

        uriParsed = parseURI("file:///home/%C4%99iafella")
        assertEquals(
            URI.create("file:/home/ęiafella"),
            uriParsed
        )
        Paths.get(uriParsed)

        uriParsed = parseURI("file:/home/%C6%84adass")
        assertEquals(
            URI.create("file:/home/Ƅadass"),
            uriParsed
        )
        Paths.get(uriParsed)
    }
}
