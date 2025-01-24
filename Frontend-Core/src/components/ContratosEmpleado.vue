<template>
    <!-- Encabezado -->
    <header class="header">
        <button @click="navInicio" class="btn-nav">Inicio</button>
        <button @click="navPlanesSeguro" class="btn-nav">Planes de Seguro</button>
        <button @click="navContratos" class="btn-nav">Contratos</button>
        <button @click="navUsosSeguro" class="btn-nav">Usos del Seguro</button>
        <button @click="cerrarSesion" class="btn-cerrar-sesion">Cerrar Sesión</button>
    </header>

    <!-- Contenedor de Contratos -->
    <div>
        <h1>Contratos de Seguro</h1>
        <h2>Lista de Contratos</h2>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Automóvil</th>
                <th>Cliente</th>
                <th>Plan de Seguro</th>
                <th>Montos Agregados</th>
                <th>Motivos</th>
                <th>Subtotal</th>
                <th>Total</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="contrato in contratos" :key="contrato.idContrato">
                <td>{{ contrato.idContrato }}</td>
                <td>{{ contrato.automovil.idAutomovil }}</td>
                <td>{{ contrato.cliente.nombre }} {{ contrato.cliente.apellido }}</td>
                <td>{{ contrato.planSeguro.nombre }}</td>
                <td>${{ contrato.valoresAgregados }}</td>
                <td>{{ contrato.motivoAgregados }}</td>
                <td>${{ contrato.valorsubtotal }}</td>
                <td>${{ contrato.valortotal }}</td>
                <td>
                    <button @click="verContrato(contrato)" class="btnVerContrato">Ver</button>
                    <button @click="editarContrato(contrato)" class="btnEditarContrato">Editar</button>
                    <button @click="analizarContrato(contrato)" class="btnAnalizarContrato">Analizar</button>
                </td>
            </tr>
            </tbody>
        </table>

        <!-- Modal para Ver Contrato -->
        <div v-if="contratoSeleccionado" class="verContrato-contenedor">
            <h3>Detalle Contrato</h3>
            <p>ID: {{ contratoSeleccionado.idContrato }}</p>
            <p>Automovil : {{ contratoSeleccionado.automovil.idAutomovil }}</p>
            <p>Cliente : {{ contratoSeleccionado.cliente.idCliente }}  {{ contratoSeleccionado.cliente.nombre }}</p>
            <p>Plan Seguro : {{ contratoSeleccionado.planSeguro.nombre }}</p>
            <p>Fecha Inicio : {{ contratoSeleccionado.fechaInicio}}</p>
            <p>Fecha Finalizacion : {{ contratoSeleccionado.fechaFinalizacion}}</p>
            <p>Valores Agregados : {{ contratoSeleccionado.valoresAgregados}}</p>
            <p>Motivo de Valores Agregados : {{ contratoSeleccionado.motivoAgregados}}</p>
            <p>Subtotal : {{ contratoSeleccionado.valorsubtotal}}</p>
            <p>Total : {{ contratoSeleccionado.valortotal}}</p>
            <button @click="cerrarModal">Cerrar</button>
        </div>

        <!-- Modal para Editar Contrato -->
        <div v-if="contratoEditando" class="editarContrato-contenedor">
            <h3>Editar Contrato</h3>
            <p>ID: {{ contratoEditando.idContrato }}</p>
            <p>Automovil : {{ contratoEditando.automovil.idAutomovil}}</p>
            <p>Cliente : {{ contratoEditando.cliente.idCliente}} {{ contratoEditando.cliente.nombre }} {{ contratoEditando.cliente.apellido }}</p>
            <p>Plan Seguro : {{ contratoEditando.planSeguro.nombre}}</p>
            <p>Fecha Inicio : {{ contratoEditando.fechaInicio}}</p>        
            <p>
            <label>Renovacion de Contrato : 
                <select v-model="contratoEditando.renovacionContrato" @change="ajustarFechas" required>
                <option :value="true">Activada</option>
                <option :value="false">Desactivada</option>
                </select>
            </label>
            </p>
            <p><label>Fecha Renovacion Contrato : <input type="date" v-model="contratoEditando.fechaRenovacion" disabled></label></p>
            <p><label>Fecha de Finalizacion <input type="date" v-model="contratoEditando.fechaFinalizacion" class="input-field" required :disabled="!contratoEditando.renovacionContrato"></label></p>

            <p><label>Valores Agregados : <input type="number" v-model="contratoEditando.valoresAgregados" step="0.01" required/></label></p>
            <p><label>Motivo Valores Agregados : <input v-model="contratoEditando.motivoAgregados"/></label></p>
            <p><label>Subtotal : <input type="number" v-model="contratoEditando.valorsubtotal" step="0.01" /></label></p>
            <p><label>Total : <input type="number" v-model="contratoEditando.valortotal" step="0.01" required/></label></p>
            <button @click="guardarCambiosContrato">Guardar</button>
            <button @click="cancelarEdicion">Cancelar</button>
        </div>
    </div>

    <!-- Filtro por Fechas -->
    <div class="filtrarFechas-contenedor">
        <h2>Filtrar Contratos por Fechas</h2>
        <div>
            <label>Fecha Inicio:</label>
            <input type="date" v-model="filtroFechaInicio" />
            <label>Fecha Fin:</label>
            <input type="date" v-model="filtroFechaFin" />
            <button @click="buscarPorFechas" class="btnBuscar">Filtrar</button>
            <button @click="cancelarFiltroFechas" class="btnCancelar">Cancelar</button>
        </div>
        <p v-if="mensajeErrorFechas" class="mensajeError">{{ mensajeErrorFechas }}</p>
        <table v-if="contratosFiltradosPorFechas.length > 0">
            <thead>
            <tr>
                <th>ID</th>
                <th>Automóvil</th>
                <th>Cliente</th>
                <th>Plan de Seguro</th>
                <th>Montos Agregados</th>
                <th>Motivos</th>
                <th>Subtotal</th>
                <th>Total</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="contrato in contratosFiltradosPorFechas" :key="contrato.idContrato">
                <td>{{ contrato.idContrato }}</td>
                <td>{{ contrato.automovil.idAutomovil }}</td>
                <td>{{ contrato.cliente.nombre }} {{ contrato.cliente.apellido }}</td>
                <td>{{ contrato.planSeguro.nombre }}</td>
                <td>{{ contrato.valoresAgregados }}%</td>
                <td>{{ contrato.motivoAgregados }}</td>
                <td>${{ contrato.valorSubtotal }}</td>
                <td>${{ contrato.valortotal }}</td>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- Filtro por Planes -->
    <div class="filtrarPlanes-contenedor">
        <h2>Filtrar Contratos por Planes</h2>
        <div>
            <label for="filtroPlan">Seleccione un Plan:</label>
            <select v-model="filtroPlan" id="filtroPlan">
                <option v-for="plan in planes" :key="plan.idPlan" :value="plan.idPlan">{{ plan.nombre }}</option>
            </select>
            <button @click="buscarPorPlan" class="btnBuscar">Filtrar</button>
            <button @click="cancelarFiltroPlan" class="btnCancelar">Cancelar</button>
        </div>
        <p v-if="mensajeErrorPlan" class="mensajeError">{{ mensajeErrorPlan }}</p>
        <table v-if="contratosFiltradosPorPlan.length > 0">
            <thead>
            <tr>
                <th>ID</th>
                <th>Automóvil</th>
                <th>Cliente</th>
                <th>Plan de Seguro</th>
                <th>Montos Agregados</th>
                <th>Motivos</th>
                <th>Subtotal</th>
                <th>Total</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="contrato in contratosFiltradosPorPlan" :key="contrato.idContrato">
                <td>{{ contrato.idContrato }}</td>
                <td>{{ contrato.automovil.idAutomovil }}</td>
                <td>{{ contrato.cliente.nombre }} {{ contrato.cliente.apellido }}</td>
                <td>{{ contrato.planSeguro.nombre }}</td>
                <td>${{ contrato.valoresAgregados }}</td>
                <td>{{ contrato.motivoAgregados }}</td>
                <td>${{ contrato.valorSubtotal }}</td>
                <td>${{ contrato.valortotal }}</td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            contratos: [],
            planes: [],
            automoviles: [],
            clientes: [],
            filtroPlan: "",
            filtroFechaInicio: "",
            filtroFechaFin: "",
            contratosFiltradosPorPlan: [],
            contratosFiltradosPorFechas: [],
            contratoSeleccionado: null,
            contratoEditando: null,
            contratoAnalizar: null,
            mensajeErrorPlan: "", 
            mensajeErrorFechas: "", 
        };
    },
    watch:{
        'contratoEditando.renovacionContrato'(newValue) {
        if (this.contratoEditando && newValue) {
            if (this.contratoEditando.fechaRenovacion) {
            if (this.contratoEditando.fechaFinalizacion !== this.contratoEditando.fechaRenovacion) {
                this.contratoEditando.fechaRenovacion = this.contratoEditando.fechaFinalizacion;
            }
            } else {
            this.contratoEditando.fechaRenovacion = this.contratoEditando.fechaFinalizacion;
            }
        }
        },
    },
    methods: {
        fetchAutomoviles(){
            axios.get('http://localhost:8080/automovil')
            .then(response => {
            this.automoviles = response.data;
            })
            .catch(error => {
            console.error("Error fetching automoviles:", error);
            
            })
        },
        fetchClientes(){
            axios.get('http://localhost:8080/cliente')
            .then(response => {
            this.clientes = response.data;
            })
            .catch(error => {
            console.error("Error fetching clientes:", error);
            
            })
        },
        fetchContratos(){
            axios.get('http://localhost:8080/contrato')
            .then(response => {
            this.contratos = response.data;
            })
            .catch(error => {
            console.error("Error fetching contratos:", error);
            })
        },
        fetchPlanes(){
        axios.get('http://localhost:8080/plan')
            .then(response => {
            this.planes = response.data;
            })
            .catch(error => {
            console.error("Error fetching planes:", error);
            })
        },
        verContrato(contrato) {
            this.contratoSeleccionado = contrato;
        },
        actualizarFechas(){
            if (this.contratoEditando.renovacionContrato && this.contratoEditando.fechaRenovacion == null ){
                this.contratoEditando.fechaRenovacion = this.contratoEditando.fechaFinalizacion;
            } else {
                this.contratoEditando.fechaRenovacion = '';
                this.contratoEditando.fechaFinalizacion = ''; 
            }
        },
        editarContrato(contrato) {
            this.contratoEditando = { ...contrato };
        },
        analizarContrato(contrato) {
            if(!contrato){
                console.error("No hay contrato para Analizar.");
                alert("No se pudo cargar el contrato para el análisis.");
                return;
            }
            this.contratoAnalizar = { ...contrato };
            localStorage.setItem('contratoAnalizar', JSON.stringify(this.contratoAnalizar)); //Guarda contrato como JSON
            this.$router.push("/empleado/analisis-contrato");
        },
        guardarCambiosContrato() {
            axios.put(`http://localhost:8080/contrato/${this.contratoEditando.idContrato}`, this.contratoEditando)
            .then(response => {
            this.fetchContratos();
            this.cancelarEdicion();
            const index =  this.contratoEditando.idContrato;
            if (index !== -1){
                this.contratos[index] = response.data;
            }
            this.contratoEditando = null;
            })
            .catch(error => {
            console.error("Error actualizando contrato:", error);
            });  
        },
        cancelarEdicion() {
            this.contratoEditando = null;
        },
        cerrarModal() {
            this.contratoSeleccionado = null;
        },
        buscarPorPlan() {
            this.mensajeErrorPlan = "";
            if (this.filtroPlan) {                
                axios.get(`http://localhost:8080/contrato/filtrar/plan`, 
                {params: { planSeguro: parseInt(this.filtroPlan)}})
                .then((response) => {
                    this.contratosFiltradosPorPlan = response.data;
                    if(this.contratosFiltradosPorPlan.length === 0){
                        this.mensajeErrorPlan = "No hay contratos registrados con ese plan.";
                    } else {
                        this.mensajeErrorPlan = "";
                    }
                })
                .catch(error => {
                    console.error("Error al buscar por plan: ", error);
                    this.mensajeErrorPlan = "Error al realizar la búsqueda.";
                    this.contratosFiltradosPorPlan = [];
                });
            } else {
                this.mensajeErrorPlan = "Por favor, selecciona un plan.";
            }
        },
        cancelarFiltroPlan() {
            this.filtroPlan = "";
            this.contratosFiltradosPorPlan = [];
        },
        buscarPorFechas() {
            this.mensajeErrorFechas = "";
            if (!this.filtroFechaInicio || !this.filtroFechaFin){
                console.error('Debe seleccionar ambas fechas');
                this.mensajeErrorFechas = "Debe seleccionar ambas fechas. "
                return;
            }

            axios.get(`http://localhost:8080/contrato/filtrar/fecha`, {
                params: {
                    fechaInicio: this.filtroFechaInicio,
                    fechaFin: this.filtroFechaFin
                }
            })
            .then(response =>{
                this.contratosFiltradosPorFechas = response.data;
                if (this.contratosFiltradosPorFechas.length === 0){
                    this.mensajeErrorFechas = "No hay contratos que  hayan iniciado en ese periodo de tiempo. "
                } else {
                    this.mensajeErrorFechas = "";
                }
            })
            .catch(error => {
                console.error('Error al buscar los coontratos:', error);
                this.mensajeErrorFechas = "Error al realizar la búsqueda."
                this.contratosFiltradosPorFechas = [];
            });
        },
        cancelarFiltroFechas() {
            this.filtroFechaInicio = "";
            this.filtroFechaFin = "";
            this.contratosFiltradosPorFechas = [];
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
        this.fetchAutomoviles();
        this.fetchClientes();
        this.fetchContratos();
        this.fetchPlanes();
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

    /* Botón para agregar nuevos elementos */
    .btn-agregar-contrato {
        display: block;
        margin: 15px auto;
        padding: 10px 20px;
        background-color: green;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    .btn-agregar-contrato:hover {
        background-color: #228b22;
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

    .btnVerContrato {
        background-color: #007bff;
        color: white;
    }

    .btnVerContrato:hover {
        background-color: #0056b3;
    }

    .btnEditarContrato {
        background-color: #ffc107;
        color: black;
    }

    .btnEditarContrato:hover {
        background-color: #e0a800;
    }

    .btnAnalizarContrato {
        background-color: #17a2b8;
        color: white;
    }

    .btnAnalizarContrato:hover {
        background-color: #117a8b;
    }

    /* Modal */
    .verContrato-contenedor, .editarContrato-contenedor {
        background-color: #fff;
        border-radius: 5px;
        padding: 20px;
        margin: 20px auto;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        width: 80%;
    }

    .verContrato-contenedor h3, .editarContrato-contenedor h3 {
        margin-top: 0;
        text-align: center;
    }

    .verContrato-contenedor p, .editarContrato-contenedor p {
        font-size: 16px;
        margin: 10px 0;
    }

    /* Filtros */
    .filtrarFechas-contenedor, .filtrarPlanes-contenedor {
        margin: 20px 0;
        padding: 15px;
        border: 1px solid #ddd;
        border-radius: 5px;
        background-color: #f9f9f9;
        text-align: center;
    }  

    .filtrarFechas-contenedor h2, .filtrarPlanes-contenedor h2 {
        margin-top: 0;
        text-align: center;
    }

    .filtrarFechas-contenedor label, .filtrarPlanes-contenedor label {
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

