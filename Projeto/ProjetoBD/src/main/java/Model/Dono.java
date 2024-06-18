package Model;

public class Dono {

    private String Nome;
    private String CPF;

    //construtor de dono
    public Dono(String nome, String CPF) {
        Nome = nome;
        this.CPF = CPF;
    }

    //getters
    public String getNome() {
        return Nome;
    }

    public String getCPF() {
        return CPF;
    }
}
