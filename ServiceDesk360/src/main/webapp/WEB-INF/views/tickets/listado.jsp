<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tickets | ServiceDesk 360</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body class="bg-light">
<main class="container py-5">
    <!-- Cabecera de la sección -->
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
        <div>
            <h1 class="h3 mb-1 fw-bold text-dark">Tickets de soporte</h1>
            <p class="text-secondary mb-0">
                <c:choose>
                    <c:when test="${vistaPropia}">Solicitudes que usted ha registrado.</c:when>
                    <c:otherwise>Solicitudes registradas para atención de soporte.</c:otherwise>
                </c:choose>
            </p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/panel" class="btn btn-outline-secondary px-3">
                <i class="bi bi-arrow-left me-1"></i> Panel
            </a>
            <a href="${pageContext.request.contextPath}/tickets/nuevo" class="btn btn-primary px-3 shadow-sm">
                <i class="bi bi-plus-lg me-1"></i> Nueva solicitud
            </a>
        </div>
    </div>

    <!-- Alertas de éxito -->
    <c:if test="${not empty mensajeExito}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i><c:out value="${mensajeExito}" />
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Barra de Filtros -->
    <div class="card border-0 shadow-sm mb-4 p-3 bg-white rounded-4">
        <form id="formFiltrosTickets" method="get" action="${pageContext.request.contextPath}/tickets" class="row g-3 align-items-center">
            <div class="col-md-3">
                <select name="estadoFiltro" class="form-select" onchange="this.form.submit()">
                    <option value="">Todos los estados</option>
                    <option value="ABIERTO" ${estadoFiltro == 'ABIERTO' ? 'selected' : ''}>Abierto</option>
                    <option value="EN_PROCESO" ${estadoFiltro == 'EN_PROCESO' ? 'selected' : ''}>En proceso</option>
                    <option value="CERRADO" ${estadoFiltro == 'CERRADO' ? 'selected' : ''}>Cerrado</option>
                </select>
            </div>
            <div class="col-md-3">
                <select name="prioridadFiltro" class="form-select" onchange="this.form.submit()">
                    <option value="">Todas las prioridades</option>
                    <option value="BAJA" ${prioridadFiltro == 'BAJA' ? 'selected' : ''}>Baja</option>
                    <option value="MEDIA" ${prioridadFiltro == 'MEDIA' ? 'selected' : ''}>Media</option>
                    <option value="ALTA" ${prioridadFiltro == 'ALTA' ? 'selected' : ''}>Alta</option>
                    <option value="CRITICA" ${prioridadFiltro == 'CRITICA' ? 'selected' : ''}>Crítica</option>
                </select>
            </div>
            <div class="col-md-6 d-flex gap-2 justify-content-md-end">
                <button type="submit" class="btn btn-primary px-4">
                    <i class="bi bi-filter me-1"></i> Filtrar
                </button>
                <a href="${pageContext.request.contextPath}/tickets" class="btn btn-outline-secondary px-3">
                    <i class="bi bi-x-circle me-1"></i> Limpiar filtros
                </a>
            </div>
        </form>
    </div>

    <!-- Tabla de Contenido -->
    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light text-uppercase fs-7 text-secondary">
                    <tr>
                        <th class="py-3 ps-4">ID</th>
                        <th class="py-3">Título</th>
                        <th class="py-3">Cliente</th>
                        <th class="py-3">Equipo</th>
                        <th class="py-3">Categoría</th>
                        <th class="py-3">Técnico</th>
                        <th class="py-3">Prioridad</th>
                        <th class="py-3">Estado</th>
                        <th class="py-3 text-center pe-4" style="min-width: 290px;">Acciones</th>
                    </tr>
                </thead>
                <tbody id="tablaTicketsBody">
                    <c:choose>
                        <c:when test="${empty ticketsDetalle}">
                            <tr>
                                <td colspan="9" class="text-center text-secondary py-5">
                                    <i class="bi bi-inbox fs-2 d-block mb-2 text-muted"></i>
                                    Aún no hay tickets registrados.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="ticket" items="${ticketsDetalle}">
                                <tr>
                                    <td class="ps-4 fw-semibold text-dark"><c:out value="${ticket.idTicket}" /></td>
                                    <td class="fw-medium text-dark"><c:out value="${ticket.titulo}" /></td>
                                    <td><c:out value="${ticket.cliente}" /></td>
                                    <td class="text-muted"><c:out value="${ticket.equipo}" /></td>
                                    <td><span class="badge bg-light text-dark border"><c:out value="${ticket.categoria}" /></span></td>
                                    <td><c:out value="${ticket.tecnico}" /></td>
                                    <td>
                                        <span class="badge ${ticket.prioridad eq 'CRITICA' ? 'bg-danger' : ticket.prioridad eq 'ALTA' ? 'bg-warning text-dark' : ticket.prioridad eq 'MEDIA' ? 'bg-info text-dark' : 'bg-secondary'}">
                                            <c:out value="${ticket.prioridad}" />
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${ticket.estado eq 'CERRADO' ? 'bg-success' : ticket.estado eq 'EN_PROCESO' ? 'bg-primary' : 'bg-secondary'}">
                                            <c:out value="${ticket.estado}" />
                                        </span>
                                    </td>
                                    <td class="pe-4 text-center">
                                        <c:if test="${not vistaPropia}">
                                            <div class="d-flex align-items-center justify-content-center gap-2">
                                                <form method="post" action="${pageContext.request.contextPath}/tickets/actualizar" class="d-flex align-items-center gap-1 m-0">
                                                    <input type="hidden" name="id" value="<c:out value='${ticket.idTicket}' />">
                                                    <input type="hidden" name="estadoEsperado" value="<c:out value='${ticket.estado}' />">
                                                    <select name="estado" class="form-select form-select-sm" style="width: 130px;">
                                                        <option value="ABIERTO" ${ticket.estado == 'ABIERTO' ? 'selected' : ''}>Abierto</option>
                                                        <option value="ASIGNADO" ${ticket.estado == 'ASIGNADO' ? 'selected' : ''}>Asignado</option>
                                                        <option value="EN_PROCESO" ${ticket.estado == 'EN_PROCESO' ? 'selected' : ''}>En proceso</option>
                                                        <option value="CERRADO" ${ticket.estado == 'CERRADO' ? 'selected' : ''}>Cerrado</option>
                                                    </select>
                                                    <button type="submit" class="btn btn-sm text-nowrap py-1 px-2" style="color: #0d6efd; border: 1px solid #0d6efd; background-color: transparent;" onmouseover="this.style.backgroundColor='#0d6efd'; this.style.color='#fff';" onmouseout="this.style.backgroundColor='transparent'; this.style.color='#0d6efd';">Actualizar</button>
                                                </form>

                                                <button type="button" class="btn btn-sm text-nowrap py-1 px-2" style="color: #dc3545; border: 1px solid #dc3545; background-color: transparent;" onmouseover="this.style.backgroundColor='#dc3545'; this.style.color='#fff';" onmouseout="this.style.backgroundColor='transparent'; this.style.color='#dc3545';"
                                                        onclick="abrirModalEliminar('<c:out value="${ticket.idTicket}" />', '<c:out value="${ticket.titulo}" />')">
                                                    Eliminar
                                                </button>
                                            </div>
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

    <!-- Paginación -->
    <c:if test="${totalPaginas > 1}">
        <nav class="mt-4" aria-label="Paginación de tickets">
            <ul class="pagination justify-content-center">
                <c:forEach var="numero" begin="1" end="${totalPaginas}">
                    <li class="page-item ${numero == paginaActual ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/tickets?estadoFiltro=${estadoFiltro}&prioridadFiltro=${prioridadFiltro}&pagina=${numero}">
                            <c:out value="${numero}" />
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>
</main>

