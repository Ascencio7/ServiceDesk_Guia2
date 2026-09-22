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
            <p class="text-secondary mb-0">
                <c:choose>
                    <c:when test="${vistaPropia}">Solicitudes que usted ha registrado.</c:when>
                    <c:otherwise>Solicitudes registradas para atención de soporte.</c:otherwise>
                </c:choose>
            </p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/panel" class="btn btn-outline-secondary">Panel</a>
            <a href="${pageContext.request.contextPath}/tickets/nuevo" class="btn btn-primary">Nueva solicitud</a>
        </div>
    </div>
    <c:if test="${not empty mensajeExito}"><div class="alert alert-success"><c:out value="${mensajeExito}" /></div></c:if>
    <form id="formFiltrosTickets" method="get" action="${pageContext.request.contextPath}/tickets" class="row g-2 mb-4">
        <div class="col-md-3"><select name="estadoFiltro" class="form-select" onchange="this.form.submit()"><option value="">Todos los estados</option><option value="ABIERTO" ${estadoFiltro == 'ABIERTO' ? 'selected' : ''}>Abierto</option><option value="EN_PROCESO" ${estadoFiltro == 'EN_PROCESO' ? 'selected' : ''}>En proceso</option><option value="CERRADO" ${estadoFiltro == 'CERRADO' ? 'selected' : ''}>Cerrado</option></select></div>
        <div class="col-md-3"><select name="prioridadFiltro" class="form-select" onchange="this.form.submit()"><option value="">Todas las prioridades</option><option value="BAJA" ${prioridadFiltro == 'BAJA' ? 'selected' : ''}>Baja</option><option value="MEDIA" ${prioridadFiltro == 'MEDIA' ? 'selected' : ''}>Media</option><option value="ALTA" ${prioridadFiltro == 'ALTA' ? 'selected' : ''}>Alta</option><option value="CRITICA" ${prioridadFiltro == 'CRITICA' ? 'selected' : ''}>Crítica</option></select></div>
        <div class="col-md-3"><button type="submit" class="btn btn-outline-primary">Filtrar</button></div>
        <div class="col-md-3"><a href="${pageContext.request.contextPath}/tickets" class="btn btn-outline-secondary">Limpiar filtros</a></div>
    </form>
    <div class="card border-0 shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light"><tr><th>ID</th><th>Título</th><th>Cliente</th><th>Equipo</th><th>Categoría</th><th>Técnico</th><th>Prioridad</th><th>Estado</th><th>Acciones</th></tr></thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty ticketsDetalle}"><tr><td colspan="9" class="text-center text-secondary py-4">Aún no hay tickets registrados.</td></c:when>
                        <c:otherwise>
                            <c:forEach var="ticket" items="${ticketsDetalle}">
                                <tr>
                                    <td><c:out value="${ticket.idTicket}" /></td>
                                    <td><c:out value="${ticket.titulo}" /></td>
                                    <td><c:out value="${ticket.cliente}" /></td>
                                    <td><c:out value="${ticket.equipo}" /></td>
                                    <td><c:out value="${ticket.categoria}" /></td>
                                    <td><c:out value="${ticket.tecnico}" /></td>
                                    <td><c:out value="${ticket.prioridad}" /></td>
                                    <td><c:out value="${ticket.estado}" /></td>
                                    <td>
                                        <c:if test="${not vistaPropia}">
                                            <form method="post" action="${pageContext.request.contextPath}/tickets/actualizar" class="d-inline-flex gap-1">
                                                <input type="hidden" name="id" value="<c:out value='${ticket.idTicket}' />">
                                                <input type="hidden" name="estadoEsperado" value="<c:out value='${ticket.estado}' />">
                                                <select name="estado" class="form-select form-select-sm">
                                                    <option value="ABIERTO">Abierto</option>
                                                    <option value="ASIGNADO">Asignado</option>
                                                    <option value="EN_PROCESO">En proceso</option>
                                                    <option value="CERRADO">Cerrado</option>
                                                </select>
                                                <button type="submit" class="btn btn-sm btn-outline-primary">Actualizar</button>
                                            </form>
                                            <form method="post" action="${pageContext.request.contextPath}/tickets/eliminar" class="d-inline">
                                                <input type="hidden" name="id" value="<c:out value='${ticket.idTicket}' />">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">Eliminar</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
    <c:if test="${totalPaginas > 1}">
        <nav class="mt-4" aria-label="Paginación de tickets"><ul class="pagination">
            <c:forEach var="numero" begin="1" end="${totalPaginas}"><li class="page-item ${numero == paginaActual ? 'active' : ''}"><a class="page-link" href="${pageContext.request.contextPath}/tickets?estadoFiltro=${estadoFiltro}&prioridadFiltro=${prioridadFiltro}&pagina=${numero}">${numero}</a></li></c:forEach>
        </ul></nav>
    </c:if>
</main>
</body>
</html>
