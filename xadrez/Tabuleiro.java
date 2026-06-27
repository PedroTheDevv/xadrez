package xadrez;

import java.util.List;

public class Tabuleiro {
    private Peca[][] casas;
    private Posicao enPassantAlvo;
    
    public Tabuleiro() {
        casas = new Peca[8][8];
        enPassantAlvo = null;
        inicializarPecas();
    }
    
    private Tabuleiro(Peca[][] casas, Posicao enPassantAlvo) {
        this.casas = casas;
        this.enPassantAlvo = enPassantAlvo;
    }
    
    public void inicializarPecas() {
        // Peças pretas (linhas 0 e 1)
        casas[0][0] = new Torre(Cor.PRETO);
        casas[0][1] = new Cavalo(Cor.PRETO);
        casas[0][2] = new Bispo(Cor.PRETO);
        casas[0][3] = new Rainha(Cor.PRETO);
        casas[0][4] = new Rei(Cor.PRETO);
        casas[0][5] = new Bispo(Cor.PRETO);
        casas[0][6] = new Cavalo(Cor.PRETO);
        casas[0][7] = new Torre(Cor.PRETO);
        for (int c = 0; c < 8; c++) {
            casas[1][c] = new Peao(Cor.PRETO);
        }
        
        // Peças brancas (linhas 6 e 7)
        casas[7][0] = new Torre(Cor.BRANCO);
        casas[7][1] = new Cavalo(Cor.BRANCO);
        casas[7][2] = new Bispo(Cor.BRANCO);
        casas[7][3] = new Rainha(Cor.BRANCO);
        casas[7][4] = new Rei(Cor.BRANCO);
        casas[7][5] = new Bispo(Cor.BRANCO);
        casas[7][6] = new Cavalo(Cor.BRANCO);
        casas[7][7] = new Torre(Cor.BRANCO);
        for (int c = 0; c < 8; c++) {
            casas[6][c] = new Peao(Cor.BRANCO);
        }
    }
    
    public Peca getPeca(Posicao pos) {
        if (!pos.eValida()) return null;
        return casas[pos.getLinha()][pos.getColuna()];
    }
    
    public void setPeca(Posicao pos, Peca peca) {
        if (pos.eValida()) {
            casas[pos.getLinha()][pos.getColuna()] = peca;
        }
    }
    
    public Posicao getEnPassantAlvo() {
        return enPassantAlvo;
    }
    
    public boolean moverPeca(Posicao origem, Posicao destino, Jogador jogador) {
        Peca peca = getPeca(origem);
        
        if (peca == null || peca.getCor() != jogador.getCor()) {
            return false;
        }
        
        List<Posicao> movimentosValidos = peca.movimentosValidos(this, origem);
        
        boolean movimentoPermitido = false;
        for (Posicao mov : movimentosValidos) {
            if (mov.equals(destino)) {
                movimentoPermitido = true;
                break;
            }
        }
        
        if (!movimentoPermitido) {
            return false;
        }
        
        // Simular movimento para verificar se deixa o rei em xeque
        Tabuleiro copia = this.clonar();
        copia.executarMovimento(origem, destino, null);
        if (copia.estaEmXeque(jogador.getCor())) {
            return false;
        }
        
        // Executar o movimento real
        executarMovimento(origem, destino, jogador);
        
        return true;
    }
    
    private void executarMovimento(Posicao origem, Posicao destino, Jogador jogador) {
        Peca peca = getPeca(origem);
        Peca pecaCapturada = getPeca(destino);
        
        // En Passant
        if (peca instanceof Peao && destino.equals(enPassantAlvo)) {
            int linhaPeaoCapturado = origem.getLinha();
            Posicao posPeaoCapturado = new Posicao(linhaPeaoCapturado, destino.getColuna());
            pecaCapturada = getPeca(posPeaoCapturado);
            setPeca(posPeaoCapturado, null);
        }
        
        if (pecaCapturada != null && jogador != null) {
            jogador.adicionarCaptura(pecaCapturada);
        }
        
        // Roque
        if (peca instanceof Rei && Math.abs(destino.getColuna() - origem.getColuna()) == 2) {
            if (destino.getColuna() > origem.getColuna()) {
                // Roque pequeno
                Peca torre = getPeca(new Posicao(origem.getLinha(), 7));
                setPeca(new Posicao(origem.getLinha(), 7), null);
                setPeca(new Posicao(origem.getLinha(), 5), torre);
                torre.marcarMovimento();
            } else {
                // Roque grande
                Peca torre = getPeca(new Posicao(origem.getLinha(), 0));
                setPeca(new Posicao(origem.getLinha(), 0), null);
                setPeca(new Posicao(origem.getLinha(), 3), torre);
                torre.marcarMovimento();
            }
        }
        
        // Atualizar en passant
        enPassantAlvo = null;
        if (peca instanceof Peao && Math.abs(destino.getLinha() - origem.getLinha()) == 2) {
            int linhaEnPassant = (origem.getLinha() + destino.getLinha()) / 2;
            enPassantAlvo = new Posicao(linhaEnPassant, origem.getColuna());
        }
        
        setPeca(destino, peca);
        setPeca(origem, null);
        peca.marcarMovimento();
        
        // Promoção de peão
        if (peca instanceof Peao) {
            int linhaPromocao = (peca.getCor() == Cor.BRANCO) ? 0 : 7;
            if (destino.getLinha() == linhaPromocao) {
                setPeca(destino, new Rainha(peca.getCor()));
            }
        }
    }
    
