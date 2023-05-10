import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Añadir el tratamiento de errores con SQLException al programa creado en el Ejercicio 4. Por ejemplo, tratar de
 * volver a insertar la tupla recién insertada en el Ejercicio 4. Producirá un error, ya que no es posible una nueva
 * tupla con la misma clave primaria. Y este error debe ser tratado como una SQLException en este ejercicio.
 * El código de error para la clave repetida en Oracle es ORA-00001 (getErrorCode () == 1).
 */
public class Ejercicio5 {
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
            try {stmt.executeUpdate("INSERT INTO idioma VALUES ('72515657', 'Castellano')");}
            catch (SQLException e) {
                if (e.getErrorCode() == 1)
                    System.err.println("El guia 72515657 ya tiene el idioma 'Castellano'.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
