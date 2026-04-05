package com.kdbrian.rickmorting

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform