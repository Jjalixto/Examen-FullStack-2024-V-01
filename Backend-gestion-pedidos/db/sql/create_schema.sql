-- Creación de la tabla cliente
CREATE TABLE IF NOT EXISTS cliente (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    password VARCHAR(50) NOT NULL
);

-- Creación de la tabla producto
CREATE TABLE IF NOT EXISTS producto (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL
);  