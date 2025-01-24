<template>
    <div>
        <!-- Encabezado -->
        <header class="header">
            <button @click="navInicioCliente" class="btn-nav">Inicio</button>
            <button @click="navContratarPlanSeguro" class="btn-nav">Contratar Plan de Seguro</button>
            <button @click="navContratos" class="btn-nav">Mis Contratos</button>
            <button @click="cerrarSesion" class="btn-cerrar-sesion">Cerrar Sesión</button>
        </header>
  
        <!-- Primera sección -->
        <h2>Actividad reciente</h2>
        <div class="destacados-container">
            <div class="destacado">
            <h3>Último Contrato Registrado</h3>
            <ul v-if="ultimoContrato">
                <li>Numero de Contrato: {{ ultimoContrato.idContrato }}</li>
                <li>Fecha Inicio: {{ ultimoContrato.fechaInicio }}</li>
                <li>Fecha Finalización: {{ ultimoContrato.fechaFinalizacion }}</li>
                <li>Valor Total: ${{ ultimoContrato.valortotal }}</li>
            </ul>
            </div>
            <div class="destacado">
            <h3>Último Uso del Seguro</h3>
            <ul v-if="ultimoUso">
                <li>Contrato ID: {{ ultimoUso.contrato.idContrato }}</li>
                <li>Fecha: {{ ultimoUso.fecha }}</li>
                <li>Tipo de Uso: {{ ultimoUso.tipoUso }}</li>
                <li>Estado: {{ ultimoUso.estadoReclamo }}</li>
            </ul>
            </div>
        </div>
    
        <!-- Segunda sección -->
        <div class="contratos-section">
            <h2>Mis Contratos</h2>
            <div class="contratos-grid">
            <div v-for="contrato in contratos" :key="contrato.idContrato" class="contrato-card">
                <h3>Contrato {{ contrato.idContrato }}</h3>
                <ul>
                    <li>Automovil con placa: {{ contrato.automovil.idAutomovil }}</li>
                    <li>Plan de Seguro: {{ contrato.planSeguro.nombre }}</li>
                    <li>Fecha Inicio: {{ contrato.fechaInicio }}</li>
                    <li>Fecha Finalización: {{ contrato.fechaFinalizacion }}</li>
                    <li>Valores Agregados: ${{ contrato.valoresAgregados }}</li>
                    <li>Motivo de Valores Agregados: {{ contrato.motivoAgregados }}</li>
                    <li>Valor Subtotal: ${{ contrato.valorsubtotal }}</li>
                    <li>Valor Total: ${{ contrato.valortotal }}</li>
                </ul>
    
                <!-- Acordeón para los usos -->
                <details class="detalleUsos">
                    <summary>Detalles de Usos del Seguro</summary>
                    <ul>
                        <li v-for="uso in usosPorContrato[contrato.idContrato]" :key="uso.idUso">
                            Fecha: {{ uso.fecha }} | Tipo: {{ uso.tipoUso }} | Estado: {{ uso.estadoReclamo }} <br>
                            Descripción: {{ uso.descripcion }} <br>
                        </li>
                    </ul>
                </details>
    
                <!-- Botón para solicitar un nuevo uso -->
                <button @click="abrirFormulario(contrato)" class="btn-nuevo-uso">Solicitar nuevo uso del seguro</button>
            </div>
            </div>
        </div>
    
        <!-- Formulario para solicitar nuevo uso -->
        <div v-if="mostrarFormulario" class="formulario-container">
            <h3>Solicitar Nuevo Uso del Seguro</h3>
            <form @submit.prevent="submitUso" class="formSolicitarUso">
                <p>
                    <label class="input-label">Contrato: #{{ nuevoUso.contrato.idContrato }}   Placa del Auto: {{ nuevoUso.contrato.automovil.idAutomovil }}  Cliente: {{ nuevoUso.contrato.cliente.nombre }} {{ nuevoUso.contrato.cliente.apellido }}</label>
                </p>
                <p>
                    <label for="tipoUso" class="input-label">Tipo de Uso:</label>
                    <select v-model="nuevoUso.tipoUso" class="input-select" required>
                        <option value="Choque grave">Choque grave</option>
                        <option value="Choque leve">Choque leve</option>
                        <option value="Daño electrico">Daño eléctrico</option>
                        <option value="Daño mecanico no grave">Daño mecánico no grave</option>
                        <option value="Mantenimiento">Mantenimiento</option>
                        <option value="Auxilio mecanico">Auxilio mecánico</option>
                    </select>
                </p>
                <p>
                    <label for="descripcion" class="input-label">Descripción:</label>
                    <textarea v-model="nuevoUso.descripcion" class="input-field" required></textarea>
                </p>
                <p>
                    <label for="montoAprobado" class="input-label">Monto: </label>
                    <input type="number" v-model="nuevoUso.montoAprobado" class="input-field" required>
                </p>
                <button type="submit" class="btn-guardar-solicitud">Enviar Solicitud</button>
                <button @click="cancelarSolicitud" class="btn-cancelar-solicitud">Cancelar</button>
            </form>
        </div>
    </div>
</template>
  
