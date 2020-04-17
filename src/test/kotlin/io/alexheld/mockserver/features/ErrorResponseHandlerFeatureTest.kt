package io.alexheld.mockserver.features

import io.alexheld.mockserver.*
import io.alexheld.mockserver.responses.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import org.junit.jupiter.api.Test
import kotlin.test.*

@KtorExperimentalAPI
class ErrorResponseHandlerFeatureTest {

    @Test
    fun `should initialize feature`(): Unit = withTestApplication(Application::module) {

        cookiesSession {

            handleRequest(HttpMethod.Put, "/setups/create") {

                this.call.attributes.put(AttributeKey("ErrErr"), ErrorResponse(OperationFailedErrorResponse("")))

                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello from Ktor Testable sample application", response.content)
            }
        }

    }

}
