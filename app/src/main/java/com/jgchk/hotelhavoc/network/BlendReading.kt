package com.jgchk.hotelhavoc.network

import com.jgchk.hotelhavoc.core.interactor.UseCase
import javax.inject.Inject

class BlendReading
@Inject constructor(private val sensorsRepository: SensorsRepository) : UseCase<PowerReading, UseCase.None>() {
    override suspend fun run(params: None) = sensorsRepository.blendingReading()
}