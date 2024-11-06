import { createApp } from 'vue'; // Importar createApp desde Vue
import App from './App.vue'; // Importar el componente raíz
import router from './router'; // Importar el router

const app = createApp(App); // Crear la aplicación Vue
app.use(router); // Usar el router
app.mount('#app'); // Montar la aplicación en el DOM