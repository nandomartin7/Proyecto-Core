<template>
    <div>
        <header class="header">
            <button @click="navInicioCliente" class="btn-nav">Inicio</button>
            <button @click="navContratarPlanSeguro" class="btn-nav">Contratar Plan de Seguro</button>
            <button @click="navContratos" class="btn-nav">Mis Contratos</button>
            <button @click="cerrarSesion" class="btn-cerrar-sesion">Cerrar Sesión</button>
        </header>
  
        <h1>Contratar Seguro de Automóvil</h1>
  
        <!--Automóvil-->
        <div class="form-container">
            <h1>Registro del Automóvil</h1>
            <form @submit.prevent="validarAutomovil">
                <p>
                    <label for="placa" class="input-label">Placa:</label>
                    <input id="placa" v-model="nuevoAutomovil.idAutomovil" class="input-field"
                    :disabled="automovilValidado" placeholder="Ingrese la placa" required/>
                </p>
                <p>
                    <label for="anio" class="input-label">Año:</label>
                    <input id="anio" v-model="nuevoAutomovil.anio" type="number" class="input-field"
                    :disabled="automovilValidado" placeholder="Ingrese el año" required/>
                </p>
                <!--
                <p>
                    <label for="marca" class="input-label">Marca:</label>
                    <input id="marca" v-model="nuevoAutomovil.marca" class="input-field"
                    :disabled="automovilValidado" placeholder="Ingrese la marca" required/>
                </p>
                <p>
                    <label for="modelo" class="input-label">Modelo:</label>
                    <input id="modelo" v-model="nuevoAutomovil.modelo" class="input-field"
                    :disabled="automovilValidado" placeholder="Ingrese el modelo" required/>
                </p>
                -->
                <p>
                    <label for="marca" class="input-label">Marca:</label>
                    <select id="marca" v-model="nuevoAutomovil.marca" class="input-select"
                        :disabled="automovilValidado" @change="actualizarModelos" required>
                        <option disabled value="">Seleccione una Marca</option>
                        <option v-for="marca in marcasDisponibles" :key="marca" :value="marca">
                            {{ marca }}
                        </option>
                    </select>
                </p>
                <p>
                    <label for="modelo" class="input-label">Modelo:</label>
                    <select id="modelo" v-model="nuevoAutomovil.modelo" class="input-select"
                            :disabled="automovilValidado || !modelosDisponibles.length" required>
                        <option disabled value="">Seleccione un modelo</option>
                        <option v-for="modelo in modelosDisponibles" :key="modelo" :value="modelo">
                            {{ modelo }}
                        </option>
                    </select>
                </p>

                <p>
                    <label for="tipo" class="input-label">Tipo:</label>
                    <select id="tipo" v-model="nuevoAutomovil.tipo" class="input-select"
                    :disabled="automovilValidado" required >
                        <option value="Automóvil">Automóvil</option>
                        <option value="Jeep / Caminoneta">Jeep / Caminoneta</option>
                        <option value="Deportivo">Deportivo</option>
                    </select>
                </p>
                <p>
                    <label for="ciudad" class="input-label">Ciudad de Circulación:</label>
                    <select id="ciudad" v-model="nuevoAutomovil.ciudadCirculacion" class="input-select" 
                    :disabled="automovilValidado" required>
                        <option value="Quito">Quito</option>
                        <option value="Guayaquil">Guayaquil</option>
                    </select>
                </p>
                <p>
                    <label for="uso" class="input-label">Uso Destinado:</label>
                    <select id="uso" v-model="nuevoAutomovil.usoDestinado" class="input-select"
                    :disabled="automovilValidado" required>
                        <option value="Familiar">Familiar</option>
                        <option value="Trabajo">Trabajo</option>
                        <option value="Carga / Comercio">Carga / Comercio</option>
                    </select>
                </p>
                <button v-if="!automovilValidado" type="submit" class="btn-guardar">Validar</button>
            </form>
        </div>
  
        <!--Planes-->
        <div v-if="automovilValidado" class="planes-container">
            <h1>Selecciona un Plan de Seguro</h1>
            <div v-for="plan in planes" :key="plan.idPlan" class="plan-card" 
            :class="{ selected: plan.idPlan === selectedPlan?.idPlan ,locked: planSeleccionado }" @click="!planSeleccionado && seleccionarPlan(plan)">
                <h3>{{ plan.nombre }}</h3>
                <ul>
                    <li>Perdidas Parciales: {{ plan.perdidasParciales ? 'Sí' : 'No' }}</li>
                    <li>Perdidas Totales: {{ plan.perdidasTotales ? 'Sí' : 'No' }}</li>
                    <li>Auxilio Mecánico: {{ plan.auxilioMecanico ? 'Sí' : 'No' }}</li>
                    <li>Mantenimiento Vehicular: {{ plan.mantenimientoVehicular ? 'Sí' : 'No' }}</li>
                </ul>
                <p class="plan-price">Precio: ${{ plan.valorPlan }} anuales</p>
            </div>
  
            <div v-if="!planSeleccionado" class="button-container">
                <button @click="volverARegistro" class="btn-regresar">Regresar</button>
                <button @click="continuarACliente" class="btn-continuar" :disabled="!selectedPlan">Continuar</button>
            </div>
        </div>
        
        <!--Informacion Cliente-->
        <div v-if="planSeleccionado" class="form-container">
            <h2>Información del Cliente</h2>
            <p><strong>Cédula:</strong> {{ cliente.idCliente }}</p>
            <p><strong>Nombre:</strong> {{ cliente.nombre }} {{ cliente.apellido }}</p>
            <p><strong>Correo:</strong> {{ cliente.correo }}</p>
            <p><strong>Teléfono:</strong> {{ cliente.telefono }}</p>
            <p><strong>Dirección:</strong> {{ cliente.direccion }}</p>

            <div v-if="!clienteValidado" class="button-container">
                <button @click="volverAPlan" class="btn-regresar">Regresar</button>
                <button @click="continuarAContrato" class="btn-continuar">Continuar</button>
            </div>
        </div>
        
        <!--Contrato-->
        <div v-if="clienteValidado" class="form-container">
            <h2>Información del Contrato</h2>
            <form>
                <p>
                    <label for="fechaInicio" class="input-label">Fecha de Inicio:</label>
                    <input id="fechaInicio" type="date" v-model="contrato.fechaInicio" class="input-field" readonly @change="calcularFechaFinalizacion"/>
                </p>
                <p>
                    <label for="duracion" class="input-label">Duración (años):</label>
                    <select id="duracion" v-model="contrato.duracion" class="input-select" required @change="calcularFechaFinalizacion">
                        <option v-for="anio in duraciones" :key="anio" :value="anio">{{ anio }}</option>
                    </select>
                </p>
                <p>
                    <label for="fechaFin" class="input-label">Fecha de Finalización:</label>
                    <input id="fechaFin" type="date" :value="contrato.fechaFinalizacion" class="input-field" readonly/>
                </p>
            </form>
            <div v-if="!contratoValidado" class="button-container">
                <button @click="volverACliente" class="btn-regresar">Regresar</button>
                <button @click="continuarAResumen" class="btn-continuar">Continuar</button>
            </div>
        </div>  

        <!--Confirmación de Contrato-->
        <div v-if="contratoValidado" class="overlay">
            <div class="modal">
                <h2>Confirmación de Contrato</h2>
                
                <div class="grid-confirmacion">
                    <div class="grid-item">
                        <h3>Datos del Automóvil</h3>
                        <p><strong>Placa:</strong> {{ nuevoAutomovil.idAutomovil }}</p>
                        <p><strong>Año:</strong> {{ nuevoAutomovil.anio }}</p>
                        <p><strong>Marca:</strong> {{ nuevoAutomovil.marca }}</p>
                        <p><strong>Modelo:</strong> {{ nuevoAutomovil.modelo }}</p>
                        <p><strong>Tipo:</strong> {{ nuevoAutomovil.tipo }}</p>
                        <p><strong>Ciudad:</strong> {{ nuevoAutomovil.ciudadCirculacion }}</p>
                        <p><strong>Uso:</strong> {{ nuevoAutomovil.usoDestinado }}</p>                
                    </div>
                    
                    <div class="grid-item">
                        <h3>Datos del Plan Seleccionado</h3>
                        <p><strong>Plan:</strong> {{ selectedPlan.nombre }}</p>
                        <p><strong>Precio:</strong> ${{ selectedPlan.valorPlan }} anuales</p>
                        <p><strong>Incluye:</strong></p>
                        <ul>
                            <li>Perdidas Parciales: {{ selectedPlan.perdidasParciales ? 'Sí' : 'No' }}</li>
                            <li>Perdidas Totales: {{ selectedPlan.perdidasTotales ? 'Sí' : 'No' }}</li>
                            <li>Auxilio Mecánico: {{ selectedPlan.auxilioMecanico ? 'Sí' : 'No' }}</li>
                            <li>Mantenimiento Vehicular: {{ selectedPlan.mantenimientoVehicular ? 'Sí' : 'No' }}</li>
                        </ul>
                    </div>

                    <div class="grid-item">
                        <h3>Datos del Cliente</h3>
                        <p><strong>Cédula:</strong> {{ cliente.idCliente }}</p>
                        <p><strong>Nombre:</strong> {{ cliente.nombre }} {{ cliente.apellido }}</p>
                        <p><strong>Correo:</strong> {{ cliente.correo }}</p>
                        <p><strong>Teléfono:</strong> {{ cliente.telefono }}</p>
                    </div>
                    
                    <div class="grid-item">
                        <h3>Información del Contrato</h3>
                        <p><strong>Fecha de Inicio:</strong> {{ contrato.fechaInicio }}</p>
                        <p><strong>Duración:</strong> {{ contrato.duracion }} años</p>
                        <p><strong>Fecha de Finalización:</strong> {{ contrato.fechaFinalizacion }}</p>                    
                    </div>
                </div>

                <div class="button-container">
                    <button @click="volverAContrato" class="btn-regresar">Regresar</button>
                    <button @click="confirmarContrato" class="btn-continuar">Confirmar y Crear</button>
                </div>
            </div>
        </div>
    </div>
