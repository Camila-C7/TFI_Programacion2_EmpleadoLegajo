package dao;

import entities.Legajo;
import entities.EstadoLegajo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LegajoDao implements GenericDao<Legajo> {
    
    @Override
    public Legajo crear(Legajo legajo, Connection conn) throws SQLException {
        
        
        String sql = "INSERT INTO legajos (nroLegajo, categoria, estado, fechaAlta, observaciones, eliminado) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, legajo.getNroLegajo());
            pstmt.setString(2, legajo.getCategoria());
            pstmt.setString(3, legajo.getEstado().toString());
            pstmt.setDate(4, java.sql.Date.valueOf(legajo.getFechaAlta()));
            pstmt.setString(5, legajo.getObservaciones());
            // eliminado = false
            pstmt.setBoolean(6, false);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del legajo falló.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    legajo.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Falló al obtener el ID del legajo.");
                }
            }
        }
        return legajo;
    }

    @Override
    public Legajo leer(long id, Connection conn) throws SQLException {
        Legajo legajo = null;
        String sql = "SELECT * FROM legajos WHERE id = ? AND eliminado = false";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    legajo = new Legajo();
                    legajo.setId(rs.getLong("id"));
                    legajo.setNroLegajo(rs.getString("nroLegajo"));
                    legajo.setCategoria(rs.getString("categoria"));
                    legajo.setEstado(EstadoLegajo.valueOf(rs.getString("estado")));
                    legajo.setFechaAlta(rs.getDate("fechaAlta").toLocalDate());
                    legajo.setObservaciones(rs.getString("observaciones"));
                    legajo.setEliminado(rs.getBoolean("eliminado"));
                }
            }
        }
        return legajo;
    }

    @Override
    public List<Legajo> leerTodos(Connection conn) throws SQLException {
        List<Legajo> legajos = new ArrayList<>();
        String sql = "SELECT * FROM legajos WHERE eliminado = false";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Legajo legajo = new Legajo();
                    legajo.setId(rs.getLong("id"));
                    legajo.setNroLegajo(rs.getString("nroLegajo"));
                    legajo.setCategoria(rs.getString("categoria"));
                    legajo.setEstado(EstadoLegajo.valueOf(rs.getString("estado")));
                    legajo.setFechaAlta(rs.getDate("fechaAlta").toLocalDate());
                    legajo.setObservaciones(rs.getString("observaciones"));
                    legajo.setEliminado(rs.getBoolean("eliminado"));
                    legajos.add(legajo);
                }
            }
        }
        return legajos;
    }

    @Override
    public void actualizar(Legajo legajo, Connection conn) throws SQLException {
        String sql = "UPDATE legajos SET nroLegajo = ?, categoria = ?, estado = ?, fechaAlta = ?, observaciones = ? " +
                     "WHERE id = ? AND eliminado = false";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, legajo.getNroLegajo());
            pstmt.setString(2, legajo.getCategoria());
            pstmt.setString(3, legajo.getEstado().toString());
            pstmt.setDate(4, java.sql.Date.valueOf(legajo.getFechaAlta()));
            pstmt.setString(5, legajo.getObservaciones());
            pstmt.setLong(6, legajo.getId());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(long id, Connection conn) throws SQLException {
        // Baja lógica
        String sql = "UPDATE legajos SET eliminado = true WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }
    

    public Legajo buscarPorNroLegajo(String nroLegajo, Connection conn) throws SQLException {
        Legajo legajo = null;
        String sql = "SELECT * FROM legajos WHERE nroLegajo = ? AND eliminado = false";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nroLegajo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    legajo = new Legajo();
                    legajo.setId(rs.getLong("id"));
                    legajo.setNroLegajo(rs.getString("nroLegajo"));
                    legajo.setCategoria(rs.getString("categoria"));
                    legajo.setEstado(EstadoLegajo.valueOf(rs.getString("estado")));
                    legajo.setFechaAlta(rs.getDate("fechaAlta").toLocalDate());
                    legajo.setObservaciones(rs.getString("observaciones"));
                    legajo.setEliminado(rs.getBoolean("eliminado"));
                }
            }
        }
        return legajo;
    }
}
