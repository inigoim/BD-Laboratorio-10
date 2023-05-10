import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.*;
import java.util.Scanner;


public class Main {

    static MysqlDataSource mds;
    static OracleDataSource ods;


    public static void main(String[] args) throws SQLException {
        mds = new MysqlDataSource();
        mds.setURL("jdbc:mysql://dif-mysql.ehu.es:3306/DBC15?&useSSL=false");
        ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@vsids11.si.ehu.es:1521:gipuzkoa");


//        System.out.printf("%n%n=================%nEJERCICIO 1%n=================%n%n");
//        ejercicio1();
//        System.out.printf("%n%n=================%nEJERCICIO 2%n=================%n%n");
//        ejercicio2();
//        System.out.printf("%n%n=================%nEJERCICIO 3%n=================%n%n");
//        ejercicio3();
//        System.out.printf("%n%n=================%nEJERCICIO 4%n=================%n%n");
//        ejercicio4();
//        System.out.printf("%n%n=================%nEJERCICIO 5%n=================%n%n");
//        ejercicio5();

        ejercicio6();
    }

    public static void mostrarClientes(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.printf("%s \t %s \t\t %s \t\t\t %d %n",
                    rs.getString("DNI"),
                    rs.getString("Nombre"),
                    rs.getString("Direccion"),
                    rs.getInt("NTelefono"));
        }
    }

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
     */
    public static void ejercicio1() {

        try (Connection conn = mds.getConnection("DBC15", "DBC15");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENTE")) {

            System.out.printf("%n%nMYSQL:%n%n");
            mostrarClientes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = ods.getConnection("BDC15", "BDC15");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENTE")) {

            System.out.printf("%n%nORACLE:%n%n");
            mostrarClientes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

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
     *     <li> Solicitar parámetros desde el teclado
     *     <li> Establecer parámetros de consulta
     *     <li> Ejecutar SQL
     *     <li> Recorrer el resultado e imprimir
     *     <li> Liberar recursos y cerrar conexión
     * </ol>
     */
    public static void ejercicio2() {

        try (Connection conn = mds.getConnection("DBC15", "DBC15");
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM CLIENTE WHERE DNI = ?")) {

            pstmt.setString(1, "10000001");
            try (ResultSet rs = pstmt.executeQuery()) {
                mostrarClientes(rs);
            }

            pstmt.setString(1, "10000004");
            try (ResultSet rs = pstmt.executeQuery()) {
                mostrarClientes(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
    public static void ejercicio3() {
        try (Connection conn = mds.getConnection("DBC15", "DBC15");

             // No tiene sentido crear un PreparedStatement si no se va a reutilizar
             Statement stm = conn.createStatement();

             Scanner sc = new Scanner(System.in)
        ) {

            System.out.print("DNI del cliente: ");
            String DNI = sc.nextLine();
            System.out.print("Numero de telefono: ");
            int NTelefono = sc.nextInt();

            String sql = String.format("SELECT * FROM CLIENTE WHERE DNI = %s AND NTelefono = %d", DNI, NTelefono);

            try (ResultSet rs = stm.executeQuery(sql)) {mostrarClientes(rs);}

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualizar la base de datos Viajes de Oracle con UPDATE, DELETE e INSERT.
     * Escoged qué tupla insertar en qué tabla, qué tupla eliminar y qué tupla actualizar.
     */
    public static void ejercicio4() {
        try (Connection conn = ods.getConnection("BDC15", "BDC15");
             Statement stmt = conn.createStatement()
        ) {
            stmt.executeUpdate("UPDATE cliente SET Direccion = 'Calle San Domingo' WHERE DNI = '10000006'");
            stmt.executeUpdate("DELETE FROM idioma WHERE DNI = 65111111");
            stmt.executeUpdate("INSERT INTO idioma VALUES ('72515657', 'Castellano')");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Añadir el tratamiento de errores con SQLException al programa creado en el Ejercicio 4. Por ejemplo, tratar de
     * volver a insertar la tupla recién insertada en el Ejercicio 4. Producirá un error, ya que no es posible una nueva
     * tupla con la misma clave primaria. Y este error debe ser tratado como una SQLException en este ejercicio.
     * El código de error para la clave repetida en Oracle es ORA-00001 (getErrorCode () == 1).
     */
    public static void ejercicio5() {
        try (Connection conn = ods.getConnection("BDC15", "BDC15");
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
     */
    public static void ejercicio6() {
        try (Connection mcnn = mds.getConnection("DBC15", "DBC15");
        Connection ocnn = ods.getConnection("BDC15", "BDC15");
        Statement mstmt = mcnn.createStatement();

        // No tiene sentido crear un PreparedStatement si no se va a reutilizar
        Statement ostmt = ocnn.createStatement();

        ResultSet rs = mstmt.executeQuery("SELECT DNI, Nombre, NTelefono FROM CLIENTE WHERE DNI = '10000001'")
        ) {
            if (rs.next()) {
                String sql = String.format("INSERT INTO guia VALUES ('%s', '%s', '%d')",
                        rs.getString("DNI"),
                        rs.getString("Nombre"),
                        rs.getInt("NTelefono"));
                ostmt.executeUpdate(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}