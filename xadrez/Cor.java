package xadrez;

public enum Cor {
    BRANCO, PRETO;
    
    public Cor oponente() {
        return this == BRANCO ? PRETO : BRANCO;
    }
    
    @Override
    public String toString() {
        return this == BRANCO ? "Branco" : "Preto";
    }
}
