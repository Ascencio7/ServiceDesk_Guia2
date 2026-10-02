# Bitácora guía 8: JSF y Managed Beans

## Decisiones de migración

- El proyecto conserva Java EE 8 y el espacio de nombres `javax.*`; no mezcla imports
  `jakarta.*`.
- Mojarra 2.3 procesa las vistas XHTML y Weld habilita CDI en Tomcat 9. `FacesServlet`
  se mapea a `*.xhtml`.
- Las vistas JSF requieren una sesión autenticada, igual que las rutas Servlet existentes.
- El repositorio de esta entrega no contenía un CRUD `ClienteDAO` previo, aunque la tabla
  `clientes` y los catálogos JDBC sí existían. Se agregó un DAO pequeño sobre la tabla
  actual, sin modificar el esquema ni duplicar las relaciones de tickets.
- `ClienteBean` y `TicketBean` son beans CDI `@Named`, `@ViewScoped` y `Serializable`.
  Mantienen datos de presentación, mientras los servicios/DAO abren y cierran sus propias
  conexiones.
- Los mensajes de éxito usan flash junto con redirección para conservar el patrón
  Post/Redirect/Get. La baja es lógica (`activo = FALSE`) y no elimina clientes que ya
  estén referenciados por tickets o equipos.
- La vista JSF de Tickets reutiliza `ServicioTickets.listarDetalle` y
  `TicketDetalleDTO`; no contiene SQL ni altera el Servlet `/tickets`.

## Comparación Servlet vs. JSF

| Responsabilidad | Servlet/JSP existente | Vista JSF |
| --- | --- | --- |
| Leer datos | `request.getParameter(...)` | `value="#{clienteBean.cliente.nombre}"` |
| Exponer el listado | `request.setAttribute(...)` y forward | `#{clienteBean.clientes}` |
| Renderizar filas | JSP/JSTL | `h:dataTable` y Expression Language |
| Guardar y navegar | Servlet, redirect/forward | Acción del bean y outcome con redirect |
| Mensajes | Atributos de request | `FacesMessage` y flash scope |
| Acceso a datos | Servicio/DAO existente | El mismo servicio/DAO; sin SQL en el bean |

## Ciclo de vida a observar

| Acción | Punto que debe observarse | Resultado esperado |
| --- | --- | --- |
| Abrir listado | `ClienteBean.init()` (`@PostConstruct`) | Se carga la lista una vez al crear la vista |
| Abrir formulario nuevo | `ClienteBean.init()` y `prepararFormulario()` | El objeto de formulario queda inicializado |
| Editar un cliente | `f:viewParam` y `prepararFormulario()` | Se carga el registro activo solicitado |
| Guardar formulario válido | actualización de propiedades y luego `guardar()` | El servicio valida y el DAO persiste |
| Enviar campos requeridos vacíos | validación JSF antes de la acción | `guardar()` no se ejecuta; se muestran errores |
| Desactivar cliente | acción `desactivar()` | Se actualiza el indicador activo y se redirige |

## Matriz de verificación

| Verificación | Resultado esperado | Estado |
| --- | --- | --- |
| Empaquetado Maven | `mvn package` genera el WAR con JSF y CDI | Ejecutado correctamente |
| XHTML procesado | Las tres vistas se renderizan mediante `FacesServlet` | Pendiente de validar en Tomcat 9 |
| Autenticación | Acceso anónimo redirige a `/acceso` | Pendiente de validar en Tomcat 9 |
| Listado de clientes | Datos reales, estado activo y controles de acción visibles | Pendiente de validar con MySQL |
| Crear y editar cliente | Inserta/actualiza; correo duplicado produce mensaje | Pendiente de validar con MySQL |
| Desactivación lógica | El cliente queda inactivo y sus relaciones permanecen | Pendiente de validar con MySQL |
| PRG y mensajes | Guardar redirige y muestra confirmación sin repetir el POST | Pendiente de validar en Tomcat 9 |
| Listado JSF de tickets | Muestra cliente, equipo, categoría, técnico, prioridad y estado | Pendiente de validar con MySQL |
| Persistencia tras reinicio | Los datos continúan en MySQL | Pendiente de validar con MySQL |

Para cerrar la evidencia práctica: ejecute `mvn clean package`, despliegue en Tomcat 9,
inicie sesión, realice cada operación con datos de prueba y complete los estados de la
matriz con los resultados observados. No se registran como ejecutadas pruebas que
requieren un servidor MySQL/Tomcat disponible.
