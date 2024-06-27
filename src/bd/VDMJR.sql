DROP DATABASE IF EXISTS VDMJR;
CREATE DATABASE VDMJR;
USE VDMJR;

CREATE TABLE empleados (
    idEmpleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellidoPaterno VARCHAR(255) NOT NULL,
    apellidoMaterno VARCHAR(255) NOT NULL,
    fechaIngreso DATE NOT NULL,
    fechaSalida DATE,
    curp VARCHAR(18) NOT NULL,
    domicilio VARCHAR(255) NOT NULL,
    telefono VARCHAR(15),
    email VARCHAR(255),  -- Campo de correo electrónico añadido
    edad INT NOT NULL,
    estatus TINYINT(1) NOT NULL DEFAULT 1,  -- 1 para activo, 0 para inactivo
    rol VARCHAR(50) NOT NULL
);


CREATE TABLE prestamos (
    idPrestamo INT AUTO_INCREMENT PRIMARY KEY,
    idEmpleado INT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fechaPrestamo DATE NOT NULL,
    fechaLimite DATE NOT NULL,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado)
);


CREATE TABLE usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombreUsuario VARCHAR(255) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
);

CREATE TABLE nominas (
    idNomina INT AUTO_INCREMENT PRIMARY KEY,
    idEmpleado INT NOT NULL,
    fecha DATE NOT NULL,
    sueldoBase DECIMAL(10, 2) NOT NULL,
    imss DECIMAL(10, 2) DEFAULT 0,
    retardos DECIMAL(10, 2) DEFAULT 0,
    multas DECIMAL(10, 2) DEFAULT 0,
    platosRotos DECIMAL(10, 2) DEFAULT 0,
    otros DECIMAL(10, 2) DEFAULT 0,
    totalPagar DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado)
);

INSERT INTO usuario (nombreUsuario, contrasena, rol) VALUES ('jorge', '1234', 'patron');

ALTER TABLE prestamos DROP COLUMN fechaLimite;

SELECT * FROM empleados;
SELECT * FROM prestamos;

SELECT 
    p.idPrestamo, 
    p.idEmpleado, 
    p.monto, 
    p.fechaPrestamo, 
    e.nombre, 
    e.apellidoPaterno, 
    e.apellidoMaterno, 
    e.rol
FROM prestamos p
JOIN empleados e ON p.idEmpleado = e.idEmpleado;

CREATE TABLE abonos (
    idAbono INT AUTO_INCREMENT PRIMARY KEY,
    idPrestamo INT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fechaAbono DATE NOT NULL,
    tipo ENUM('abono', 'liquidacion') NOT NULL,
    FOREIGN KEY (idPrestamo) REFERENCES prestamos(idPrestamo)
);

ALTER TABLE abonos
DROP FOREIGN KEY abonos_ibfk_1;

ALTER TABLE abonos
ADD CONSTRAINT abonos_ibfk_1 FOREIGN KEY (idPrestamo) REFERENCES prestamos(idPrestamo) ON DELETE CASCADE;

SELECT * FROM nominas;

SELECT * FROM abonos;

select * from empleados;
