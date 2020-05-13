package com.scheme.agendaapi.util.wrapper

import org.springframework.stereotype.Component
import java.util.logging.Level
import java.util.logging.Logger


@Component
class LoggingWrapper {
    private val logger: Logger = Logger.getLogger(this.javaClass.simpleName)

    fun logInfo(message: String, exception: Exception) {
        logger.log(Level.INFO, message, exception)
    }
    fun logError(message: String, exception: Exception) {
        logger.log(Level.SEVERE, message, exception)
    }
}