package com.bruna.berrybrowse.app;

import com.bruna.berrybrowse.file.LinkFileRepository;
import com.bruna.berrybrowse.link.*;

import java.io.File;
import java.util.*;

/**
 * Central de Configuração: <p>
 * - Gerenciamento da interface de interação com o User (CLI) <p>
 * - Criação da tarefa agendada via schtasks (processo-filho) através do método AutomaticConfigInWindows() <p>
 */
public class Config {

    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);

        String mascote = """   
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@+%%*-@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=%*+*%%#*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@**#+++++##-@@@@@@@@@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@:**==:===*-%@@@@@@@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   %@@-*+=-- :-= #@@@@@@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  .++.  .==-- .%@@@@@@%=@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@    :*#%.   . %@@@%-*:@@@@=@++*%%%%%####*@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@*     =***.    #@@@@@@@@@@@%--=========#+#@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@       =**...   :@@@@@@@@@@%+ ::  ===+*+%@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@@        ..:....    @@@@@@@@%*=  .:--+++.@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@@        ....::....   #%%%%%*==:-* -===.@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@@         ...::::....   -====--:-##*-.@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@@@       .=+*#%@#-....     ::-:.:****+=.@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@@@@        =%@%#%%@@@@#        -==+++++:  :.@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@@:        :++**##@@@@@@%%          ===== . .=@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@           ==+**###%%%%%%%+          :-@@::.-@@@@@@@@@@@    
                @@@@@@@@@@@@@@%     -      .==++***###%%##%#          : ...+:@@@@@@@@@@@@    
                @@@@@@@@@@@@+   .@@@##%@@@%=+==++****####*#:-*###* +=.  .  @@@@@@@@@@@@@@    
                @@@@@@@@@@@@   =@@@@=@@@@@@@=++=++++*****#+*@@@@@@@@@@@=  :@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@   .@@=%@@@@@@@@=-+++++++***:%@@@@@@@@-@@@.   @@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@    ....-@@@@@@@@*= :--.:*@@@@@@@@@@@+%:    @@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@          =@@@@@@@@@@@@@@@@@@@=::        .@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@      .:@@@@@@@@@@@@@@@@@@@@@@#-:.     .@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@-  .-  =@@@@@@@@@@@@@@@@@@@#.  :   -@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@+*=  @@@@@@@@@@@@@@@@@@@@@@@@%@=  -+#@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@-##@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#@%#+@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@-%#%@@@@@%%@@@@@@@@@@@@@@@@@@@@@@%%*+@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@%#-%######%#**+-:.....:=+*#%%%%%%%%%:@@@@@@@@@@@@@@@@@@@@    
                @@@@@@@@@@@@@@@@@@%*=----=#%%@@@@@@@@@@@@%%%*=--=--*@@@@@@@@@@@@@@@@@@@@@
                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                                  
                """;
        System.out.println(mascote);
        String texto = (
                "=========================================================================\n" +
                        "            BEM-VINDO À ÁREA DE CONFIGURAÇÃO DO BERRYBROWSE!             \n" +
                        "                                 V1.0.0                                 \n" +
                        "=========================================================================\n"
        );
        System.out.println(texto);

        Set<String> list = new LinkedHashSet<>();
        LinkService service = new LinkService(list, new LinkValidator(),
                new LinkConverter(), new LinkFileRepository());
        Set<String> linksValidados = new LinkedHashSet<>();
        int opcaoDoMenu;
        do {
            menu();
            try {
                opcaoDoMenu = scan.nextInt();
                scan.nextLine();
            } catch(InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
                scan.nextLine();
                continue;
            }
            if(opcaoDoMenu <= 0 || opcaoDoMenu > 3) {
                System.out.println("Opção incorreta! Por favor, digite apenas 1, 2 ou 3.");
                continue;
            }
            if(opcaoDoMenu == 3) {
                System.out.println("\n=========================================================================");
                System.out.println("                              Saindo...                               ");
                System.out.println("=========================================================================");
                return;
            }
            if(opcaoDoMenu == 1) {
                Thread.sleep(1000);
                System.out.println("COMEÇANDO LEITURA DO BERRYBROWSE... [AGUARDE]");

                // Primeiro, verifica se o User não pode ter clicado sem querer
                list.addAll(service.readLinks());
                if (!list.isEmpty()) {
                    System.out.println("Parece que você já tinha links registrados! Vamos recomeçar!");
                    list.clear(); // Limpa a lista.
                }
            }
            if(opcaoDoMenu == 2) {
                list.clear();
            }
            Thread.sleep(1000);

            // Usamos o tamanho da lista pré-processada VS da lista processada, para fins de comparação
            // Se os tamanhos não baterem significa que o processamento não foi bem sucessido. Ou seja,
            // o processamento presente em saveLinks() não funcionou.
           boolean cond = false;
            while(!cond) {
                list.clear();
                list.addAll(addLinks());
                linksValidados = service.saveLinks(list);
                if (list.size() != linksValidados.size()) {
                    System.out.println("Você digitou algum link inválido! Por favor, vamos recomeçar.");
                } else {
                    String msg = (linksValidados.size() == 1) ? "Link registrado!" : "Links registrados!";
                    System.out.println(msg + " Testando no navegador em 3 segundos...");
                    cond = true;
                }
            }
            break;
        } while (true);

        // Teste
        service.OpenLinksInBrowser(linksValidados);

        System.out.print("\nDeseja que o BerryBrowse abra estes links sozinho ao ligar o PC? (S/N): ");
        String opcaoAgendarLinks = scan.nextLine().trim().toUpperCase();

        if (opcaoAgendarLinks.equals("S")) {
            AutomaticConfigInWindows();
            System.out.println("\n=========================================================================");
            System.out.println("                Processo concluído! Pressione ENTER para sair.            ");
            System.out.println("=========================================================================");
            scan.nextLine();
            scan.close();
        } else {
            System.out.println("\n=========================================================================");
            System.out.println("                Pressione ENTER para sair.                               ");
            System.out.println("=========================================================================");
            scan.nextLine();
            scan.close();
          }
        }


    static void menu () {
        String texto = """
                 ------------------------------------------------------------------------
                |  Opcão 1 - Registrar novos links                                       |
                |------------------------------------------------------------------------|
                |  Opção 2 - Escolher novos links (já cadastrei links)                   |  
                |------------------------------------------------------------------------|
                |  Opção 3 - Sair do sistema                                             |
                 ------------------------------------------------------------------------
                Digite a opção desejada:""";
        System.out.println(texto);

    }

    // Esse método não retorna os links filtrados/validados, apenas exatamente o que o User colocou.
    static Set<String> addLinks () {
        Scanner scan = new Scanner(System.in);
        Set<String> novaLista = new LinkedHashSet<>();
        int opcaoQuantidadeLinks = 0;
        do {
            try {
                System.out.println("Qual a quantidade exata de links? (1 a 3)");
                opcaoQuantidadeLinks = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número inteiro");
                scan.nextLine();
                continue;
            }
            if (opcaoQuantidadeLinks < 1 || opcaoQuantidadeLinks > 3) {
                System.out.println("Opção inválida!");
            }
        } while (opcaoQuantidadeLinks < 1 || opcaoQuantidadeLinks > 3);

        if (opcaoQuantidadeLinks == 1) {
            System.out.printf("Foi registrado: %d link\n", opcaoQuantidadeLinks);
        } else {
            System.out.printf("Foram registrados: %d links\n", opcaoQuantidadeLinks);
        }
        int qtd = opcaoQuantidadeLinks;
        System.out.println("Agora pode digitar!");
        // Pega a quantidade escolhida pelo User, e realiza um loop "for" dentro desse limite.
        // Isso evita o User de colocar mais que 3 links.
        for (int i = 1; i <= qtd; i++) {
            System.out.printf("%d° link: ", i);
            String links = scan.nextLine();
            novaLista.add(links);
        }
        return novaLista;
    }

    private static void AutomaticConfigInWindows () {
        // O file aqui é > links.txt
        // Este é um PATH abstrato e absoluto porém, DINÂMICO, de onde for gerado o .jar de Config.class.
        // E Config.class é apenas uma âncora.
        // p -> .../Config/app/BerryBrowse.jar
        try {
            File p = new File(Config.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath());
            // Nessa etapa, tentamos encontrar o BerryBrowse.exe, pulando diretórios.
            // É importante que o User não mexa na organização das pastas do projeto.
            // Subimos três pastas. Se não, faz um fallback (caso ele rode na IDE).
            /*
             * p.getParentFile()             -> .../Pasta_do_Usuario/Config/app/
             *
             * C:p.getParentFile().getParent() -> .../Pasta_do_Usuario/Config/
             *
             * p.getParentFile().getParent().getParent() -> .../Pasta_do_Usuario/ (RAIZ)
             */
             // Caso rootOfJar seja FALSE, o rootOfJar recebe apenas p.getParentFile().
             // Como na IDE o .exe ainda não existe (pois o app não foi empacotado),
             // mantemos a lógica e tratamos a ausência no método rootOfExe.exists().
            File rootOfJar = p.getName().endsWith(".jar") ?
                    p.getParentFile().getParentFile().getParentFile() :
                    p.getParentFile();
            // rootOfExe = .../Pasta_do_Usuario/ + BerryLauncher/BerryLauncher.exe
            //
            File rootOfExe = new File(rootOfJar, "BerryLauncher/BerryLauncher.exe");
            if(rootOfExe.exists()) {
                String pathString = rootOfExe.getAbsolutePath();
                // Usamos ProcessBuilder para passar os comandos para o schtasks, via CMD (shell).
                ProcessBuilder pb = new ProcessBuilder(
                        "schtasks", "/create",
                        "/tn", "BerryAutoStart",
                        "/tr", pathString,
                        "/sc", "onlogon",
                        "/rl", "HIGHEST",
                        "/f"
                );
                // Esse método faz o processo-filho (schtasks) herdar o terminal do processo-pai (BerryBrowse).
                // Dessa forma, consigamos receber na IDE os avisos de ERRO do S.O para o processo-filho (schtasks), caso haja algo.
                pb.inheritIO();
                // Criamos o processo-filho (schtasks.exe):
                pb.start();
                System.out.println("[SUCESSO] BerryBrowse configurado no Agendador de Tarefas!");
            } else {
                System.out.println("Aviso: Executável não encontrado. Pulando agendamento (Ambiente IDE).");
            }
        } catch (Exception e) {
            System.out.println("[ERRO] Falha ao agendar. Tente executar como Administrador.");
        }
    }
 }







