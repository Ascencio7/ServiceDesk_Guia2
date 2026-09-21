<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuevo ticket | ServiceDesk 360</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body class="bg-light">
<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="h3 mb-0">Registrar ticket de soporte</h1>
                <a href="${pageContext.request.contextPath}/panel" class="btn btn-outline-secondary">Volver al panel</a>
            </div>
            <c:if test="${not empty errores}">
                <div class="alert alert-danger" role="alert">
                    <ul class="mb-0">
                        <c:forEach var="error" items="${errores}"><li><c:out value="${error}" /></li></c:forEach>
                    </ul>
                </div>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/tickets/nuevo" class="card border-0 shadow-sm p-4">
                <div class="mb-3">
                    <label for="titulo" class="form-label">Título</label>
                    <input id="titulo" name="titulo" class="form-control" maxlength="100" required value="<c:out value='${tituloAnterior}' />">
                </div>
                <div class="row g-3 mb-4">
                    <div class="col-md-6">
                        <label for="clienteId" class="form-label">Cliente</label>
                        <select id="clienteId" name="clienteId" class="form-select" required>
                            <option value="">Seleccione un cliente</option>
                            <c:forEach var="cliente" items="${clientes}">
                                <option value="${cliente.id}" ${clienteAnterior == cliente.id ? 'selected' : ''}><c:out value="${cliente.nombre}" /></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label for="equipoId" class="form-label">Equipo afectado</label>
                        <select id="equipoId" name="equipoId" class="form-select" required>
                            <option value="">Seleccione un equipo</option>
                            <c:forEach var="equipo" items="${equipos}">
                                <option value="${equipo.id}" ${equipoAnterior == equipo.id ? 'selected' : ''}><c:out value="${equipo.nombre}" /></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label for="categoriaId" class="form-label">Categoría</label>
                        <select id="categoriaId" name="categoriaId" class="form-select" required>
                            <c:forEach var="categoria" items="${categorias}"><option value="${categoria.id}" ${categoriaAnterior == categoria.id ? 'selected' : ''}><c:out value="${categoria.nombre}" /></option></c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label for="tecnicoId" class="form-label">Técnico responsable</label>
                        <select id="tecnicoId" name="tecnicoId" class="form-select" required>
                            <c:forEach var="tecnico" items="${tecnicos}"><option value="${tecnico.id}" ${tecnicoAnterior == tecnico.id ? 'selected' : ''}><c:out value="${tecnico.nombre}" /></option></c:forEach>
                        </select>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="descripcion" class="form-label">Descripción</label>
                    <textarea id="descripcion" name="descripcion" class="form-control" rows="6" required><c:out value="${descripcionAnterior}" /></textarea>
                </div>
                <div class="mb-4">
                    <label for="detalleInicial" class="form-label">Seguimiento inicial</label>
                    <textarea id="detalleInicial" name="detalleInicial" class="form-control" rows="3" required><c:out value="${detalleInicialAnterior}" /></textarea>
                </div>
                <div class="mb-4">
                    <label for="prioridad" class="form-label">Prioridad</label>
                    <select id="prioridad" name="prioridad" class="form-select" required>
                        <option value="">Seleccione una prioridad</option>
                        <option value="BAJA" ${prioridadAnterior == 'BAJA' ? 'selected' : ''}>Baja</option>
                        <option value="MEDIA" ${prioridadAnterior == 'MEDIA' ? 'selected' : ''}>Media</option>
                        <option value="ALTA" ${prioridadAnterior == 'ALTA' ? 'selected' : ''}>Alta</option>
                        <option value="CRITICA" ${prioridadAnterior == 'CRITICA' ? 'selected' : ''}>Crítica</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Registrar ticket</button>
            </form>
        </div>
    </div>
</main>
</body>
</html>
