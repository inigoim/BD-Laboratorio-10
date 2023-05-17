import com.mysql.cj.jdbc.MysqlDataSource;

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
 *
 * @author Iñigo Imaña
 * @author Leire Gesteira
 * @author Marcos Chouciño
 */
public class Ejercicio2 {
    public static void main(String[] args) {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");


        try (Connection conn = mds.getConnection();
             PreparedStatement pstmt =
                     conn.prepareStatement("SELECT * FROM CLIENTE WHERE DNI = ?")
        ) {
            pstmt.setString(1, "10000001");
            mostrarCliente(pstmt);
            pstmt.setString(1, "10000004");
            mostrarCliente(pstmt);
        }
        catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos:");
            System.err.println(e.getMessage());
        }

    }

    public static void mostrarCliente(PreparedStatement pstmt){
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                System.out.printf("%s \t %s \t\t %s \t\t\t %d %n",
                        rs.getString("DNI"),
                        rs.getString("Nombre"),
                        rs.getString("Direccion"),
                        rs.getInt("NTelefono"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los datos del cliente:");
            System.err.println(e.getMessage());
        }
    }

}
