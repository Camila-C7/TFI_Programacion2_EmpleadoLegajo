package entities;

import java.time.LocalDate;


public class Empleado {
    
    private long id;
    private boolean eliminado;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private LocalDate fechaingreso;
    private String area;
    private Legajo legajo;
    
    
    // Constructores

    public Empleado() {}

    public Empleado(long id, boolean eliminado, String nombre, String apellido, String dni, String email, LocalDate fechaingreso, String area, Legajo legajo) {
        this.id = id;
        this.eliminado = eliminado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaingreso = fechaingreso;
        this.area = area;
        this.legajo = legajo;
    }

    public Empleado(String nombre, String apellido, String dni, String email, LocalDate fechaingreso, String area, Legajo legajo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaingreso = fechaingreso;
        this.area = area;
        this.legajo = legajo;
    }
    
    // Getters

    public long getId() {return id;}

    public boolean isEliminado() {return eliminado;}

    public String getNombre() {return nombre;}

    public String getApellido() {return apellido;}

    public String getDni() {return dni;}

    public String getEmail() {return email;}

    public LocalDate getFechaIngreso() {return fechaingreso;}

    public String getArea() {return area;}

    public Legajo getLegajo() {return legajo;}
    
    
    // Setters

    public void setId(long id) {this.id = id;}

    public void setEliminado(boolean eliminado) {this.eliminado = eliminado;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public void setDni(String dni) {this.dni = dni;}

    public void setEmail(String email) {this.email = email;}

    public void setFechaIngreso(LocalDate fechaingreso) {this.fechaingreso = fechaingreso;}

    public void setArea(String area) {this.area = area;}

    public void setLegajo(Legajo legajo) {this.legajo = legajo;}
    
    
    // toString

    @Override
    public String toString() {
        return "Empleado{" + "id=" + id + ", eliminado=" + eliminado + ", nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni + ", email=" + email + ", fechaingreso=" + fechaingreso + ", area=" + area + ", legajo=" + legajo + '}';
    }
    
    
    
    
}
