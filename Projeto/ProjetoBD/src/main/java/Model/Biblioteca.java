package Model;

import DAO.BibliotecaDAO;

public class Biblioteca {
    private String nome;
    private String cidade;
    static Integer num = 0;
    private Integer idBiblioteca = 0;
    private Dono dono;
    BibliotecaDAO bibliotecaDAO = new BibliotecaDAO();

    //construtores de biblioteca
    public Biblioteca(String nome, String cidade, Dono dono) {
        this.nome = nome;
        this.cidade = cidade;
        num++;
        this.idBiblioteca = num;
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

    public static Integer getNum() {
        return num;
    }
}
