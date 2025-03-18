package com.alejo.minitimers.data

data class PersonalizedTimer(
    val personalizedTime: Long
)

val personalizedtimersList = listOf(
    PersonalizedTimer(5_000L),
    PersonalizedTimer(15_000L),
    PersonalizedTimer(30_000L),
    PersonalizedTimer(60_000L),
    PersonalizedTimer(120_000L),
    PersonalizedTimer(300_000L),
)