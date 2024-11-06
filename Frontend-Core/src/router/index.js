import { createRouter, createWebHistory } from 'vue-router';
import Index from '../components/Index.vue';
import InicioAdmin from '@/components/InicioAdmin.vue';

const routes = [
  {
    path: '/',
    name: 'LoginPage',
    component: Index
  },
  {
    path: '/inicio-admin',
    name: 'InicioAdmin',
    component: InicioAdmin,
    meta: { requiresAuth: true, role: 'admin' }
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

// Proteger las rutas
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (to.meta.requiresAuth) {
    if(!token){
      next({ name: 'LoginPage' }); // Redirigir a Login si no hay token
    } else if (to.meta.role && role !== to.meta.role){
      next({ name: 'LoginPage' });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;