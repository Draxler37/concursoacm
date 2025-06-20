// Variables globales
let preguntasAsignadas = [];
let preguntaSeleccionada = null;
const idParticipante = 1;

// Cargar preguntas asignadas al equipo del participante
function cargarPreguntasAsignadas() {
    fetch(`http://localhost:8080/equipos-preguntas/participante/${idParticipante}`)
        .then(res => {
            if (!res.ok) {
                // Si la respuesta no es OK, leemos el texto del error y lo lanzamos para que lo capture el .catch
                return res.text().then(text => {
                    throw new Error(text || 'Error en la respuesta del servidor.');
                });
            }
            return res.json();
        })
        .then(data => {
            // --- CASO DE ÉXITO (200 OK) ---
            // 1. Mostrar las tarjetas
            document.getElementById('tarjeta-preguntas').style.display = 'block';
            document.getElementById('tarjeta-responder').style.display = 'block';

            // 2. Ocultar cualquier mensaje de error previo que pudiera existir
            let msgDiv = document.getElementById('main-error-msg');
            if (msgDiv) msgDiv.style.display = 'none';

            // 3. Procesar los datos recibidos
            preguntasAsignadas = (data.preguntas || []).map(p => ({
                idPregunta: p.idPregunta,
                titulo: p.titulo || p.texto || `Pregunta #${p.idPregunta}`,
                contenido: p.texto || p.titulo
            }));

            // 4. Renderizar el contenido dentro de las tarjetas
            renderPreguntasAsignadas();
        })
        .catch(error => {
            // --- CASO DE ERROR (Cualquier tipo) ---
            let mensajeError;
            // Verificamos el mensaje de error específico que nos interesa
            if (error.message && error.message.toLowerCase().includes('no estás asignado a un equipo')) {
                mensajeError = 'No estás asignado a un equipo.';
            } else {
                mensajeError = 'No se pudieron cargar las preguntas.';
                mostrarToastError(); // Mostrar el toast solo para errores genéricos/conexión
            }
            mostrarMensaje(mensajeError, 'error');
        });
}


// Renderizar la lista de preguntas
function renderPreguntasAsignadas() {
    const lista = document.getElementById('preguntas-list');
    const noPreguntas = document.getElementById('no-preguntas');
    const seccionResponder = document.getElementById('pregunta-seleccionada').parentNode;

    lista.innerHTML = '';

    if (!preguntasAsignadas || preguntasAsignadas.length === 0) {
        noPreguntas.style.display = 'block'; // Usar 'block' en lugar de '' para ser explícito
        // Ocultamos la sección de respuesta si no hay preguntas
        seccionResponder.style.display = 'none';
        return;
    }

    noPreguntas.style.display = 'none';
    seccionResponder.style.display = 'block'; // Asegurarse que la sección de respuesta sea visible
    document.getElementById('pregunta-seleccionada').textContent = 'Selecciona una pregunta de la lista para responder.';
    document.getElementById('form-respuesta').style.display = 'none'; // Ocultar el form hasta que se seleccione una

    preguntasAsignadas.forEach(p => {
        const li = document.createElement('li');
        li.className = 'question-item';
        li.textContent = p.titulo || `Pregunta #${p.idPregunta}`;
        li.dataset.id = p.idPregunta;
        li.onclick = () => seleccionarPregunta(p);
        lista.appendChild(li);
    });
}

// Seleccionar una pregunta para responder
function seleccionarPregunta(pregunta) {
    preguntaSeleccionada = pregunta;

    document.querySelectorAll('.question-item').forEach(el => el.classList.remove('active'));
    document.querySelector(`.question-item[data-id='${pregunta.idPregunta}']`).classList.add('active');

    document.getElementById('pregunta-seleccionada').textContent = pregunta.contenido || pregunta.titulo;
    document.getElementById('form-respuesta').style.display = 'block';
    document.getElementById('respuesta').value = '';
    document.getElementById('estado-respuesta').textContent = '';
    document.getElementById('estado-respuesta').className = 'response-status'; // Limpiar clase success/error
}

