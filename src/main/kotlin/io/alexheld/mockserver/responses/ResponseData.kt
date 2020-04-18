package io.alexheld.mockserver.responses

import kotlin.reflect.*


open class Response(val errors: MutableList<Error>) {
    constructor() : this(mutableListOf())

    val isError: Boolean get() = errors.size > 0
}

open class GenericResponse<T>(val data: T?) : Response() {

    constructor (errors: MutableList<Error>) : this(null){
        this.errors.addAll(errors)
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
