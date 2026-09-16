USE servicedesk360_db;

CREATE TABLE IF NOT EXISTS usuarios (
  id_usuario BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  correo VARCHAR(160) NOT NULL UNIQUE,
  hash_clave VARCHAR(200) NOT NULL,
  rol VARCHAR(20) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT chk_usuario_rol CHECK (rol IN ('SOLICITANTE','TECNICO','ADMINISTRADOR'))
);

SELECT id_usuario, nombre, correo, rol, activo FROM usuarios;