package com.jgchk.hotelhavoc.network

import retrofit2.Call
import retrofit2.http.GET

internal interface SensorsApi {
    @GET("chop") fun chop(): Call<AccelerationReading>
    @GET("cook") fun cook(): Call<AccelerationReading>
    @GET("blend") fun blend(): Call<PowerReading>
    @GET("oven") fun oven(): Call<DoorReading>
}