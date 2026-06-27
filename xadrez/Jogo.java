package xadrez;

import java.util.Scanner;

public class Jogo {
    private Tabuleiro tabuleiro;
    private Jogador jogadorBranco;
    private Jogador jogadorPreto;
    private Jogador jogadorAtual;
    private boolean partidaAtiva;
    private Scanner scanner;
    
    public Jogo() {
        scanner = new Scanner(System.in);
    }
    
    public void iniciar() {
        System.out.println("=== JOGO DE XADREZ ===");
        System.out.println();
        
        System.out.print("Nome do Jogador 1 (inicia com Branco): ");
        String nome1 = scanner.nextLine();
        System.out.print("Nome do Jogador 2 (inicia com Preto): ");
        String nome2 = scanner.nextLine();
        
        jogadorBranco = new Jogador(nome1, Cor.BRANCO);
        jogadorPreto = new Jogador(nome2, Cor.PRETO);
        
        boolean continuarJogando = true;
        
        while (continuarJogando) {
            jogarPartida();
            
            System.out.println("\n=== PLACAR ===");
            System.out.println(jogadorBranco.getNome() + ": " + jogadorBranco.getPartidasVencidas() + " vitória(s)");
            System.out.println(jogadorPreto.getNome() + ": " + jogadorPreto.getPartidasVencidas() + " vitória(s)");
            
            System.out.print("\nJogar outra partida? (s/n): ");
            String resposta = scanner.nextLine().toLowerCase();
            
            if (resposta.equals("s") || resposta.equals("sim")) {
                inverterPapeis();
            } else {
                continuarJogando = false;
            }
        }
        
        System.out.println("\nObrigado por jogar!");
    }
    
    private void jogarPartida() {
        tabuleiro = new Tabuleiro();
        jogadorBranco.limparCapturas();
        jogadorPreto.limparCapturas();
        jogadorAtual = jogadorBranco;
        partidaAtiva = true;
        
        System.out.println("\n=== NOVA PARTIDA ===");
        System.out.println(jogadorBranco.getNome() + " joga com as Brancas");
        System.out.println(jogadorPreto.getNome() + " joga com as Pretas");
        System.out.println("\nComandos:");
        System.out.println("  - Movimento: origem destino (ex: e2 e4)");
        System.out.println("  - Desistir: 'desistir'");
        
        while (partidaAtiva) {
            tabuleiro.exibir(jogadorBranco.getPecasCapturadas(), jogadorPreto.getPecasCapturadas());
            
            if (tabuleiro.estaEmXeque(jogadorAtual.getCor())) {
                System.out.println("*** XEQUE! ***");
            }
            
            System.out.println("Turno: " + jogadorAtual.getNome() + " (" + jogadorAtual.getCor() + ")");
            System.out.print("Seu movimento: ");
            
            String entrada = scanner.nextLine().trim().toLowerCase();
            
            if (entrada.equals("desistir")) {
                Jogador vencedor = (jogadorAtual == jogadorBranco) ? jogadorPreto : jogadorBranco;
                vencedor.incrementarVitorias();
                System.out.println("\n" + jogadorAtual.getNome() + " desistiu!");
                System.out.println(vencedor.getNome() + " venceu a partida!");
                partidaAtiva = false;
                continue;
            }
            
            if (!processarMovimento(entrada)) {
                System.out.println("Movimento inválido! Tente novamente.");
                continue;
            }
            
            if (verificarFimDeJogo()) {
                partidaAtiva = false;
                continue;
            }
            
            trocarTurno();
        }
    }
    
    private boolean processarMovimento(String entrada) {
        String[] partes = entrada.split("\\s+");
        
        if (partes.length != 2) {
            return false;
        }
        
        Posicao origem = new Posicao(partes[0]);
        Posicao destino = new Posicao(partes[1]);
        
        if (!origem.eValida() || !destino.eValida()) {
            return false;
        }
        
        return tabuleiro.moverPeca(origem, destino, jogadorAtual);
    }
    
    private void trocarTurno() {
        jogadorAtual = (jogadorAtual == jogadorBranco) ? jogadorPreto : jogadorBranco;
    }
    
    private boolean verificarFimDeJogo() {
        Jogador oponente = (jogadorAtual == jogadorBranco) ? jogadorPreto : jogadorBranco;
        
        if (tabuleiro.estaEmXequeMate(oponente.getCor())) {
            tabuleiro.exibir(jogadorBranco.getPecasCapturadas(), jogadorPreto.getPecasCapturadas());
            System.out.println("\n*** XEQUE-MATE! ***");
            System.out.println(jogadorAtual.getNome() + " venceu a partida!");
            jogadorAtual.incrementarVitorias();
            return true;
        }
        
        if (tabuleiro.estaEmAfogamento(oponente.getCor())) {
            tabuleiro.exibir(jogadorBranco.getPecasCapturadas(), jogadorPreto.getPecasCapturadas());
            System.out.println("\n*** AFOGAMENTO! ***");
            System.out.println("A partida terminou em empate.");
            return true;
        }
        
        return false;
    }
    
    private void inverterPapeis() {
        System.out.println("\n=== INVERTENDO CORES ===");
        
        Cor tempCor = jogadorBranco.getCor();
        jogadorBranco.setCor(jogadorPreto.getCor());
        jogadorPreto.setCor(tempCor);
        
        Jogador temp = jogadorBranco;
        jogadorBranco = jogadorPreto;
        jogadorPreto = temp;
        
        System.out.println(jogadorBranco.getNome() + " agora joga com as Brancas");
        System.out.println(jogadorPreto.getNome() + " agora joga com as Pretas");
    }
}
