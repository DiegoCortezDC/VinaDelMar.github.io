// Utilidad para capitalizar la primera letra de cada palabra en una cadena
function capitalizeWords(string) {
    return string.replace(/\b\w/g, char => char.toUpperCase());
}

// Validación de CURP en mayúsculas y longitud correcta
function validarCURP(curp) {
    const regexCURP = /^[A-Z]{4}\d{6}[HM][A-Z]{5}[A-Z0-9]\d$/;
    return regexCURP.test(curp);
}

// Validación de fecha de ingreso no posterior a la fecha actual
function validarFechaIngreso(fechaIngreso) {
    const fechaActual = new Date();
    const fechaIngresada = new Date(fechaIngreso);
    return fechaIngresada <= fechaActual;
}

// Formatear los datos del empleado
function formatearEmpleado(empleado) {
    empleado.nombre = capitalizeWords(empleado.nombre);
    empleado.apellidoPaterno = capitalizeWords(empleado.apellidoPaterno);
    empleado.apellidoMaterno = capitalizeWords(empleado.apellidoMaterno);
    empleado.domicilio = capitalizeWords(empleado.domicilio);
    empleado.rol = capitalizeWords(empleado.rol);
    empleado.curp = empleado.curp.toUpperCase();
    return empleado;
}

function validarEmpleado(empleado) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const curpRegex = /^[A-Z]{4}\d{6}[HM][A-Z]{5}[A-Z0-9]\d$/;
    const telefonoRegex = /^[0-9]{10}$/;

    if (!empleado.nombre) {
        Swal.fire('Error', 'El nombre es obligatorio.', 'error');
        return false;
    }
    if (!empleado.apellidoPaterno) {
        Swal.fire('Error', 'El apellido paterno es obligatorio.', 'error');
        return false;
    }
    if (!empleado.apellidoMaterno) {
        Swal.fire('Error', 'El apellido materno es obligatorio.', 'error');
        return false;
    }
    if (!telefonoRegex.test(empleado.telefono)) {
        Swal.fire('Error', 'El teléfono debe tener 10 dígitos numéricos.', 'error');
        return false;
    }
    if (!empleado.domicilio) {
        Swal.fire('Error', 'El domicilio es obligatorio.', 'error');
        return false;
    }
    if (!curpRegex.test(empleado.curp)) {
        Swal.fire('Error', 'El CURP no tiene un formato válido.', 'error');
        return false;
    }
    if (isNaN(empleado.edad) || empleado.edad < 18) {
        Swal.fire('Error', 'La edad debe ser un número y ser mayor o igual a 18 años.', 'error');
        return false;
    }
    if (!emailRegex.test(empleado.email)) {
        Swal.fire('Error', 'El correo electrónico no tiene un formato válido.', 'error');
        return false;
    }
    if (!empleado.fechaIngreso) {
        Swal.fire('Error', 'La fecha de ingreso es obligatoria.', 'error');
        return false;
    }
    if (!validarFechaIngreso(empleado.fechaIngreso)) {
        Swal.fire('Error', 'La fecha de ingreso no puede ser posterior a la fecha actual.', 'error');
        return false;
    }
    return true;
}

function cargarEmpleados() {
    fetch("http://localhost:8084/estadiasNominaVDMJR/api/empleados/getAll")
        .then(response => response.json())
        .then(data => {
            console.log(data); // Log para ver los datos recibidos
            empleados = data;
            let mostrar = "";
            empleados.forEach(empleado => {
                let fechaIngreso = empleado.fechaIngreso;
                if (fechaIngreso) {
                    fechaIngreso = new Date(fechaIngreso).toISOString().split('T')[0];
                } else {
                    fechaIngreso = 'No disponible'; // Manejar como prefieras cuando no hay fecha
                }

                let fila = `
                <tr onclick="abrirModalEmpleado(${empleado.idEmpleado})">
                    <td>${empleado.nombre}</td>
                    <td>${empleado.apellidoPaterno}</td>
                    <td>${empleado.apellidoMaterno}</td>
                    <td>${empleado.telefono}</td>
                    <td>${empleado.domicilio}</td>
                    <td>${empleado.curp}</td>
                    <td>${empleado.edad}</td>
                    <td>${empleado.rol}</td>
                    <td>${empleado.email}</td>
                    <td>${fechaIngreso}</td>
                </tr>`;
                mostrar += fila;
            });
            document.getElementById("tablaEmpleados").innerHTML = mostrar;
        })
        .catch(error => {
            console.error('Error al cargar empleados:', error);
        });
}

