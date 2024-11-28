<template>
  <!-- Encabezado -->
  <header class="header">
    <h1>Bienvenido</h1>
    <button @click="navContratarPlanSeguro" class="btn-nav">Contratar Plan de Seguro</button>
    <button @click="navContratos" class="btn-nav">Mis Contratos</button>
    <button @click="cerrarSesion" class="btn-cerrar-sesion">Cerrar Sesión</button>
  </header>

  <!-- Carrusel de Planes -->
  <div class="carrusel-container">
    <h2 class="carrusel-title">Explora Nuestros Planes</h2>
    <div class="carrusel">
      <button @click="prevSlide" class="carrusel-btn prev">&lt;</button>
      <div class="carrusel-slide">
        <div
          v-for="(plan, index) in planes"
          :key="plan.idPlan"
          class="carrusel-card"
          :class="{ active: index === currentSlide }"
        >
          <h3>{{ plan.nombre }}</h3>
          <ul>
            <li>Perdidas Parciales: {{ plan.perdidasParciales ? 'Incluido' : 'No Incluido' }}</li>
            <li>Perdidas Totales: {{ plan.perdidasTotales ? 'Incluido' : 'No Incluido' }}</li>
            <li>Auxilios Mecánicos: {{ plan.auxilioMecanico ? 'Incluido' : 'No Incluido' }}</li>
            <li>Mantenimiento Vehicular: {{ plan.mantenimientoVehicular ? 'Incluido' : 'No Incluido' }}</li>
          </ul>
          <p class="carrusel-price">Precio: ${{ plan.valorPlan }} anuales</p>
          <button @click="verMasInformacionPlanes" class="btn-mas-informacion">Contratar un nuevo Plan</button>
        </div>
      </div>
      <button @click="nextSlide" class="carrusel-btn next">&gt;</button>
    </div>
  </div>

  <h2 class="contratos-title">Mis Contratos</h2>
  <!--Contenedor para Contratos-->
  <div class="graficos-flexbox">
    <div v-for="contrato in contratos" :key="contrato.idContrato" class="contrato-contenedor">
      <h3>Contrato</h3>
      <ul>
        <li>Numero de Contrato: {{ contrato.idContrato }}</li>
        <li>Fecha de Inicio del Contrato: {{ contrato.fechaInicio }}</li>
        <li>Fecha de Finalización del Contrato: {{ contrato.fechaFinalizacion }}</li>
        <li>Precio Total del Contrato: ${{ contrato.valortotal }}</li>
      </ul>
      <h3>Automovil</h3>
      <ul>
        <li>Placa: {{ contrato.automovil.idAutomovil }}</li>
        <li>Marca: {{ contrato.automovil.marca }}</li>
        <li>Modelo: {{ contrato.automovil.modelo }}</li>
        <li>Año: {{ contrato.automovil.anio }}</li>
      </ul>
      <h3>Plan Seguro</h3>
      <ul>
        <li>Nombre: {{ contrato.planSeguro.nombre }}</li>
        <li>Valor del Plan: ${{ contrato.planSeguro.valorPlan }}</li>
      </ul>
    </div>
  </div>
</template>
  
<script>
import axios from "axios";

export default {
  data() {
    return {
      planes: [], // Almacenará los datos de los planes
      currentSlide: 0, // Controla la posición actual del carrusel
      contratos: [],
      clienteActual: '',
    };
  },
  methods: {
    //Metodos la seccion del Carrusel de planes
    fetchPlanes() {
      axios.get("https://proyecto-core-backend-production.up.railway.app/plan")
      .then((response) => {
        this.planes = response.data;
      })
      .catch((error) => {
        console.error("Error al obtener planes:", error);
      });
    },
    nextSlide() {
      this.currentSlide = (this.currentSlide + 1) % this.planes.length;
    },
    prevSlide() {
      this.currentSlide =
        (this.currentSlide - 1 + this.planes.length) % this.planes.length;
    },
    autoSlide(){
      setInterval(() => {
        this.nextSlide();
      }, 5000);
    },
    verMasInformacionPlanes() {
      this.$router.push("cliente/contratar-plan");
    },

    fetchContratos(){
      const cliente = JSON.parse(localStorage.getItem('cliente'));
      this.clienteActual = cliente;
      this.idCliente = this.clienteActual.idCliente;

      axios.get("https://proyecto-core-backend-production.up.railway.app/contrato/filtrar/cliente",{
        params: { idcliente: this.idCliente },
      })
      .then((response) => {
        this.contratos = response.data;
      })
      .catch((error) => {
        console.error("Error al obtener contratos:", error);
      })
    },

    

    navContratarPlanSeguro() {
      this.$router.push("/cliente/contratar-plan");
    },
    navContratos() {
      this.$router.push("/cliente/mis-contratos");
    },
    cerrarSesion() {
      localStorage.clear();
      this.$router.push("/login-cliente");
    },
  },
  mounted() {
    this.fetchPlanes();
    this.fetchContratos();
    this.autoSlide();
  },
};
</script>


