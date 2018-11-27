package com.jgchk.hotelhavoc.network

data class ThreeAxisReading(val x: Int, val y: Int, val z: Int) {
    override fun toString() = "(x=$x, y=$y, z=$z)"
    fun magnitude() = (x + y + z) / 3.0
}