// Función para capitalizar la primera letra de cada palabra
function capitalizeWords(string) {
    return string.replace(/\b\w/g, char => char.toUpperCase());
}

// Evento para manejar el login
document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('loginButton').addEventListener('click', function(event) {
        event.preventDefault();  // Prevenir el comportamiento por defecto del formulario

        const usuario = document.getElementById('usuario').value;
        const password = document.getElementById('password').value;

        if (!usuario || !password) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor, completa todos los campos.'
            });
            return;
        }

        fetch('http://localhost:8084/estadiasNominaVDMJR/api/acceso/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `usuario=${encodeURIComponent(usuario)}&password=${encodeURIComponent(password)}`
        })
        .then(response => {
            if (!response.ok) {
                response.json().then(data => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: data.message || 'Contraseña o usuario incorrecto'
                    });
                });
                throw new Error('Solicitud no fue exitosa.');
            }
            return response.json();
        })
        .then(data => {
            if (data.status === 'success') {
                localStorage.setItem('token', data.token);  // Guardar el token en localStorage
                Swal.fire({
                    icon: 'success',
                    title: '¡Bienvenido!',
                    text: 'Autenticación exitosa, serás redirigido.',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    window.location.href = 'menuPrincipal.html';
                });
            } else {
                Swal.fire({
                    icon: 'warning',
                    title: 'Acceso Denegado',
                    text: 'No tienes permiso para acceder a esta sección.'
                });
            }
        })
        .catch(error => {
            console.error('Error durante el proceso de login:', error);
        });
    });

    // Evento para manejar el registro de usuario
    document.getElementById('signUp').addEventListener('click', function(event) {
        event.preventDefault();  // Prevenir el comportamiento por defecto del formulario

        const user = capitalizeWords(document.getElementById('user').value);
        const pass = document.getElementById('pass').value;
        const email = document.getElementById('email').value;
        const rol = capitalizeWords(document.getElementById('rol').value);

        if (!user || !pass || !email || !rol) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor, completa todos los campos.'
            });
            return;
        }

        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor, ingresa un correo electrónico válido.'
            });
            return;
        }

        const usuario = {
            nombreUsuario: user,
            contrasena: pass,
            email: email,
            rol: rol
        };

        fetch('http://localhost:8084/estadiasNominaVDMJR/api/usuarios/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(usuario)
        })
        .then(response => {
            if (!response.ok) {
                response.json().then(data => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: data.message || 'Error al registrar usuario'
                    });
                });
                throw new Error('Solicitud no fue exitosa.');
            }
            return response.json();
        })
        .then(data => {
            if (data.status === 'success') {
                Swal.fire({
                    icon: 'success',
                    title: '¡Registro Exitoso!',
                    text: 'Usuario registrado correctamente.',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    $('#miModal').modal('hide');
                });
            } else {
                Swal.fire({
                    icon: 'warning',
                    title: 'Registro Denegado',
                    text: 'No se pudo registrar el usuario.'
                });
            }
        })
        .catch(error => {
            console.error('Error durante el proceso de registro:', error);
        });
    });
});
