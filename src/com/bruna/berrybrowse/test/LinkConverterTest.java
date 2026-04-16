package com.bruna.berrybrowse.test;

import com.bruna.berrybrowse.link.LinkConverter;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LinkConverterTest {

    @Test
    public void toUris() {
        LinkConverter converter = new LinkConverter();

        Set<String> rawLinks = new LinkedHashSet<>(
                Arrays.asList(" ", "https://www.youtube.com/", null, "ht!tp://invalido")
        );

        Set<URI> result = converter.toUris(rawLinks);

        // Apenas o link válido deve passar: "https://www.youtube.com/"
        assertEquals(1, result.size());
    }
}