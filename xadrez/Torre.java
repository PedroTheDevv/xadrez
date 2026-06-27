package xadrez;

import java.util.ArrayList;
import java.util.List;

public class Torre extends Peca {
    
    public Torre(Cor cor) {
        super(cor);
    }
    
    @Override
    public List<Posicao> movimentosValidos(Tabuleiro tab, Posicao pos) {
        List<Posicao> movimentos = new ArrayList<>();
        
        int[][] direcoes = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] dir : direcoes) {
            adicionarMovimentosLinha(tab, pos, movimentos, dir[0], dir[1]);
        }
        
        return movimentos;
    }
    
    @Override
    public String getSimbolo() {
    return cor == Cor.BRANCO ? "\u2656" : "\u265C";
    }
}
