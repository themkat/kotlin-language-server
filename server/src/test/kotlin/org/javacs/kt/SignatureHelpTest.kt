package org.javacs.kt

import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Ignore
import org.junit.Test

class SignatureHelpTest : SingleFileTestFixture("signatureHelp", "SignatureHelp.kt") {
    @Test fun `provide signature help for a function call`() {
        val help = languageServer.textDocumentService.signatureHelp(signatureHelpParams(file, 3, 20)).get()!!

        assertThat(help.signatures, hasSize(2))
        assertThat(help.activeParameter, equalTo(0))
        assertThat(help.activeSignature, equalTo(0))
        assertThat(help.signatures.map { it.label }, hasItems("fun foo(bar: String): Unit", "fun foo(bar: Int): Unit"))
        assertThat(help.signatures.map { it.documentation.left }, hasItems("Call foo with a String", "Call foo with an Int"))
        assertThat(help.signatures.flatMap { it.parameters.map { it.label.left }}, hasItems("bar: String", "bar: Int"))
        assertThat(help.signatures.flatMap { it.parameters.map { it.documentation.left }}, hasItems("String param", "Int param"))
    }

    @Test fun `provide signature help for a constructor`() {
        val help = languageServer.textDocumentService.signatureHelp(signatureHelpParams(file, 4, 21)).get()!!

        assertThat(help.signatures, hasSize(2))
        assertThat(help.activeParameter, equalTo(0))
        assertThat(help.activeSignature, equalTo(0))
        assertThat(help.signatures.map { it.label }, hasItems("constructor Constructor(bar: String)", "constructor Constructor(bar: Int)"))
        assertThat(help.signatures.map { it.documentation.left }, hasItems("Construct with a String", "Construct with an Int"))
        assertThat(help.signatures.flatMap { it.parameters.map { it.label.left }}, hasItems("bar: String", "bar: Int"))
        assertThat(help.signatures.flatMap { it.parameters.map { it.documentation.left }}, hasItems("String param", "Int param"))
    }

    @Test fun `find active parameter`() {
        val help = languageServer.textDocumentService.signatureHelp(signatureHelpParams(file, 5, 32)).get()!!

        assertThat(help.activeParameter, equalTo(1))
    }

    @Test fun `find active parameter in the middle of list`() {
        val help = languageServer.textDocumentService.signatureHelp(signatureHelpParams(file, 5, 30)).get()!!

        assertThat(help.activeParameter, equalTo(0))
    }

    @Ignore @Test fun `find active signature using types`() {
        val help = languageServer.textDocumentService.signatureHelp(signatureHelpParams(file, 5, 32)).get()!!

        assertThat(help.signatures[help.activeSignature].label, equalTo("fun multiParam(first: String, second: String): Unit"))
    }

    @Test fun `find active signature using number of arguments`() {
        val help = languageServer.textDocumentService.signatureHelp(signatureHelpParams(file, 6, 34)).get()!!

        assertThat(help.signatures[help.activeSignature].label, equalTo("fun oneOrTwoArgs(first: String, second: String): Unit"))
    }

    @Test fun `provide signature help for a Kotlin SDK function`() {
        val help = languageServer.textDocumentService.signatureHelp(signatureHelpParams(file, 54, 10)).get()!!

        assertThat(help.signatures, hasSize(3))
        assertThat(help.activeParameter, equalTo(1))
        assertThat(help.activeSignature, equalTo(-1))
        assertThat(help.signatures.map { it.label }, hasItems("fun <T> listOf(vararg elements: T): List<T>", "inline fun <T> listOf(): List<T>", "fun <T> listOf(element: T): List<T>"))
        assertThat(help.signatures.map { it.documentation.left }, hasItems("Returns a new read-only list of given elements.", "Returns an empty read-only list.", "Returns an immutable list containing only the specified object element."))
    }
}
