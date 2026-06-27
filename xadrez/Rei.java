package xadrez;

import java.util.ArrayList;
import java.util.List;

public class Rei extends Peca {
    
    public Rei(Cor cor) {
        super(cor);
    }
    
    public List<Posicao> movimentosBasicos(Tabuleiro tab, Posicao pos) {
        List<Posicao> movimentos = new ArrayList<>();
        
        int[][] direcoes = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},          {0, 1},
            {1, -1}, {1, 0}, {1, 1}
        };
        
        for (int[] dir : direcoes) {
            Posicao novaPosicao = new Posicao(pos.getLinha() + dir[0], pos.getColuna() + dir[1]);
            if (novaPosicao.eValida()) {
                Peca pecaDestino = tab.getPeca(novaPosicao);
                if (pecaDestino == null || pecaDestino.getCor() != this.cor) {
                    movimentos.add(novaPosicao);
                }
            }
        }
        return movimentos;
    }

    @Override
    public List<Posicao> movimentosValidos(Tabuleiro tab, Posicao pos) {
        // 1. Pega os movimentos normais de andar 1 casa
        List<Posicao> movimentos = movimentosBasicos(tab, pos);

        // 2. Agora sim, valida o Roque de forma segura
        if (!movimentou && !tab.estaEmXeque(cor)) {
            // Roque pequeno
            if (podeRoquePequeno(tab, pos)) {
                movimentos.add(new Posicao(pos.getLinha(), pos.getColuna() + 2));
            }
            // Roque grande
            if (podeRoqueGrande(tab, pos)) {
                movimentos.add(new Posicao(pos.getLinha(), pos.getColuna() - 2));
            }
        }

        return movimentos;
    }
    
    private boolean podeRoquePequeno(Tabuleiro tab, Posicao pos) {
        Posicao posTorre = new Posicao(pos.getLinha(), 7);
        Peca torre = tab.getPeca(posTorre);
        
        if (torre == null || !(torre instanceof Torre) || torre.jaMovimentou()) {
            return false;
        }
        
        // Verificar casas vazias entre rei e torre
        for (int c = pos.getColuna() + 1; c < 7; c++) {
            if (tab.getPeca(new Posicao(pos.getLinha(), c)) != null) {
                return false;
            }
        }
        
        // Verificar se o rei não passa por xeque
        for (int c = pos.getColuna(); c <= pos.getColuna() + 2; c++) {
            Tabuleiro copia = tab.clonar();
            copia.setPeca(pos, null);
            copia.setPeca(new Posicao(pos.getLinha(), c), this);
            if (copia.estaEmXeque(cor)) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean podeRoqueGrande(Tabuleiro tab, Posicao pos) {
        Posicao posTorre = new Posicao(pos.getLinha(), 0);
        Peca torre = tab.getPeca(posTorre);
        
        if (torre == null || !(torre instanceof Torre) || torre.jaMovimentou()) {
            return false;
        }
        
        // Verificar casas vazias entre rei e torre
        for (int c = 1; c < pos.getColuna(); c++) {
            if (tab.getPeca(new Posicao(pos.getLinha(), c)) != null) {
                return false;
            }
        }
        
        // Verificar se o rei não passa por xeque
        for (int c = pos.getColuna(); c >= pos.getColuna() - 2; c--) {
            Tabuleiro copia = tab.clonar();
            copia.setPeca(pos, null);
            copia.setPeca(new Posicao(pos.getLinha(), c), this);
            if (copia.estaEmXeque(cor)) {
                return false;
            }
        }
        
        return true;
    }
    
   @Override
    public String getSimbolo() {
    return cor == Cor.BRANCO ? "\u2654" : "\u265A";
    }
}
