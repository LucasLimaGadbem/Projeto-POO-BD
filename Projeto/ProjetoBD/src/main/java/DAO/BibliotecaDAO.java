package DAO;

import Model.Biblioteca;
import Model.Dono;
import Model.Livro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaDAO extends ConnectionDAO{


    //DAO - Data Access Object
    boolean sucesso = false; //Para saber se funcionou

    //INSERT
    public boolean insertBiblioteca(String nome, String cidade, Integer idbiblioteca, Dono dono) {

        connectToDB();

        String sql = "INSERT INTO Biblioteca (Nome, Cidade, Dono_CPF, idBiblioteca) values(?,?,?,?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1,nome);
            pst.setString(2, cidade);
            pst.setString(3, dono.getCPF());
            pst.setInt(4, idbiblioteca);
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

    //UPDATE
    public boolean updateBibliotecaNome(int idBiblioteca, String nome) {
        connectToDB();
        String sql = "UPDATE Biblioteca SET nome=? where idBiblioteca=?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setInt(2, idBiblioteca);
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

    //DELETE
    public boolean deleteBiblioteca(int idBiblioteca) {
        connectToDB();
        String sql = "DELETE FROM Biblioteca where idBiblioteca=?";
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idBiblioteca);
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
    public ArrayList<Biblioteca> selectBiblioteca() {
        ArrayList<Biblioteca> bibliotecas = new ArrayList<>();
        connectToDB();
        String sql = "SELECT * FROM Biblioteca";

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            System.out.println("Lista de bibliotecas: ");

            while (rs.next()) {

                Biblioteca bibliotecaAux = new Biblioteca(rs.getString("Nome"), rs.getString("Cidade"), rs.getInt("idBiblioteca"));

                System.out.println("matricula = " + bibliotecaAux.getIdBiblioteca());
                System.out.println("nome = " + bibliotecaAux.getNome());
                System.out.println("cidade = " + bibliotecaAux.getCidade());
                System.out.println("--------------------------------");

                bibliotecas.add(bibliotecaAux);
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
        return bibliotecas;
    }

    //mostra os livros de cada biblioteca
    public ArrayList<Biblioteca> selecionada(int id) {
        ArrayList<Biblioteca> bibliotecas = new ArrayList<>();
        connectToDB();
        String sql = "SELECT * FROM Biblioteca WHERE idBiblioteca = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            System.out.println("Lista de livros: ");

            List<Biblioteca> tempBibliotecas = new ArrayList<>();

            while (rs.next()) {
                Biblioteca biblioteca = new Biblioteca(rs.getString("Nome"), rs.getString("Cidade"), rs.getInt("idBiblioteca"));
                tempBibliotecas.add(biblioteca);
            }

            rs.close();
            pst.close();

            for (Biblioteca biblioteca : tempBibliotecas) {
                System.out.println("Livros na biblioteca " + biblioteca.getNome() + ":");
                ArrayList<Livro> livros = buscarLivrosPorBiblioteca(biblioteca.getIdBiblioteca());
                for (Livro livro : livros) {
                    System.out.println("Nome do livro: " + livro.getNome() + ", Número: " + livro.getNum());
                }
            }

            sucesso = true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            sucesso = false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Erro ao fechar: " + e.getMessage());
            }
        }

        return bibliotecas;
    }

    //auxilia para mostrar os livros de cada biblioteca
    public ArrayList<Livro> buscarLivrosPorBiblioteca(int idBiblioteca) {
        ArrayList<Livro> livros = new ArrayList<>();
        connectToDB();
        String sql = "SELECT * FROM Livro INNER JOIN Biblioteca_has_Livro ON Livro.Numero = Biblioteca_has_Livro.Livro_Numero WHERE Biblioteca_has_Livro.Biblioteca_idBiblioteca = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, idBiblioteca);
            rs = pst.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro(rs.getString("Nome"), rs.getInt("Numero"));
                livros.add(livro);
            }
            sucesso = true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            sucesso = false;
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        return livros;
    }

    //conta a quantidade de bibliotecas
    public int quantidadeBibliotecas() {

        int count = 0;
        connectToDB();
        String sql = "SELECT COUNT(DISTINCT idBiblioteca) AS total FROM Biblioteca WHERE idBiblioteca <> 0";

        try (PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()){

            if (rs.next()) {
                count = rs.getInt("total");
            }

            sucesso = true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            sucesso = false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        return count;
    }

    //pega a biblioteca de acordo com o nome
    public Biblioteca getBibliotecaByName(String name) {
        connectToDB();
        String sql = "SELECT * FROM Biblioteca WHERE nome = ?";
        Biblioteca biblioteca = null;

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("Nome");
                    String localizacao = rs.getString("Cidade");
                    int idBiblioteca = rs.getInt("idBiblioteca");
                    biblioteca = new Biblioteca(nome, localizacao, idBiblioteca);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        return biblioteca;
    }
}
