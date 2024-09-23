-- Inserción de datos en la tabla clientes
INSERT INTO cliente (nombre, email, direccion, telefono,password)
VALUES
('Juan Pérez', 'juan.perez@example.com', 'Calle Falsa 123, Ciudad', '555-1234','10101011'),
('María García', 'maria.garcia@example.com', 'Avenida Siempre Viva 742, Ciudad', '555-5678','111111111'),
('Pedro Sánchez', 'pedro.sanchez@example.com', 'Calle Luna 456, Ciudad', '555-9876','30303030');

-- Inserción de datos en la tabla productos
INSERT INTO producto (nombre, descripcion, precio, stock)
VALUES
('Laptop', 'Laptop de alta gama con 16GB de RAM y 512GB SSD', 1200.00, 50),
('Smartphone', 'Smartphone con pantalla de 6.5 pulgadas y 128GB de almacenamiento', 600.00, 200),
('Monitor', 'Monitor 4K de 27 pulgadas', 300.00, 100),
('Teclado', 'Teclado mecánico retroiluminado', 80.00, 150);
