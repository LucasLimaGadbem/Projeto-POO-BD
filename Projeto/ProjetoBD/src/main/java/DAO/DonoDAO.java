package DAO;

import Model.Dono;
import java.sql.SQLException;
import java.util.ArrayList;

public class DonoDAO extends ConnectionDAO {

    //DAO - Data Access Object
    boolean sucesso = false; //Para saber se funcionou

    //INSERT
    public boolean insertDono(Dono dono) {

        connectToDB();

        String sql = "INSERT INTO Dono (Nome, CPF) values(?,?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, dono.getNome());
            pst.setString(2, dono.getCPF());
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

    //SELECT
    public ArrayList<Dono> selectDono() {
        ArrayList<Dono> donos = new ArrayList<>();
        connectToDB();
        String sql = "SELECT * FROM Dono";

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            System.out.println("Lista de donos: ");

            while (rs.next()) {

                Dono donoAux = new Dono(rs.getString("Nome"), rs.getString("CPF"));

                System.out.println("Nome = " + donoAux.getNome());
                System.out.println("CPF = " + donoAux.getCPF());
                System.out.println("--------------------------------");

                donos.add(donoAux);
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
        return donos;
    }
}