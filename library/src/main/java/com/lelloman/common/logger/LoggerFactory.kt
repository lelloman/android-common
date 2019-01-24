package com.lelloman.common.logger

interface LoggerFactory {

    fun getLogger(clazz: Class<*>): Logger
}

internal class LoggerFactoryImpl : LoggerFactory {

    override fun getLogger(clazz: Class<*>): Logger {
        return AndroidLogger(clazz.simpleName)
    }
}