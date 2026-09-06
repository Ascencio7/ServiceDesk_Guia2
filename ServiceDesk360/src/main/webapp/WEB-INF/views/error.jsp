<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error | ServiceDesk 360</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<main class="container py-5">
    <div class="alert alert-danger shadow-sm">
        <h1 class="h4">No fue posible completar la operación</h1>
        <p><c:out value="${mensajeError}" /></p>
        <a class="btn btn-outline-danger" href="${pageContext.request.contextPath}/panel">Volver al panel</a>
    </div>
</main>
</body>
</html>