</template>
  
<script>
  import axios from "axios";
  
  export default {
    data() {
        return {
            nuevoAutomovil: {
                idAutomovil: "",
                anio: "",
                marca: "",
                modelo: "",
                tipo: "",
                ciudadCirculacion: "",
                usoDestinado: "",
            },
            automovilValidado: false,
            marcasDisponibles: ["Chevrolet", "Hyundai", "Kia"],
            modelosPorMarca: {
                Chevrolet: ["Spark", "Aveo", "D Max", "Tracker"],
                Hyundai: ["Tucson", "Santa Fe", "Elantra", "Accent"],
                Kia: ["Picanto", "Rio", "Sportage", "Seltos"],
            },
            modelosDisponibles: [],

  
            planes: [],
            selectedPlan: null,
            planSeleccionado: false,

            cliente: {},
            clienteValidado: false,

            contrato: {
                fechaInicio: new Date().toISOString().split("T")[0],
                duracion: 1, // Duración en años por defecto
                fechaFinalizacion: "",
            },
            duraciones: [1, 2, 3, 4, 5],
            contratoValidado: false,
            nuevoContrato: {
                automovil: null,
                cliente: null,
                planSeguro: null,
                fechaInicio: '',
                fechaFinalizacion: '',
                valorsubtotal: '',
                valortotal: '',
            },
        };
    },
    methods: {
        //Automovil
        validarAutomovil() {
            axios.get(`http://localhost:8080/automovil/${this.nuevoAutomovil.idAutomovil}`)
            .then((response) => {
                if (response.data) {
                alert("El automóvil con esta placa ya está registrado.");
                }
            })
            .catch(() => {
                this.automovilValidado = true;
                this.nuevoContrato.automovil = this.nuevoAutomovil;
            });
        },
        volverEditar() {
            this.automovilValidado = false;
            this.nuevoContrato.automovil = null;
        },
        actualizarModelos() {
            this.modelosDisponibles = this.modelosPorMarca[this.nuevoAutomovil.marca] || [];
            this.nuevoAutomovil.modelo = ""; // Reinicia el modelo seleccionado
        },
        
        //Planes
        fetchPlanes() {
            axios.get("http://localhost:8080/plan")
            .then((response) => {
                this.planes = response.data;
            })
            .catch((error) => {
                console.error("Error al obtener los planes:", error);
            });
        },
        seleccionarPlan(plan) {
            if (!this.planSeleccionado){
                this.selectedPlan = plan
                this.nuevoContrato.planSeguro = plan
            }
        },
        volverARegistro() {
            this.automovilValidado = false;
            this.nuevoContrato.planSeguro = null;
        },
        continuarACliente() {
            this.planSeleccionado = true;
        },

        //Cliente
        fetchCliente() {
            const clienteData = localStorage.getItem("cliente");
            if (clienteData) {
                this.cliente = JSON.parse(clienteData);
                this.nuevoContrato.cliente = this.cliente;
            }
        },
        volverAPlan() {
            this.nuevoContrato.cliente = null
            this.planSeleccionado = false;
        },
        continuarAContrato() {
            this.clienteValidado = true;
        },
        
        //Contrato
        calcularFechaFinalizacion() {
            if (this.contrato.fechaInicio && this.contrato.duracion) {
            const fechaInicio = new Date(this.contrato.fechaInicio);
            const fechaFin = new Date(
                fechaInicio.setFullYear(fechaInicio.getFullYear() + this.contrato.duracion)
            );
                this.contrato.fechaFinalizacion = fechaFin.toISOString().split("T")[0];

                this.nuevoContrato.fechaInicio = this.contrato.fechaInicio;
                this.nuevoContrato.fechaFinalizacion = this.contrato.fechaFinalizacion;
            }
        },
        volverACliente() {
            this.nuevoContrato.fechaFinalizacion = '';
            this.nuevoContrato.fechaInicio = '';
            this.clienteValidado = false;
        },
        continuarAResumen() {
            this.contratoValidado = true;
        },


        volverAContrato() {
            this.contratoValidado = false;
        },
        confirmarContrato() {
            this.contratoValidado = true;

            if (!this.nuevoContrato.automovil || !this.nuevoContrato.planSeguro ||
                !this.nuevoContrato.cliente || !this.nuevoContrato.fechaInicio || !this.nuevoContrato.fechaFinalizacion){
                alert('Falran datos en el contrato porfavor verifique todas las secciones. ');
                return;
            }


            axios.post('http://localhost:8080/automovil', this.nuevoAutomovil)
            .then(() => {
                axios.post('http://localhost:8080/contrato', this.nuevoContrato)
                .then(() => {
                    this.limpiarCampos();
                    this.$router.push('/cliente/mis-contratos');
                })
                .catch(error => {
                    console.error(error);
                    alert("Ocurrio un error al registrar el contrato, verifique los datos e intente nuevamente");
                    
                    axios.delete(`http://localhost:8080/automovil/${this.nuevoAutomovil.idAutomovil}`)
                    .then(() => {
                        alert("Intente nuevamente todo el proceso de contratación por favor.")
                    })
                    .catch( error => {
                        console.error("Error eliminando automovil:", error);          
                    });
                });

            })
            .catch(error => {
               console.error(error);
               alert("Ocurrio un error al registrar el automóvil, verifique los datos e intente nuevamente");
            });
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

        limpiarCampos() {
            this.nuevoContrato = {
                automovil: null,
                cliente: null,
                planSeguro: null,
                fechaInicio: "",
                fechaFinalizacion: "",
                valorsubtotal: "",
                valortotal: "",
            };
            this.automovilValidado = false;
            this.planSeleccionado = false;
            this.clienteValidado = false;
            this.contratoValidado = false;
        }
    },
    mounted(){
        this.fetchPlanes();
        this.fetchCliente();
    }
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
    
    /* Contenedor del formulario */
    .form-container {
        margin: 20px auto;
        max-width: 600px;
        padding: 20px;
        background-color: #f9f9f9;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }
    
    .form-container h1{
        text-align: center;
    }

    .input-label {
        display: block;
        font-weight: bold;
        margin-bottom: 5px;
    }
    
    .input-field,
    .input-select {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 16px;
    }
    
    .btn-guardar,
    .btn-regresar,
    .btn-continuar {
        width: 48%;
        padding: 10px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
        margin-top: 10px;
    }
    
    .btn-guardar {
        background-color: #28a745;
        color: white;
    }
    
    .btn-regresar {
        background-color: #dc3545;
        color: white;
    }
    
    .btn-continuar {
        background-color: #28a745;
        color: white;
    }
    
    .btn-guardar:hover,
    .btn-regresar:hover,
    .btn-continuar:hover {
        opacity: 0.9;
    }
    
    .button-container {
        display: flex;
        justify-content: space-between;
    }
  
  
    /* Contenedor de planes */
  .planes-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
    margin: 20px;
    padding: 0 20px;
  }
  
  .plan-card {
    border: 1px solid #ddd;
    padding: 15px;
    background-color: #f9f9f9;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    cursor: pointer;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
  }
  
  .plan-card:hover {
    transform: scale(1.05);
    box-shadow: 0 6px 8px rgba(0, 0, 0, 0.15);
  }
  
  .plan-card.selected {
    border: 2px solid #28a745;
  }
  
  .plan-card h3 {
    margin-top: 0;
    font-size: 18px;
    color: #333;
    text-align: center;
  }
  
  .plan-card ul {
    padding: 0;
    list-style: none;
    margin: 10px 0;
  }
  
  .plan-card ul li {
    margin: 5px 0;
    font-size: 14px;
  }
  
  .plan-price {
    font-weight: bold;
    text-align: center;
    margin: 10px 0;
  }
  
  /* Botones */
  .button-container {
    display: flex;
    justify-content: space-between;
    margin: 20px 0;
    padding: 0 20px;
  }
  
  .btn-regresar,
  .btn-continuar {
    width: 48%;
    padding: 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
  }
  
  .btn-regresar {
    background-color: #dc3545;
    color: white;
  }
  
  .btn-continuar {
    background-color: #28a745;
    color: white;
  }
  
  .btn-continuar:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
  
  .btn-regresar:hover,
  .btn-continuar:hover {
    opacity: 0.9;
  }


  /* Contenedor de Cliente*/
  .cliente-container {
    margin: 20px auto;
    max-width: 600px;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }



  /* Confirmación */
  .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.5); /* Transparencia del fondo */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000; /* Asegura que el modal esté por encima de todo */
  }

/* Ventana modal */
    .modal {
        background: #fff;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.25);
        max-width: 1140px;
        width: 100%; /* Responsivo para pantallas pequeñas */
        position: relative;
        z-index: 1001;
    }

    /* Contenido del modal */
    .modal h2 {
        text-align: center;
        margin-bottom: 20px;
    }

    .modal .grid-confirmacion {
        display: flex;
        gap: 20px;
        justify-content: space-between;
    }

    .modal .grid-item {
        flex: 1 1 calc(50% - 20px);
        background-color: #f9f9f9;
        padding: 15px;
        border-radius: 10px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .modal .grid-item h3 {
        text-align: center;
        margin-bottom: 10px;
    }

    .modal .grid-item ul {
        text-align: left;
        list-style: disc;
        padding-left: 20px;
    }

    .modal .grid-item li {
        margin-bottom: 20px;

    }
    /* Botones dentro del modal */
    .modal .button-container {
        display: flex;
        justify-content: space-between;
        margin-top: 20px;
    }

    @media (max-width: 768px) {
        .grid-item {
            flex: 1 1 100%; /* Ocupa todo el ancho en pantallas pequeñas */
        }
    }

  </style>
  