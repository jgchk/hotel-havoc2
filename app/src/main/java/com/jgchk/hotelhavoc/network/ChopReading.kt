package com.jgchk.hotelhavoc.network

import com.jgchk.hotelhavoc.core.interactor.UseCase
import javax.inject.Inject

class ChopReading
@Inject constructor(private val sensorsRepository: SensorsRepository) : UseCase<AccelerationReading, UseCase.None>() {
    override suspend fun run(params: None) = sensorsRepository.choppingReading()
}