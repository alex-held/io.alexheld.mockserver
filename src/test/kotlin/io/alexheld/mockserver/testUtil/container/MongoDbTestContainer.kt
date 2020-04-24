package io.alexheld.mockserver.testUtil.container

import io.alexheld.mockserver.config.*
import org.springframework.util.*
import org.testcontainers.containers.*
import java.io.*
import java.net.*
import java.util.*


class DockerComposeEnvironment(composeYaml: String) : DockerComposeContainer<DockerComposeEnvironment>(composeYaml)


class MongoDBTestContainer : GenericContainer<MongoDBTestContainer>, MongoDBConnection {

    private val defaultPort = 27017
    fun getMappedPort() = this.getMappedPort(defaultPort)

    constructor() : super("mongo") {
        this
            .withExposedPorts(defaultPort)
            .withLabels(mutableMapOf(
                "transientTestContainer" to "yes",
                "type" to "MongoDB"))
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

     override val database: String
        get() = UUID.randomUUID().toString()

     override val connectionString: String
        get() =  "mongodb://${containerIpAddress}:${getMappedPort()}"

}


object ContainerFactory {

    fun createAndStartMongoDBTestContainer() = MongoDBTestContainer()

    /*
     Creates and start containers based on the provided [DockerCompose.yaml]
     */
    fun createAndStartDockerCompose(yaml: String) = DockerComposeEnvironment(yaml)
}


