package com.bruna.berrybrowse.link;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Responsavél pela limpeza profunda de links URIs, filtrando: <p>
 * - Hosts vazios <p>
 * - Hosts numéricos (IPv4) <p>
 * - Schemes diferentes de http/https <p>
 */
public class LinkValidator {

    public Set<URI> validateAndFilter(Set<URI> uriSet) {
        Set<URI> urisToReturn = new LinkedHashSet<>(uriSet);

        urisToReturn.removeIf(uri ->
                        uri.getHost() == null ||
                        uri.getHost().matches("\\d+\\.\\d+\\.\\d+\\.\\d+") ||
        !("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())));
        return urisToReturn;
    }
}

