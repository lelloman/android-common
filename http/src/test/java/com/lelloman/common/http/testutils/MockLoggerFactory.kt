package com.lelloman.common.http.testutils

import com.lelloman.common.logger.Logger
import com.lelloman.common.logger.LoggerFactory

class MockLoggerFactory(private val logger: Logger = MockLogger()) : LoggerFactory {

    override fun getLogger(clazz: Class<*>) = logger
}