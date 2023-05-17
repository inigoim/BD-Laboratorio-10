import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;

/**
 * <b> Procedimientos almacenados en MySQL. </b> Define un procedimiento almacenado (procedimiento o función)
 * sin parámetros diferente a los ejemplos de las diapositivas y ejecútalos desde Java.
 *
 * @author Iñigo Imaña
 * @author Leire Gesteira
 * @author Marcos Chouciño
 */
public class Ejercicio8 {

    public static void main(String[] args) {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        ds.setUser("DBC15"); ds.setPassword("DBC15");

        // Procedimiento almacenado en MySQL
        String consultaInigo =
                "SELECT DISTINCT " +
                        "personal.Nombre, personal.Salario, personal.SuperDni, trabaja_en.Horas, buque.NombreBuque " +
                        "FROM " +
                        "((((personal JOIN familiar ON personal.Dni = familiar.DniP AND familiar.Parentesco LIKE 'Hij_') " +
                        "JOIN trabaja_en ON personal.Dni=trabaja_en.DniP AND trabaja_en.Horas >= 30) " +
                        "JOIN han_utilizado ON trabaja_en.NumProy = han_utilizado.NumProy) " +
                        "JOIN buque ON han_utilizado.NombreBuque = buque.NombreBuque) " +
                        "JOIN tipobuque ON buque.Tipo = tipobuque.Tipo AND tipobuque.Tonelaje >= 10000";

        // Añadir procedimiento a la base de datos
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP PROCEDURE IF EXISTS consulta_inigo");
            stmt.executeUpdate("CREATE PROCEDURE consulta_inigo() " + consultaInigo);
        } catch (SQLException e) {
            System.err.println("Error al añadir procedimiento en MySQL:");
            System.err.println(e.getMessage());
        }

        // Llamar al procedimiento
        try (Connection conn = ds.getConnection();
             CallableStatement cstmt = conn.prepareCall("{call consulta_inigo}");
             ResultSet rs = cstmt.executeQuery()
        ) {
            while (rs.next()) {
                System.out.printf("%s, %d, %s, %d, %s\n",
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5));
            }
        }
        catch (SQLException e) {
            System.err.println("Error al llamar al procedimiento en MySQL:");
            System.err.println(e.getMessage());
        }
    }
}
