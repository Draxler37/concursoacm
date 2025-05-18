function renderParticipantes(participantes) {
    const container = document.getElementById('participantesContainer');
    container.innerHTML = '';
    if (participantes.length === 0) {
        container.innerHTML = '<div class="col-12 text-center text-muted">No se encontraron participantes.</div>';
        return;
    }
    participantes.forEach(p => {
        const card = document.createElement('div');
        card.className = 'col-md-4';
        card.innerHTML = `
            <div class="card participante-card animate__animated animate__fadeInUp">
                <div class="card-body d-flex align-items-start">
                    <img src="../static/images/default-profile.png" class="rounded-circle perfil-img me-3" alt="Perfil" width="64" height="64">
                    <div class="w-100">
                        <div class="d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">${p.nombre}</h5>
                            <span class="ms-2"><i class="fa-solid fa-flag"></i> ${p.nombrePais || ''}</span>
                        </div>
                        <p class="mb-1"><strong>Sexo:</strong> ${p.sexo === 'M' ? 'Masculino' : (p.sexo === 'F' ? 'Femenino' : p.sexo)}</p>
                        <p class="mb-1"><strong>Edad:</strong> ${p.edad}</p>
                        <p class="mb-0"><strong>Equipo:</strong> ${p.nombreEquipo || '-'}</p>
                    </div>
                </div>
            </div>
        `;
        container.appendChild(card);
    });
}

$(function () {
    // Poblar selects desde la API (igual que antes)
    function poblarSelect(url, $select, nombreCampo, extraHtml) {
        $.get(url, function (data) {
            $select.empty();
            $select.append(`<option value="">${extraHtml || $select.attr('placeholder') || $select.attr('id')}</option>`);
            data.forEach(function (item) {
                let bandera = item.bandera ? `<img src='${item.bandera}' class='bandera-img me-1' width='20'/>` : '';
                $select.append(`<option value="${item.id}">${bandera}${item[nombreCampo]}</option>`);
            });
        });
    }

    poblarSelect('http://localhost:8080/paises', $('#filtroPais'), 'nombre', 'País');
    poblarSelect('http://localhost:8080/equipos', $('#filtroEquipo'), 'nombre', 'Equipo');
    poblarSelect('http://localhost:8080/regiones', $('#filtroRegion'), 'nombre', 'Región');

    let paginaActual = 1;
    const participantesPorPagina = 12;
    let todosLosParticipantes = [];

    function filtrarParticipantes() {
        const ci       = $('#filtroCI').val().toLowerCase();
        const nombre   = $('#filtroNombre').val().toLowerCase();
        const pais     = $('#filtroPais').val();
        const equipo   = $('#filtroEquipo').val();
        // const region = $('#filtroRegion').val(); // si no lo usas, elimínalo

        return todosLosParticipantes.filter(p => {
            return (!ci     || (p.numCarnet   && p.numCarnet.toLowerCase().includes(ci))) &&
                (!nombre || (p.nombre      && p.nombre.toLowerCase().includes(nombre))) &&
                (!pais   || (p.nombrePais  && p.nombrePais === pais)) &&
                (!equipo || (p.nombreEquipo&& p.nombreEquipo === equipo));
        });
    }

    function renderPaginacion(total, pagina) {
        const totalPaginas = Math.ceil(total / participantesPorPagina);
        const paginacion = $("<nav aria-label='Paginación'><ul class='pagination justify-content-center'></ul></nav>");
        const ul = paginacion.find('ul');
        for (let i = 1; i <= totalPaginas; i++) {
            ul.append(`<li class='page-item${i === pagina ? ' active' : ''}'><a class='page-link' href='#'>${i}</a></li>`);
        }
        $('#participantesContainer').after(paginacion);
        ul.find('a').click(function(e) {
            e.preventDefault();
            paginaActual = parseInt($(this).text());
            mostrarParticipantesFiltrados();
        });
    }

    function mostrarParticipantesFiltrados() {
        $("nav[aria-label='Paginación']").remove();
        const filtrados = filtrarParticipantes();
        const inicio = (paginaActual - 1) * participantesPorPagina;
        const fin    = inicio + participantesPorPagina;
        renderParticipantes(filtrados.slice(inicio, fin));
        if (filtrados.length > participantesPorPagina) {
            renderPaginacion(filtrados.length, paginaActual);
        }
    }

    // Handler del formulario/botón
    $('#filtroParticipantes').on('submit', function(e) {
        e.preventDefault();
        paginaActual = 1;
        console.log('Botón de búsqueda pulsado');

        // **Aquí** cargamos los participantes y luego mostramos
        $.get('http://localhost:8080/participantes', function(data) {
            todosLosParticipantes = data;
            mostrarParticipantesFiltrados();
        }).fail(function(xhr, status, error) {
            console.error('Error al cargar participantes:', error);
            $('#participantesContainer').html('<div class="col-12 text-center text-danger">No se pudo cargar la lista de participantes.</div>');
        });
    });
});