// Enviar respuesta al backend
document.getElementById('form-respuesta').onsubmit = function (e) {
    e.preventDefault();
    if (!preguntaSeleccionada) return;
    const respuestaTexto = document.getElementById('respuesta').value.trim();
    if (!respuestaTexto) {
        // Opcional: mostrar un pequeño aviso de que la respuesta no puede estar vacía
        const estadoRespuesta = document.getElementById('estado-respuesta');
        estadoRespuesta.textContent = 'La respuesta no puede estar vacía.';
        estadoRespuesta.className = 'response-status error';
        return;
    }

    const botonEnviar = this.querySelector('button[type="submit"]');
    botonEnviar.disabled = true;
    botonEnviar.textContent = 'Enviando...';

    fetch(`http://localhost:8080/respuestas/responder/${idParticipante}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            idPregunta: preguntaSeleccionada.idPregunta,
            respuestaParticipante: respuestaTexto
        })
    })
        .then(res => {
            if (!res.ok) return res.text().then(text => Promise.reject(new Error(text)));
            return res.json();
        })
        .then(() => {
            const estadoRespuesta = document.getElementById('estado-respuesta');
            estadoRespuesta.textContent = 'Respuesta enviada correctamente.';
            estadoRespuesta.className = 'response-status success';
            document.getElementById('form-respuesta').style.display = 'none';
            // Opcional: marcar la pregunta como respondida visualmente
            document.querySelector(`.question-item[data-id='${preguntaSeleccionada.idPregunta}']`).style.opacity = '0.5';
            document.querySelector(`.question-item[data-id='${preguntaSeleccionada.idPregunta}']`).style.pointerEvents = 'none';
        })
        .catch(error => {
            const estadoRespuesta = document.getElementById('estado-respuesta');
            estadoRespuesta.textContent = error.message || 'Error al enviar respuesta.';
            estadoRespuesta.className = 'response-status error';
        })
        .finally(() => {
            botonEnviar.disabled = false;
            botonEnviar.textContent = 'Enviar Respuesta';
        });
};

function mostrarMensaje(msg, tipo) {
    let mainContainer = document.querySelector('.main-content');
    let msgDiv = document.getElementById('main-error-msg');

    // Ocultar las tarjetas siempre que se muestre este mensaje principal
    document.getElementById('tarjeta-preguntas').style.display = 'none';
    document.getElementById('tarjeta-responder').style.display = 'none';

    if (!msgDiv) {
        msgDiv = document.createElement('div');
        msgDiv.id = 'main-error-msg';
        msgDiv.className = 'alert';
        msgDiv.style.margin = '40px auto';
        msgDiv.style.textAlign = 'center';
        msgDiv.style.fontSize = '1.2rem';
        // Insertar antes del contenedor principal de tarjetas
        mainContainer.parentNode.insertBefore(msgDiv, mainContainer);
    }
    msgDiv.textContent = msg;
    msgDiv.className = `alert ${tipo === 'error' ? 'alert-danger' : 'alert-success'}`;
    msgDiv.style.display = 'block';
}

// --- TOAST DE ERROR VISUAL ---
if (!document.getElementById('errorToast')) {
    const toastHtml = `
    <div class="toast-container position-fixed bottom-0 end-0 p-3">
        <div id="errorToast" class="toast align-items-center text-bg-danger border-0" role="alert"
            aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    Error de conexión con el servidor. Por favor, inténtalo nuevamente.
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"
                    aria-label="Cerrar"></button>
            </div>
        </div>
    </div>`;
    $(document.body).append(toastHtml);
}
function mostrarToastError() {
    const toastEl = document.getElementById('errorToast');
    if (toastEl) {
        const toast = bootstrap.Toast.getOrCreateInstance(toastEl);
        toast.show();
    }
}

// Inicialización
document.addEventListener('DOMContentLoaded', cargarPreguntasAsignadas);