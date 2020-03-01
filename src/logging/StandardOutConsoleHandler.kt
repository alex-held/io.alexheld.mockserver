package io.alexheld.mockserver.logging

import java.util.logging.*

class StandardOutConsoleHandler : ConsoleHandler() {

    init {
        setOutputStream(System.out)
    }
}
