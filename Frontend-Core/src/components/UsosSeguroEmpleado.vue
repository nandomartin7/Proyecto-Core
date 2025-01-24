<template>
  <header class="header">
    <button @click="navInicio" class="btn-nav">Inicio</button>
    <button @click="navPlanesSeguro" class="btn-nav">Planes de Seguro</button>
    <button @click="navContratos" class="btn-nav">Contratos</button>
    <button @click="navUsosSeguro" class="btn-nav">Usos del Seguro</button>
    <button @click="cerrarSesion" class="btn-cerrar-sesion">Cerrar Sesión</button>
  </header>

  <!-- Tabla principal de usos del seguro -->
  <div>
    <h1>Usos del Seguro</h1>
    <h2>Lista General de Usos del Seguro</h2>
    <table v-if="usos.length > 0" class="tabla">
      <thead>
        <tr>
          <th>ID</th>
          <th>Contrato</th>
          <th>Fecha</th>
          <th>Tipo de Uso</th>
          <th>Descripcion</th>
          <th>Monto Aprobado</th>
          <th>Estado Reclamo</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="uso in usos" :key="uso.idUso">
          <td>{{ uso.idUso }}</td>
          <td>IdContrato: {{ uso.contrato.idContrato }} <br>  
              Placa: {{ uso.contrato.automovil.idAutomovil }} <br>  
              Cliente: {{ uso.contrato.cliente.nombre }} {{ uso.contrato.cliente.apellido }} <br> 
              Plan: {{ uso.contrato.planSeguro.nombre }}</td>
          <td>{{ uso.fecha }}</td>
          <td>{{ uso.tipoUso }}</td>
          <td>{{ uso.descripcion }}</td>
          <td>${{ uso.montoAprobado }}</td>
          <td>{{ uso.estadoReclamo }}</td>
          <td><button @click="editarUso(uso)" class="btnEditarUso">Editar</button></td>
        </tr>
      </tbody>
    </table>

    <!--Modal para editar Uso del Seguro-->
    <div v-if="usoEditando" class="editarUso-contenedor">
      <h3>Editar Uso del Seguro</h3>
      <p>ID: {{ usoEditando.idUso }}</p>
      <p>Contrato: {{ usoEditando.contrato.idContrato }}
        Placa: {{ usoEditando.contrato.automovil.idAutomovil }}  
        Cliente: {{ usoEditando.contrato.cliente.nombre }} {{ usoEditando.contrato.cliente.apellido }} 
        Plan: {{ usoEditando.contrato.planSeguro.nombre }}</p>
      <p>Fecha: {{ usoEditando.fecha }}</p>
      <p>
        <label>Tipo de Uso : 
          <select v-model="usoEditando.tipoUso" required>
            <option value="Choque grave">Choque Grave</option>
            <option value="Choque leve">Choque Leve</option>
            <option value="Daño electrico">Daño Electrico</option>
            <option value="Daño mecanico no grave">Daño Mecanico</option>
            <option value="Mantenimiento">Mantenimiento</option>
            <option value="Auxilio Mecánico">Auxilio Mecanico</option>
          </select>
        </label>
      </p>
      <p><label>Descripcion: <input v-model="usoEditando.descripcion" required /></label></p>

      <p><label>Monto Aprobado: <input type="number" v-model="usoEditando.montoAprobado" step="0.01" required/></label></p>
      <p>
        <label>Estado de Reclamo : 
          <select v-model="usoEditando.estadoReclamo" required>
            <option value="Aprobado">Aprobado</option>
            <option value="Pendiente">Pendiente</option>
            <option value="Rechazado">Rechazado</option>
          </select>
        </label>
      </p>
      <button @click="guardarCambiosUso">Guardar</button>
      <button @click="cancelarEdicion">Cancelar</button>
    </div>
  </div>

  <!-- Filtro por fechas -->
  <div class="filtrarFechas-contenedor">
    <h2>Filtrar Usos del Seguro por Fechas</h2>
    <div>
      <label>Fecha Inicio:</label>
      <input type="date" v-model="filtroFechaInicio" />
      <label>Fecha Fin:</label>
      <input type="date" v-model="filtroFechaFin" />
      <button @click="filtrarPorFechas" class="btnFiltrar">Filtrar</button>
      <button @click="cancelarFiltroFechas" class="btnCancelar">Cancelar</button>
    </div>
    <p v-if="mensajeErrorFechas" class="mensajeError">{{ mensajeErrorFechas }}</p>
    <table v-if="usosFiltradosPorFechas.length > 0">
      <thead>
        <tr>
          <th>ID</th>
          <th>Contrato</th>
          <th>Fecha</th>
          <th>Tipo de Uso</th>
          <th>Descripcion</th>
          <th>Monto Aprobado</th>
          <th>Estado Reclamo</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="uso in usosFiltradosPorFechas" :key="uso.idUso">
          <td>{{ uso.idUso }}</td>
          <td>Contrato: {{ uso.contrato.idContrato }} <br>  
              Placa: {{ uso.contrato.automovil.idAutomovil }} <br>  
              Cliente: {{ uso.contrato.cliente.nombre }} {{ uso.contrato.cliente.apellido }} <br> 
              Plan: {{ uso.contrato.planSeguro.nombre }}</td>
          <td>{{ uso.fecha }}</td>
          <td>{{ uso.tipoUso }}</td>
          <td>{{ uso.descripcion }}</td>
          <td>${{ uso.montoAprobado }}</td>
          <td>{{ uso.estadoReclamo }}</td>
        </tr>
      </tbody>
    </table>
  </div>

  <!-- Filtro por contrato -->
  <div class="filtrarContrato-contenedor">
    <h2>Filtrar Usos por Contrato</h2>
    <div class="filtro-contrato">
      <label>ID Contrato:</label>
      <select v-model="filtroContrato" id="filtroContrato">
        <option v-for="contrato in contratos" :key="contrato.idContrato" :value="contrato.idContrato">Contrato:{{ contrato.idContrato }}   Placa:{{ contrato.automovil.idAutomovil }}</option>
      </select>
      <button @click="filtrarPorContrato" class="btnFiltrar">Filtrar</button>
      <button @click="cancelarFiltroContrato" class="btnCancelar">Cancelar</button>
    </div>
    <p v-if="mensajeErrorContrato" class="mensajeError">{{ mensajeErrorContrato }}</p>
    <table v-if="usosFiltradosPorContrato.length > 0">
      <thead>
        <tr>
          <th>ID</th>
          <th>Contrato</th>
          <th>Fecha</th>
          <th>Tipo de Uso</th>
          <th>Descripcion</th>
          <th>Monto Aprobado</th>
          <th>Estado Reclamo</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="uso in usosFiltradosPorContrato" :key="uso.idUso">
          <td>{{ uso.idUso }}</td>
          <td>Contrato: {{ uso.contrato.idContrato }} <br>  
              Placa: {{ uso.contrato.automovil.idAutomovil }} <br>  
              Cliente: {{ uso.contrato.cliente.nombre }} {{ uso.contrato.cliente.apellido }} <br> 
              Plan: {{ uso.contrato.planSeguro.nombre }}</td>
          <td>{{ uso.fecha }}</td>
          <td>{{ uso.tipoUso }}</td>
          <td>{{ uso.descripcion }}</td>
          <td>${{ uso.montoAprobado }}</td>
          <td>{{ uso.estadoReclamo }}</td>
        </tr>
      </tbody>
    </table>
  </div>

</template>

<script>
import axios from "axios";

export default {
    
  data() {
    return {
      usos: [],
      contratos: [],
      usosFiltradosPorFechas: [],
      usosFiltradosPorContrato: [],
      filtroFechaInicio: "",
      filtroFechaFin: "",
      filtroContrato: "",
      usoEditando: null,
      mensajeErrorFechas: "",
      mensajeErrorContrato: "",
    };
  },
  methods: {
    fetchUsos() {
      axios.get("http://localhost:8080/uso")
      .then((response) => {
        this.usos = response.data;
      })
      .catch((error) => {
        console.error("Error al obtener usos del seguro:", error);
      });
    },
    fetchContratos(){
      axios.get("http://localhost:8080/contrato")
      .then((response) => {
        this.contratos = response.data;
      })
      .catch((error) => {
        console.error("Error al obtener contratos:", error);
      });
    },
    editarUso(uso){
      this.usoEditando = { ...uso };
    },
    guardarCambiosUso(){
      axios.put(`http://localhost:8080/uso/${this.usoEditando.idUso}`, this.usoEditando)
        .then(response => {
          this.fetchUsos();
          this.cancelarEdicion();
          const index =  this.usoEditando.idUso;
          if (index !== -1){
            this.usos[index] = response.data;
          }
          this.usoEditando = null;
        })
        .catch(error => {
          console.error("Error actualizando uso de seguro:", error);
        });  
    },
    cancelarEdicion() {
      this.usoEditando = null;
    },

    filtrarPorFechas() {
      this.mensajeErrorFechas = "";
      if (!this.filtroFechaInicio || !this.filtroFechaFin) {
        console.error('Debe seleccionar ambas fechas');
        this.mensajeErrorFechas = "Debe seleccionar ambas fechas. "
        return;
      }
      
      axios.get("http://localhost:8080/uso/filtrar/fecha", {
          params: {
            fechaInicio: this.filtroFechaInicio,
            fechaFin: this.filtroFechaFin,
          },
        })
        .then((response) => {
          this.usosFiltradosPorFechas = response.data;
          if (this.usosFiltradosPorFechas.length === 0){
            this.mensajeErrorFechas = "No hay usos del seguro en ese periodo de tiempo"
          } else {
            this.mensajeErrorFechas = "";
          }
        })
        .catch((error) => {
          console.error("Error al filtrar por fechas:", error);
          this.mensajeErrorFechas = "Error al realizar la búsqueda."
          this.usosFiltradosPorFechas = [];
        });
    },
    cancelarFiltroFechas() {
      this.filtroFechaInicio = "";
      this.filtroFechaFin = "";
      this.usosFiltradosPorFechas = [];
      this.mensajeErrorFechas = "";
    },
    filtrarPorContrato() {
      this.mensajeErrorContrato = "";
      if (this.filtroContrato) {
        axios.get("http://localhost:8080/uso/filtrar/contrato", {
          params: { idContrato: this.filtroContrato },
        })
        .then((response) => {
          this.usosFiltradosPorContrato = response.data;
          if( this.usosFiltradosPorContrato.length === 0){
            this.mensajeErrorContrato = "No hay usos del seguro registrados con ese contrato"
          } else{
            this.mensajeErrorContrato = "";
          }
        })
        .catch((error) => {
          console.error("Error al filtrar por contrato:", error);
          this.mensajeErrorContrato = "Error al realizar la búsqueda.";
          this.usosFiltradosPorContrato = [];
        });
      } else {
        this.mensajeErrorContrato = "Por favor, selecciona un contrato. ";
      }
      
    },
    cancelarFiltroContrato() {
      this.filtroContrato = "";
      this.usosFiltradosPorContrato = [];
      this.mensajeErrorContrato = "";
    },
    navInicio(){
      this.$router.push("/empleado")
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
      // Lógica para cerrar sesión
      localStorage.clear();
      this.$router.push("/login-empleado");
    },
  },
  mounted() {
    this.fetchUsos();
    this.fetchContratos();
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

  /* Título */
  h1 {
    text-align: center;
    color: #333;
  }

  /* Tabla */
  table {
    width: 100%;
    border-collapse: collapse;
    margin: 20px 0;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }

  thead {
    background-color: #333;
    color: white;
  }

  th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
  }

  th {
    background-color: #333;
    color: white;
  }

  tbody tr:nth-child(even) {
    background-color: #f9f9f9;
  }

  tbody tr:hover {
    background-color: #f1f1f1;
  }

  /* Botones de acción */
  button {
    padding: 8px 12px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .btnEditarUso {
    background-color: #ffc107;
    color: black;
  }

  .btnEditarUso:hover {
    background-color: #e0a800;
  }

  /* Modal */
  .editarUso-contenedor {
    background-color: #fff;
    border-radius: 5px;
    padding: 20px;
    margin: 20px auto;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    width: 80%;
  }

  .editarUso-contenedor h3 {
    margin-top: 0;
    text-align: center;
  }

  .editarUso-contenedor p {
    font-size: 16px;
    margin: 10px 0;
  }

/* Filtros */
  .filtrarFechas-contenedor, .filtrarContrato-contenedor {
    margin: 20px 0;
    padding: 15px;
    border: 1px solid #ddd;
    border-radius: 5px;
    background-color: #f9f9f9;
    text-align: center;
  }

  .filtrarFechas-contenedor h2, .filtrarContrato-contenedor h2 {
    margin-top: 0;
    text-align: center;
  }

  .filtrarFechas-contenedor label, .filtrarContrato-contenedor label {
    margin-right: 10px;
    font-weight: bold;
  }

  .mensajeError {
    color: red;
    font-weight: bold;
    text-align: center;
    margin-top: 10px;
  }
</style>