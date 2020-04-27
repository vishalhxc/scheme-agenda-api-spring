package com.scheme.agendaapi.util

import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDWrapper() {
    fun generateUUID(): UUID {
        return UUID.randomUUID()
    }
}