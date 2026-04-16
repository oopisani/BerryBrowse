package com.bruna.berrybrowse.link;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Responsavél pela conversão e limpeza básica de links: <p>
 * - Este conversor (toUris) ignora entradas nulas, vazias ou compostas apenas por espaços em branco. <p>
 * - A validação de sintaxe é delegada ao método create(String), capturando falhas
 *   de formação de URL através de IllegalArgumentException. <p>
 */
public class LinkConverter {

    public Set<URI> toUris(Set<String> rawLinks) {
        Set<URI> urisToReturn = new LinkedHashSet<>();
        for(String link : rawLinks) {
            // Se o link for null ou Blank, ele não entra no Try e roda de novo o for.
            if(link == null || link.isBlank()) {
                continue;
            }
            try {
                // Importante saber ue URI.create() fará uma limpeza básica de acordo com o RFC 2396
                URI uri = URI.create(link);
                    urisToReturn.add(uri);
                } catch(IllegalArgumentException  e) {
                    System.out.printf("Link inválido:'%s'. Erro: %s%n", link, e.getClass().getSimpleName());
                }
        }
        return urisToReturn;
    }
}