<!-- Modal de Bootstrap para Confirmar Eliminación -->
<div class="modal fade" id="modalEliminar" tabindex="-1" aria-labelledby="modalEliminarLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow rounded-4">
            <div class="modal-header bg-danger text-white rounded-top-4">
                <h5 class="modal-title fw-bold" id="modalEliminarLabel">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Confirmar eliminación
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body py-4">
                <p class="mb-1">¿Está seguro de que desea eliminar el ticket <strong id="ticketIdTexto" class="text-danger"></strong>?</p>
                <p class="text-muted small mb-0 text-truncate" id="ticketTituloTexto"></p>
            </div>
            <div class="modal-footer border-0 pb-3">
                <button type="button" class="btn btn-light px-3" data-bs-dismiss="modal">Cancelar</button>
                <form id="formEliminarReal" method="post" action="${pageContext.request.contextPath}/tickets/eliminar" class="d-inline">
                    <input type="hidden" id="inputIdTicket" name="id" value="">
                    <!-- Botón de confirmar eliminación con color rojo institucional / danger fijo -->
                    <button type="submit" class="btn px-4 text-white" style="background-color: #b33b26; border: none;" onmouseover="this.style.backgroundColor='#96311f';" onmouseout="this.style.backgroundColor='#b33b26';">Sí, eliminar</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Scripts de Bootstrap 5 y lógica personalizada -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const tbody = document.getElementById("tablaTicketsBody");
        if (tbody) {
            const rows = Array.from(tbody.querySelectorAll("tr"));
            if (rows.length > 0 && !rows[0].querySelector("td[colspan]")) {
                rows.sort((a, b) => {
                    const idA = parseInt(a.cells[0].innerText.trim()) || 0;
                    const idB = parseInt(b.cells[0].innerText.trim()) || 0;
                    return idA - idB;
                });
                rows.forEach(row => tbody.appendChild(row));
            }
        }
    });

    function abrirModalEliminar(idTicket, tituloTicket) {
        document.getElementById('inputIdTicket').value = idTicket;
        document.getElementById('ticketIdTexto').innerText = '#' + idTicket;
        document.getElementById('ticketTituloTexto').innerText = 'Título: ' + tituloTicket;
        
        var myModal = new bootstrap.Modal(document.getElementById('modalEliminar'));
        myModal.show();
    }
</script>
</body>
</html>