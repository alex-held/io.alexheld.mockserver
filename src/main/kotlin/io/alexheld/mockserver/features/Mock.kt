/*
package io.alexheld.mockserver.features

import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.responses.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.koin.core.*

class Mock(val pipeline: Application) : ApplicationCallPipeline(), KoinComponent {

    suspend fun interceptor(context: PipelineContext<Unit, ApplicationCall>) {

        val mockService: MockService by inject()
        val port = context.context.request.local.port

        println("[MOCK] context.proceedWith(.Response(mutableListOf(OperationFailedErrorResponse()))")
        println("[MOCK] intercepting on port $port")

        if (port == MockConfig.MOCK_PORT)
            mockService.executeMockPipeline(context)

    }

    */
/**
     * Installable feature for [Mock]
     *//*

    @Suppress("PublicApiImplicitType")
    companion object Feature : ApplicationFeature<Application, Mock, Mock> {

        override val key: AttributeKey<Mock> = AttributeKey("Mock")

        override fun install(pipeline: Application, configure: Mock.() -> Unit): Mock {
            val mock = Mock(pipeline).apply(configure)

            pipeline.receivePipeline.intercept(ApplicationReceivePipeline.Before) {
                context.application.attributes.put(AttributeKey("ErrErr"), ErrorResponse(OperationFailedErrorResponse("")))
            }

            pipeline.intercept(Call) { mock.interceptor(this) }
            return mock
        }
    }
}
*/
