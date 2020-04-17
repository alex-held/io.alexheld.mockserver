package io.alexheld.mockserver.responses

import arrow.core.*
import kotlin.reflect.*




open class Response(val errors: MutableList<Error>) {
    constructor() : this(mutableListOf())

    val isError: Boolean get() = errors.size > 0
}

open class GenericResponse<T>(errors: MutableList<Error>) : Response() {

    var data: Option<T>

    init {
        data = none()
    }

    constructor(responseData: T) : this(mutableListOf()) {
        data = Some(responseData)
    }

}


open class ErrorResponse(errors: MutableList<Error>) : Response(errors) {
    constructor(error: Error) : this(mutableListOf(error))
}

open class GenericErrorResponse<T>(errors: MutableList<Error>) : GenericResponse<T>(errors) {
    constructor(error: Error) : this(mutableListOf(error))
}


open class OkResponse : Response()
open class GenericOkResponse<T>(data: T) : GenericResponse<T>(data)


abstract class Error(val message: String, val errorCode: ErrorCode)

open class OperationFailedErrorResponse(message: String) : Error(message, ErrorCode.Setup_Deletion_Failed)

enum class ErrorCode(val code: Int, errorType: KClass<*>) {
    Setup_Deletion_Failed(300, OperationFailedErrorResponse::class)
}
