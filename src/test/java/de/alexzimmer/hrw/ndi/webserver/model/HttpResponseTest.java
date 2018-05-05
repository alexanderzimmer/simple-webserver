package de.alexzimmer.hrw.ndi.webserver.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpResponseTest {


    @Test
    public void testFormatKey() {
        assertEquals(HttpResponseHeaders.formatKey("content-type"), "Content-Type");
        assertEquals(HttpResponseHeaders.formatKey("Content-type"), "Content-Type");
        assertEquals(HttpResponseHeaders.formatKey("content-Type"), "Content-Type");
        assertEquals(HttpResponseHeaders.formatKey("Content-Type"), "Content-Type");
        assertEquals(HttpResponseHeaders.formatKey("CONTENT-TYPE"), "Content-Type");
        assertEquals(HttpResponseHeaders.formatKey("COnTenT-TyPE"), "Content-Type");
    }

}