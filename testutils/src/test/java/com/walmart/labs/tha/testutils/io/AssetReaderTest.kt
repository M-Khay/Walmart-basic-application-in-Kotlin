package com.walmart.labs.tha.testutils.io

import org.junit.Test

import org.junit.Assert.*

class AssetReaderTest {

    @Test
    fun loadResource1() {
        val expected = "test\n"
        val actual = AssetReader().loadResource("test1.txt")
        assertEquals(expected, actual)
    }

    @Test
    fun loadResource2() {
        val expected = "multi\nline\ntest\n"
        val actual = AssetReader().loadResource("test2.txt")
        assertEquals(expected, actual)
    }
}