package main;

import service.EmpleadoService;
import service.LegajoService;
import entities.Empleado;
import entities.Legajo;
import entities.EstadoLegajo;

import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        EmpleadoService empleadoService = new EmpleadoService();
        LegajoService legajoService = new LegajoService();

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("====== MENU PRINCIPAL ======");
            System.out.println("1. Crear empleado + legajo");
            System.out.println("2. Listar empleados");
            System.out.println("3. Buscar empleado por ID");
            System.out.println("4. Buscar empleado por DNI");
            System.out.println("5. Actualizar empleado");
            System.out.println("6. Eliminar empleado (baja logica)");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            opcion = Integer.parseInt(sc.nextLine());

            try {

                switch (opcion) {
                    case 1:
                        // CREAR EMPLEADO + LEGAJO
                        System.out.println("Nombre: ");
                        String nombre = sc.nextLine();
                        System.out.println("Apellido: ");
                        String apellido = sc.nextLine();
                        System.out.println("DNI: ");
                        String dni = sc.nextLine();
                        System.out.println("Fecha ingreso (AAAA-MM-DD): ");
                        String fecha = sc.nextLine();
                        System.out.println("Area: ");
                        String area = sc.nextLine();

                        System.out.println("Nro Legajo: ");
                        String nroLeg = sc.nextLine();
                        System.out.println("Categoria: ");
                        String categoria = sc.nextLine();
                        System.out.println("Estado (ACTIVO/INACTIVO): ");
                        EstadoLegajo estado = EstadoLegajo.valueOf(sc.nextLine().toUpperCase());
                        System.out.println("Fecha alta legajo (AAAA-MM-DD): ");
                        String fechaAlta = sc.nextLine();

                        Legajo leg = new Legajo();
                        leg.setNroLegajo(nroLeg);
                        leg.setCategoria(categoria);
                        leg.setEstado(estado);
                        leg.setFechaAlta(java.time.LocalDate.parse(fechaAlta));

                        Empleado emp = new Empleado();
                        emp.setNombre(nombre);
                        emp.setApellido(apellido);
                        emp.setDni(dni);
                        emp.setFechaIngreso(java.time.LocalDate.parse(fecha));
                        emp.setArea(area);
                        emp.setLegajo(leg);

                        empleadoService.insertar(emp);
                        System.out.println("Empleado creado exitosamente!");
                        break;

                    case 2:
                        // LISTAR
                        empleadoService.getAll().forEach(System.out::println);
                        break;

                    case 3:
                        // BUSCAR POR ID
                        System.out.println("ID empleado: ");
                        long id = Long.parseLong(sc.nextLine());
                        System.out.println(empleadoService.getById(id));
                        break;

                    case 4:
                        // BUSCAR POR DNI (usando getAll)
                        System.out.println("DNI: ");
                        String buscarDni = sc.nextLine();
                            
                        Empleado encontrado = null;
                            for (Empleado em : empleadoService.getAll()) {
                                if (em.getDni() != null && em.getDni().equalsIgnoreCase(buscarDni)) {
                                    encontrado = em;
                                    break;
                                }
                            }

                            if (encontrado != null) {
                                System.out.println(encontrado);
                            } else {
                                System.out.println("No se encontró ningún empleado con ese DNI.");
                            }
                            break;


                    case 5:
                        // ACTUALIZAR
                        System.out.println("ID del empleado a actualizar: ");
                        long idUpd = Long.parseLong(sc.nextLine());

                        Empleado e = empleadoService.getById(idUpd);
                        if (e == null) {
                            System.out.println("Empleado no encontrado");
                            break;
                        }

                        System.out.println("Nuevo apellido: ");
                        e.setApellido(sc.nextLine());

                        empleadoService.actualizar(e);
                        System.out.println("Empleado actualizado!");
                        break;

                    case 6:
                        // BAJA LOGICA
                        System.out.println("ID de empleado: ");
                        long idDel = Long.parseLong(sc.nextLine());
                        empleadoService.eliminar(idDel);
                        System.out.println("Empleado eliminado logicamente.");
                        break;

                    case 0:
                        System.out.println("Saliendo...");
                        break;

                    default:
                        System.out.println("Opcion invalida");
                }

            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }

        }
    }
}

Agrego main.java con el menú principal
    
