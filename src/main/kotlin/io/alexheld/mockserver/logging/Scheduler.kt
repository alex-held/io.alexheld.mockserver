package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.services.*
import java.util.concurrent.*

class Scheduler(private val logger: LogService) {

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

//
//    private fun run(command: Runnable) {
//        try {
//            command.run()
//        } catch (throwable: Throwable) {
//            logger.logEvent(
//                Log(throwable.localizedMessage, LogMessageType.WARN)
//                    .withLogLevel(Level.INFO)
//                    .withThrowable(throwable)
//            )
//        }
//    }
//
//    private fun <T> run(input: T, command: Consumer<T>) {
//        try {
//            command.accept(input)
//        } catch (throwable: Throwable) {
//            logger.logEvent(
//                Log(throwable.localizedMessage, LogMessageType.WARN)
//                    .withLogLevel(Level.INFO)
//                    .withThrowable(throwable)
//            )
//        }
//    }
}
