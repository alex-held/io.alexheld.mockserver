package io.alexheld.mockserver.config


import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.serialization.*
import io.alexheld.mockserver.web.*
import io.alexheld.mockserver.web.ErrorResponse
import io.alexheld.mockserver.web.controllers.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.*
import kotlin.math.*


class Mock(val pipeline: Application) : ApplicationCallPipeline() {

    val setupService get() = Instances.setupService

    suspend fun interceptor(context: PipelineContext<Unit, ApplicationCall>) {
        val port = context.context.request.local.port
        println("[MOCK] intercepting on port $port")

        if (port == MockConfig.MOCK_PORT)
            executeMockPipeline(context)
        else
            executeAdministrationPipeline(context)

    }

    suspend fun executeAdministrationPipeline(context: PipelineContext<Unit, ApplicationCall>){
        context.application.log.debug("[MOCK] Executing Admin Pipeline")
    }

    suspend fun executeMockPipeline(context: PipelineContext<Unit, ApplicationCall>) {
        context.application.log.debug("[MOCK] Executing Admin Pipeline")
        val setup = setupService.getMatchingSetup(context.call)
        if (setup != null){
            val json = Serializer.serialize(setup)
            context.call.respondText(json, contentType = ContentType.Application.Json, status = HttpStatusCode.OK)
        }
        else {
            context.call.respondText("NO MATCHING SETUP CONFIGURED", contentType = ContentType.Text.Plain, status = HttpStatusCode.NotFound)
        }
    }

    /**
     * Installable feature for [Mock]
     */
    @Suppress("PublicApiImplicitType")
    companion object Feature : ApplicationFeature<Application, Mock, Mock> {

        override val key: AttributeKey<Mock> = AttributeKey("Mock")

        override fun install(pipeline: Application, configure: Mock.() -> Unit): Mock {
            val mock = Mock(pipeline).apply(configure)
            pipeline.intercept(ApplicationCallPipeline.Call) { mock.interceptor(this) }
            return mock
        }
    }
}

