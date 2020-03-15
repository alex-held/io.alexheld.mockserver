package io.alexheld.mockserver.serialization.yaml

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.serialization.*
import io.alexheld.mockserver.testUtil.*
import org.junit.jupiter.api.*

class MockServerRepresenterTests {

    fun generateSubject(): Iterator<YamlLogDocument> = iterator {
        Generator.enableDebugGeneration = true
        while (true) {
            yield(
                YamlLogDocument
                    .match(
                        Request(path = "/api/some/path"), Setup(
                            request = Request(path = "/api/some/path", method = "OPTIONS"),
                            action = Action("hello world")
                        )
                    )
            )
        }
    }


    @Test
    fun canSerializeYamlLogDocument() {
        // Arrange
        val subject = generateSubject().asSequence().map { yamlLog -> yamlLog }.take(3)

        // Act
        val representer = YamlConfiguration.buildRepresenter()
        val presentation = representer.represent(take())

        val yaml = YamlConfiguration.buildDump().dumpAllToString(subject.iterator())

        // Assert
        yaml.dump("Sequence<YamlLogDocument>")
    }

    fun take(count: Int = 1): YamlLogDocument = generateSubject().asSequence().first()
}
