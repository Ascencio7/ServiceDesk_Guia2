<%-- 
    Document   : index
    Created on : 08 aug 2026, 2:21:42 p. m.
    Author     : Vladimir Ascencio
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat, java.util.Date" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ServiceDesk 360 | Dashboard Principal</title>
    
    <!-- Framework CSS Bootstrap 5 y CDN de FontAwesome para iconos -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    
    <!-- CSS Personalizado -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body class="bg-light d-flex flex-column min-vh-100">

    <header>
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
            <div class="container">
                <a class="navbar-brand d-flex align-items-center gap-2 fw-bold" href="#">
                    <i class="bi bi-headset fs-3"></i> ServiceDesk 360
                </a>
                <span class="navbar-text text-white-50 d-none d-md-inline small">
                    Plataforma empresarial de gestión de soporte
                </span>
            </div>
        </nav>
    </header>

    <!-- Contenido Principal -->
    <main class="container my-auto py-5">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                
                <!-- Card de Bienvenida -->
                <div class="card shadow-lg border-0 rounded-4 overflow-hidden mb-4">
                    <div class="card-body p-4 p-md-5">
                        
                        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 gap-2">
                            <span class="badge bg-success-subtle text-success border border-success px-3 py-2 rounded-pill">
                                <i class="bi bi-check-circle-fill me-1"></i> Tomcat Activo
                            </span>
                            <small class="text-muted">
                                <% 
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    out.print("Servidor activo: " + sdf.format(new Date()));
                                %>
                            </small>
                        </div>

                        <h1 class="card-title h2 fw-bold text-dark mb-3">
                            Primera aplicación web desplegada
                        </h1>
                        <p class="card-text text-secondary fs-5 mb-4">
                            Confirmación de despliegue correcto del proyecto Java Web JSP en el servidor de aplicaciones <strong>Apache Tomcat</strong>.
                        </p>

                        <hr class="my-4 text-secondary opacity-25">

                        <!-- Checklist de Objetivos -->
                        <h2 class="h5 fw-bold mb-3 text-primary">
                            <i class="bi bi-list-check me-2"></i>Objetivos de la Semana 1
                        </h2>
                        
                        <div class="row g-3 mb-4">
                            <div class="col-md-6">
                                <div class="d-flex align-items-start gap-2 bg-light p-3 rounded-3 border-start border-4 border-primary">
                                    <i class="bi bi-gear-fill text-primary mt-1"></i>
                                    <div>
                                        <strong>Configuración:</strong> Entorno Java Web (JDK, IDE, servidor).
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="d-flex align-items-start gap-2 bg-light p-3 rounded-3 border-start border-4 border-primary">
                                    <i class="bi bi-diagram-3-fill text-primary mt-1"></i>
                                    <div>
                                        <strong>Arquitectura:</strong> Reconocimiento del modelo cliente-servidor.
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="d-flex align-items-start gap-2 bg-light p-3 rounded-3 border-start border-4 border-primary">
                                    <i class="bi bi-rocket-takeoff-fill text-primary mt-1"></i>
                                    <div>
                                        <strong>Despliegue:</strong> Puesta en marcha de la aplicación inicial.
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="d-flex align-items-start gap-2 bg-light p-3 rounded-3 border-start border-4 border-primary">
                                    <i class="bi bi-journal-code text-primary mt-1"></i>
                                    <div>
                                        <strong>Documentación:</strong> Registro técnico del proyecto integrador.
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Botones de Acción -->
                        <div class="d-flex flex-wrap gap-2 pt-2">
                            <a href="${pageContext.request.contextPath}/acceso" class="btn btn-primary btn-lg px-4">
                                <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar acceso
                            </a>
                            <a href="${pageContext.request.contextPath}/registro" class="btn btn-outline-secondary btn-lg px-4">
                                <i class="bi bi-person-plus me-2"></i>Crear cuenta temporal
                            </a>
                            <a href="${pageContext.request.contextPath}/diagnostico" class="btn btn-light text-secondary border btn-lg px-4">
                                <i class="bi bi-cpu me-2"></i>Diagnóstico del sistema
                            </a>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    </main>

    <!-- Footer -->
    <footer class="footer mt-auto py-3 bg-white border-top text-center text-muted small">
        <div class="container">
            <span>&copy; 2026 ServiceDesk 360 — Sistema de Soporte Técnico</span>
        </div>
    </footer>

    <!-- Bootstrap Script -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>