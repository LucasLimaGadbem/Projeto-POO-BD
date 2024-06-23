package DAO;

import Model.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClienteDAO extends ConnectionDAO{

    //DAO - Data Access Object
    boolean sucesso = false; //Para saber se funcionou

    //INSERT
    public boolean insertCliente(String nome, int matricula) {

        connectToDB();

        String sql = "INSERT INTO Cliente (Nome, Matricula) values(?,?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1,nome);
            pst.setInt(2, matricula);
            pst.execute();
            sucesso = true;
        } catch (SQLException exc) {
            System.out.println("Erro: " + exc.getMessage());
            sucesso = false;
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException exc) {
                System.out.println("Erro: " + exc.getMessage());
            }
        }
        return sucesso;
    }

    //DELETE
    public boolean deleteCliente(int Matricula) {
        connectToDB();
        String checkSql = "SELECT COUNT(*) FROM Livro WHERE Cliente_Matricula = ?";
        String sql = "DELETE FROM Cliente where Matricula = ?";
        try {
            // Verifica se o cliente está com algum livro
            pst = con.prepareStatement(checkSql);
            pst.setInt(1, Matricula);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Se houver livros com Cliente_Matricula igual à Matricula, não permite a exclusão
                System.out.println("Não é possível deletar o cliente. O cliente está com livros.");
                sucesso = false;
            } else {
                // Se não houver livros com Cliente_Matricula igual à Matricula, procede com a exclusão
                pst = con.prepareStatement(sql);
                pst.setInt(1, Matricula);
                pst.execute();
                sucesso = true;
            }
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
            sucesso = false;
        } finally {
            try {
                if (con != null) con.close();
                if (pst != null) pst.close();
            } catch (SQLException exc) {
                System.out.println("Erro: " + exc.getMessage());
            }
        }

        return sucesso;
    }

    //UPDATE
    public boolean updateClienteNome(int Matricula, String nome) {
        connectToDB();
        String sql = "UPDATE Cliente SET nome=? where Matricula=?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setInt(2, Matricula);
            pst.execute();
            sucesso = true;
        } catch (SQLException ex) {
            System.out.println("Erro = " + ex.getMessage());
            sucesso = false;
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException exc) {
                System.out.println("Erro: " + exc.getMessage());
            }
        }
        return sucesso;
    }

    //SELECT
    public ArrayList<Cliente> selectCliente() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        connectToDB();
        String sql = "SELECT * FROM Cliente";

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            System.out.println("Lista de clientes: ");

            while (rs.next()) {

                Cliente clienteAux = new Cliente(rs.getString("Nome"), rs.getInt("Matricula"));

                System.out.println("nome = " + clienteAux.getNome());
                System.out.println("matricula = " + clienteAux.getMatricula());
                System.out.println("--------------------------------");

                clientes.add(clienteAux);
            }
            sucesso = true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            sucesso = false;
        } finally {
            try {
                con.close();
                st.close();
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        return clientes;
    }

    //busca o cliente de acordo com a matricula
    public Cliente busca(int mat) {
        Cliente clienteAux = null;
        connectToDB();
        String sql = "SELECT Nome, Matricula FROM Cliente WHERE Matricula = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, mat);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    clienteAux = new Cliente(rs.getString("Nome"), rs.getInt("Matricula"));
                }
            }
            sucesso = true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            sucesso = false;
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
        return clienteAux;
    }

    //conta a quantidade de clientes
    public int quantidade() {

        int count = 0;
        connectToDB();
        String sql = "SELECT MAX(Matricula) AS total FROM Cliente";

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            if (rs.next()) {
                count = rs.getInt("total");
            }

            sucesso = true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            sucesso = false;
        } finally {
            try {
                con.close();
                st.close();
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        return count;
    }
}
