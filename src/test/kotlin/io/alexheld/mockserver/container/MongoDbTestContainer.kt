package io.alexheld.mockserver.container

import org.springframework.util.*
import org.testcontainers.containers.*
import org.testcontainers.containers.output.*
import java.io.*
import java.net.*


class DockerComposeEnvironment(composeYaml: String) : DockerComposeContainer<DockerComposeEnvironment>(composeYaml)


class MongoDBTestContainer : GenericContainer<MongoDBTestContainer> {

    private val defaultPort = 27017

    fun getMappedPort() = this.getMappedPort(defaultPort)
    fun getConnectionString() = "mongodb://${containerIpAddress}:${getMappedPort()}"

    constructor() : super("mongo") {
        this
            .withExposedPorts(defaultPort)
            .withLabels(mutableMapOf(
                "transientTestContainer" to "yes",
                "type" to "MongoDB")
            ).withLogConsumer(Slf4jLogConsumer(logger()))
    }


    @Throws(ContainerLaunchException::class)
    fun containerStartsAndPublicPortIsAvailable() {
        val sw = StopWatch()
        sw.start()
        while (true)
            try {
                if (sw.totalTimeSeconds > 20) {
                    sw.stop()
                    throw ContainerLaunchException("The expected port ${getMappedPort()} is not available!")
                }
                Socket(containerIpAddress, getMappedPort())
                return
            } catch (ex: IOException) {
                Thread.sleep(200)
            }
    }
}


object ContainerFactory {

    fun createAndStartMongoDBTestContainer() = MongoDBTestContainer()

    /*
     Creates and start containers based on the provided [DockerCompose.yaml]
     */
    fun createAndStartDockerCompose(yaml: String) = DockerComposeEnvironment(yaml)
}


