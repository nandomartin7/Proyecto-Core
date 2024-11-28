import { createRouter, createWebHistory } from 'vue-router';
//Admin
import Index from '../components/Index.vue';
import InicioAdmin from '@/components/InicioAdmin.vue';
//Cliente
import LoginCliente from '@/components/LoginCliente.vue';
import InicioCliente from '@/components/InicioCliente.vue';
import ContratarPlanCliente from '@/components/ContratarPlanCliente.vue';
import MisContratos from '@/components/MisContratos.vue';
//Empleado
import LoginEmpleado from '@/components/LoginEmpleado.vue';
import InicioEmpleado from '@/components/InicioEmpleado.vue';
import PlanesSeguroEmpleado from '@/components/PlanesSeguroEmpleado.vue';
import ContratosEmpleado from '@/components/ContratosEmpleado.vue';
import UsosSeguroEmpleado from '@/components/UsosSeguroEmpleado.vue';
import AnalisisContrato from '@/components/AnalisisContrato.vue';

const routes = [
  {
    path: '/',
    name: 'LoginPage',
    component: Index
  },
  {
    path: '/admin/inicio',
    name: 'InicioAdmin',
    component: InicioAdmin,
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: '/login-cliente',
    name: 'LoginCliente',
    component: LoginCliente
  },
  {
    path: '/cliente/',
    name: 'InicioCliente',
    component: InicioCliente,
    meta: { requiresAuth: true, role: 'cliente' }
  },
  {
    path: '/cliente/contratar-plan',
    name: 'ContratarPlanCliente',
    component: ContratarPlanCliente,
    meta: { requiresAuth: true, role: 'cliente' }
  },
  {
    path: '/cliente/mis-contratos',
    name: 'MisContratos',
    component: MisContratos,
    meta: { requiresAuth: true, role: 'cliente' }
  },




  {
    path: '/login-empleado',
    name: 'LoginEmpleado',
    component: LoginEmpleado
  },
  {
    path: '/empleado/',
    name: 'InicioEmpleado',
    component: InicioEmpleado,
    meta: { requiresAuth: true, role: 'empleado' }
  },
  {
    path: '/empleado/planes-seguro',
    name: 'PlanesSeguroEmpleado',
    component: PlanesSeguroEmpleado,
    meta: { requiresAuth: true, role: 'empleado' }
  },
  {
    path: '/empleado/contratos',
    name: 'ContratosEmpleado',
    component: ContratosEmpleado,
    meta: { requiresAuth: true, role: 'empleado' }
  },
  {
    path: '/empleado/usos-seguro',
    name: 'UsosSeguroEmpleado',
    component: UsosSeguroEmpleado,
    meta: { requiresAuth: true, role: 'empleado' }
  },
  {
    path: '/empleado/analisis-contrato',
    name: 'AnalisisContrato',
    component: AnalisisContrato,
    meta: { requiresAuth: true, role: 'empleado' }
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