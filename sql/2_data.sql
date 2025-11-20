-- Insertar datos de prueba
USE TFI_Programacion2;

-- Legajos
INSERT INTO legajo (numero, fecha_ingreso) VALUES 
('L-1001', '2023-03-15'),
('L-1002', '2024-01-10');

-- Empleados (Asociados a los legajos)
INSERT INTO empleado (nombre, apellido, legajo_id) VALUES 
('Juan', 'Perez', 1), -- Juan tiene el legajo 1
('Maria', 'Gomez', 2); -- Maria tiene el legajo 2

-- (Prueba de restricción 1-a-1)
-- Esta línea fallaría, lo cual es correcto.
-- INSERT INTO empleado (nombre, apellido, legajo_id) VALUES ('Carlos', 'Lopez', 1);