package com.walmart.labs.tha.testutils.io

import androidx.annotation.CallSuper
import com.squareup.moshi.Moshi

open class BaseMoshiParsingTest {

    protected lateinit var moshi: Moshi

    @CallSuper
    open fun setup() {
        moshi = Moshi.Builder().build()
    }
}