import axios from 'axios';

// Crear una instancia de Axios
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/', // Cambia esto si tu backend no está en localhost
});

// Agregar un interceptor para incluir el token en cada solicitud
axiosInstance.interceptors.request.use(config => {
    const token = localStorage.getItem('token'); // Asegúrate de que el token esté guardado
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// Exportar la instancia de Axios
export default axiosInstance;
