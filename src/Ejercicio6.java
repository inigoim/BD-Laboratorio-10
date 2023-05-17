import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.*;

/**
 * El cliente con DNI 10000001 va a pasar a formar parte de la plantilla de guías de la empresa. En adelante,
 * aparecerá recogido además de como cliente como guía en la BD. Refleja esta operación en la base de datos
 * instalada en el servidor de Oracle obteniendo los datos necesarios del servidor de MySQL.
 * <p>
 * Para realizar este ejercicio necesitaréis dos conexiones de bases de datos una sobre el sistema de gestión
 * de bases de datos MySQL, desde donde obtendréis los datos, y otra sobre Oracle, para modificar los datos.
 * <p>
 * Pasos necesarios:
 * <ol>
 *     <li> crear dos conexiones una a cada Base de Datos
 *     <li> Crear dos Objectos {@link Statement} y {@link PreparedStatement} (uno con cada conexión)
 *     <li> Ejecutar la consulta de MySQL
 *     <li> Recorrer el resultado de 4 y establecer parámetros para consulta de Oracle
 *     <li> Ejecutar la consulta INSERT en Oracle
 *     <li> Librero recursos y cerrar conexión
 * </ol>
 *
 * @author Iñigo Imaña
 * @author Leire Gesteira
 * @author Marcos Chouciño
 */
public class Ejercicio6 {
    public static void main(String[] args) throws SQLException {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");

        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa");
        ods.setUser("BDC15"); ods.setPassword("BDC15");

        try (Connection mcnn = mds.getConnection();
             Connection ocnn = ods.getConnection();
             Statement mstmt = mcnn.createStatement();

             // No tiene sentido crear un PreparedStatement si no se va a reutilizar
             PreparedStatement opstmt = ocnn.prepareStatement
                     ("INSERT INTO guia (DNI, Nombre, NTelefono) VALUES (?, ?, ?)");

             ResultSet rs = mstmt.executeQuery("SELECT DNI, Nombre, NTelefono FROM CLIENTE WHERE DNI = '10000001'")
        ) {
            if (rs.next()) {
                opstmt.setString(1, rs.getString(1));
                opstmt.setString(2, rs.getString(2));
                opstmt.setInt(3, rs.getInt(3));
                try {
                    opstmt.executeUpdate();
                } catch (SQLException e) {
                    if (e.getErrorCode() == 1)
                        System.err.println("El guia 10000001 ya existe en la base de datos Oracle.");
                }
            }

        }
        catch (SQLException e) {
            System.err.println("Error durante la conexión:");
            System.err.println(e.getMessage());
        }
    }
}
