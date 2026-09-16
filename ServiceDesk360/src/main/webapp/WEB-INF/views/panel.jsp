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
            <form id="formCerrarSesion" action="${pageContext.request.contextPath}/cerrar-sesion" method="post">
                <button class="btn btn-outline-light btn-sm" type="button" id="abrirModalCerrarSesion">
                    <i class="bi bi-box-arrow-right me-1"></i>Salir
                </button>
            </form>
        </div>
    </div>
</header>
<main class="container py-5">
    <section class="card border-0 shadow-sm mb-4">
        <div class="card-body p-4">
            <c:choose>
                <c:when test="${sessionScope.usuarioAutenticado.rol == 'TECNICO'}">
                    <p class="text-primary fw-semibold mb-2">Panel técnico</p>
                </c:when>
                <c:otherwise>
                    <p class="text-primary fw-semibold mb-2">Panel general</p>
                </c:otherwise>
            </c:choose>
            <h1 class="h2">Bienvenido, <c:out value="${sessionScope.usuarioAutenticado.nombreCompleto}" />.</h1>
            <p class="text-secondary mb-0">
                <c:out value="${sessionScope.usuarioAutenticado.correo}" />
                <span class="mx-2">|</span>
                Rol: <c:out value="${sessionScope.usuarioRolDescripcion}" />
            </p>
        </div>
    </section>

    <section class="row g-4">
        <c:choose>
            <c:when test="${sessionScope.usuarioAutenticado.rol == 'TECNICO'}">
                <div class="col-md-6">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body p-4 d-flex flex-column">
                            <i class="bi bi-wrench-adjustable-circle text-primary fs-1 mb-3"></i>
                            <h2 class="h4">Atención de tickets</h2>
                            <p class="text-secondary flex-grow-1">Revisa, prioriza y atiende las solicitudes asignadas por soporte.</p>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/tickets">
                                <i class="bi bi-arrow-right me-1"></i>Ver tickets asignados
                            </a>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body p-4 d-flex flex-column">
                            <i class="bi bi-clipboard-check text-success fs-1 mb-3"></i>
                            <h2 class="h4">Diagnóstico</h2>
                            <p class="text-secondary flex-grow-1">Gestiona y registra la resolución de incidentes reportados por los solicitantes.</p>
                            <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/diagnostico">
                                <i class="bi bi-arrow-right me-1"></i>Ir a diagnóstico
                            </a>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="col-md-6">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body p-4 d-flex flex-column">
                            <i class="bi bi-ticket-detailed text-primary fs-1 mb-3"></i>
                            <h2 class="h4">Tickets de soporte</h2>
                            <p class="text-secondary flex-grow-1">Consulta las solicitudes de soporte que has registrado.</p>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/tickets">
                                <i class="bi bi-arrow-right me-1"></i>Ver mis tickets
                            </a>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body p-4 d-flex flex-column">
                            <i class="bi bi-plus-circle text-warning fs-1 mb-3"></i>
                            <h2 class="h4">Nuevo ticket</h2>
                            <p class="text-secondary flex-grow-1">Crea una nueva solicitud para recibir ayuda técnica.</p>
                            <a class="btn btn-outline-warning" href="${pageContext.request.contextPath}/tickets/nuevo">
                                <i class="bi bi-plus-lg me-1"></i>Crear solicitud
                            </a>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body p-4 d-flex flex-column">
                            <i class="bi bi-laptop text-info fs-1 mb-3"></i>
                            <h2 class="h4">Mis equipos</h2>
                            <p class="text-secondary flex-grow-1">Registra los equipos que pueden relacionarse con tus solicitudes.</p>
                            <a class="btn btn-outline-info" href="${pageContext.request.contextPath}/equipos/nuevo">
                                <i class="bi bi-plus-lg me-1"></i>Registrar equipo
                            </a>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>
<footer class="mt-auto py-3 bg-white border-top text-center text-muted small">ServiceDesk 360</footer>
<div id="modalCerrarSesion" class="modal-sesion" hidden>
    <div class="modal-sesion-contenido" role="dialog" aria-modal="true" aria-labelledby="tituloCerrarSesion">
        <button type="button" class="modal-sesion-cerrar" id="cancelarCerrarSesion" aria-label="Cerrar ventana">&times;</button>
        <div class="modal-sesion-icono"><i class="bi bi-box-arrow-right"></i></div>
        <h2 id="tituloCerrarSesion" class="h4 mb-2">¿Cerrar sesión?</h2>
        <p class="text-secondary mb-4">Tu sesión se cerrará y tendrás que iniciar acceso nuevamente.</p>
        <div class="d-flex justify-content-end gap-2">
            <button type="button" class="btn btn-light border" id="cancelarCerrarSesionTexto">Cancelar</button>
            <button type="button" class="btn btn-danger" id="confirmarCerrarSesion">Cerrar sesión</button>
        </div>
    </div>
</div>
<style>
    .modal-sesion { position: fixed; inset: 0; z-index: 1050; display: grid; place-items: center; padding: 1rem; background: rgba(15, 31, 48, .58); }
    .modal-sesion[hidden] { display: none; }
    .modal-sesion-contenido { position: relative; width: min(100%, 430px); padding: 2rem; border-radius: 18px; background: #fff; box-shadow: 0 24px 70px rgba(0, 0, 0, .25); text-align: center; }
    .modal-sesion-cerrar { position: absolute; top: .7rem; right: .9rem; border: 0; background: transparent; color: #6c757d; font-size: 1.8rem; line-height: 1; }
    .modal-sesion-icono { display: grid; place-items: center; width: 52px; height: 52px; margin: 0 auto 1rem; border-radius: 50%; background: #fff0f0; color: #dc3545; font-size: 1.4rem; }
</style>
<script>
    (function () {
        const modal = document.getElementById('modalCerrarSesion');
        const abrir = document.getElementById('abrirModalCerrarSesion');
        const cancelar = document.getElementById('cancelarCerrarSesion');
        const cancelarTexto = document.getElementById('cancelarCerrarSesionTexto');
        const confirmar = document.getElementById('confirmarCerrarSesion');
        const formulario = document.getElementById('formCerrarSesion');

        function cerrarModal() {
            modal.hidden = true;
            abrir.focus();
        }

        abrir.addEventListener('click', function () {
            modal.hidden = false;
            confirmar.focus();
        });
        cancelar.addEventListener('click', cerrarModal);
        cancelarTexto.addEventListener('click', cerrarModal);
        confirmar.addEventListener('click', function () {
            formulario.submit();
        });
        modal.addEventListener('click', function (evento) {
            if (evento.target === modal) cerrarModal();
        });
        document.addEventListener('keydown', function (evento) {
            if (evento.key === 'Escape' && !modal.hidden) cerrarModal();
        });
    }());
</script>
</body>
</html>
