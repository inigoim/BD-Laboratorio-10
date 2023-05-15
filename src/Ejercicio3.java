import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.*;
import java.util.Scanner;

/**
 * Conectar con la base de datos Viajes de MySQL y obtener los datos de un cliente concreto
 * cuyo dni y teléfono se introducen por teclado (utilizando la clase {@link Scanner}).
 * Los pasos son similares a los del ejercicio anterior:
 * <ol>
 *     <li> Crear conexión
 *     <li> Preparar consulta
 *     <li> Solicitar parámetros desde el teclado
 *     <li> Establecer parámetros de consulta
 *     <li> Ejecutar SQL
 *     <li> Recorrer el resultado e imprimir
 *     <li> Liberar recursos y cerrar conexión
 * </ol>
 */
public class Ejercicio3 {
    public static void main(String[] args) throws SQLException {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");

        try (Connection conn = mds.getConnection();
             PreparedStatement pstm =conn.prepareStatement
                     ("SELECT * FROM CLIENTE WHERE DNI = ? AND NTelefono = ?");
             Scanner sc = new Scanner(System.in)
        ) {
            System.out.print("DNI del cliente: ");
            String DNI = sc.nextLine();
            System.out.print("Numero de telefono: ");
            int NTelefono = sc.nextInt();

            pstm.setString(1, DNI);
            pstm.setInt(2, NTelefono);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s \t %s \t\t %s \t\t\t %d %n",
                            rs.getString("DNI"),
                            rs.getString("Nombre"),
                            rs.getString("Direccion"),
                            rs.getInt("NTelefono"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