function agregarEmpleado() {
    const empleado = {
        nombre: document.getElementById("nombreAgregar").value,
        apellidoPaterno: document.getElementById("apellidoPaternoAgregar").value,
        apellidoMaterno: document.getElementById("apellidoMaternoAgregar").value,
        telefono: document.getElementById("telefonoAgregar").value,
        domicilio: document.getElementById("domicilioAgregar").value,
        curp: document.getElementById("curpAgregar").value,
        edad: parseInt(document.getElementById("edadAgregar").value),
        fechaIngreso: document.getElementById("fechaIngresoAgregar").value,
        rol: document.getElementById("rolAgregar").value,
        email: document.getElementById("emailAgregar").value, // Recoger el correo electrónico
        estatus: 1 // Activo por defecto
    };

    // Validaciones
    if (!validarEmpleado(empleado)) {
        return;
    }

    // Formatear los datos del empleado
    formatearEmpleado(empleado);

    fetch("http://localhost:8084/estadiasNominaVDMJR/api/empleados/save", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(empleado)
    })
        .then(response => response.json())
        .then(() => {
            Swal.fire('Empleado Agregado', 'El empleado ha sido agregado exitosamente.', 'success');
            $('#modalAgregarEmpleado').modal('hide');
            cargarEmpleados();
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire('Error', 'Hubo un error al agregar el empleado.', 'error');
        });
}

function prepararFormularioAgregar() {
    // Limpiar todos los campos del formulario
    document.getElementById("idEmpleadoAgregar").value = '';
    document.getElementById("nombreAgregar").value = '';
    document.getElementById("apellidoPaternoAgregar").value = '';
    document.getElementById("apellidoMaternoAgregar").value = '';
    document.getElementById("telefonoAgregar").value = '';
    document.getElementById("domicilioAgregar").value = '';
    document.getElementById("curpAgregar").value = '';
    document.getElementById("edadAgregar").value = '';
    document.getElementById("rolAgregar").selectedIndex = 0;
    document.getElementById("fechaIngresoAgregar").value = '';
    document.getElementById("emailAgregar").value = ''; // Limpiar campo de email

    // Mostrar el modal de agregar
    $('#modalAgregarEmpleado').modal('show');
}

function abrirModalEmpleado(idEmpleado) {
    const empleado = empleados.find(emp => emp.idEmpleado === idEmpleado);
    if (empleado) {
        document.getElementById("idEmpleadoEditar").value = empleado.idEmpleado;
        document.getElementById("nombreEditar").value = capitalizeWords(empleado.nombre);
        document.getElementById("apellidoPaternoEditar").value = capitalizeWords(empleado.apellidoPaterno);
        document.getElementById("apellidoMaternoEditar").value = capitalizeWords(empleado.apellidoMaterno);
        document.getElementById("telefonoEditar").value = empleado.telefono;
        document.getElementById("domicilioEditar").value = capitalizeWords(empleado.domicilio);
        document.getElementById("curpEditar").value = empleado.curp.toUpperCase();
        document.getElementById("edadEditar").value = empleado.edad;
        
        // Asignar la fecha de ingreso
        const fechaIngreso = empleado.fechaIngreso ? new Date(empleado.fechaIngreso).toISOString().split('T')[0] : '';
        document.getElementById("fechaIngresoEditar").value = fechaIngreso;
        
        // Asignar el rol
        document.getElementById("rolEditar").value = empleado.rol.toLowerCase();
        
        document.getElementById("emailEditar").value = empleado.email;

        $('#modalEditarEmpleado').modal('show');
    } else {
        console.error('Empleado no encontrado con id:', idEmpleado);
    }
}


function actualizarEmpleado() {
    const empleado = {
        idEmpleado: document.getElementById("idEmpleadoEditar").value,
        nombre: document.getElementById("nombreEditar").value,
        apellidoPaterno: document.getElementById("apellidoPaternoEditar").value,
        apellidoMaterno: document.getElementById("apellidoMaternoEditar").value,
        telefono: document.getElementById("telefonoEditar").value,
        domicilio: document.getElementById("domicilioEditar").value,
        curp: document.getElementById("curpEditar").value,
        edad: parseInt(document.getElementById("edadEditar").value),
        fechaIngreso: document.getElementById("fechaIngresoEditar").value,
        rol: document.getElementById("rolEditar").value,
        email: document.getElementById("emailEditar").value, // Asegurar que el email se incluye
        estatus: 1 // Asumimos que el empleado está activo
    };

    // Validaciones
    if (!validarEmpleado(empleado)) {
        return;
    }

    // Formatear los datos del empleado
    formatearEmpleado(empleado);

    fetch(`http://localhost:8084/estadiasNominaVDMJR/api/empleados/update`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(empleado)
    })
        .then(response => response.json())
        .then(() => {
            Swal.fire('Actualizado', 'El empleado ha sido actualizado exitosamente.', 'success');
            $('#modalEditarEmpleado').modal('hide');
            cargarEmpleados();
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire('Error', 'No se pudo actualizar el empleado.', 'error');
        });
}

