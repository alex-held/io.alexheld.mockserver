package io.alexheld.mockserver.api.setups

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.infrastracture.*


/**
 * Handles incoming [CreateSetupCommand] to create new [Setup] on the mockserver.
 */
class CreateArticleHandlerImpl(val txManager: MockServerTxManager) : CreateSetupHandler {

    override fun invoke(command: CreateSetupCommand): Setup {
        return txManager.tx {
            Setup(request =  command.request, action =  command.action)
        }
    }
}

data class CreateSetupCommand(val request: Request?, val action: Action)
interface CreateSetupHandler {
    operator fun invoke(command: CreateSetupCommand): Setup
}
