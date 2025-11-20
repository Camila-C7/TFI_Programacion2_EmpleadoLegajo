package dao;

import dao.GenericDao;
import entities.Empleado;
import entities.Legajo;
import entities.EstadoLegajo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class EmpleadoDao implements GenericDao<Empleado> {
    
    @Override
    public Empleado crear(Empleado empleado, Connection conn) throws SQLException{
        
        
        String sql = "INSERT INTO empleados (nombre, apellido, dni, email, fechaIngreso, area, legajo_id, eliminado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            
            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getApellido());
            pstmt.setString(3, empleado.getDni());
            pstmt.setString(4, empleado.getEmail());
            pstmt.setDate(5, java.sql.Date.valueOf(empleado.getFechaIngreso()));
            pstmt.setString(6, empleado.getArea());
            
            
            if (empleado.getLegajo() != null) {
                pstmt.setLong(7, empleado.getLegajo().getId());
            } else {
                pstmt.setNull(7, java.sql.Types.BIGINT);
            }
            
            
            pstmt.setBoolean(8, false); 
            
            
            
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del empleado falló, no se insertaron filas.");
            }

            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Asignamos el ID al objeto
                    empleado.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("La creación del empleado falló, no se obtuvo el ID.");
                }
            }
        }
        return empleado;
    }
    
    
        @Override
    public Empleado leer(long id, Connection conn) throws SQLException {
        Empleado empleado = null;
                
        
        String sql = "SELECT * FROM empleados e " +
                     "LEFT JOIN legajos l ON e.legajo_id = l.id " +
                     "WHERE e.id = ? AND e.eliminado = false";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Construimos el objeto empleado
                    empleado = new Empleado();
                    empleado.setId(rs.getLong("id"));
                    empleado.setNombre(rs.getString("nombre"));
                    empleado.setApellido(rs.getString("apellido"));
                    empleado.setDni(rs.getString("dni"));
                    empleado.setEmail(rs.getString("email"));
                    empleado.setFechaIngreso(rs.getDate("fechaIngreso").toLocalDate());
                    empleado.setArea(rs.getString("area"));
                    empleado.setEliminado(rs.getBoolean("eliminado"));

                    // construimos el objeto Legajo
                    long legajoId = rs.getLong("legajo_id");
                    if (!rs.wasNull()) {
                        Legajo legajo = new Legajo();
                        legajo.setId(legajoId);
                        
                        legajo.setNroLegajo(rs.getString("nroLegajo"));
                        legajo.setCategoria(rs.getString("categoria"));
                        legajo.setEstado(EstadoLegajo.valueOf(rs.getString("estado")));
                        legajo.setFechaAlta(rs.getDate("fechaAlta").toLocalDate());
                        legajo.setObservaciones(rs.getString("observaciones"));
                        
                        // Se asigna el legajo al empleado
                        empleado.setLegajo(legajo);
                    }
                }
            }
        }
        return empleado;
    }
       
    @Override
    public List<Empleado> leerTodos(Connection conn) throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        

        String sql = "SELECT * FROM empleados WHERE eliminado = false"; 

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Empleado empleado = new Empleado();
                    empleado.setId(rs.getLong("id"));
                    empleado.setNombre(rs.getString("nombre"));
                    empleado.setApellido(rs.getString("apellido"));
                    empleado.setDni(rs.getString("dni"));
                    empleado.setEmail(rs.getString("email"));
                    empleado.setFechaIngreso(rs.getDate("fechaIngreso").toLocalDate());
                    empleado.setArea(rs.getString("area"));
                    
                    // Armamos el Legajo del empleado
                    long legajoId = rs.getLong("legajo_id");
                    if (!rs.wasNull()) {
                        Legajo legajo = new Legajo();
                        legajo.setId(legajoId);
                        
                        legajo.setNroLegajo(rs.getString("nroLegajo"));
                        legajo.setCategoria(rs.getString("categoria"));
                        legajo.setEstado(EstadoLegajo.valueOf(rs.getString("estado")));
                        legajo.setFechaAlta(rs.getDate("fechaAlta").toLocalDate());
                        legajo.setObservaciones(rs.getString("observaciones"));
                        
                        // Se asigna el legajo al empleado
                        empleado.setLegajo(legajo);
                        
                    // añadimos el empleado a la lista    
                    empleados.add(empleado);
                }
            }
        }
        return empleados;
    }
    }
    
    @Override
    public void actualizar(Empleado empleado, Connection conn) throws SQLException {
        String sql = "UPDATE empleados SET nombre = ?, apellido = ?, dni = ?, email = ?, fechaIngreso = ?, area = ?, legajo_id = ? " +
                     "WHERE id = ? AND eliminado = false";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getApellido());
            pstmt.setString(3, empleado.getDni());
            pstmt.setString(4, empleado.getEmail());
            pstmt.setDate(5, java.sql.Date.valueOf(empleado.getFechaIngreso()));
            pstmt.setString(6, empleado.getArea());
            
            
            if (empleado.getLegajo() != null) {
                //obtenemos el id
                pstmt.setLong(7, empleado.getLegajo().getId());
            } else {
                // Significa que este empleado no tiene legajo
                pstmt.setNull(7, java.sql.Types.BIGINT);
            }

            pstmt.setLong(8, empleado.getId());

            pstmt.executeUpdate();
        }
    }
    
    
    @Override
    public void eliminar(long id, Connection conn) throws SQLException {
        

        String sql = "UPDATE empleados SET eliminado = true WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }
    
    
    public Empleado buscarPorDni(String dni, Connection conn) throws SQLException {
        Empleado empleado = null;

        String sql = "SELECT * FROM empleados WHERE dni = ? AND eliminado = false";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    empleado = new Empleado();
                    empleado.setId(rs.getLong("id"));
                    empleado.setNombre(rs.getString("nombre"));
                    empleado.setApellido(rs.getString("apellido"));
                    empleado.setDni(rs.getString("dni"));
                    empleado.setEmail(rs.getString("email"));
                    empleado.setFechaIngreso(rs.getDate("fechaIngreso").toLocalDate());
                    empleado.setArea(rs.getString("area"));
                    empleado.setEliminado(rs.getBoolean("eliminado"));

                    long legajoId = rs.getLong("legajo_id");
                    if (!rs.wasNull()) {
                        Legajo legajo = new Legajo();
                        legajo.setId(legajoId);
                        // Opcional: podés traer más datos del legajo con otro SELECT si lo necesitás
                        empleado.setLegajo(legajo);
                    }
                }
            }
        }
        return empleado;
    }
        
}
    