<script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        contratos: [],
        usosPorContrato: {},
        ultimoContrato: null,
        ultimoUso: null,
        mostrarFormulario: false,
        nuevoUso: {
            contrato: null,
            tipoUso: "",
            descripcion: "",
            montoAprobado: "",
        },
      };
    },
    methods: {
        fetchContratos() {
            const cliente = JSON.parse(localStorage.getItem("cliente"));
            axios
            .get("http://localhost:8080/contrato/filtrar/cliente", {
                params: { idcliente: cliente.idCliente },
            })
            .then((response) => {
                this.contratos = response.data;
                this.ultimoContrato = this.contratos[this.contratos.length - 1];
                this.fetchUsos();
            })
            .catch((error) => {
                console.error("Error al obtener contratos:", error);
            });
        },
        fetchUsos() {
            this.contratos.forEach((contrato) => {
            axios
                .get("http://localhost:8080/uso/filtrar/contrato", {
                params: { idContrato: contrato.idContrato },
                })
                .then((response) => {
                this.usosPorContrato[contrato.idContrato] = response.data;
                if (!this.ultimoUso && response.data.length > 0) {
                    this.ultimoUso = response.data[response.data.length - 1];
                }
                })
                .catch((error) => {
                console.error("Error al obtener usos:", error);
                });
            });
        },
        abrirFormulario(contrato) {
            this.mostrarFormulario = true;
            this.nuevoUso.contrato = contrato;
        },
        submitUso() {
            axios.post("http://localhost:8080/uso", this.nuevoUso)
            .then(() => {
                alert("Solicitud de uso registrada correctamente.");
                this.mostrarFormulario = false;
                this.nuevoUso = { contrato: null, tipoUso: "", descripcion: "", montoAprobado: "" };
                this.fetchUsos();
            })
            .catch((error) => {
                console.error("Error al registrar uso:", error);
            });
        },
        cancelarSolicitud(){
            this.mostrarFormulario = false;
            this.nuevoUso.contrato = null;
        },

        navInicioCliente() {
            this.$router.push("/cliente");
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
      this.fetchContratos();
    },
  };
</script>
  

<style scoped>
    /* Encabezado */    
    .header {
        background-color: #333;
        color: #fff;
        padding: 15px 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    
    .btn-nav,
    .btn-cerrar-sesion {
        background-color: #333;
        color: #fff;
        border: none;
        padding: 10px 20px;
        border-radius: 5px;
        cursor: pointer;
    }
    
    .btn-cerrar-sesion {
        background-color: #ff1a1a;
    }
    
    .btn-cerrar-sesion:hover {
        background-color: #ff4d4d;
    }


    /* Contenedor de destacados */

    .destacados-container{
        display: flex;
        justify-content: space-evenly;
        align-items: flex-start;
        gap: 20px;
        flex-wrap: wrap;
    }
    
    .destacado{
        border: 1px solid #ddd;
        padding: 20px 100px;
        background-color: #f9f9f9;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    .destacado h3{
        margin-top: 0;
        text-align: center;
        font-size: 20px;
    }

    .destacado ul{
        padding: 0;
        list-style-type: none;
    }

    .destacado li{
        margin-bottom: 10px;
    }

    /* Contenedor de Mis Contratos */

    .contratos-section h2{
        text-align: center;
    }

    .contratos-grid {
        display: flex;
        justify-content: space-evenly;
        align-items: flex-start;
        gap: 20px;
        flex-wrap: wrap;
    }

    @media (max-width: 768px){
        .contratos-grid{
            flex-direction: column;
            align-items: center;
        }
    }

    .contrato-card{
        border: 1px solid #ddd;
        padding: 30px 70px;
        text-align: center;
        background-color: #f9f9f9;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    .contrato-card h3 {
        margin-top: 0;
        text-align: center;
    }
    
    .contrato-card ul {
        padding: 0;
        margin: 25px 0;
        list-style-type: none;
    }

    .contrato-card li{
        margin-bottom: 20px;
        text-align: start;
    }


    .detalleUsos ul{
        list-style-type: disc
    }

    .detalleUsos li{
        margin-bottom: 20px;
    }

    .btn-nuevo-uso {
        background-color: #007bff;
        color: white;
        border: none;
        padding: 10px 20px;
        border-radius: 5px;
        cursor: pointer;
        margin-top: 30px;
    }

    .btn-nuevo-uso:hover{
        background-color: #0056b3;
    }

    /*Formulario para solicitar uso de seguro */

    .formulario-container {
        margin: 20px auto;
        max-width: 600px;
        padding: 20px;
        background-color: #f9f9f9;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }
    
    .formulario-container h3{
        text-align: center;
    }

    .label {
        font-size: 25px;
    }
    
    .input-field{
        width: 96.5%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 16px;
    }
    
    .input-select {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 16px;
    }

    .btn-guardar-solicitud,
    .btn-cancelar-solicitud {
        width: 48%;
        padding: 10px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
        margin: 0px 6px;
    }

    .btn-guardar-solicitud {
        background-color: #28bd4b;
        color: white;
    }

    .btn-guardar-solicitud:hover {
        background-color: #28a745;
        color: white;
    } 

    .btn-cancelar-solicitud {
        background-color: #dc3545;
        color: white;
    }

    .btn-cancelar-solicitud:hover {
        background-color: #ff0019;
        color: white;
    }


</style>
