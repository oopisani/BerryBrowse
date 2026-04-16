package com.bruna.berrybrowse.test;

import com.bruna.berrybrowse.file.LinkFileRepository;
import com.bruna.berrybrowse.link.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

class LinkServiceTest {
    Set<String> rawLinks = new LinkedHashSet<>(Arrays.asList(
            "https://pt.wikipedia.org",
            "https://github.com",
            "https://www.google.com"));

    LinkConverter converter = new LinkConverter();
    LinkValidator validator = new LinkValidator();
    LinkFileRepository repo = new LinkFileRepository();

    @Test
    void testDesktopNull() throws Exception { // Adicionamos 'throws Exception' aqui
        LinkService service = new LinkService(rawLinks, validator, converter, repo);

        // Simulamos um ambiente sem Desktop
        service.setDesktop(null);

        Set<String> linksLimpos = service.saveLinks(rawLinks);
        // O teste passa, se não estourar uma exceção não tratada aqui
        service.OpenLinksInBrowser(linksLimpos);
    }
}