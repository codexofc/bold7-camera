package com.light.lightcamera

import org.junit.Assert.*
import org.junit.Test

class LinkUtilsTest {

    @Test
    fun isFidoLink_identifiesFidoSchemes() {
        assertTrue(LinkUtils.isFidoLink("fido:g1-eyJhbGciOi..."))
        assertTrue(LinkUtils.isFidoLink("FIDO:G1-EYJ..."))
        assertTrue(LinkUtils.isFidoLink("fido://g1-eyJhbGciOi..."))
        assertTrue(LinkUtils.isFidoLink("fido-cross-device:abc123"))
        assertTrue(LinkUtils.isFidoLink("webauthn:sample_payload"))
        assertTrue(LinkUtils.isFidoLink("  fido:with_leading_whitespace"))
    }

    @Test
    fun isFidoLink_rejectsNonFidoLinks() {
        assertFalse(LinkUtils.isFidoLink("https://example.com"))
        assertFalse(LinkUtils.isFidoLink("http://example.com"))
        assertFalse(LinkUtils.isFidoLink("mailto:user@example.com"))
        assertFalse(LinkUtils.isFidoLink("plain text"))
        assertFalse(LinkUtils.isFidoLink(null))
        assertFalse(LinkUtils.isFidoLink(""))
    }

    @Test
    fun isUrlOrFidoLink_identifiesUrlsAndFidoLinks() {
        assertTrue(LinkUtils.isUrlOrFidoLink("fido:g1-eyJ..."))
        assertTrue(LinkUtils.isUrlOrFidoLink("https://example.com"))
        assertTrue(LinkUtils.isUrlOrFidoLink("http://example.com"))
        assertFalse(LinkUtils.isUrlOrFidoLink("just random barcode text"))
        assertFalse(LinkUtils.isUrlOrFidoLink(null))
    }
}
