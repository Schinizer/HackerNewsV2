package com.schinizer.hackernews.composetracer

import android.content.Context
import androidx.compose.runtime.Composer
import androidx.compose.runtime.CompositionTracer
import androidx.compose.runtime.InternalComposeTracingApi
import androidx.startup.Initializer
import androidx.tracing.Trace

/**
 * Composition tracer implemented using androidx.tracing APIs
 */
@OptIn(InternalComposeTracingApi::class)
class ComposeTracer : Initializer<Unit> {
    override fun create(context: Context) {
        Composer.setTracer(object : CompositionTracer {
            override fun traceEventStart(key: Int, dirty1: Int, dirty2: Int, info: String) =
                Trace.beginSection(info.takeLast(127))
            override fun traceEventEnd() = Trace.endSection()
            override fun isTraceInProgress(): Boolean = Trace.isEnabled()
        })
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}