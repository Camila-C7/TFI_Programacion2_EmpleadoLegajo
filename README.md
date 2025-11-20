### Trabajo Final Integrador – Programación II
_Tecnicatura universitaria en Programacion - UTN_

Aplicación Java con relación 1→1 (Empleado → Legajo), utilizando JDBC, el patrón DAO, servicios con transacciones, validaciones y menú de consola. Incluye UML, SQL y documentación completa.

_____________________________________________________________________________________________________________________________________________________

**Estructura del proyecto:**
/src
    /config
    /dao
    /entities
    /service
    /main
/sql
    1_structure.sql
    2_data.sql
/nbproject

**Tecnologías utilizadas:**
- Java 17
- JDBC
- MySQL
- Patrón DAO
- Arquitectura por capas
- Manejo de transacciones (commit/rollback)
- Validaciones y reglas de negocio

**Funcionalidades principales:**
- CRUD de Empleado
- CRUD de Legajo
- Relación 1→1 unidireccional
- Validaciones de negocio (DNI único, legajo único, campos obligatorios)
- Baja lógica
- Manejo de errores con BusinessException
- Menú de consola intuitivo

**Requisitos de ejecución:**
- Java 17 (o superior)
- MySQL Server en ejecución
- Driver JDBC de MySQL agregado al proyecto

**Creación de la base de datos:**
1. Abrir MySQL Workbench.
2. Ejecutar `sql/1_structure.sql`.
3. Ejecutar `sql/2_data.sql`.

**Cómo ejecutar la aplicación:**
1. Abrir el proyecto en NetBeans.
2. Verificar credenciales en `DatabaseConnection.java`.
3. Ejecutar `src/main/main.java`.
4. Usar el menú para crear, listar, buscar, actualizar y eliminar empleados/legajos.

**Video de presentación:**
> -


**Integrantes:**
- ALEJO TOMAS OLIVA COCA
- CAMILA CASTAÑO
- CHRISTIAN FERNANDO ORMACHEA
- ENZO MEDINA
