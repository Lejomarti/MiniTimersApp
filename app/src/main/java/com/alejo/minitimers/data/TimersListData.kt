package com.alejo.minitimers.data

data class Timer(
    val time: Long
)

val timersList = listOf(
    Timer(5_000L),
    Timer(15_000L),
    Timer(30_000L),
    Timer(60_000L),
    Timer(120_000L),
    Timer(300_000L),
)
