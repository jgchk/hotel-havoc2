package com.jgchk.hotelhavoc.network


data class AccelerationReading(
    val acceleration: String,
    val threeAxis: ThreeAxisReading
) {
    override fun toString() = "AccelerationReading(isAccelerating=$acceleration, threeAxisReading=$threeAxis)"
}