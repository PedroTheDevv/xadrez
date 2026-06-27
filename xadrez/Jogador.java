package xadrez;

import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private String nome;
    private Cor cor;
    private int partidasVencidas;
    private List<Peca> pecasCapturadas;
    
    public Jogador(String nome, Cor cor) {
        this.nome = nome;
        this.cor = cor;
        this.partidasVencidas = 0;
        this.pecasCapturadas = new ArrayList<>();
    }
    
    public String getNome() {
        return nome;
    }
    
    public Cor getCor() {
        return cor;
    }
    
    public void setCor(Cor cor) {
        this.cor = cor;
    }
    
    public int getPartidasVencidas() {
        return partidasVencidas;
    }
    
    public void incrementarVitorias() {
        partidasVencidas++;
    }
    
    public List<Peca> getPecasCapturadas() {
        return pecasCapturadas;
    }
    
    public void adicionarCaptura(Peca peca) {
        pecasCapturadas.add(peca);
    }
    
    public void limparCapturas() {
        pecasCapturadas.clear();
    }
}
