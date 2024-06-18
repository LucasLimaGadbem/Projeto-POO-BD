package Model;

import DAO.ClienteDAO;

public class Cliente {

    private String Nome;
    ClienteDAO clienteDAO = new ClienteDAO();
    private Integer matricula = clienteDAO.quantidade();

    //construtores de cliente
    public Cliente() {
    }

    public Cliente(String nome, Integer matricula) {
        Nome = nome;
        this.matricula = matricula;
    }

    public Cliente(String nome) {
        Nome = nome;
        this.matricula++;
        clienteDAO.insertCliente(nome, matricula);
    }

    public Cliente(Cliente busca) {
        this.Nome = busca.getNome();
        this.matricula = busca.getMatricula();
    }

    //getters and setters
    public String getNome() {
        return Nome;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }
}
