package Model;

import DAO.LivroDAO;

public class Livro {

    private final String Nome;
    Cliente cliente = new Cliente();
    LivroDAO livroDAO = new LivroDAO();
    private Integer num = livroDAO.quant();
    Integer bibliotecaId;

    //construtores
    public Livro(String nome, Integer mat, int clienteMatricula) {
        this.Nome = nome;
        this.num = mat;
        cliente.setMatricula(clienteMatricula);
    }

    public Livro(String nome, Integer mat) {
        this.Nome = nome;
        this.num = mat;
    }

    public Livro(String nome) {
        Nome = nome;
        this.num++;
        livroDAO.insertLivro(num, nome);
    }

    //getters and setters
    public Integer getNum() {
        return num;
    }

    public String getNome() {
        return Nome;
    }

    public void setBibliotecaId(Integer bibliotecaId) {
        this.bibliotecaId = bibliotecaId;
    }

    public Integer getCliente() {
        return cliente.getMatricula();
    }
}
