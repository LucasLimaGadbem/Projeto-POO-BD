package DAO;

import Model.Biblioteca;
import Model.Livro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BibliotecaHasLivroDAO extends ConnectionDAO{

    //linka biblioteca e livro
    public void insertBibliotecaHasLivro(Biblioteca b, Livro l) {
        connectToDB();

        String checkLivroSql = "SELECT COUNT(*) FROM Livro WHERE Numero = ?";
        String checkBibliotecaSql = "SELECT COUNT(*) FROM Biblioteca WHERE idBiblioteca = ?";
        String insertSql = "INSERT INTO Biblioteca_has_Livro (Biblioteca_idBiblioteca, Livro_Numero) VALUES (?, ?)";

        boolean livroExists = false;
        boolean bibliotecaExists = false;

        try (PreparedStatement checkLivroStmt = con.prepareStatement(checkLivroSql);
             PreparedStatement checkBibliotecaStmt = con.prepareStatement(checkBibliotecaSql)) {

            checkLivroStmt.setInt(1, l.getNum());
            try (ResultSet rs = checkLivroStmt.executeQuery()) {
                if (rs.next()) {
                    livroExists = rs.getInt(1) > 0;
                }
            }

            checkBibliotecaStmt.setInt(1, b.getIdBiblioteca());
            try (ResultSet rs = checkBibliotecaStmt.executeQuery()) {
                if (rs.next()) {
                    bibliotecaExists = rs.getInt(1) > 0;
                }
            }

            if (livroExists && bibliotecaExists) {
                try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, b.getIdBiblioteca());
                    insertStmt.setInt(2, l.getNum());
                    insertStmt.execute();
                }
            } else {
                if (!livroExists) {
                    System.out.println("Erro: O Livro com Numero " + l.getNum() + " não existe.");
                }
                if (!bibliotecaExists) {
                    System.out.println("Erro: A Biblioteca com idBiblioteca " + b.getIdBiblioteca() + " não existe.");
                }
            }
        } catch (SQLException exc) {
            System.out.println("Erro: " + exc.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                System.out.println("Erro ao fechar: " + exc.getMessage());
            }
        }
    }
}
