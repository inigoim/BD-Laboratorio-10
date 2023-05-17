import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Conectar con la base de datos Viajes de MySQL y de Oracle y obtener los datos de los clientes.
 * Para ello utiliza las siguientes URL y conéctate con el mismo usuario y contraseña
 * que se ha utilizado en laboratorios anteriores, en MySQL DBCXX y en Oracle BDCXX.
 * <p> jdbc:mysql://dif-mysql.ehu.es:3306/DBCXX?&useSSL=false
 * <p> jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa
 * <p> Recuerda los pasos necesarios:
 * <ol>
 *     <li> Crear conexión (2 conexiones)
 *     <li> Crear Objeto {@link Statement} (2 objetos)
 *     <li> Ejecutar SQL (2 ejecuciones)
 *     <li> Recorrer cada uno de los resultados obtenidos e imprimir
 *     <li> Liberar recursos y cerrar conexión
 * </ol>
 *
 * @author Iñigo Imaña
 * @author Leire Gesteira
 * @author Marcos Chouciño
 */
public class Ejercicio1 {
    public static void main(String[] args) throws SQLException {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");

        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa");
        ods.setUser("BDC15"); ods.setPassword("BDC15");


        System.out.printf("MYSQL:%n%n");
        mostrarClientes(mds);

        System.out.printf("%n%n%nOracle:%n%n");
        mostrarClientes(ods);
    }

    public static void mostrarClientes(DataSource ds) {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENTE")
        ) {
            while (rs.next()) {
                System.out.printf("%s \t %s \t\t %s \t\t\t %d %n",
                        rs.getString("DNI"),
                        rs.getString("Nombre"),
                        rs.getString("Direccion"),
                        rs.getInt("NTelefono"));
            }
        }
        catch (SQLException e) {
            System.err.println("Error al obtener los datos de los clientes:");
            System.err.println(e.getMessage());
        }
    }

}
