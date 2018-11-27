package com.jgchk.hotelhavoc.network

import com.jgchk.hotelhavoc.core.interactor.UseCase
import javax.inject.Inject

class OvenReading
@Inject constructor(private val sensorsRepository: SensorsRepository) : UseCase<DoorReading, UseCase.None>() {
    override suspend fun run(params: None) = sensorsRepository.ovenReading()
}