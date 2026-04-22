package com.kdbrian.nursa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform