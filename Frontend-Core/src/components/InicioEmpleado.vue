<template>
  <!-- Encabezado -->
  <header class="header">
    <h1>Bienvenido</h1>
    <button @click="navPlanesSeguro" class="btn-nav">Planes de Seguro</button>
    <button @click="navContratos" class="btn-nav">Contratos</button>
    <button @click="navUsosSeguro" class="btn-nav">Usos del Seguro</button>
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
          <button @click="verMasInformacionPlanes" class="btn-mas-informacion">Más Información de Planes</button>
        </div>
      </div>
      <button @click="nextSlide" class="carrusel-btn next">&gt;</button>
    </div>
  </div>

  <div class="graficos-flexbox">
    <!-- Contenedor del Gráfico de Contratos-->
    <div class="grafico-container">
      <h2>Proporción de Contratos por Plan</h2>
      <canvas id="contratosChart" width="300" height="300"></canvas>
      <button @click="verMasInformacionContratos" class="btn-mas-informacion">Más Información de Contratos</button>
    </div>

    <!-- Contenedor del Gráfico de Usos-->
    <div class="grafico-usos-container">
      <h2>Frecuencia de Tipos de Uso del Seguro</h2>
      <canvas id="usosChart"></canvas>
      <button @click="verMasInformacionUsos" class="btn-mas-informacion">Más Información de Usos de Seguro</button>
    </div>
  </div>
</template>
  
<script>
import axios from "axios";
import Chart from "chart.js/auto";

