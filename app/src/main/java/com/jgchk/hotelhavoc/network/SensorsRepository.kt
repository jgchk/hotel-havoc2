package com.jgchk.hotelhavoc.network

import com.jgchk.hotelhavoc.core.exception.Failure
import com.jgchk.hotelhavoc.core.functional.Either
import com.jgchk.hotelhavoc.core.platform.NetworkHandler
import retrofit2.Call
import javax.inject.Inject

interface SensorsRepository {
    fun choppingReading(): Either<Failure, AccelerationReading>
    fun cookingReading(): Either<Failure, AccelerationReading>
    fun blendingReading(): Either<Failure, PowerReading>
    fun ovenReading(): Either<Failure, DoorReading>

    class Network
    @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val service: SensorsService
    ) : SensorsRepository {
        override fun choppingReading(): Either<Failure, AccelerationReading> {
            return when (networkHandler.isConnected) {
                true -> request(service.chop())
                false, null -> Either.Left(Failure.NetworkConnection())
            }
        }

        override fun cookingReading(): Either<Failure, AccelerationReading> {
            return when (networkHandler.isConnected) {
                true -> request(service.cook())
                false, null -> Either.Left(Failure.NetworkConnection())
            }
        }

        override fun blendingReading(): Either<Failure, PowerReading> {
            return when (networkHandler.isConnected) {
                true -> request(service.blend())
                false, null -> Either.Left(Failure.NetworkConnection())
            }
        }

        override fun ovenReading(): Either<Failure, DoorReading> {
            return when (networkHandler.isConnected) {
                true -> request(service.oven())
                false, null -> Either.Left(Failure.NetworkConnection())
            }
        }

        private fun <T> request(call: Call<T>): Either<Failure, T> {
            val response = call.execute()
            return when (response.isSuccessful) {
                true -> Either.Right(response.body()!!)
                false -> Either.Left(Failure.ServerError())
            }
        }
    }
}