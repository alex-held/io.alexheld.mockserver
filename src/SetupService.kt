package io.alexheld.mockserver

public data class Setup(public var id: Int?, public var request: RequestMatcher?)
public class RequestMatcher(var path: String?, var method: String?)

interface SetupRepository {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: Int): Setup?
}

class SetupRepositoryImpl : SetupRepository {
    private var setups = mutableMapOf<Int, Setup>()

    override fun list(): List<Setup> {
        return setups.values.toList()
    }

    override fun add(setup: Setup): Setup {
        val id = setups.keys.max() ?: 1
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
