package service;

import config.DatabaseConnection;
import dao.EmpleadoDao;
import dao.LegajoDao;
import entities.Empleado;
import entities.Legajo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmpleadoService implements GenericService<Empleado> {

    private final EmpleadoDao empleadoDao;
    private final LegajoDao legajoDao;

    public EmpleadoService() {
        this.empleadoDao = new EmpleadoDao();
        this.legajoDao = new LegajoDao();
    }



    @Override
    public Empleado insertar(Empleado empleado) throws Exception {
        validarEmpleado(empleado);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);


            Empleado existente = empleadoDao.buscarPorDni(empleado.getDni(), conn);
            if (existente != null) {
                throw new BusinessException("Ya existe un empleado con el DNI: " + empleado.getDni());
            }


            if (empleado.getLegajo() != null) {
                Legajo legajo = empleado.getLegajo();
                validarLegajo(legajo);

                Legajo legajoExistente = legajoDao.buscarPorNroLegajo(legajo.getNroLegajo(), conn);
                if (legajoExistente != null) {
                    throw new BusinessException("Ya existe un legajo con el número: " + legajo.getNroLegajo());
                }


                Legajo legajoCreado = legajoDao.crear(legajo, conn);
                empleado.setLegajo(legajoCreado);
            }

            Empleado creado = empleadoDao.crear(empleado, conn);

            conn.commit();
            return creado;
        } catch (Exception e) {
            if (conn != null) {
                rollbackSilencioso(conn);
            }
            throw e;
        } finally {
            cerrarConexion(conn);
        }
    }

    @Override
    public Empleado actualizar(Empleado empleado) throws Exception {
        if (empleado.getId() == 0) {
            throw new BusinessException("El ID del empleado es obligatorio para actualizar.");
        }
        validarEmpleado(empleado);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Empleado existente = empleadoDao.leer(empleado.getId(), conn);
            if (existente == null || existente.isEliminado()) {
                throw new BusinessException("No existe un empleado activo con ID: " + empleado.getId());
            }


            if (!existente.getDni().equals(empleado.getDni())) {
                Empleado otro = empleadoDao.buscarPorDni(empleado.getDni(), conn);
                if (otro != null) {
                    throw new BusinessException("Ya existe otro empleado con el DNI: " + empleado.getDni());
                }
            }


            empleadoDao.actualizar(empleado, conn);

            conn.commit();
            return empleado;
        } catch (Exception e) {
            if (conn != null) {
                rollbackSilencioso(conn);
            }
            throw e;
        } finally {
            cerrarConexion(conn);
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Empleado emp = empleadoDao.leer(id, conn);
            if (emp == null || emp.isEliminado()) {
                throw new BusinessException("No existe un empleado activo con ID: " + id);
            }


            empleadoDao.eliminar(id, conn);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                rollbackSilencioso(conn);
            }
            throw e;
        } finally {
            cerrarConexion(conn);
        }
    }

    @Override
    public Empleado getById(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return empleadoDao.leer(id, conn);
        }
    }

    @Override
    public List<Empleado> getAll() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return empleadoDao.leerTodos(conn);
        }
    }


    /**
     * Crea un Legajo y un Empleado en una sola transacción.
     * @param empleado datos del empleado (sin id)
     * @param legajo datos del legajo (sin id)
     */
    public Empleado crearEmpleadoConLegajo(Empleado empleado, Legajo legajo, boolean simularError) throws Exception {
        validarEmpleado(empleado);
        validarLegajo(legajo);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);


            Empleado empExistente = empleadoDao.buscarPorDni(empleado.getDni(), conn);
            if (empExistente != null) {
                throw new BusinessException("Ya existe un empleado con el DNI: " + empleado.getDni());
            }


            Legajo legajoExistente = legajoDao.buscarPorNroLegajo(legajo.getNroLegajo(), conn);
            if (legajoExistente != null) {
                throw new BusinessException("Ya existe un legajo con el número: " + legajo.getNroLegajo());
            }


            if (empleado.getLegajo() != null) {
                throw new BusinessException("El empleado ya tiene un legajo asociado (relación 1→1).");
            }


            Legajo legajoCreado = legajoDao.crear(legajo, conn);


            empleado.setLegajo(legajoCreado);
            Empleado empleadoCreado = empleadoDao.crear(empleado, conn);

            if (simularError) {
                throw new RuntimeException("Error simulado para demostrar rollback.");
            }

            conn.commit();
            return empleadoCreado;
        } catch (Exception e) {
            if (conn != null) {
                rollbackSilencioso(conn);
            }
            throw e;
        } finally {
            cerrarConexion(conn);
        }
    }

    // ==== Validaciones ====

    private void validarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new BusinessException("El empleado no puede ser null.");
        }
        if (empleado.getNombre() == null || empleado.getNombre().isBlank()) {
            throw new BusinessException("El nombre del empleado es obligatorio.");
        }
        if (empleado.getApellido() == null || empleado.getApellido().isBlank()) {
            throw new BusinessException("El apellido del empleado es obligatorio.");
        }
        if (empleado.getDni() == null || empleado.getDni().isBlank()) {
            throw new BusinessException("El DNI del empleado es obligatorio.");
        }
        if (empleado.getDni().length() > 15) {
            throw new BusinessException("El DNI no puede superar los 15 caracteres.");
        }
        if (empleado.getEmail() != null && !empleado.getEmail().isBlank()
                && !empleado.getEmail().contains("@")) {
            throw new BusinessException("El email del empleado tiene un formato inválido.");
        }
        LocalDate fechaIngreso = empleado.getFechaIngreso();
        if (fechaIngreso != null && fechaIngreso.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de ingreso no puede ser futura.");
        }
    }

    private void validarLegajo(Legajo legajo) {
        if (legajo == null) {
            throw new BusinessException("El legajo no puede ser null.");
        }
        if (legajo.getNroLegajo() == null || legajo.getNroLegajo().isBlank()) {
            throw new BusinessException("El número de legajo es obligatorio.");
        }
        if (legajo.getNroLegajo().length() > 20) {
            throw new BusinessException("El número de legajo no puede superar los 20 caracteres.");
        }
        if (legajo.getEstado() == null) {
            throw new BusinessException("El estado del legajo es obligatorio.");
        }
    }

    // ==== Utilitarios ====

    private void rollbackSilencioso(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            System.err.println("Error al hacer rollback: " + ex.getMessage());
        }
    }

    private void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }
}