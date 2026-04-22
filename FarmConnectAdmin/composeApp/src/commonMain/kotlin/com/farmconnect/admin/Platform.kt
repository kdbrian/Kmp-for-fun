package com.farmconnect.admin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform