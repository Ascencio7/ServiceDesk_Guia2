<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis equipos | ServiceDesk 360</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body class="bg-light">
<main class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-7">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div><h1 class="h3 mb-1">Registrar equipo</h1><p class="text-secondary mb-0">El equipo quedará disponible al crear un ticket.</p></div>
                <a href="${pageContext.request.contextPath}/panel" class="btn btn-outline-secondary">Volver al panel</a>
            </div>
            <c:if test="${not empty mensajeError}"><div class="alert alert-danger"><c:out value="${mensajeError}" /></div></c:if>
            <form method="post" class="card border-0 shadow-sm p-4">
                <div class="mb-3"><label for="codigo" class="form-label">Código de inventario</label><input id="codigo" name="codigo" class="form-control" required maxlength="40" value="<c:out value='${codigoAnterior}' />"></div>
                <div class="mb-3"><label for="tipo" class="form-label">Tipo</label><input id="tipo" name="tipo" class="form-control" required maxlength="80" placeholder="Laptop, impresora, router..." value="<c:out value='${tipoAnterior}' />"></div>
                <div class="row g-3 mb-4"><div class="col-md-6"><label for="marca" class="form-label">Marca</label><input id="marca" name="marca" class="form-control" maxlength="80" value="<c:out value='${marcaAnterior}' />"></div><div class="col-md-6"><label for="modelo" class="form-label">Modelo</label><input id="modelo" name="modelo" class="form-control" maxlength="80" value="<c:out value='${modeloAnterior}' />"></div></div>
                <button type="submit" class="btn btn-primary">Guardar equipo</button>
            </form>
        </div>
    </div>
</main>
</body>
</html>