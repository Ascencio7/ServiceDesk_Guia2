<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tickets | ServiceDesk 360</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body class="bg-light">
<main class="container py-5">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
        <div>
            <h1 class="h3 mb-1">Tickets de soporte</h1>
            <p class="text-secondary mb-0">Solicitudes registradas en la sesión de trabajo.</p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/panel" class="btn btn-outline-secondary">Panel</a>
            <a href="${pageContext.request.contextPath}/tickets/nuevo" class="btn btn-primary">Nuevo ticket</a>
        </div>
    </div>
    <c:if test="${not empty mensajeExito}"><div class="alert alert-success"><c:out value="${mensajeExito}" /></div></c:if>
    <div class="card border-0 shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light"><tr><th>ID</th><th>Título</th><th>Solicitante</th><th>Prioridad</th><th>Estado</th></tr></thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty tickets}"><tr><td colspan="5" class="text-center text-secondary py-4">Aún no hay tickets registrados.</td></tr></c:when>
                        <c:otherwise>
                            <c:forEach var="ticket" items="${tickets}">
                                <tr>
                                    <td><c:out value="${ticket.id}" /></td>
                                    <td><c:out value="${ticket.titulo}" /></td>
                                    <td><c:out value="${ticket.solicitante.nombreCompleto}" /></td>
                                    <td><c:out value="${ticket.prioridad}" /></td>
                                    <td><c:out value="${ticket.estado}" /></td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>
</body>
</html>
