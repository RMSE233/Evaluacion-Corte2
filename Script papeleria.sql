-- 1. Crear la base de datos ejecutando por separado: CREATE DATABASE papeleria_campus;
-- 2. Conectarse a la base de datos 'papeleria_campus' antes de ejecutar este script.

DROP TABLE IF EXISTS producto;

CREATE TABLE producto (
    id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(80) NOT NULL,
    categoria VARCHAR(20) NOT NULL,
    precio NUMERIC(10,2) NOT NULL,
    stock INTEGER NOT NULL,
    
    -- 5 Restricciones CHECK exigidas por la rúbrica (Criterio C1)
    CONSTRAINT chk_codigo_normalizado CHECK (
        char_length(btrim(codigo)) BETWEEN 1 AND 10 
        AND codigo = btrim(upper(codigo))
    ),
    CONSTRAINT chk_nombre_longitud CHECK (
        char_length(btrim(nombre)) BETWEEN 3 AND 80
    ),
    CONSTRAINT chk_categoria_valida CHECK (
        categoria IN ('Papelería', 'Tecnología', 'Accesorios')
    ),
    CONSTRAINT chk_precio_positivo CHECK (
        precio > 0 AND precio <= 99999999.99
    ),
    CONSTRAINT chk_stock_no_negativo CHECK (
        stock >= 0
    )
);

-- Limpiar tabla y reiniciar contador de ID a 1
TRUNCATE TABLE producto RESTART IDENTITY;

-- Insertar exactamente los 12 productos obligatorios
INSERT INTO producto (codigo, nombre, categoria, precio, stock) VALUES
('P001', 'Cuaderno universitario', 'Papelería', 65.00, 25),
('P002', 'Cuaderno cuadriculado', 'Papelería', 72.50, 12),
('P003', 'Lapicero azul', 'Papelería', 12.00, 80),
('P004', 'Resma de papel carta', 'Papelería', 185.00, 0),
('T001', 'Memoria USB 32 GB', 'Tecnología', 210.00, 8),
('T002', 'Mouse inalámbrico', 'Tecnología', 320.00, 6),
('T003', 'Teclado USB', 'Tecnología', 450.00, 4),
('T004', 'Cable HDMI', 'Tecnología', 180.00, 0),
('A001', 'Mochila escolar', 'Accesorios', 850.00, 5),
('A002', 'Estuche de lápices', 'Accesorios', 95.00, 18),
('A003', 'Botella reutilizable', 'Accesorios', 240.00, 0),
('A004', 'Regla metálica', 'Accesorios', 45.00, 20);