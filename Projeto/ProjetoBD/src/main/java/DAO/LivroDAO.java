package DAO;

import Model.Livro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LivroDAO extends ConnectionDAO{

    //DAO - Data Access Object
    boolean sucesso = false; //Para saber se funcionou

    //INSERT
    public boolean insertLivro(Integer Numero, String Nome) {

        connectToDB();

        String sql = "INSERT INTO Livro (Numero, Nome) values(?,?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, Numero);
            pst.setString(2, Nome);
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

    //aluga o livro para um cliente
    public boolean aluga(int clienteMatricula, int num) {
        connectToDB();
        boolean sucesso = false;
        String checkSql = "SELECT Matricula FROM Cliente WHERE Matricula = ?";
        String checkLivroSql = "SELECT Cliente_Matricula FROM Livro WHERE Numero = ?";
        String updateSql = "UPDATE Livro SET Cliente_Matricula = ? WHERE Numero = ?";

        try {
            pst = con.prepareStatement(checkSql);
            pst.setInt(1, clienteMatricula);
            rs = pst.executeQuery();
            if (!rs.next()) {
                System.out.println("Cliente não encontrado.");
                return false;
            }

            pst = con.prepareStatement(checkLivroSql);
            pst.setInt(1, num);
            rs = pst.executeQuery();

            if (rs.next()) {
                int currentMatricula = rs.getInt("Cliente_Matricula");
                if (currentMatricula != 0) {
                    System.out.println("Este livro já está alugado.");
                    return false;
                }
            } else {
                System.out.println("Livro não encontrado.");
                return false;
            }

            pst = con.prepareStatement(updateSql);
            pst.setInt(1, clienteMatricula);
            pst.setInt(2, num);
            pst.executeUpdate();
            sucesso = true;
        } catch (SQLException exc) {
            System.out.println("Erro: " + exc.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException exc) {
                System.out.println("Erro: " + exc.getMessage());
            }
        }
        return sucesso;
    }

    //DELETE
    public boolean deleteLivro(int Numero) {
        connectToDB();
        String checkSql = "SELECT Cliente_Matricula FROM Livro WHERE Numero = ?";
        String sql = "DELETE FROM Livro where Numero = ?";
        try {
            pst = con.prepareStatement(checkSql);
            pst.setInt(1, Numero);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt("Cliente_Matricula") != 0) {
                System.out.println("Não é possível deletar o livro. Ele está alugado a um cliente.");
                sucesso = false;
            } else {
                pst = con.prepareStatement(sql);
                pst.setInt(1, Numero);
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
    public boolean updateLivroNome(int numero, String nome) {
        connectToDB();
        String sql = "UPDATE Livro SET nome=? where numero=?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setInt(2, numero);
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

    //mostra os livros
    public ArrayList<Livro> selectLivro() {
        ArrayList<Livro> livros = new ArrayList<>();
        connectToDB();
        String sql = "SELECT * FROM Livro";
        String bibliotecaSql = "SELECT Biblioteca_idBiblioteca FROM Biblioteca_has_Livro WHERE Livro_Numero = ?";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql);
             PreparedStatement pst = con.prepareStatement(bibliotecaSql)) {

            System.out.println("Lista de livros: ");

            while (rs.next()) {
                Livro livroAux = new Livro(rs.getString("Nome"), rs.getInt("Numero"), rs.getInt("Cliente_Matricula"));
                System.out.println("nome = " + livroAux.getNome());
                System.out.println("numero = " + livroAux.getNum());
                System.out.println("numero matricula cliente = " + livroAux.getCliente());

                pst.setInt(1, livroAux.getNum());
                try (ResultSet rsBiblioteca = pst.executeQuery()) {
                    if (rsBiblioteca.next()) {
                        int bibliotecaId = rsBiblioteca.getInt("Biblioteca_idBiblioteca");
                        System.out.println("bibliotecaId = " + bibliotecaId);
                        livroAux.setBibliotecaId(bibliotecaId);
                    } else {
                        System.out.println("Nenhuma biblioteca associada a este livro.");
                    }
                }

                System.out.println("--------------------------------");

                livros.add(livroAux);
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            sucesso = false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
        return livros;
    }

    //devolve o livro de um cliente
    public boolean devolver(int clienteMatricula, int num) {
        connectToDB();
        boolean sucesso = false;
        String checagem = "SELECT Matricula FROM Cliente WHERE Matricula = ?";
        String checkSql = "SELECT Cliente_Matricula FROM Livro WHERE Numero = ?";
        String updateSql = "UPDATE Livro SET Cliente_Matricula = NULL WHERE Numero = ?";

        try {
            pst = con.prepareStatement(checagem);
            pst.setInt(1, clienteMatricula);
            rs = pst.executeQuery();

            if(!rs.next()){
                System.out.println("Cliente nao encontrado");
                return false;
            }
            pst = con.prepareStatement(checkSql);
            pst.setInt(1, num);
            rs = pst.executeQuery();

            if (rs.next()) {
                int currentMatricula = rs.getInt("Cliente_Matricula");
                if (currentMatricula == clienteMatricula) {
                    pst = con.prepareStatement(updateSql);
                    pst.setInt(1, num);
                    pst.executeUpdate();
                    sucesso = true;
                    System.out.println("Livro devolvido com sucesso.");
                } else {
                    System.out.println("Este livro não está alugado por este cliente.");
                }
            } else {
                System.out.println("Livro não encontrado.");
            }
        } catch (SQLException exc) {
            System.out.println("Erro: " + exc.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException exc) {
                System.out.println("Erro: " + exc.getMessage());
            }
        }
        return sucesso;
    }

    //mostra os livros alugados de um cliente
    public ArrayList<Livro> livrosAlugados(int clienteMatricula) {
        ArrayList<Livro> livros = new ArrayList<>();
        connectToDB();
        String sql = "SELECT * FROM Livro WHERE Cliente_Matricula = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, clienteMatricula);
            rs = pst.executeQuery();

            while (rs.next()) {
                String nome = rs.getString("Nome");
                int numero = rs.getInt("Numero");
                int matricula = rs.getInt("Cliente_Matricula");
                Livro livro = new Livro(nome, numero, matricula);
                livros.add(livro);

                System.out.println("Nome: " + livro.getNome());
                System.out.println("Numero do livro: " + livro.getNum());
                System.out.println("Matricula do cliente: " + livro.getCliente());
                System.out.println("--------------------------------");
            }
            sucesso = true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            sucesso = false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        return livros;
    }

    //conta a quantidade de livros
    public int quant() {

        int count = 0;
        connectToDB();
        String sql = "SELECT COUNT(DISTINCT Numero) AS total FROM Livro WHERE Numero <> 0";

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