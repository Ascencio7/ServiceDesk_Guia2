USE servicedesk360_db;
INSERT IGNORE INTO clientes(nombre, correo) VALUES ('Ana Lopez', 'ana.lopez@demo.local'), ('Carlos Mendez', 'carlos.mendez@demo.local');
INSERT IGNORE INTO categorias(nombre) VALUES ('Software'), ('Hardware'), ('Acceso');
INSERT IGNORE INTO tecnicos(nombre, correo, especialidad) VALUES ('Maria Ruiz', 'maria.ruiz@demo.local', 'Aplicaciones web');
INSERT IGNORE INTO equipos(id_cliente, codigo_inventario, tipo, marca, modelo) SELECT id_cliente, 'EQ-001', 'Laptop', 'Dell', 'Latitude' FROM clientes WHERE correo = 'ana.lopez@demo.local';
