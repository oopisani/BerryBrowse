package com.bruna.berrybrowse.link;
import com.bruna.berrybrowse.file.LinkFileRepository;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Serviço central que:
 * - Converte e valida links
 * - Abre links no navegador padrão
 * - Persistência de dados
 */
public class LinkService {
  private Desktop desktop;
  private Set<String> rawLinks;
  private LinkValidator linkValidator;
  private LinkConverter linkConverter;
  private LinkFileRepository linkFileRepository;
  private Set<URI> validatedUris = new LinkedHashSet<>();

    // Tenta acessar Desktop, se suportado.
    public LinkService(Set<String> rawLinks, LinkValidator safeLink, LinkConverter linkConverter,
                       LinkFileRepository linkFileRepository) {
        if(Desktop.isDesktopSupported() && !GraphicsEnvironment.isHeadless()) {
            try {
                this.desktop = Desktop.getDesktop();
            } catch(UnsupportedOperationException e) {
                System.out.printf("Não foi possível acessar recursos do Desktop (%s). " +
                        "Seu computador pode não suportar a API do Desktop ou não ter interface gráfica.%n",
                        e.getClass().getSimpleName());
            }
        }
        this.rawLinks = rawLinks;
        this.linkValidator = safeLink;
        this.linkConverter = linkConverter;
        this.linkFileRepository = linkFileRepository;
    }

    public void setDesktop(Desktop desktop) {
        this.desktop = desktop;
    }

    public Set<URI> convertAndCleanLinks() throws Exception {
        // Converte para URI.
        Set<URI> convertedUris =  linkConverter.toUris(rawLinks);
        // Passa a lista convertida do User para validatedUris.
        validatedUris = linkValidator.validateAndFilter(convertedUris);

        return validatedUris;
    }
     // Rodamos esse método apenas >após< chamar saveLinks() no Config.
    public void OpenLinksInBrowser(Set<String> linksToOpen) throws Exception {
       if(linksToOpen.isEmpty()) {
           System.out.println("Você precisa salvar links para que eu possa abrir.");
       } else {
           Set<URI> convertedUris = linkConverter.toUris(linksToOpen);
           for (URI el : convertedUris) {
               try {
                   // Tentativas para rodar no navegador:
                   // TENTATIVA 1: Usar a API padrão do Java (Desktop).
                   if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                       desktop.browse(el);
                   } else {
                       // TENTATIVA 2: Plano B (Comando direto do Windows via CMD)
                       // Se a API da Desktop falhar, a gente força pelo sistema.
                       ProcessBuilder pb = new ProcessBuilder(
                               "rundll32",
                               "url.dll,FileProtocolHandler", el.toString());
                       pb.inheritIO();
                       pb.start();
                   }
               } catch (Exception e) {
                   // Se falhar, nós LANÇAMOS a exceção para o Config capturar no 'catch'.
                   throw new Exception("Falha ao abrir o link: " + el + ". Motivo: " + e.getMessage());
               }
           }
       }
    }
    public Set<String> saveLinks(Set<String> list) throws Exception {
        // Precisamos converter & limpar/validar antes de salvar.
        Set<URI> finalUris = convertAndCleanLinks();
        // E devemos converter a lista para String, para poder usar Files.write() que está presente no método saveLinks.
        Set<String> linksToReturn = new LinkedHashSet<>();
        for (URI uri : finalUris) {
            linksToReturn.add(uri.toString());
        }
        linkFileRepository.saveLinks(linksToReturn);
        return linksToReturn;
    }
     // Lê links salvos do arquivo.
    public Set<String> readLinks() throws IOException {
        return linkFileRepository.readLinks();
    }
}
