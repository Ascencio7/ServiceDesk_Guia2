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
                <a href="${pageContext.request.contextPath}/tickets" class="btn btn-outline-secondary">Volver</a>
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
                <div class="mb-3">
                    <label for="descripcion" class="form-label">Descripción</label>
                    <textarea id="descripcion" name="descripcion" class="form-control" rows="6" required><c:out value="${descripcionAnterior}" /></textarea>
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
