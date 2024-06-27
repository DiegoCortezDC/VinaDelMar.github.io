function cargarEmpleados() {
    fetch('http://localhost:8084/estadiasNominaVDMJR/api/empleados/getAll')
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById('empleadoSelect');
            data.forEach(empleado => {
                const option = document.createElement('option');
                option.value = empleado.idEmpleado;
                option.textContent = `${capitalizeWords(empleado.nombre)} ${capitalizeWords(empleado.apellidoPaterno)} ${capitalizeWords(empleado.apellidoMaterno)}`;
                option.dataset.email = empleado.email;
                select.appendChild(option);
            });
        })
        .catch(error => console.error('Error al cargar empleados:', error));
}

function capitalizeWords(string) {
    return string.replace(/\b\w/g, char => char.toUpperCase());
}

function calcularTotal() {
    const sueldoBase = parseFloat(document.getElementById("sueldoBase").value) || 0;
    const imss = parseFloat(document.getElementById("imss").value) || 0;
    const retardos = parseFloat(document.getElementById("retardos").value) || 0;
    const multas = parseFloat(document.getElementById("multas").value) || 0;
    const platosRotos = parseFloat(document.getElementById("platosRotos").value) || 0;
    const otros = parseFloat(document.getElementById("otros").value) || 0;

    const totalDeducciones = imss + retardos + multas + platosRotos + otros;
    const totalPagar = sueldoBase - totalDeducciones;
    document.getElementById("totalPagar").value = totalPagar.toFixed(2);
}

function guardarYEnviarNomina() {
    const select = document.getElementById('empleadoSelect');
    if (!select) {
        console.error('El elemento select no se encontró');
        return;
    }

    const selectedOption = select.options[select.selectedIndex];
    if (!selectedOption) {
        Swal.fire('Error', 'Por favor, selecciona un empleado.', 'error');
        return;
    }

    const email = selectedOption.dataset.email;
    if (!email) {
        Swal.fire('Error', 'El correo electrónico del empleado no se encontró.', 'error');
        return;
    }

    const sueldoBase = parseFloat(document.getElementById("sueldoBase").value);
    if (isNaN(sueldoBase) || sueldoBase <= 0) {
        Swal.fire('Error', 'El sueldo base debe ser un número positivo.', 'error');
        return;
    }

    const nomina = {
        idEmpleado: selectedOption.value,
        sueldoBase: sueldoBase,
        imss: parseFloat(document.getElementById("imss").value) || 0,
        retardos: parseFloat(document.getElementById("retardos").value) || 0,
        multas: parseFloat(document.getElementById("multas").value) || 0,
        platosRotos: parseFloat(document.getElementById("platosRotos").value) || 0,
        otros: parseFloat(document.getElementById("otros").value) || 0,
        totalPagar: parseFloat(document.getElementById("totalPagar").value),
        fecha: new Date().toISOString().split('T')[0],
        email: email
    };

    fetch('http://localhost:8084/estadiasNominaVDMJR/api/nominas/saveAndSend', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(nomina)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => Promise.reject(text));
        }
        return response.json();
    })
    .then(() => {
        Swal.fire('Enviado', 'La nómina ha sido guardada y enviada exitosamente.', 'success');
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire('Error', 'No se pudo guardar y enviar la nómina: ' + error, 'error');
    });
}
