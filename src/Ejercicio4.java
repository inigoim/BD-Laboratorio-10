import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Actualizar la base de datos Viajes de Oracle con UPDATE, DELETE e INSERT.
 * Escoged qué tupla insertar en qué tabla, qué tupla eliminar y qué tupla actualizar.
 */
public class Ejercicio4 {
    public static void main(String[] args) throws SQLException {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");

        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa");
        ods.setUser("BDC15"); ods.setPassword("BDC15");

        try (Connection conn = ods.getConnection();
             Statement stmt = conn.createStatement()
        ) {
            stmt.executeUpdate("UPDATE cliente SET Direccion = 'Calle San Domingo' WHERE DNI = '10000006'");
            stmt.executeUpdate("DELETE FROM idioma WHERE DNI = 65111111");
            stmt.executeUpdate("INSERT INTO idioma VALUES ('72515657', 'Castellano')");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
