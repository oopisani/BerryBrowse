package com.bruna.berrybrowse.test;

import com.bruna.berrybrowse.file.LinkFileRepository;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class LinkFileRepositoryTest {

    @Test
    void saveAndReadLinks() throws IOException {
        LinkFileRepository repo = new LinkFileRepository();

        Set<String> links = new LinkedHashSet<>();
        links.add("https://www.google.com");
        links.add("https://github.com");

        // Execução
        repo.saveLinks(links);
        Set<String> result = repo.readLinks();

        // Validação
        assertNotNull(result, "O resultado nao deveria ser nulo");
        assertEquals(links, result, "Os links lidos devem ser iguais aos salvos");
    }
}