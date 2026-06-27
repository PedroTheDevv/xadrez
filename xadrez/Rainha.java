package xadrez;

import java.util.ArrayList;
import java.util.List;

public class Rainha extends Peca {
    
    public Rainha(Cor cor) {
        super(cor);
    }
    
    @Override
    public List<Posicao> movimentosValidos(Tabuleiro tab, Posicao pos) {
        List<Posicao> movimentos = new ArrayList<>();
        
        // Movimentos nas 8 direções
        int[][] direcoes = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // retas
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // diagonais
        };
        
        for (int[] dir : direcoes) {
            adicionarMovimentosLinha(tab, pos, movimentos, dir[0], dir[1]);
        }
        
        return movimentos;
    }
    
   @Override
     public String getSimbolo() {
    return cor == Cor.BRANCO ? "\u2655" : "\u265B";
    }
}
