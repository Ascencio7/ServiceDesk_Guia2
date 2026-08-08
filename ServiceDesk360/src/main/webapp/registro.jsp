<%-- 
    Document   : index
    Created on : 08 aug 2026, 2:40:37 p. m.
    Author     : Vladimir Ascencio
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro | ServiceDesk 360</title>
    
    <!-- Bootstrap 5 CSS y Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    
    <!-- CSS Personalizado -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilos.css">
</head>
<body class="bg-light d-flex align-items-center min-vh-100 py-5">

<main class="container">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            
            <!-- Branding / Banner -->
            <div class="text-center mb-4">
                <div class="d-inline-flex align-items-center justify-content-center bg-primary text-white rounded-circle p-3 mb-2 shadow-sm" style="width: 60px; height: 60px;">
                    <i class="bi bi-person-plus-fill fs-2"></i>
                </div>
                <h2 class="fw-bold text-dark mb-1">ServiceDesk 360</h2>
                <p class="text-muted small">Plataforma empresarial de gestión de soporte técnico</p>
            </div>

            <!-- Card del Formulario -->
            <div class="card shadow-lg border-0 rounded-4">
                <div class="card-body p-4 p-sm-5">
                    
                    <h1 class="h4 fw-bold text-dark mb-1">Crear cuenta temporal</h1>
                    <p class="text-secondary small mb-4">
                        Complete la información para ingresar al caso modelo.
                    </p>

                    <%-- Mostrar la alerta únicamente si existe un mensaje de error --%>
                    <c:if test="${not empty mensajeError}">
                        <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center gap-2 rounded-3 mb-4" role="alert">
                            <i class="bi bi-exclamation-triangle-fill fs-5"></i>
                            <div><c:out value="${mensajeError}" /></div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <!-- Formulario de Registro -->
                    <form action="${pageContext.request.contextPath}/registro" method="post" autocomplete="off" novalidate>
                        
                        <!-- Nombre completo -->
                        <div class="mb-3">
                            <label for="nombre" class="form-label fw-semibold text-secondary">
                                Nombre completo
                            </label>
                            <div class="input-group">
                                <span class="input-group-text bg-light text-muted border-end-0">
                                    <i class="bi bi-person-fill"></i>
                                </span>
                                <input id="nombre" 
                                       name="nombre" 
                                       type="text" 
                                       class="form-control border-start-0 bg-light" 
                                       placeholder="Ej. Juan Pérez"
                                       maxlength="80" 
                                       required>
                            </div>
                        </div>

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
                                       required>
                            </div>
                        </div>

                        <!-- Rol inicial -->
                        <div class="mb-3">
                            <label for="rol" class="form-label fw-semibold text-secondary">
                                Rol inicial
                            </label>
                            <div class="input-group">
                                <span class="input-group-text bg-light text-muted border-end-0">
                                    <i class="bi bi-shield-lock-fill"></i>
                                </span>
                                <select id="rol" name="rol" class="form-select border-start-0 bg-light" required>
                                    <option value="" disabled selected>Seleccione un rol</option>
                                    <option value="SOLICITANTE">Solicitante</option>
                                    <option value="TECNICO">Técnico</option>
                                </select>
                            </div>
                        </div>

                        <!-- Contraseña -->
                        <div class="mb-3">
                            <label for="clave" class="form-label fw-semibold text-secondary">
                                Contraseña
                            </label>
                            <div class="input-group">
                                <span class="input-group-text bg-light text-muted border-end-0">
                                    <i class="bi bi-key-fill"></i>
                                </span>
                                <input id="clave" 
                                       name="clave" 
                                       type="password" 
                                       class="form-control border-start-0 bg-light" 
                                       placeholder="Mínimo 8 caracteres"
                                       minlength="8" 
                                       maxlength="64" 
                                       required>
                            </div>
                        </div>

                        <!-- Confirmar contraseña -->
                        <div class="mb-4">
                            <label for="confirmarClave" class="form-label fw-semibold text-secondary">
                                Confirmar contraseña
                            </label>
                            <div class="input-group">
                                <span class="input-group-text bg-light text-muted border-end-0">
                                    <i class="bi bi-check2-square"></i>
                                </span>
                                <input id="confirmarClave" 
                                       name="confirmarClave" 
                                       type="password" 
                                       class="form-control border-start-0 bg-light" 
                                       placeholder="Repita su contraseña"
                                       minlength="8" 
                                       maxlength="64" 
                                       required>
                            </div>
                        </div>

                        <!-- Acciones -->
                        <div class="d-grid gap-2 mb-3">
                            <button type="submit" class="btn btn-primary btn-lg fw-semibold shadow-sm">
                                <i class="bi bi-person-check-fill me-2"></i>Crear cuenta
                            </button>
                        </div>

                        <div class="text-center pt-2">
                            <span class="text-muted small me-1">¿Ya tienes una cuenta?</span>
                            <a href="${pageContext.request.contextPath}/acceso" class="text-primary text-decoration-none fw-semibold small">
                                Iniciar acceso
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