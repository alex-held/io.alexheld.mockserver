package io.alexheld.mockserver.api.setups

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.infrastracture.*
import io.alexheld.mockserver.responses.*


/**
 * Handles incoming [DeleteSetupCommand] to delete a [Setup] from the mockserver.
 */
class DeleteSetupHandlerImpl(val txManager: MockServerTxManager) : DeleteSetupHandler {

    override fun invoke(command: DeleteSetupCommand): GenericResponse<List<Setup>> {
        return txManager.tx {
            try {
                val response = this.deleteSetup(command)
                val deleted = response.data
                GenericResponse(deleted)
            } catch (ex: Exception) {
                GenericErrorResponse(OperationFailedErrorResponse(ex.localizedMessage))
            }
        }
    }
}

data class DeleteSetupCommand(val request: Request?, val action: Action)

interface DeleteSetupHandler {
    operator fun invoke(command: DeleteSetupCommand): GenericResponse<List<Setup>>
}
