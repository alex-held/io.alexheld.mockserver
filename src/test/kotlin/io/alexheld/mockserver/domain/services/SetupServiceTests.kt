package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.testUtil.*
import io.mockk.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import java.time.*
/*

class SetupServiceTests {

    private fun createSubject(repo: SetupRepository, logService: LogService, gen: GenerationServiceImpl.GenerationStrategy = GenerationServiceImpl.GenerationStrategy.Debug_Constant): SetupService {
        return SetupServiceImpl(repo, logService, GenerationServiceImpl(gen))
    }

    @Test
    fun `list should list all saved setups`(){
        // Arrange
        val expected = listOf(
            Setup("1", Instant.now(), null,  Action(null, 200)),
            Setup("2", Instant.now(), null,  Action(null, 400)))
        val repo = mockk<SetupRepository>()
        val logService = mockk<LogService>()

        every { logService.add(any()) } returns IdentifiableLog.generateNew<OperationData>(
            ApiCategory.Log,
            LogMessageType.Operation,
            OperationData(ApiOperation.List, expected), GenerationServiceImpl(GenerationServiceImpl.GenerationStrategy.Debug_Constant))

        every { repo.list() } returns expected


        val sut = createSubject(repo, logService)

        // Act
        val actual = sut.list()
        actual.dumpAsJson("SetupService.list()")

        // Assert
        verify {
            repo.list()
            logService.add(any<IdentifiableLog<*>>())
        }
        actual.shouldContainAll(expected)
    }
}
*/