<style scoped>
  /* General */
  body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f4f4f4;
  }

  /* Encabezado */
  .header {
    background-color: #333;
    color: #fff;
    padding: 15px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .header h1 {
    color: #fff;
    margin: 0;
    font-size: 20px;
  }

  .btn-nav {
    background-color: #333;
    color: #fff;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
  }

  .btn-nav:hover {
    background-color: #615e5e;
  }

  .btn-cerrar-sesion {
    background-color: #ff1a1a;
    color: #fff;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
  }

  .btn-cerrar-sesion:hover {
    background-color: #ff4d4d;
  }

  /* Contenedor principal */
  div {
    padding: 20px;
  }

  /* Carrusel */
  .carrusel-container {
    font-family: Arial, sans-serif;
    padding: 20px;
    text-align: center;
  }

  .carrusel-title {
    font-size: 24px;
    margin-bottom: 20px;
  }

  .carrusel {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    max-width: 100%;
  }

  .carrusel-slide {
    background-color: #f9f9f9;
    border-radius: 5px;
    flex: 0.4;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    /*position: relative;*/
  }

  .carrusel-card {
    flex: 0 0 100%; /* Cada tarjeta ocupa todo el ancho del contenedor */
    text-align: center;
    opacity: 0;
    transform: scale(0.8);
    transition: opacity 0.5s ease-in-out, transform 0.5s ease-in-out;
    display: none; /* Ocultar todos los elementos por defecto */
  }

  .carrusel-card.active {
    opacity: 1;
    transform: scale(1);
    display: block; /* Mostrar solo el plan activo */
  }

  .carrusel-card.active ul{
    padding: 0;
    list-style-type: none;
  }

  .carrusel-btn {
    background-color: #333;
    color: white;
    border: none;
    border-radius: 50%;
    font-size: 18px;
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }

  .carrusel-btn:hover {
    background-color: #555;
  }

  .carrusel-btn.prev {
    position: absolute;
    left: 400px;
    transform: translateY(-50%);
  }

  .carrusel-btn.next {
    position: absolute;
    right: 400px;
    transform: translateY(-50%);
  }

  .carrusel-price {
    font-size: 16px;
    font-weight: bold;
    margin-top: 10px;
    color: #333;
  }

  .btn-mas-informacion {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    margin-top: 10px;
  }

  .btn-mas-informacion:hover {
    background-color: #0056b3;
  }

  .plan-price {
    font-size: 18px;
    color: #333;
    font-weight: bold;
    margin-top: 10px;
  }

  /* FlexBox de graficos */
  .graficos-flexbox {
    display: flex;
    justify-content: space-evenly ; /* Espacio entre columnas */
    align-items: flex-start; /* Alinear elementos al inicio vertical */
    gap: 20px; /* Espaciado entre las columnas */
    flex-wrap: wrap; /* Permite que los elementos se ajusten si la pantalla es pequeña */
  }

  @media (max-width: 768px) {
    .graficos-flexbox {
      flex-direction: column; /* Cambia de filas a columnas */
      align-items: center; /* Centra los elementos */
    }
  }

  .contratos-title{
    text-align: center;
    font-size: 24px;
  }

  .planes-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    margin: 20px;
  }
  
  .contrato-contenedor {
    border: 1px solid #ddd;
    padding: 15px;
    background-color: #f9f9f9;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .contrato-contenedor h3 {
    margin-top: 0;
    text-align: center;
  }
  
  .contrato-contenedor ul {
    padding-left: 20px;
    list-style-type: none;
  }
  
  .contrato-contenedor p {
    font-size: 18px;
    font-weight: bold;
    text-align: center;
  }
  
</style>
