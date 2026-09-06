<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel | ServiceDesk 360</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body class="bg-light d-flex flex-column min-vh-100">
<header class="navbar navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/panel">
            <i class="bi bi-headset me-2"></i>ServiceDesk 360
        </a>
        <div class="d-flex align-items-center gap-3 text-white">
            <span class="small"><c:out value="${sessionScope.usuarioAutenticado.nombreCompleto}" /></span>
            <form action="${pageContext.request.contextPath}/cerrar-sesion" method="post">
                <button class="btn btn-outline-light btn-sm" type="submit">
                    <i class="bi bi-box-arrow-right me-1"></i>Salir
                </button>
            </form>
        </div>
    </div>
</header>
<main class="container py-5">
    <section class="card border-0 shadow-sm mb-4">
        <div class="card-body p-4">
            <p class="text-primary fw-semibold mb-2">Panel general</p>
            <h1 class="h2">Bienvenido, <c:out value="${sessionScope.usuarioAutenticado.nombreCompleto}" />.</h1>
            <p class="text-secondary mb-0">
                <c:out value="${sessionScope.usuarioAutenticado.correo}" />
                <span class="mx-2">|</span>
                Rol: <c:out value="${sessionScope.usuarioAutenticado.rol}" />
            </p>
        </div>
    </section>
    <section class="row g-4">
        <div class="col-md-6">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body p-4 d-flex flex-column">
                    <i class="bi bi-ticket-detailed text-primary fs-1 mb-3"></i>
                    <h2 class="h4">Tickets de soporte</h2>
                    <p class="text-secondary flex-grow-1">Registra y consulta las solicitudes de soporte técnico.</p>
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/tickets">
                        <i class="bi bi-arrow-right me-1"></i>Gestionar tickets
                    </a>
                </div>
            </div>
        </div>
    </section>
</main>
<footer class="mt-auto py-3 bg-white border-top text-center text-muted small">ServiceDesk 360</footer>
</body>
</html>
