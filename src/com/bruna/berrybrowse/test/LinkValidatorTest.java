package com.bruna.berrybrowse.test;

import com.bruna.berrybrowse.link.LinkConverter;
import com.bruna.berrybrowse.link.LinkValidator;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LinkValidatorTest {

    @Test
    void validateAndFilter() {
        LinkConverter converter = new LinkConverter();
        LinkValidator validator = new LinkValidator();

        Set<String> rawLinks = new LinkedHashSet<>(Arrays.asList("/products/tenis-nike",
                "www.google.com",
                "http://192.168.0.1",
                "http://127.0.0.1:8080/api/users",
                "http://10.0.0.5/login",
                "http://172.16.1.20/dashboard",
                "https://8.8.8.8",
                "contato@gmail.com",
                "+5551999999999",
                "C:/Users/Bruna/Desktop/arquivo.pdf",
                "/images/logo.png")
        );

        Set<URI> validUris = new LinkedHashSet<>(converter.toUris(rawLinks));

        Set<URI> filteredUris = validator.validateAndFilter(validUris);

        // Nenhum deles deve ser válido
        assertEquals(0, filteredUris.size());
    }

}