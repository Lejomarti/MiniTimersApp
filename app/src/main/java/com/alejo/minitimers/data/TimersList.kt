package com.alejo.minitimers.data

data class Timer(
    val time: Long
)

val timersList = listOf(
    Timer(8_000L),
    Timer(60_000L),
    Timer(10_000L),
    Timer(15_000L),
    Timer(5_000L),
)
