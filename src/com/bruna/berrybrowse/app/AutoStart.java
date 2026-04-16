package com.bruna.berrybrowse.app;

import com.bruna.berrybrowse.file.LinkFileRepository;
import com.bruna.berrybrowse.link.LinkConverter;
import com.bruna.berrybrowse.link.LinkService;
import com.bruna.berrybrowse.link.LinkValidator;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Ponto de entrada para a execução automática do sistema (BerryLauncher).
 * Responsável por disparar a abertura dos links durante a inicialização do S.O.:
 * - Verifica a existência de links via LinkService.
 * - Caso existam, executa a abertura no navegador padrão.
 * - Caso contrário, emite um alerta no console.
 */
public class AutoStart {

    public static void main(String[] args) throws Exception {

        Set<String> list = new LinkedHashSet<>();
        LinkService service = new LinkService(list, new LinkValidator(), new LinkConverter(), new LinkFileRepository());
       // Lê se links.txt já existe
        list.addAll(service.readLinks());

        if(list.isEmpty()) {
            System.out.println("Não há links cadastrados. Rode o ConfigApp para adicionar links ao BerryBrowse" +
                    "ou verifique o caminho da sua pasta.");
        } else {
            // Aqui é esperado que os links existam, então já passaram pelo saveLinks();
            service.OpenLinksInBrowser(list);
        }


    }
}
