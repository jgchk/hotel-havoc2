package com.jgchk.hotelhavoc.network


data class DoorReading(
    val status: String
) {
    override fun toString() = "DoorReading(status='$status')"
}