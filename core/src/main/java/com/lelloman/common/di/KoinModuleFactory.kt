package com.lelloman.common.di

import org.koin.core.module.Module

interface KoinModuleFactory {
    fun makeKoinModule(override: Boolean = false): Module
}