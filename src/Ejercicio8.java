import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Ejercicio8 {
    public static void main(String[] args) throws SQLException {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");

        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa");
        ods.setUser("BDC15"); ods.setPassword("BDC15");

        try (Connection conn = mds.getConnection()) {
            anadirProcedimiento(conn);
            llamarProcedimiento(conn);
        } catch (SQLException e) {
            System.err.println("Error al conectar");
            System.err.print(e.getMessage());
        }

        try (Connection conn = ods.getConnection()) {
            anadirProcedimiento(conn);
            llamarProcedimiento(conn);
        } catch (SQLException e) {
            System.err.println("Error al conectar");
            System.err.print(e.getMessage());
        }
    }

    public static void anadirProcedimiento(Connection conn) {
        String procedimiento =
                "BEGIN " +
                "UPDATE hotel SET Capacidad = Capacidad + 10 WHERE IdHotel = 'h94';" +
                "END";
        String anadirProcedimiento = "CREATE OR REPLACE PROCEDURE procedimientoX AS " + procedimiento;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(anadirProcedimiento);
        } catch (SQLException e) {
            System.err.println("Error al añadir procedimiento");
            System.err.print(e.getMessage());
        }
    }

    public static void llamarProcedimiento(Connection conn) {
        String llamada = "CALL procedimientoX";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(llamada);
        } catch (SQLException e) {
            System.err.println("Error al llamar procedimiento");
            System.err.print(e.getMessage());
        }
    }
}
