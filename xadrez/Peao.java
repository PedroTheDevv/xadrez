package xadrez;

import java.util.ArrayList;
import java.util.List;

public class Peao extends Peca {
    
    public Peao(Cor cor) {
        super(cor);
    }
    
    @Override
    public List<Posicao> movimentosValidos(Tabuleiro tab, Posicao pos) {
        List<Posicao> movimentos = new ArrayList<>();
        
        int direcao = (cor == Cor.BRANCO) ? -1 : 1;
        int linhaInicial = (cor == Cor.BRANCO) ? 6 : 1;
        
        // Movimento para frente (1 casa)
        Posicao umaCasa = new Posicao(pos.getLinha() + direcao, pos.getColuna());
        if (umaCasa.eValida() && tab.getPeca(umaCasa) == null) {
            movimentos.add(umaCasa);
            
            // Movimento para frente (2 casas) - apenas no primeiro movimento
            if (pos.getLinha() == linhaInicial) {
                Posicao duasCasas = new Posicao(pos.getLinha() + 2 * direcao, pos.getColuna());
                if (tab.getPeca(duasCasas) == null) {
                    movimentos.add(duasCasas);
                }
            }
        }
        
        // Capturas diagonais
        int[] colunasCaptura = {pos.getColuna() - 1, pos.getColuna() + 1};
        for (int colCaptura : colunasCaptura) {
            Posicao posCaptura = new Posicao(pos.getLinha() + direcao, colCaptura);
            if (posCaptura.eValida()) {
                Peca pecaCaptura = tab.getPeca(posCaptura);
                if (pecaCaptura != null && pecaCaptura.getCor() != this.cor) {
                    movimentos.add(posCaptura);
                }
                
                // En Passant
                if (tab.getEnPassantAlvo() != null && posCaptura.equals(tab.getEnPassantAlvo())) {
                    movimentos.add(posCaptura);
                }
            }
        }
        
        return movimentos;
    }
    
    @Override
    public String getSimbolo() {
    return cor == Cor.BRANCO ? "\u2659" : "\u265F";
    }
}
