# Bitácora guía 5: JDBC y MySQL

## Decisiones

- Se usa MySQL Connector/J desde Maven y una cuenta `servicedesk_app` con permisos limitados.
- La configuración se carga desde `%USERPROFILE%\\.servicedesk360\\db.properties`; no se guarda ninguna clave en Java ni Git.
- Las consultas usan `PreparedStatement` y todos los recursos JDBC usan `try-with-resources`.
- El alta de ticket y su primer seguimiento comparten una transacción; un fallo ejecuta `rollback`.

## Evidencias y resultados esperados

| Prueba | Resultado esperado |
| --- | --- |
| Crear esquema y datos | Se crean seis tablas y datos relacionados sin errores. |
| Buscar cliente por correo | Devuelve solo la coincidencia parametrizada. |
| Insertar cliente | Devuelve un id `AUTO_INCREMENT` positivo. |
| Insertar referencia inexistente | MySQL rechaza la clave foránea. |
| Registrar ticket y seguimiento | `commit` confirma las dos filas. |
| Forzar categoría inexistente | `rollback` deja cero filas parciales. |
| Reiniciar MySQL/Tomcat | Los registros siguen en MySQL. |
| Credenciales inválidas o servidor detenido | Se registra el diagnóstico técnico sin exponer la contraseña. |

## Frontera con la guía 6

`LaboratorioJDBC`, `ClienteRegistro` y `TicketVista` son estructuras exploratorias.
La guía 6 debe extraer interfaces DAO, mapear las entidades persistentes y sustituir
los directorios en memoria del listener. La primera entidad recomendada para CRUD
completo es `tickets`, porque concentra relaciones con clientes, equipos, técnicos,
categorías y seguimientos.