    public boolean estaEmXeque(Cor cor) {
        Posicao posRei = encontrarRei(cor);
        if (posRei == null) return false;
        
        Cor corOponente = cor.oponente();
        
        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                Peca peca = casas[l][c];
                if (peca != null && peca.getCor() == corOponente) {
                    List<Posicao> movimentos;
                    if (peca instanceof Rei) {
                        // Se for o Rei oponente, pega só os movimentos básicos de 1 casa dele
                        movimentos = ((Rei) peca).movimentosBasicos(this, new Posicao(l, c));
                    } else {
                        // Se for qualquer outra peça, usa o movimentosValidos normal
                        movimentos = peca.movimentosValidos(this, new Posicao(l, c));
                    }
                    for (Posicao mov : movimentos) {
                        if (mov.equals(posRei)) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean estaEmXequeMate(Cor cor) {
        if (!estaEmXeque(cor)) {
            return false;
        }
        return !temMovimentoLegal(cor);
    }
    
    public boolean estaEmAfogamento(Cor cor) {
        if (estaEmXeque(cor)) {
            return false;
        }
        return !temMovimentoLegal(cor);
    }
    
    private boolean temMovimentoLegal(Cor cor) {
        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                Peca peca = casas[l][c];
                if (peca != null && peca.getCor() == cor) {
                    Posicao origem = new Posicao(l, c);
                    List<Posicao> movimentos = peca.movimentosValidos(this, origem);
                    
                    for (Posicao destino : movimentos) {
                        Tabuleiro copia = this.clonar();
                        copia.executarMovimento(origem, destino, null);
                        if (!copia.estaEmXeque(cor)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private Posicao encontrarRei(Cor cor) {
        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                Peca peca = casas[l][c];
                if (peca instanceof Rei && peca.getCor() == cor) {
                    return new Posicao(l, c);
                }
            }
        }
        return null;
    }
    
    public Tabuleiro clonar() {
        Peca[][] novasCasas = new Peca[8][8];
        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                novasCasas[l][c] = casas[l][c];
            }
        }
        return new Tabuleiro(novasCasas, enPassantAlvo);
    }
    
    public void exibir(List<Peca> capturadasBranco, List<Peca> capturadasPreto) {

    final String RESET = "\u001B[0m";
    final String CASA_CLARA = "\u001B[47m";
    final String CASA_ESCURA = "\u001B[42m";

    System.out.println();

    System.out.print("Capturadas (Branco): ");
    for (Peca p : capturadasBranco) {
        System.out.print(p.getSimbolo() + " ");
    }
    System.out.println("\n");

    System.out.println("     A  B  C  D  E  F  G  H");

    for (int l = 0; l < 8; l++) {

        System.out.print((8 - l) + "  ");

        for (int c = 0; c < 8; c++) {

            String fundo = ((l + c) % 2 == 0)
                    ? CASA_CLARA
                    : CASA_ESCURA;

            Peca peca = casas[l][c];

            if (peca == null) {
                System.out.print(fundo + "   " + RESET);
            } else {
                System.out.print(fundo + " " + peca.getSimbolo() + " " + RESET);
            }
        }

        System.out.println("  " + (8 - l));
    }

    System.out.println("     A  B  C  D  E  F  G  H");
    System.out.println();

    System.out.print("Capturadas (Preto): ");
    for (Peca p : capturadasPreto) {
        System.out.print(p.getSimbolo() + " ");
    }
    System.out.println("\n");
}
}
