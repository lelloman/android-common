package com.lelloman.common.logger

@Suppress("unused")
class CompoundLogger(private vararg val loggers: Logger) : Logger {
    override fun i(msg: String) = loggers.forEach { it.i(msg) }

    override fun d(msg: String, throwable: Throwable?) = loggers.forEach { it.d(msg, throwable) }

    override fun w(msg: String, throwable: Throwable?) =
        loggers.forEach { it.w(msg, throwable) }

    override fun e(msg: String, throwable: Throwable?) =
        loggers.forEach { it.e(msg, throwable) }
}