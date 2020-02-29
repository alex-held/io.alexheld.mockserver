package io.alexheld.mockserver

import com.fasterxml.jackson.annotation.*
import org.koin.dsl.*
import org.koin.experimental.builder.*

val setupModule = module {
    singleBy<SetupService, SetupServiceImpl>()
    singleBy<SetupRepository, SetupRepositoryImpl>()
}

//@JsonIgnoreProperties(ignoreUnknown = true)
public data class Setup(

    @JsonProperty(required = false)
    public var id: Int?,

    @JsonProperty(required = false)
    public var requestMatcher: RequestMatcher?
)

public class RequestMatcher(
    @JsonProperty(required = false)
    var path: String?,

    @JsonProperty(required = false)
    var method: String?
)

interface SetupRepository {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: Int): Setup?
}

class SetupRepositoryImpl() : SetupRepository {
    private val setups: MutableMap<Int, Setup> = mutableMapOf<Int, Setup>()

    override fun list(): List<Setup> {
        return setups.values.toList()
    }

    override fun add(setup: Setup): Setup {
        var id = setups.keys.max() ?: 0
        id++
        setup.id = id
        setups[id] = setup
        return setup
    }

    override fun delete(id: Int): Setup? {
        return setups.remove(id)
    }
}


interface SetupService {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: Int): Setup?
}


class SetupServiceImpl(private val repository: SetupRepository) : SetupService {

    override fun list(): List<Setup> {
        return repository.list()
    }

    override fun add(setup: Setup): Setup {
        return repository.add(setup)
    }

    override fun delete(id: Int): Setup? {
        return repository.delete(id)
    }


}
