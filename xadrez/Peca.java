package xadrez;

import java.util.List;

public abstract class Peca {
    protected Cor cor;
    protected boolean movimentou;
    
    public Peca(Cor cor) {
        this.cor = cor;
        this.movimentou = false;
    }
    
    public abstract List<Posicao> movimentosValidos(Tabuleiro tab, Posicao pos);
    
    public abstract String getSimbolo();
    
    public Cor getCor() {
        return cor;
    }
    
    public void marcarMovimento() {
        this.movimentou = true;
    }
    
    public boolean jaMovimentou() {
        return movimentou;
    }
    
    protected void adicionarMovimentosLinha(Tabuleiro tab, Posicao pos, List<Posicao> movimentos,
                                            int deltaLinha, int deltaColuna) {
        int novaLinha = pos.getLinha() + deltaLinha;
        int novaColuna = pos.getColuna() + deltaColuna;
        
        while (novaLinha >= 0 && novaLinha < 8 && novaColuna >= 0 && novaColuna < 8) {
            Posicao novaPosicao = new Posicao(novaLinha, novaColuna);
            Peca pecaDestino = tab.getPeca(novaPosicao);
            
            if (pecaDestino == null) {
                movimentos.add(novaPosicao);
            } else {
                if (pecaDestino.getCor() != this.cor) {
                    movimentos.add(novaPosicao);
                }
                break;
            }
            
            novaLinha += deltaLinha;
            novaColuna += deltaColuna;
        }
    }
}
