package io.alexheld.mockserver.infrastracture

import io.alexheld.mockserver.api.setups.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.responses.*



interface TransactionManager<Repository> {
    fun <T> tx(block: Repository.() -> T): T
}


typealias MockServerTxManager = TransactionManager<MockServerRepository>



interface MockServerRepository {

    /**
     * Finds all [Setup] stored on the server
     */
    fun getAllSetups(): GenericResponse<List<Setup>>

    /**
     * Finds the first [Setup] which matches the provided [predicate]
     */
    fun findFirstSetup(predicate: Predicate<Setup>): GenericResponse<Setup>

    /*
        * Finds the first [Setup] which matches the provided [Predicate]
        */
    fun findSetups(predicate: Predicate<Setup>): GenericResponse<List<Setup>>

    /**
     * Finds the first [Setup] which matches the provided [Predicate]
     */
    fun createSetup(cmd: CreateSetupCommand): GenericResponse<Setup>

    /**
     * Finds the first [Setup] which matches the provided [Predicate]
     */
    fun deleteSetup(cmd: DeleteSetupCommand): GenericResponse<List<Setup>>
}

typealias Predicate<T> = (T) -> Boolean



class MockServerRepositoryImpl(val setups: SetRepository) : MockServerRepository {

    override fun getAllSetups(): GenericResponse<List<Setup>> = setups.findAll()

    override fun findFirstSetup(predicate: Predicate<Setup>): GenericResponse<Setup> {
        TODO("Not yet implemented")
    }

    override fun findSetups(predicate: Predicate<Setup>): GenericResponse<List<Setup>> {
        TODO("Not yet implemented")
    }

    override fun createSetup(cmd: CreateSetupCommand): GenericResponse<Setup> {
        TODO("Not yet implemented")
    }

    override fun deleteSetup(cmd: DeleteSetupCommand): GenericResponse<List<Setup>> {
        TODO("Not yet implemented")
    }

}
