import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Ejercicio8 {
    private static MysqlDataSource mds;
    private static OracleDataSource ods;

    public static void main(String[] args) throws SQLException {
        mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");

        ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa");
        ods.setUser("BDC15"); ods.setPassword("BDC15");

        String procedimiento = "UPDATE hotel SET Capacidad = Capacidad + 10 WHERE IdHotel = 'h94';";
        anadirProcedimientoMySQL(procedimiento, "aumentarCapacidad");
        anadirProcedimientoOracle(procedimiento, "aumentarCapacidad");

        try (Connection conn = mds.getConnection();
             CallableStatement cstmt = conn.prepareCall("{call aumentarCapacidad}")) {
            cstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al llamar al procedimiento en MySQL");
            System.err.print(e.getMessage());
        }

        try (Connection conn = ods.getConnection();
             CallableStatement cstmt = conn.prepareCall("{call aumentarCapacidad}")) {
            cstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al llamar al procedimiento en Oracle");
            System.err.print(e.getMessage());
        }
    }

    public static void anadirProcedimientoMySQL(String procedimiento, String nombre) {

        String borrarProcedimiento = String.format("DROP PROCEDURE IF EXISTS %s", nombre);
        String anadirProcedimiento = String.format("CREATE PROCEDURE %s() %s ", nombre, procedimiento);

        try (Connection conn = mds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(borrarProcedimiento);
            stmt.executeUpdate(anadirProcedimiento);
        } catch (SQLException e) {
            System.err.println("Error al añadir procedimiento en MYSQL");
            System.err.println(e.getMessage());
        }
    }

    public static void anadirProcedimientoOracle(String procedimiento, String nombre) {

        String anadirProcedimiento = String.format("CREATE OR REPLACE PROCEDURE %s AS " +
                "BEGIN %s END;", nombre, procedimiento);

        System.out.println(anadirProcedimiento);

        try (Connection conn = ods.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(anadirProcedimiento);
        } catch (SQLException e) {
            System.err.println("Error al añadir procedimiento en Oracle");
            System.err.println(e.getMessage());
        }
    }
}