export default {
  data() {
    return {
      planes: [], // Almacenará los datos de los planes
      currentSlide: 0, // Controla la posición actual del carrusel
      contratos: [],
      contratosChart: null,
      usos: [],
      usosChart: null,
    };
  },
  methods: {
    //Metodos la seccion del Carrusel de planes
    fetchPlanes() {
      axios.get("http://localhost:8080/plan")
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
      this.$router.push("/empleado/planes-seguro");
    },

    //Métodos para el gráfico de contratos
    fetchContratos() {
      axios.get("http://localhost:8080/contrato")
      .then((response) => {
        this.contratos = response.data;
        this.renderContratosChart(); // Renderizar el gráfico después de obtener los datos
      })
      .catch((error) => {
        console.error("Error al obtener contratos:", error);
      });
    },
    
    renderContratosChart() {
      const contratosPorPlan = {};
      const nombresPlanes = {};

      // Agrupar contratos por plan y contar
      this.contratos.forEach((contrato) => {
        const idPlan = contrato.planSeguro.idPlan;
        contratosPorPlan[idPlan] = (contratosPorPlan[idPlan] || 0) + 1;
        nombresPlanes[idPlan] = contrato.planSeguro.nombre;
      });

      // Preparar datos para el gráfico
      const labels = Object.keys(contratosPorPlan).map(
        (idPlan) => nombresPlanes[idPlan]
      );
      const data = Object.values(contratosPorPlan);

      // Renderizar el gráfico con Chart.js
      const ctx = document.getElementById("contratosChart").getContext("2d");
      if (this.contratosChart) this.contratosChart.destroy(); // Destruir gráfico previo si existe
      this.contratosChart = new Chart(ctx, {
        type: "pie",
        data: {
          labels: labels,
          datasets: [
            {
              label: "Contratos por Plan",
              data: data,
              backgroundColor: [
                "#FF6384",
                "#36A2EB",
                "#FFCE56",
                "#4BC0C0",
                "#9966FF",
                "#FF9F40",
              ],
              hoverOffset: 4,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: true,
          aspectRatio: 1.5,
          plugins: {
            tooltip: {
              callbacks: {
                label: function (context) {
                  const label = context.label || "";
                  const value = context.raw || 0;
                  const total = context.dataset.data.reduce(
                    (sum, val) => sum + val,
                    0
                  );
                  const percentage = ((value / total) * 100).toFixed(2);
                  return `${label}: ${value} contratos (${percentage}%)`;
                },
              },
            },
          },
        },
      });
    },
    verMasInformacionContratos() {
      this.$router.push("/empleado/contratos");
    },

    //Métodos para el gráfico de Usos del Seguro 
    fetchUsos() {
      axios.get("http://localhost:8080/uso") // Cambia por el endpoint correcto
      .then((response) => {
        this.usos = response.data;
        this.renderUsosChart(); // Renderizar el gráfico después de obtener los datos
      })
      .catch((error) => {
        console.error("Error al obtener los usos:", error);
      });
    },
    procesarUsos(data) {
      // Inicializar el contador para cada tipo de uso
      const tiposDeUso = {
        "Choque grave": 0,
        "Choque leve": 0,
        "Daño electrico": 0,
        "Daño mecanico no grave": 0,
        "Mantenimiento": 0,
        "Auxilio mecanico": 0,
      };

      // Recorrer la lista y contar cada uso
      data.forEach((uso) => {
        if (uso.tipoUso in tiposDeUso) { // Usar "in" para comprobar existencia de claves
          tiposDeUso[uso.tipoUso]++;
        }
      });

      // Convertir el objeto en un arreglo de objetos
      return Object.keys(tiposDeUso).map((key) => ({
        tipoUso: key,
        cantidad: tiposDeUso[key],
      }));
    },

    renderUsosChart() {
      const usosProcesados = this.procesarUsos(this.usos);
      const labels = usosProcesados.map((uso) => uso.tipoUso);
      const data = usosProcesados.map((uso) => uso.cantidad);

      // Renderizar el gráfico con Chart.js
      const ctx = document.getElementById("usosChart").getContext("2d");
      if (this.usosChart) this.usosChart.destroy(); // Destruir gráfico previo si existe
      this.usosChart = new Chart(ctx, {
        type: "bar",
        data: {
          labels: labels,
          datasets: [
            {
              label: "Frecuencia de Tipos de Uso",
              data: data,
              backgroundColor: [
                "#FF6384",
                "#36A2EB",
                "#FFCE56",
                "#4BC0C0",
                "#9966FF",
                "#FF9F40",
              ],
              borderColor: "#000",
              borderWidth: 1,
            },
          ],
        },
        options: {
          indexAxis: "y", // Gráfico horizontal
          responsive: true,
          scales: {
            x: {
              beginAtZero: true,
            },
          },
          plugins: {
            tooltip: {
              callbacks: {
                label: function (context) {
                  return `${context.label}: ${context.raw} usos`;
                },
              },
            },
          },
        },
      });
    },
    verMasInformacionUsos() {
      this.$router.push("/empleado/usos-seguro");
    },

    navPlanesSeguro() {
      this.$router.push("/empleado/planes-seguro");
    },
    navContratos() {
      this.$router.push("/empleado/contratos");
    },
    navUsosSeguro() {
      this.$router.push("/empleado/usos-seguro");
    },
    cerrarSesion() {
      localStorage.clear();
      this.$router.push("/login-empleado");
    },
  },
  mounted() {
    this.fetchPlanes();
    this.fetchContratos();
    this.fetchUsos();
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
    justify-content: space-between; /* Espacio entre columnas */
    align-items: flex-start; /* Alinear elementos al inicio vertical */
    gap: 20px; /* Espaciado entre las columnas */
    flex-wrap: wrap; /* Permite que los elementos se ajusten si la pantalla es pequeña */
  }

  .grafico-container,
  .grafico-usos-container {
    flex: 1; /* Cada columna ocupa el mismo espacio */
    max-width: 48%; /* Asegura que cada gráfico no supere el 48% del ancho */
    text-align: center;
  }

  @media (max-width: 768px) {
  .graficos-flexbox {
    flex-direction: column; /* Cambia de filas a columnas */
    align-items: center; /* Centra los elementos */
  }

  .grafico-container,
  .grafico-usos-container {
    max-width: 100%; /* Ocupan todo el ancho */
  }
}


  /* Contenedor del Gráfico Contrato*/
  .grafico-container {
    margin: 20px auto;
    text-align: center;
    max-width: 600px; /* Limitar el ancho máximo */
    padding: 10px;
    background-color: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }

  .grafico-container h2 {
    font-size: 20px;
    margin-bottom: 15px;
  }

  #grafico-container canvas {
    max-width: 400px;
    max-height: 400px;
    width: 100%; /* Asegurar que el gráfico se ajuste */
    height: auto;
  }

  /*Contenedor del Gráfico Usos*/
  .grafico-usos-container {
    margin: 20px auto;
    max-width: 600px; /* Limitar el ancho */
    height: 500px;
    text-align: center;
    align-content: center;
    padding: 10px;
    background-color: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }

  .grafico-usos-container h2 {
    font-size: 20px;
    margin-bottom: 15px;
  }

  #grafico-usos-container canvas {
    width: 100%;
    height: auto;
  }
</style>
