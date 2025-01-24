<template>
    <div>
        <!--Encabezado-->
        <header class="header">
            <button @click="navInicio" class="btn-nav">Inicio</button>
            <button @click="navPlanesSeguro" class="btn-nav">Planes de Seguro</button>
            <button @click="navContratos" class="btn-nav">Contratos</button>
            <button @click="navUsosSeguro" class="btn-nav">Usos del Seguro</button>
            <button @click="cerrarSesion" class="btn-cerrar-sesion">Cerrar Sesión</button>
        </header>
        
        <div class="title-contenedor">
            <h1>Planes de Seguro</h1>
            <!-- Botón para agregar un nuevo plan -->
            <button @click="toggleAgregarPlan" class="btn-agregar-plan">
                {{ showAgregarPlan ? "Cancelar" : "Agregar un nuevo Plan" }}
            </button>
        </div>
  
        <!-- Formulario para agregar o editar un plan -->
        <div v-if="showAgregarPlan">
            <h2>Agregar Plan de Seguro</h2>

            <form @submit.prevent="submitPlan" class="formAgregarPlan">
                <p><input v-model="nuevoPlan.nombre" class="input-field" placeholder="Nombre del Plan" required/></p>
                <p>    
                    <label>Incluye cobertura para Pedidas Parciales: </label>
                    <select v-model="nuevoPlan.perdidasParciales" class="input-select" required>
                    <option :value="true">SI</option>
                    <option :value="false">NO</option>
                    </select>
                </p>           
                <p><label>Valor Perdidas Parciales : <input type="number" v-model.number="nuevoPlan.valorPerdidasParciales" class="input-field" placeholder="Perdidas Parciales" required step="0.01" :disabled="!nuevoPlan.perdidasParciales"/></label></p> 
                <p>    
                    <label>Incluye cobertura para Pedidas Totales: </label>
                    <select v-model="nuevoPlan.perdidasTotales" class="input-select" required>
                    <option :value="true">SI</option>
                    <option :value="false">NO</option>
                    </select>
                </p>
                <p><label>Valor Perdidas Totales : <input type="number" v-model.number="nuevoPlan.valorPerdidasTotales" class="input-field" placeholder="Perdidas Totales" required step="0.01" :disabled="!nuevoPlan.perdidasTotales"/></label></p>
                <p>    
                    <label>Incluye cobertura para Auxilio Mecanico: </label>
                    <select v-model="nuevoPlan.auxilioMecanico" class="input-select" required>
                    <option :value="true">SI</option>
                    <option :value="false">NO</option>
                    </select>
                </p>
                <p>    
                    <label>Incluye cobertura para Mantenimiento Vehicular: </label>
                    <select v-model="nuevoPlan.mantenimientoVehicular" class="input-select" required>
                    <option :value="true">SI</option>
                    <option :value="false">NO</option>
                    </select>
                </p>
                <p><input type="number" v-model.number="nuevoPlan.valorPlan" class="input-field" placeholder="Valor del Plan " required step="0.01"/></p>
                <button type="submit" class="btnGuardar">Guardar</button>   
            </form>
        </div>
    
        <!-- Contenedor de planes en 2 columnas -->
        <div class="planes-grid">
            <div v-for="plan in planes" :key="plan.idPlan" class="plan-contenedor">
                <h3>{{ plan.nombre }}</h3>
                <ul>
                    <li>Incluye Perdidas Parciales: {{ plan.perdidasParciales ? 'SI' : 'NO' }}</li>
                    <li>Valor de Perdidas Parciales: {{ plan.valorPerdidasParciales }}</li>
                    <li>Incluye Perdidas Totales: {{ plan.perdidasTotales ? 'SI' : 'NO' }}</li>
                    <li>Valor de Perdidas Totales: {{ plan.valorPerdidasTotales }}</li>
                    <li>Incluye Auxilio Mecanico: {{ plan.auxilioMecanico ? 'SI' : 'NO' }}</li>
                    <li>Incluye Mantenimiento Vehicular: {{ plan.mantenimientoVehicular ? 'SI' : 'NO' }}</li>
                    <li>Valor del Plan: $ {{ plan.valorPlan }}</li>
                </ul>
                <p>${{ plan.valorPlan }} anuales</p>
                <button @click="editarPlan(plan)" class="btn-editar-plan">Editar Plan</button>
            </div>
        </div>

        <!--Modal para Editar Plan de Seguro-->
        <div v-if="planEditando">
            <h3>Detalle Plan</h3>
            <p>ID: {{ planEditando.idPlan }}</p>
            <p><label>Nombre : <input v-model="planEditando.nombre" required/></label></p>
            <p>
            <label>Incluye Perdidas Parciales : 
                <select v-model="planEditando.perdidasParciales" required>
                <option :value="true">SI</option>
                <option :value="false">NO</option>
                </select>
            </label>
            </p>
            <p><label>Valor Perdidas Parciales : <input type="number" v-model="planEditando.valorPerdidasParciales" step="0.01" required :disabled="!planEditando.perdidasParciales"/></label></p>
            <p>
            <label>Incluye Perdidas Totales : 
                <select v-model="planEditando.perdidasTotales" required>
                <option :value="true">SI</option>
                <option :value="false">NO</option>
                </select>
            </label>
            </p>
            <p><label>Valor Perdidas Totales : <input type="number" v-model="planEditando.valorPerdidasTotales" step="0.01" required :disabled="!planEditando.perdidasTotales"/></label></p>
            <p>
            <label>Incluye Auxilio Mecanico : 
                <select v-model="planEditando.auxilioMecanico" required>
                <option :value="true">SI</option>
                <option :value="false">NO</option>
                </select>
            </label>
            </p>
            <p>
            <label>Incluye Mantenimiento Vehicular  : 
                <select v-model="planEditando.mantenimientoVehicular" required>
                <option :value="true">SI</option>
                <option :value="false">NO</option>
                </select>
            </label>
            </p>
            <p><label>Valores del Plan : <input type="number" v-model="planEditando.valorPlan" step="0.01" required/></label></p>
            <button @click="guardarCambiosPlan">Guardar</button>
            <button @click="cancelarEdicion">Cancelar</button>
        </div>
    </div>
