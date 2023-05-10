import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Conectar con la base de datos Viajes de MySQL y obtener datos de clientes
 * utilizando consultas preparadas, {@link PreparedStatement}.
 * <ol type="a">
 *     <li> Utilizando sentencias preparadas, obtener y escribir los datos del cliente con dni 10000001.
 *     <li> Dentro del mismo programa, reutilizar la consulta para obtener los datos del cliente 10000004.
 * </ol>
 * Recuerda los pasos necesarios:
 * <ol>
 *     <li> Crear conexión
 *     <li> Preparar consulta
 *     <li> Establecer parámetros de consulta
 *     <li> Ejecutar SQL
 *     <li> Recorrer el resultado e imprimir
 *     <li> Reutilización
 *     <li> Liberar recursos y cerrar conexión
 * </ol>
 */
public class Ejercicio2 {
    public static void main(String[] args) throws SQLException {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");

        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa");
        ods.setUser("BDC15"); ods.setPassword("BDC15");

        try (Connection conn = mds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM CLIENTE WHERE DNI = ?")) {

            mostrarCliente(pstmt, "10000001");
            mostrarCliente(pstmt, "10000004");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void mostrarCliente(PreparedStatement pstmt, String DNI) throws SQLException {
        pstmt.setString(1, DNI);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                System.out.printf("%s \t %s \t\t %s \t\t\t %d %n",
                        rs.getString("DNI"),
                        rs.getString("Nombre"),
                        rs.getString("Direccion"),
                        rs.getInt("NTelefono"));
            }
        }
    }

}
