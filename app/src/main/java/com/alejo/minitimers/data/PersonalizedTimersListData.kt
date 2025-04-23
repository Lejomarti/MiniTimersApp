package com.alejo.minitimers.data

data class PersonalizedTimer(
    val personalizedTime: Long
)

val personalizedtimersList = listOf(
    PersonalizedTimer(5_000L),
    PersonalizedTimer(10_000L),
    PersonalizedTimer(20_000L),
    PersonalizedTimer(30_000L),
    PersonalizedTimer(40_000L),
    PersonalizedTimer(50_000L),
    PersonalizedTimer(60_000L),
    PersonalizedTimer(90_000L),
    PersonalizedTimer(105_000L),
    PersonalizedTimer(600_000L),
    PersonalizedTimer(1800_000L),
    PersonalizedTimer(2400_000L),
)