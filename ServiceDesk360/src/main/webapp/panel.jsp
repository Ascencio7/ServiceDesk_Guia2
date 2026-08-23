<%-- 
    Document   : panel
    Author     : Vladimir Ascencio
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Control | ServiceDesk 360</title>
    
    <!-- Bootstrap 5 CSS & Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    
    <!-- CSS Personalizado -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body class="bg-light d-flex flex-column min-vh-100">

    <!-- Navbar del Sistema -->
    <header>
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm py-2">
            <div class="container">
                <!-- Logotipo / Marca -->
                <a class="navbar-brand d-flex align-items-center gap-2 fw-bold" href="#">
                    <i class="bi bi-headset fs-3"></i> ServiceDesk 360
                </a>

                <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarPanel" aria-controls="navbarPanel" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarPanel">
                    <!-- Información del Usuario y Cierre de Sesión -->
                    <div class="ms-auto d-flex align-items-center gap-3 pt-2 pt-lg-0">
                        <div class="d-flex align-items-center gap-2 text-white">
                            <div class="bg-white text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold shadow-sm" style="width: 38px; height: 38px;">
                                <i class="bi bi-person-fill fs-5"></i>
                            </div>
                            <div class="d-flex flex-column leading-tight">
                                <span class="fw-semibold small">
                                    <c:out value="${sessionScope.usuarioAutenticado.nombreCompleto}" default="Usuario" />
                                </span>
                                <span class="badge bg-light text-primary border text-capitalize align-self-start" style="font-size: 0.7rem;">
                                    <c:out value="${sessionScope.usuarioAutenticado.rol}" default="Soporte" />
                                </span>
                            </div>
                        </div>

                        <form action="${pageContext.request.contextPath}/cerrar-sesion" method="post" class="m-0">
                            <button type="submit" class="btn btn-outline-light btn-sm d-flex align-items-center gap-1 rounded-2 px-3">
                                <i class="bi bi-box-arrow-right"></i> Salir
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </nav>
    </header>

    <!-- Contenido del Dashboard -->
    <main class="container my-5">
        
        <!-- Banner de Bienvenida -->
        <div class="card border-0 shadow-sm rounded-4 mb-4 bg-white overflow-hidden">
            <div class="card-body p-4 p-md-5">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <span class="badge bg-primary-subtle text-primary fw-semibold px-3 py-2 rounded-pill mb-2">
                            <i class="bi bi-speedometer2 me-1"></i> Panel General
                        </span>
                        <h1 class="h2 fw-bold text-dark mb-2">
                            Bienvenido, <c:out value="${sessionScope.usuarioAutenticado.nombreCompleto}" />!
                        </h1>
                        <p class="text-secondary mb-0">
                            Correo: <c:out value="${sessionScope.usuarioAutenticado.correo}" />
                            <span class="mx-2">•</span>
                            <i class="bi bi-shield-check me-1"></i> Rol: <strong><c:out value="${sessionScope.usuarioAutenticado.rol}" /></strong>
                        </p>
                    </div>
                    <div class="col-md-4 text-center text-md-end d-none d-md-block">
                        <i class="bi bi-display text-primary opacity-25" style="font-size: 5rem;"></i>
                    </div>
                </div>
            </div>
        </div>

        <!-- Rejilla de Módulos (Tarjetas) -->
        <div class="row g-4">
            
            <!-- Módulo: Solicitudes -->
            <div class="col-md-4">
                <div class="card h-100 border-0 shadow-sm rounded-4 hover-shadow transition-all">
                    <div class="card-body p-4 d-flex flex-column">
                        <div class="d-flex align-items-center justify-content-between mb-3">
                            <div class="bg-primary-subtle text-primary rounded-3 p-3">
                                <i class="bi bi-ticket-detailed-fill fs-3"></i>
                            </div>
                            <span class="badge bg-warning-subtle text-warning border border-warning px-2 py-1 rounded-pill small">En desarrollo</span>
                        </div>
                        <h2 class="h5 fw-bold text-dark mb-2">Solicitudes</h2>
                        <p class="text-secondary small flex-grow-1">
                            Módulo central de tickets e incidencias que se desarrollará progresivamente en las siguientes fases.
                        </p>
                        <button class="btn btn-light text-primary fw-semibold border rounded-3 w-100 mt-3 d-flex align-items-center justify-content-center gap-1" disabled>
                            Acceder <i class="bi bi-arrow-right"></i>
                        </button>
                    </div>
                </div>
            </div>

            <!-- Módulo: Equipos -->
            <div class="col-md-4">
                <div class="card h-100 border-0 shadow-sm rounded-4 hover-shadow transition-all">
                    <div class="card-body p-4 d-flex flex-column">
                        <div class="d-flex align-items-center justify-content-between mb-3">
                            <div class="bg-info-subtle text-info rounded-3 p-3">
                                <i class="bi bi-pc-display-horizontal fs-3"></i>
                            </div>
                            <span class="badge bg-secondary-subtle text-secondary border px-2 py-1 rounded-pill small">Próximamente</span>
                        </div>
                        <h2 class="h5 fw-bold text-dark mb-2">Equipos</h2>
                        <p class="text-secondary small flex-grow-1">
                            Gestión, registro y seguimiento detallado de activos tecnológicos e inventario asignado.
                        </p>
                        <button class="btn btn-light text-secondary fw-semibold border rounded-3 w-100 mt-3 d-flex align-items-center justify-content-center gap-1" disabled>
                            Acceder <i class="bi bi-arrow-right"></i>
                        </button>
                    </div>
                </div>
            </div>

            <!-- Módulo: Reportes -->
            <div class="col-md-4">
                <div class="card h-100 border-0 shadow-sm rounded-4 hover-shadow transition-all">
                    <div class="card-body p-4 d-flex flex-column">
                        <div class="d-flex align-items-center justify-content-between mb-3">
                            <div class="bg-success-subtle text-success rounded-3 p-3">
                                <i class="bi bi-graph-up-arrow fs-3"></i>
                            </div>
                            <span class="badge bg-secondary-subtle text-secondary border px-2 py-1 rounded-pill small">Unidad 5</span>
                        </div>
                        <h2 class="h5 fw-bold text-dark mb-2">Reportes</h2>
                        <p class="text-secondary small flex-grow-1">
                            Métricas, indicadores de rendimiento y analítica visual. Se incorporarán durante la Unidad 5.
                        </p>
                        <button class="btn btn-light text-secondary fw-semibold border rounded-3 w-100 mt-3 d-flex align-items-center justify-content-center gap-1" disabled>
                            Acceder <i class="bi bi-arrow-right"></i>
                        </button>
                    </div>
                </div>
            </div>

        </div>
    </main>

    <!-- Footer -->
    <footer class="footer mt-auto py-3 bg-white border-top text-center text-muted small">
        <div class="container">
            <span>&copy; 2026 ServiceDesk 360 — Sistema de Gestión de Soporte</span>
        </div>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>