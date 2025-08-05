package org.cmppractice.project1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform