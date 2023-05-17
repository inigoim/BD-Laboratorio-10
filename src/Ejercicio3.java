import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 *
 * @author Iñigo Imaña
 * @author Leire Gesteira
 * @author Marcos Chouciño
 */
public class Ejercicio3 {
    public static void main(String[] args) {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");


        // Preparar los recursos
        try (Connection conn = mds.getConnection();
             PreparedStatement pstm = conn.prepareStatement
                     ("SELECT * FROM CLIENTE WHERE DNI = ? AND NTelefono = ?");
             Scanner sc = new Scanner(System.in)
        ) {
            // Leer datos de teclado
            System.out.print("DNI del cliente: ");
            String DNI = sc.nextLine();
            System.out.print("Numero de telefono: ");
            int NumeroTelefono = sc.nextInt();

            pstm.setString(1, DNI);
            pstm.setInt(2, NumeroTelefono);

            // Ejecutar SQL y mostrar el resultado
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
            System.err.println("Error en la ejecución:");
            System.err.println(e.getMessage());
        }
    }
}
