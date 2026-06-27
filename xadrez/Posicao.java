package xadrez;

public class Posicao {
    private int linha;
    private int coluna;
    
    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }
    
    public Posicao(String notacao) {
        if (notacao == null || notacao.length() != 2) {
            this.linha = -1;
            this.coluna = -1;
            return;
        }
        char colunaChar = Character.toLowerCase(notacao.charAt(0));
        char linhaChar = notacao.charAt(1);
        
        this.coluna = colunaChar - 'a';
        this.linha = 8 - Character.getNumericValue(linhaChar);
    }
    
    public int getLinha() {
        return linha;
    }
    
    public int getColuna() {
        return coluna;
    }
    
    public boolean eValida() {
        return linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8;
    }
    
    public String getNotacao() {
        char colunaChar = (char) ('a' + coluna);
        int linhaNum = 8 - linha;
        return "" + colunaChar + linhaNum;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Posicao posicao = (Posicao) obj;
        return linha == posicao.linha && coluna == posicao.coluna;
    }
    
    @Override
    public int hashCode() {
        return 31 * linha + coluna;
    }
}
