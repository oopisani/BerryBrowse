package com.bruna.berrybrowse.file;
import com.bruna.berrybrowse.app.Config;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Responsável pela persistência de dados do sistema: <p>
 * - Gerenciamento de Path Dinâmico & Absoluto (âncora no JAR) <p>
 * - Operações de leitura e escrita no arquivo "links.txt" <p>
 */
public class LinkFileRepository {
    private Path path;

    public LinkFileRepository() {
        try {
            // O file aqui é > links.txt
            // Este é um PATH abstrato e absoluto porém, DINÂMICO, de onde for gerado o .jar de Config.class.
            // E Config.class é apenas uma âncora.
            // p inicia em: .../Pasta_do_Usuario/Config/app/BerryBrowse.jar
            File p = new File(Config.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath());
            // Nessa etapa tentamos encontrar links.txt, pulando diretórios.
            // Subimos três pastas.
            /*
             * abstractPath.getParentFile()            -> .../Config/app/
             * abstractPath.getParentFile().getParent() -> .../Config/
             * abstractPath.getParentFile().getParent().getParent() -> RAIZ (Onde o ZIP foi extraído)
             */
            // Se for IDE (else), sobe apenas 1 nível para trabalhar dentro de 'out/production'.
            File rootOfJar = p.getName().endsWith(".jar") ? p.getParentFile()
                    .getParentFile().getParentFile() : p.getParentFile();
            // rootOfJar = Raiz + BerryLauncher/links.txt
            //
            File pathOfFile = new File(rootOfJar,"BerryLauncher/links.txt");
            // Caso esteja rodando na IDE, não irá existir a pasta BerryLauncher
            File launcherFolder = new File(rootOfJar, "BerryLauncher");
            // Não existindo, definimos o PATH como o correto para a IDE:
            if(!launcherFolder.exists()) {
                pathOfFile = new File(rootOfJar, "BerryBrowse/links.txt");
            }
            // Converte File para Path para utilizar as funcionalidades avançadas da API Java NIO.2.
            this.path = pathOfFile.toPath();

            //
        } catch (Exception e) {
            // Se cair aqui, não é IDE, é problema de permissão ou sistema
            System.out.println("[ERRO CRÍTICO] Falha ao mapear diretório de links: " + e.getMessage());
            this.path = Path.of("links.txt"); // Caminho relativo emergencial
        }

    }
   /*  StandartOpenOption.CREATE = Cria um File se ele não existe
    *  TRUNCATE_EXISTING = Se o file já existe e é chamado, ele é sobreescrito
    */
    public void saveLinks(Set<String> links) throws IOException {
        Files.write(path, links, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // Lê links
    public Set<String> readLinks() throws IOException {
        // Se o arquivo não existe no PATH definido
        if(!Files.isRegularFile(path)) {
            return new LinkedHashSet<>();
        } else {
            // Se não, lemos todas as linhas do arquivo existente na PATH definida.
            return new LinkedHashSet<>(Files.readAllLines(path));
        }
    }
}