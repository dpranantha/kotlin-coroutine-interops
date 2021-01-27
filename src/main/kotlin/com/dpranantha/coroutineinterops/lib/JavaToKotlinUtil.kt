package com.dpranantha.coroutineinterops.lib

import java.util.*

fun <T> Optional<T>.unwrap(): T? = orElse(null)
fun <T> T?.wrap() = Optional.ofNullable(this)
