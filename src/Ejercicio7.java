import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa");
        ods.setUser("BDC15");
        ods.setPassword("BDC15");

        try (Connection conn = ods.getConnection();
             Statement stmt = conn.createStatement()
        ) {
            conn.setAutoCommit(false);
            String viajesNuevos = "(SELECT Destino, add_months(FechaSalida, 12), Dias, PrecioDia, CiudadSalida, DNI " +
                    "FROM viaje " +
                    "WHERE FechaSalida BETWEEN TO_DATE('01/01/2022', 'DD/MM/YYYY') AND TO_DATE('31/12/2022', 'DD/MM/YYYY'))";
            try {
                stmt.executeUpdate("INSERT INTO viaje " + viajesNuevos);
                conn.commit();
                System.out.println("Commit");
            } catch (SQLException e) {
                System.err.print(e.getMessage());
                conn.rollback();
                System.err.println("Rollback");
            }
        }
    }
}
