package service;

import config.DatabaseConnection;
import dao.LegajoDao;
import entities.Legajo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LegajoService implements GenericService<Legajo> {

    private final LegajoDao legajoDao;

    public LegajoService() {
        this.legajoDao = new LegajoDao();
    }

    @Override
    public Legajo insertar(Legajo legajo) throws Exception {
        validarLegajo(legajo);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

           
            Legajo existente = legajoDao.buscarPorNroLegajo(legajo.getNroLegajo(), conn);
            if (existente != null) {
                throw new BusinessException("Ya existe un legajo con el número: " + legajo.getNroLegajo());
            }

            Legajo creado = legajoDao.crear(legajo, conn);

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
    public Legajo actualizar(Legajo legajo) throws Exception {
        if (legajo.getId() == null || legajo.getId() == 0) {
            throw new BusinessException("El ID del legajo es obligatorio para actualizar.");
        }
        validarLegajo(legajo);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Legajo existente = legajoDao.leer(legajo.getId(), conn);
            if (existente == null || Boolean.TRUE.equals(existente.getEliminado())) {
                throw new BusinessException("No existe un legajo activo con ID: " + legajo.getId());
            }

            
            if (!existente.getNroLegajo().equals(legajo.getNroLegajo())) {
                Legajo otro = legajoDao.buscarPorNroLegajo(legajo.getNroLegajo(), conn);
                if (otro != null) {
                    throw new BusinessException("Ya existe otro legajo con el número: " + legajo.getNroLegajo());
                }
            }

            legajoDao.actualizar(legajo, conn);

            conn.commit();
            return legajo;
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

            Legajo legajo = legajoDao.leer(id, conn);
            if (legajo == null || Boolean.TRUE.equals(legajo.getEliminado())) {
                throw new BusinessException("No existe un legajo activo con ID: " + id);
            }

            legajoDao.eliminar(id, conn);

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
    public Legajo getById(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return legajoDao.leer(id, conn);
        }
    }

    @Override
    public List<Legajo> getAll() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return legajoDao.leerTodos(conn);
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
        LocalDate fechaAlta = legajo.getFechaAlta();
        if (fechaAlta != null && fechaAlta.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de alta del legajo no puede ser futura.");
        }
    }

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