package com.acs.oboeruapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform