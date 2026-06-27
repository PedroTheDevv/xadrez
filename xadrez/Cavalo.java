package xadrez;

import java.util.ArrayList;
import java.util.List;

public class Cavalo extends Peca {
    
    public Cavalo(Cor cor) {
        super(cor);
    }
    
    @Override
    public List<Posicao> movimentosValidos(Tabuleiro tab, Posicao pos) {
        List<Posicao> movimentos = new ArrayList<>();
        
        int[][] saltos = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };
        
        for (int[] salto : saltos) {
            Posicao novaPosicao = new Posicao(pos.getLinha() + salto[0], pos.getColuna() + salto[1]);
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
    public String getSimbolo() {
    return cor == Cor.BRANCO ? "\u2658" : "\u265E";
    }
}
