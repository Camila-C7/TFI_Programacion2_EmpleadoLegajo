package dao;

import config.DatabaseConnection; 
import entities.Empleado;
import entities.Legajo;
import entities.EstadoLegajo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;


/**
 *
 * @author alejo
 */
public class PruebaDao {
    public static void main(String[] args) {
        
        EmpleadoDao empleadoDao = new EmpleadoDao();
        LegajoDao legajoDao = new LegajoDao();

        Connection conn = null;
        long legajoIdCreado = 0;
        long empleadoIdCreado = 0;

        try {

            conn = DatabaseConnection.getConnection();


            conn.setAutoCommit(false);
            System.out.println("--- Transacción iniciada ---");


            System.out.println("\n--- [Prueba 1: CREAR] ---");
            
            Legajo nuevoLegajo = new Legajo(
                "23164", 
                "Junior", LocalDate.now(),
                "Observaciones de prueba",
                    EstadoLegajo.ACTIVO
            );
            legajoDao.crear(nuevoLegajo, conn);
            legajoIdCreado = nuevoLegajo.getId();
            System.out.println("Legajo creado con ID: " + legajoIdCreado);

            Empleado nuevoEmpleado = new Empleado(
                "Marta", 
                "Sanchez", 
                "30123456", 
                "marta@correo.com", 
                LocalDate.of(2022, 5, 10), 
                "Contabilidad",nuevoLegajo
            );
            nuevoEmpleado.setLegajo(nuevoLegajo);
            
            empleadoDao.crear(nuevoEmpleado, conn);
            empleadoIdCreado = nuevoEmpleado.getId();
            System.out.println("Empleado creado con ID: " + empleadoIdCreado);

            

            System.out.println("\n--- [Prueba 2: LEER] ---");
            Empleado empleadoLeido = empleadoDao.leer(empleadoIdCreado, conn);
            
            if (empleadoLeido != null && empleadoLeido.getLegajo() != null) {
                System.out.println("Lectura OK: " + empleadoLeido.getNombre());
                System.out.println("Legajo asociado: " + empleadoLeido.getLegajo().getNroLegajo());
            } else {
                throw new SQLException("Error en LEER, no se encontró el empleado o el legajo asociado.");
            }


            System.out.println("\n--- [Prueba 3: ACTUALIZAR] ---");
            System.out.println("Area antigua: " + empleadoLeido.getArea());
            empleadoLeido.setArea("Recursos Humanos"); 
            empleadoDao.actualizar(empleadoLeido, conn);
            
            Empleado empleadoActualizado = empleadoDao.leer(empleadoIdCreado, conn);
            System.out.println("Area nueva: " + empleadoActualizado.getArea());
            

            System.out.println("\n--- [Prueba 4: ELIMINAR LÓGICO] ---");
            empleadoDao.eliminar(empleadoIdCreado, conn);
            System.out.println("Empleado con ID " + empleadoIdCreado + " marcado como eliminado.");
            
            Empleado empleadoEliminado = empleadoDao.leer(empleadoIdCreado, conn);
            if (empleadoEliminado == null) {
                System.out.println("Verificación OK: El empleado eliminado no se puede leer.");
            } else {
                throw new SQLException("Error en ELIMINAR, el empleado aún se puede leer.");
            }
            
            legajoDao.eliminar(legajoIdCreado, conn);
            System.out.println("Legajo con ID " + legajoIdCreado + " marcado como eliminado.");

            System.out.println("\n--- Pruebas exitosas, realizando COMMIT ---");
            conn.commit();

        } catch (Exception e) {
            System.err.println("\n--- !!! ERROR EN LA TRANSACCIÓN !!! ---");
            System.err.println("Se detectó un error: " + e.getMessage());
            e.printStackTrace();
            
            try {
                if (conn != null) {
                    System.err.println("--- Revertimos los cambios (ROLLBACK) ---");
                    conn.rollback();
                }
            } catch (SQLException se) {
                System.err.println("Error al intentar hacer rollback:");
                se.printStackTrace();
            }
            
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("\n--- Conexión cerrada ---");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
