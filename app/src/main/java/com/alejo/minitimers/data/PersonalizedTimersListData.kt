package com.alejo.minitimers.data

data class PersonalizedTimer(
    val personalizedTime: Long
)

val personalizedtimersList = listOf(
    PersonalizedTimer(3_000L),
    PersonalizedTimer(40_000L),
    PersonalizedTimer(180_000L),
    PersonalizedTimer(300_000L),
    PersonalizedTimer(5_000L),
)