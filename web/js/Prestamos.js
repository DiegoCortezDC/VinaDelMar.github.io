var empleados = [];
var prestamoActual = {};

function mostrarEmpleados() {
    fetch('http://localhost:8084/estadiasNominaVDMJR/api/empleados/getAll')
        .then(response => response.json())
        .then(data => {
            empleados = data;
            let empleadosHTML = empleados.map(emp =>
                `<tr onclick="abrirModalPrestamo(${emp.idEmpleado})">
                    <td>${capitalizeWords(emp.nombre)}</td>
                    <td>${capitalizeWords(emp.apellidoPaterno)}</td>
                    <td>${capitalizeWords(emp.apellidoMaterno)}</td>
                    <td>${capitalizeWords(emp.rol)}</td>
                </tr>`
            ).join('');
            document.getElementById('listaEmpleados').innerHTML = empleadosHTML;
            document.getElementById('empleadosActivos').style.display = 'block';
            document.getElementById('tablaPrestamos').style.display = 'none';
        })
        .catch(error => console.error('Error al cargar empleados:', error));
}

function capitalizeWords(string) {
    return string.replace(/\b\w/g, char => char.toUpperCase());
}

function abrirModalPrestamo(idEmpleado) {
    const empleado = empleados.find(emp => emp.idEmpleado === idEmpleado);
    document.getElementById('nombreCompleto').value = `${capitalizeWords(empleado.nombre)} ${capitalizeWords(empleado.apellidoPaterno)} ${capitalizeWords(empleado.apellidoMaterno)}`;
    $('#modalPrestamo').modal('show');
}

function registrarPrestamo() {
    const empleado = empleados.find(emp => `${capitalizeWords(emp.nombre)} ${capitalizeWords(emp.apellidoPaterno)} ${capitalizeWords(emp.apellidoMaterno)}` === document.getElementById('nombreCompleto').value);
    const monto = parseFloat(document.getElementById('montoPrestamo').value);
    const fechaPrestamo = document.getElementById('fechaPrestamo').value;

    if (!monto || monto <= 0) {
        Swal.fire('Error', 'El monto del préstamo debe ser un número positivo.', 'error');
        return;
    }

    if (!fechaPrestamo) {
        Swal.fire('Error', 'La fecha del préstamo es obligatoria.', 'error');
        return;
    }

    const prestamo = {
        idEmpleado: empleado.idEmpleado,
        monto: monto,
        fechaPrestamo: fechaPrestamo
    };

    fetch('http://localhost:8084/estadiasNominaVDMJR/api/prestamos/save', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(prestamo)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => Promise.reject(text));
        }
        return response.json();
    })
    .then(() => {
        $('#modalPrestamo').modal('hide');
        mostrarPrestamos();
        Swal.fire('Registrado', 'Préstamo registrado exitosamente.', 'success');
    })
    .catch(error => {
        console.error('Error registrando el préstamo:', error);
        Swal.fire('Error', 'Error al registrar el préstamo.', 'error');
    });
}

function mostrarPrestamos() {
    fetch('http://localhost:8084/estadiasNominaVDMJR/api/prestamos/getAll')
    .then(response => response.json())
    .then(data => {
        let prestamosHTML = data.map(prestamo =>
            `<tr onclick="abrirAbonoModal(${prestamo.idPrestamo})">
                <td>${capitalizeWords(prestamo.nombreEmpleado)}</td>
                <td>${capitalizeWords(prestamo.apellidoPaterno)}</td>
                <td>${capitalizeWords(prestamo.apellidoMaterno)}</td>
                <td>${capitalizeWords(prestamo.rol)}</td>
                <td>$${prestamo.monto.toFixed(2)}</td>
                <td>${new Date(prestamo.fechaPrestamo).toLocaleDateString()}</td>
            </tr>`
        ).join('');
        document.getElementById('tablaPrestamosDetalles').getElementsByTagName('tbody')[0].innerHTML = prestamosHTML;
        document.getElementById('tablaPrestamos').style.display = 'block';
        document.getElementById('empleadosActivos').style.display = 'none';
    })
    .catch(error => {
        console.error('Error al cargar préstamos:', error);
        Swal.fire('Error', 'No se pudieron cargar los préstamos.', 'error');
    });
}

function abrirAbonoModal(idPrestamo) {
    prestamoActual.id = idPrestamo;
    cargarHistorialAbonos(idPrestamo); // Cargar historial al abrir el modal
    $('#abonoModal').modal('show');
}

function realizarAbono() {
    let monto = parseFloat(document.getElementById('montoAbono').value);

    if (!monto || monto <= 0) {
        Swal.fire('Error', 'El monto del abono debe ser un número positivo.', 'error');
        return;
    }

    let abono = {
        idPrestamo: prestamoActual.id,
        monto: monto,
        fechaAbono: new Date().toISOString(),
        tipo: 'abono'
    };

    fetch('http://localhost:8084/estadiasNominaVDMJR/api/prestamos/realizarAbono', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(abono)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => Promise.reject(text));
        }
        return response.json();
    })
    .then(data => {
        $('#abonoModal').modal('hide');
        if (data === "completamente pagado") {
            Swal.fire('Préstamo pagado', 'El préstamo ha sido pagado completamente.', 'success').then(() => {
                mostrarPrestamos(); // Actualizar la tabla de préstamos
            });
        } else {
            Swal.fire('Abono realizado', data, 'success').then(() => {
                mostrarPrestamos();
                cargarHistorialAbonos(prestamoActual.id);
            });
        }
    })
    .catch(error => {
        console.error('Error al realizar el abono:', error);
        Swal.fire('Error', 'No se pudo realizar el abono.', 'error');
    });
}

function cargarHistorialAbonos(idPrestamo) {
    fetch(`http://localhost:8084/estadiasNominaVDMJR/api/prestamos/abonos/${idPrestamo}`)
    .then(response => response.json())
    .then(data => {
        let historialHTML = data.map(abono =>
            `<tr>
                <td>${abono.monto.toFixed(2)}</td>
                <td>${new Date(abono.fechaAbono).toLocaleDateString()}</td>
                <td>${abono.tipo}</td>
            </tr>`
        ).join('');
        document.getElementById('historialAbonos').innerHTML = historialHTML;
    })
    .catch(error => {
        console.error('Error al cargar el historial de abonos:', error);
        Swal.fire('Error', 'No se pudo cargar el historial de abonos.', 'error');
    });
}

function abrirHistorialAbonosModal(idPrestamo) {
    cargarHistorialAbonos(idPrestamo);
    $('#historialAbonosModal').modal('show');
}
