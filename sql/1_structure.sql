-- 1. CREACIÓN DE LA BASE DE DATOS
CREATE DATABASE IF NOT EXISTS TFI_Programacion2;
USE TFI_Programacion2;

-- 2. TABLA B (Legajo)
-- Se crea primero porque Empleado depende de ella.
CREATE TABLE legajo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(20) NOT NULL UNIQUE,
    fecha_ingreso DATE,
    
    -- Campo para borrado lógico
    eliminado BOOLEAN DEFAULT FALSE 
);

-- 3. TABLA A (Empleado)
CREATE TABLE empleado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    
    -- Relación 1-a-1:
    -- Es la Clave Foránea (FK)
    legajo_id INT NOT NULL,
    
    -- Restricción UNIQUE para asegurar 1-a-1
    -- Un 'legajo_id' solo puede estar en un 'empleado'.
    UNIQUE(legajo_id), 
    
    -- Definición de la Clave Foránea
    FOREIGN KEY (legajo_id) REFERENCES legajo(id),
    
    -- Campo para borrado lógico
    eliminado BOOLEAN DEFAULT FALSE
);