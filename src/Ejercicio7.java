import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <b>Transacciones en ORACLE.</b> Insertar las tuplas necesarias para repetir los viajes del año 2022 en el
 * año 2023 con las mismas características (mismos hoteles, excursiones y guías). Puedes usar la función
 * add_months(fecha, 12). Ejecútalo dos veces para ver que la segunda ejecución terminaría en rollback.
 * <p>
 *     Pasos necesarios:
 *     <ol>
 *         <li> Crear inicialmente la conexión a null
 *         <li> Establecer autocommit a false
 *         <li> Terminar con commit en caso de éxito (después de terminar la transacción)
 *          o con rollback en caso de error (dentro del catch) </li>
 *     </ol>
 */
public class Ejercicio7 {

    public static void main(String[] args) throws SQLException {
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        mds.setUser("DBC15"); mds.setPassword("DBC15");

        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa");
        ods.setUser("BDC15"); ods.setPassword("BDC15");

        try(Connection conn = ods.getConnection();) {
            conn.setAutoCommit(false);
            conn.commit();
        }

    }
}
