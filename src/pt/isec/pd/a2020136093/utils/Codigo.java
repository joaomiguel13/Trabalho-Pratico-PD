package pt.isec.pd.a2020136093.utils;

public class Codigo {
    int codigo;
    int tempo;

    public Codigo(int codigo, int tempo) {
        this.codigo = codigo;
        this.tempo = tempo;
    }

    public int getCodigo() {
        return codigo;
    }
    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
}
