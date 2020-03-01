package io.alexheld.mockserver.logging

import org.slf4j.event.*
import java.util.concurrent.*
import java.util.function.*

class Scheduler(private val logger: MockServerLogger) {

    private val scheduler: ScheduledExecutorService = ScheduledThreadPoolExecutor(
        ConfigurationProperties.Options.MOCKSERVER_ACTION_HANDLER_THREAD_COUNT.Value(),
        SchedulerThreadFactory("Scheduler"),
        ThreadPoolExecutor.CallerRunsPolicy()
    )

    class SchedulerThreadFactory(private val name: String) : ThreadFactory {

        private var threadInitNumber = 1

        @Override
        @SuppressWarnings("NullableProblems")
        override fun newThread(runnable: Runnable): Thread {
            val thread = Thread(runnable, "MockServer-${name}${threadInitNumber++}")
            thread.isDaemon = true
            return thread
        }
    }


    private fun run(command: Runnable) {
        try {
            command.run()
        } catch (throwable: Throwable) {
            logger.logEvent(
                Log()
                    .withType(Log.LogMessageType.WARN)
                    .withLogLevel(Level.INFO)
                    .withMessageFormat(throwable.localizedMessage)
                    .withThrowable(throwable)
            )
        }
    }

    private fun <T> run(input: T, command: Consumer<T>) {
        try {
            command.accept(input)
        } catch (throwable: Throwable) {
            logger.logEvent(
                Log()
                    .withType(Log.LogMessageType.WARN)
                    .withLogLevel(Level.INFO)
                    .withMessageFormat(throwable.localizedMessage)
                    .withThrowable(throwable)
            )
        }
    }
}