function eliminarEmpleado() {
    const idEmpleado = document.getElementById("idEmpleadoEditar").value;
    if (!idEmpleado) {
        Swal.fire('Error', 'ID de empleado no encontrado.', 'error');
        return;
    }

    Swal.fire({
        title: '¿Estás seguro?',
        text: "No podrás revertir esto!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar!'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`http://localhost:8084/estadiasNominaVDMJR/api/empleados/delete?id=${idEmpleado}`, {
                method: 'POST'
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('HTTP status ' + response.status);
                    }
                    return response.json();
                })
                .then(data => {
                    Swal.fire(
                        'Eliminado!',
                        'El empleado ha sido eliminado.',
                        'success'
                    );
                    cargarEmpleados();
                    cargarInactivos(); // Actualizar la lista de empleados inactivos
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire('Error', 'No se pudo eliminar el empleado.', 'error');
                });
        }
    });
}


function cargarInactivos() {
    fetch("http://localhost:8084/estadiasNominaVDMJR/api/empleados/getInactivos")
        .then(response => response.json())
        .then(data => {
            empleadosInactivos = data;
            let mostrar = "";
            data.forEach(empleado => {
                let fechaSalida = empleado.fechaSalida;
                if (fechaSalida) {
                    fechaSalida = new Date(fechaSalida).toISOString().split('T')[0];
                } else {
                    fechaSalida = 'No disponible'; // Manejar cuando no hay fecha de salida
                }

                let fila = `
                <tr onclick="abrirModalEmpleadoInactivo(${empleado.idEmpleado})">
                    <td>${empleado.nombre}</td>
                    <td>${empleado.apellidoPaterno}</td>
                    <td>${empleado.apellidoMaterno}</td>
                    <td>${empleado.telefono}</td>
                    <td>${empleado.domicilio}</td>
                    <td>${empleado.curp}</td>
                    <td>${empleado.edad}</td>
                    <td>${empleado.rol}</td>
                    <td>${empleado.email}</td>
                    <td>${fechaSalida}</td>
                </tr>`;
                mostrar += fila;
            });
            document.getElementById("tablaEmpleadosInactivos").innerHTML = mostrar;
        })
        .catch(error => console.error('Error al cargar empleados inactivos:', error));
}

function toggleTables(tableType) {
    if (tableType === 'active') {
        document.getElementById('activeTable').style.display = 'block';
        document.getElementById('inactiveTable').style.display = 'none';
        cargarEmpleados();
    } else {
        document.getElementById('activeTable').style.display = 'none';
        document.getElementById('inactiveTable').style.display = 'block';
        cargarInactivos();
    }
}

function abrirModalEmpleadoInactivo(idEmpleado) {
    const empleado = empleadosInactivos.find(emp => emp.idEmpleado === idEmpleado);
    if (empleado) {
        document.getElementById("idEmpleadoInactivo").value = empleado.idEmpleado;
        document.getElementById("nombreInactivo").value = capitalizeWords(empleado.nombre);
        document.getElementById("apellidoPaternoInactivo").value = capitalizeWords(empleado.apellidoPaterno);
        document.getElementById("apellidoMaternoInactivo").value = capitalizeWords(empleado.apellidoMaterno);
        document.getElementById("telefonoInactivo").value = empleado.telefono;
        document.getElementById("domicilioInactivo").value = capitalizeWords(empleado.domicilio);
        document.getElementById("curpInactivo").value = empleado.curp.toUpperCase();
        document.getElementById("edadInactivo").value = empleado.edad;
        document.getElementById("rolInactivo").value = empleado.rol;
        document.getElementById("fechaSalidaInactivo").value = new Date(empleado.fechaSalida).toISOString().split('T')[0];

        $('#modalEmpleadoInactivo').modal('show');
    } else {
        console.error('Empleado inactivo no encontrado con id:', idEmpleado);
    }
}

function reactivarEmpleado(idEmpleado) {
    const empleado = {
        idEmpleado: idEmpleado,
        fechaIngreso: new Date().toISOString().split('T')[0],
        estatus: 1
    };

    fetch("http://localhost:8084/estadiasNominaVDMJR/api/empleados/reactivate", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(empleado)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('HTTP status ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            Swal.fire('Reactivado!', 'El empleado ha sido reactivado exitosamente.', 'success');
            $('#modalEmpleadoInactivo').modal('hide');
            cargarInactivos(); // Recargar la lista de empleados inactivos
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire('Error', 'No se pudo reactivar el empleado.', 'error');
        });
}
