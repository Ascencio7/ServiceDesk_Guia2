<%-- 
    Document   : index
    Created on : 08 aug 2026, 2:23:12 p. m.
    Author     : Vladimir Ascencio
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acceso | ServiceDesk 360</title>
    
    <!-- Bootstrap 5 CSS y Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    
    <!-- CSS Personalizado -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body class="bg-light d-flex align-items-center min-vh-100 py-5">

<main class="container">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-5">
            
            <!-- Banner -->
            <div class="text-center mb-4">
                <div class="d-inline-flex align-items-center justify-content-center bg-primary text-white rounded-circle p-3 mb-2 shadow-sm" style="width: 60px; height: 60px;">
                    <i class="bi bi-headset fs-2"></i>
                </div>
                <h2 class="fw-bold text-dark mb-1">ServiceDesk 360</h2>
                <p class="text-muted small">Gestión empresarial de soporte técnico</p>
            </div>

            <!-- Card del Formulario -->
            <div class="card shadow-lg border-0 rounded-4">
                <div class="card-body p-4 p-sm-5">
                    
                    <h1 class="h4 fw-bold text-dark mb-1">Iniciar acceso</h1>
                    <p class="text-secondary small mb-4">
                        Ingrese con la cuenta temporal creada en esta práctica.
                    </p>

                    <%-- Mostrar alertas de éxito o error dinámicamente --%>
                    <c:if test="${not empty mensajeExito}">
                        <div class="alert alert-success alert-dismissible fade show d-flex align-items-center gap-2 rounded-3 mb-4" role="alert">
                            <i class="bi bi-check-circle-fill fs-5"></i>
                            <div><c:out value="${mensajeExito}" /></div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty mensajeError}">
                        <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center gap-2 rounded-3 mb-4" role="alert">
                            <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                            <div><c:out value="${mensajeError}" /></div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <!-- Formulario de Acceso -->
                    <form action="${pageContext.request.contextPath}/acceso" method="post" novalidate>
                        
                        <!-- Correo Electrónico -->
                        <div class="mb-3">
                            <label for="correo" class="form-label fw-semibold text-secondary">
                                Correo electrónico
                            </label>
                            <div class="input-group">
                                <span class="input-group-text bg-light text-muted border-end-0">
                                    <i class="bi bi-envelope-fill"></i>
                                </span>
                                <input id="correo" 
                                       name="correo" 
                                       type="email" 
                                       class="form-control border-start-0 bg-light" 
                                       placeholder="usuario@ejemplo.com"
                                       maxlength="100" 
                                       value="<c:out value='${ultimoUsuario}' />" 
                                       autocomplete="email" 
                                       required>
                            </div>
                        </div>

                        <!-- Contraseña -->
                        <div class="mb-3">
                            <label for="clave" class="form-label fw-semibold text-secondary">
                                Contraseña
                            </label>
                            <div class="input-group">
                                <span class="input-group-text bg-light text-muted border-end-0">
                                    <i class="bi bi-lock-fill"></i>
                                </span>
                                <input id="clave" 
                                       name="clave" 
                                       type="password" 
                                       class="form-control border-start-0 bg-light" 
                                       placeholder="••••••••"
                                       maxlength="64" 
                                       autocomplete="current-password" 
                                       required>
                            </div>
                        </div>

                        <!-- Checkbox para Recordar -->
                        <div class="form-check mb-4">
                            <input class="form-check-input" type="checkbox" name="recordar" id="recordar" value="si">
                            <label class="form-check-label text-secondary small" for="recordar">
                                Recordar únicamente mi correo en este navegador
                            </label>
                        </div>

                        <!-- Acciones -->
                        <div class="d-grid gap-2 mb-3">
                            <button type="submit" class="btn btn-primary btn-lg fw-semibold shadow-sm">
                                <i class="bi bi-box-arrow-in-right me-2"></i>Ingresar
                            </button>
                        </div>

                        <div class="text-center pt-2">
                            <span class="text-muted small me-1">¿No tienes una cuenta?</span>
                            <a href="${pageContext.request.contextPath}/registro" class="text-primary text-decoration-none fw-semibold small">
                                Crear cuenta temporal
                            </a>
                        </div>
                        
                    </form>

                </div>
            </div>

            <!-- Footer auxiliar -->
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/" class="text-secondary text-decoration-none small">
                    <i class="bi bi-arrow-left me-1"></i> Volver a la página principal
                </a>
            </div>

        </div>
    </div>
</main>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>