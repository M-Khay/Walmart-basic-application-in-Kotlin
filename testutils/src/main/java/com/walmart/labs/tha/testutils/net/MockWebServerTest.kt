package com.walmart.labs.tha.testutils.net

import androidx.annotation.CallSuper
import okhttp3.mockwebserver.MockWebServer

open class MockWebServerTest {

    lateinit var server : MockWebServer

    @CallSuper
    open fun before() {
        server = MockWebServer()
        server.start()
    }

    @CallSuper
    open fun after() {
        server.shutdown()
    }
}