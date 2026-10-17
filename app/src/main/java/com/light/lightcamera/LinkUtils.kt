package com.light.lightcamera

object LinkUtils {
    /**
     * Checks whether a string represents a FIDO / WebAuthn cross-device authentication link.
     */
    fun isFidoLink(value: String?): Boolean {
        if (value.isNullOrBlank()) return false
        val trimmed = value.trim()
        return trimmed.startsWith("fido:", ignoreCase = true) ||
                trimmed.startsWith("fido-cross-device:", ignoreCase = true) ||
                trimmed.startsWith("webauthn:", ignoreCase = true)
    }

    /**
     * Checks whether a string represents a web URL or FIDO link.
     */
    fun isUrlOrFidoLink(value: String?): Boolean {
        if (value.isNullOrBlank()) return false
        val trimmed = value.trim()
        return isFidoLink(trimmed) ||
                trimmed.startsWith("http://", ignoreCase = true) ||
                trimmed.startsWith("https://", ignoreCase = true)
    }
}
