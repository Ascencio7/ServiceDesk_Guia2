# Ficha Técnica Ejecutiva del Proyecto

## 1. Identificación del Proyecto
* **Nombre de la Plataforma:** ServiceDesk 360
* **Asignatura / Módulo:** Desarrollo de Aplicaciones Multiplataforma (COIDS3504)
* **Entidad Académica:** ITCA-FEPADE — Escuela de Ingeniería en Computación
* **Programa Académico:** Ingeniería de Desarrollo de Software
* **Periodo:** Ciclo VIII – Año 2026
* **Dominio:** Gestión de Servicios de TI (ITSM) y Soporte Técnico Enterprise

---

## 2. Diagnóstico y Contexto Operativo
Actualmente, **TecnoSoporte Centroamérica** gestiona sus operaciones de atención al cliente mediante flujos de trabajo fragmentados: canales informales (mensajería instantánea, llamadas telefónicas) y registros no centralizados en hojas de cálculo. Esta dinámica genera los siguientes cuellos de botella:

* **Pérdida de trazabilidad:** Las incidencias y requerimientos se traspapelan sin un historial estructurado.
* **Falta de visibilidad:** Inexistencia de estados de atención en tiempo real (*SLA* desactualizados).
* **Ausencia de analítica:** La dirección carece de métricas sobre tiempos de respuesta, resolución en primer contacto y productividad del equipo técnico.

**Solución Propuesta:** Implementación de **ServiceDesk 360**, un sistema web centralizado diseñado para homologar el registro de activos, captura de incidencias, asignación de tickets y control de niveles de servicio.

---

## 3. Matriz de Perfiles de Usuario (Stakeholders)

| Perfil / Rol | Atribuciones y Flujos Principales |
| :--- | :--- |
| **Cliente / Solicitante** | Registro de tickets de soporte, consulta de estatus en tiempo real y revisión de historial de atenciones. |
| **Técnico de Soporte** | Diagnóstico de incidentes, actualización de estados de avance, registro de bitácoras y cierre de atenciones. |
| **Administrador / Gerencia** | Control de inventario, asignación de prioridades, gestión de usuarios y consulta de dashboard analítico. |

---

## 4. Arquitectura Modular

* **Módulo I: Gestión de Activos y Clientes**  
  Control de inventario de infraestructura tecnológica y vinculación directa con cuentas corporativas o clientes finales.

* **Módulo II: Mesa de Ayuda (Tickets)**  
  Creación, categorización, escalamiento y asignación automatizada de solicitudes de asistencia.

* **Módulo III: Flujo de Estados y Trazabilidad**  
  Ciclo de vida del ticket (*Pendiente* $\rightarrow$ *En Proceso* $\rightarrow$ *Resuelto* $\rightarrow$ *Cerrado*).

* **Módulo IV: Analítica y Reporte de Gestión**  
  Generación de KPIs, tiempos promedio de atención (MTTR) e indicadores de satisfacción.

---

## 5. Entregables del Sprint Inicial (Semana 1)
* **Infraestructura Base:** Aprovisionamiento del proyecto Java Web con arquitectura Maven y servidor **Apache Tomcat 9**.
* **Estructura del Proyecto:** Despliegue del contexto `/servicedesk360` y organización del árbol de directorios.
* **Interfaz Principal (`index.jsp`):** Portal inicial estilizado con maquetación responsiva e identidad corporativa.
* **Componente de Prueba (`DiagnosticoServlet`):** Servlet para validación de canal de comunicación HTTP, verificación de variables del servidor y respuesta `JSON`/`HTML`.
* **Control de Versiones:** Inicialización del repositorio Git, definición de reglas `.gitignore` y documentación ejecutiva en `README.md`.

---

## 6. Fuera de Alcance (Sprints Posteriores)
* **Capa de Persistencia:** Interacción directa con Base de Datos Relacional mediante JDBC/JPA.
* **Seguridad:** Módulo de autenticación, encriptación de credenciales y control de acceso basado en roles (RBAC).
* **Integraciones:** Servicio de notificaciones SMTP para envío automático de alertas.

---

## 7. Estructura del Equipo de Desarrollo

```text
                    [ LÍDER / BACKEND ]
               Configuración Maven, Servlets & Git
                                |
        +-----------------------+-----------------------+
        |                                               |
[ FRONTEND JSP ]                                [ DISEÑO UI/UX ]
Estructura y vistas                        Hojas de estilo CSS
        |                                               |
        +-----------------------+-----------------------+
                                |
                      [ QA & DOCUMENTACIÓN ]
                       Ficha técnica y pruebas