</template>
  
<script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        showAgregarPlan: false,
        planes: [],
        planEditando: null,
        nuevoPlan: {
            nombre: '',
            perdidasParciales: '',
            valorPerdidasParciales: '',
            perdidasTotales: '',
            valorPerdidasTotales: '',
            auxilioMecanico: '',
            mantenimientoVehicular: '',
            valorPlan: '',
        },
      };
    },
    watch:{
        'nuevoPlan.perdidasParciales'(value){
            if(!value){
                this.nuevoPlan.valorPerdidasParciales = 0.00;
            }
        },
        'nuevoPlan.perdidasTotales'(value){
            if(!value){
                this.nuevoPlan.valorPerdidasTotales = 0.00;
            }
        },
        'planEditando.perdidasParciales'(value){
            if(this.planEditando && !value){
                this.planEditando.valorPerdidasParciales = 0.00;
            }
        },
        'planEditando.perdidasTotales'(value){
            if(this.planEditando && !value){
                this.planEditando.valorPerdidasTotales = 0.00;
            }
        },
    },
 
    methods: {
        fetchPlanes() {
            axios.get("http://localhost:8080/plan")
            .then((response) => {
                this.planes = response.data;
            })
            .catch((error) => {
                console.error("Error al obtener planes:", error);
            });
        },
        toggleAgregarPlan() {
            this.showAgregarPlan = !this.showAgregarPlan;
        },
        submitPlan() {
            axios.post("http://localhost:8080/plan", this.nuevoPlan)
            .then(() => {
                this.fetchPlanes();
                this.resetPlan()
            })
            .catch((error) => {
                console.error("Error al agregar plan:", error);
            });
        },
        editarPlan(plan) {
            this.planEditando = { ...plan };
        },
        guardarCambiosPlan() {
            axios.put(`http://localhost:8080/plan/${this.planEditando.idPlan}`, this.planEditando)
            .then(() => {
                this.fetchPlanes();
                this.cancelarEdicion();
            })
            .catch((error) => {
                console.error("Error al guardar cambios en el plan:", error);
            });
        },
        cancelarEdicion() {
            this.planEditando = null;
            this.resetPlan();
        },
        resetPlan(){
            this.nuevoPlan= {
                nombre: '',
                perdidasParciales: '',
                valorPerdidasParciales: '',
                perdidasTotales: '',
                valorPerdidasTotales: '',
                auxilioMecanico: '',
                mantenimientoVehicular: '',
                valorPlan: '',
            };
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
      this.fetchPlanes();
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
    
    .header h1 {
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
    
    
    .btn-cerrar-sesion {
        background-color: red;
        color: white;
        border: none;
        padding: 10px;
        cursor: pointer;
    }

    .title-contenedor h1{
        text-align: center;
    }
    
    .btn-agregar-plan {
        margin: 15px;
        padding: 10px;
        background-color: green;
        color: white;
        border: none;
        cursor: pointer;
    }
    
    .planes-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
        margin: 20px;
    }
    
    .plan-contenedor {
        border: 1px solid #ddd;
        padding: 15px;
        background-color: #f9f9f9;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }
    
    .plan-contenedor h3 {
        margin-top: 0;
        text-align: center;
    }
    
    .plan-contenedor ul {
        padding-left: 20px;
        list-style-type: disc;
    }
    
    .plan-contenedor p {
        font-size: 18px;
        font-weight: bold;
        text-align: center;
    }
    
    .btn-editar-plan {
        margin-top: 10px;
        padding: 5px 10px;
        background-color: #007bff;
        color: white;
        border: none;
        cursor: pointer;
    }
</style>
  