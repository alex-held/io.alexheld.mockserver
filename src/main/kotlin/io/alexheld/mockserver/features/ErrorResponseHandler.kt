package io.alexheld.mockserver.features

import org.koin.core.*



/*

*/
/**
 * Handles requests which return either a [GenericErrorResponse] or an [ErrorResponse] gracefully.
 *
 * To configure the [ErrorResponseHandler] please configure the [ErrorResponseHandler.Configuration]
 * @sample
 * install(ErrorResponseHandler) {
 *    this.loggerFactory = inject()
 * }
 *//*

class ErrorResponseHandler(val config: Configuration) : ApplicationCallPipeline(), KoinComponent {

    // Body of the feature
    private suspend fun intercept(context: PipelineContext<Response, ApplicationCall>) {

        val factory: MockServerLoggerFactory by inject()
        val logger = factory.createLogger<ErrorResponseHandler>()

        logger.info(
"""
    > Starting execution of ${ErrorResponseHandler::class.simpleName}
    > [${context.call.callId}] ${config.message}
    > ~~~
""".trimIndent())

        when (val subject = context.subject) {
            is ErrorResponse -> logger.error(subject)
            is OkResponse -> logger.success("no error!!")
            else -> {
                logger.info("what happened? no error no success!")
            }
        }

        context.call.response.pipeline.intercept(ApplicationSendPipeline.Transform)  { ctx: PipelineContext<Response, ApplicationCall> ->
            ctx.context.respond(HttpStatusCode.InternalServerError, ctx.subject)
            proceed()
        }
    }


    */
/**
     * [ErrorResponseHandler] feature's configuration
     *//*

    class Configuration {
        var message: String  ="Hello World!"
    }


    */
/**
     * Installable feature for [ErrorResponseHandler]
     *//*

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, ErrorResponseHandler> {

        override val key: AttributeKey<ErrorResponseHandler> = AttributeKey("ErrorResponseHandler")


        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): ErrorResponseHandler {

            // Call user code to configure a feature
            val configuration = Configuration().apply(configure)

            // Create a feature instance
            val feature = ErrorResponseHandler(configuration)

            // Install an interceptor that will be run on each call and call feature instance
            pipeline.intercept(Call) { block: PipelineContext<Response, ApplicationCall> -> feature.intercept(block) }

            // Return a feature instance so that client code can use it
            return feature
        }

    }
}
*/
