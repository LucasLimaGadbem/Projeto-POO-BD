package Model;

import DAO.BibliotecaDAO;

public class Biblioteca {
    private final String nome;
    private final String cidade;
    Integer idBiblioteca;
    private Dono dono;
    BibliotecaDAO bibliotecaDAO = new BibliotecaDAO();
    private Integer numero = bibliotecaDAO.quantidadeBibliotecas();

    //construtores de biblioteca
    public Biblioteca(String nome, String cidade, Dono dono) {
        this.nome = nome;
        this.cidade = cidade;
        this.numero++;
        this.idBiblioteca = this.numero;
        this.dono = dono;
        bibliotecaDAO.insertBiblioteca(nome, cidade, idBiblioteca, dono);
    }

    public Biblioteca(String nome, String cidade, Integer idBiblioteca) {
        this.nome = nome;
        this.cidade = cidade;
        this.idBiblioteca = idBiblioteca;
    }

    //getters
    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }

    public Integer getIdBiblioteca() {
        return idBiblioteca;
    }
}
