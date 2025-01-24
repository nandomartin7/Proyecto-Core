<template>
    <!-- Encabezado -->
    <header class="header">
        <button @click="navInicio" class="btn-nav">Inicio</button>
        <button @click="navPlanesSeguro" class="btn-nav">Planes de Seguro</button>
        <button @click="navContratos" class="btn-nav">Contratos</button>
        <button @click="navUsosSeguro" class="btn-nav">Usos del Seguro</button>
        <button @click="cerrarSesion" class="btn-cerrar-sesion">Cerrar Sesión</button>
    </header>

    <h2>Evaluacion de contrato {{ contratoAnalizado.idContrato }}</h2>
    <div class="info-contrato">
        <h2>Informacion General del Contrato Analizado</h2>
        <div class="info-contrato-grid">
            <div class="info-contrato-grid-content">
                <ul>
                    <!-- Información General del Contrato -->
                    <h3>Información General</h3>
                    <li><strong>Id Contrato:</strong> {{ contratoAnalizado.idContrato }}</li>
                    <li><strong>Fecha Inicio:</strong> {{ contratoAnalizado.fechaInicio }}</li>
                    <li><strong>Fecha Finalización:</strong> {{ contratoAnalizado.fechaFinalizacion }}</li>
                    <li><strong>Valores Agregados:</strong> ${{ contratoAnalizado.valoresAgregados }}</li>
                    <li><strong>Motivo de Agregados:</strong> {{ contratoAnalizado.motivoAgregados || 'No Aplica' }}</li>
                    <li><strong>Subtotal:</strong> ${{ contratoAnalizado.valorsubtotal }}</li>
                    <li><strong>Total:</strong> ${{ contratoAnalizado.valortotal }}</li>
                </ul>
            </div>
            <div class="info-contrato-grid-content">
                <ul>
                    <!-- Información del Automóvil -->
                    <h3>Información del Automóvil</h3>
                    <li><strong>Placa:</strong> {{ contratoAnalizado.automovil.idAutomovil }}</li>
                    <li><strong>Año:</strong> {{ contratoAnalizado.automovil.anio }}</li>
                    <li><strong>Tipo:</strong> {{ contratoAnalizado.automovil.tipo }}</li>
                    <li><strong>Marca:</strong> {{ contratoAnalizado.automovil.marca }}</li>
                    <li><strong>Modelo:</strong> {{ contratoAnalizado.automovil.modelo }}</li>
                    <li><strong>Ciudad de Circulación:</strong> {{ contratoAnalizado.automovil.ciudadCirculacion }}</li>
                    <li><strong>Uso Destinado:</strong> {{ contratoAnalizado.automovil.usoDestinado }}</li>
                </ul>
            </div>
            <div class="info-contrato-grid-content">
                <ul>
                    <!-- Información del Cliente -->
                    <h3>Información del Cliente</h3>
                    <li><strong>Nombre:</strong> {{ contratoAnalizado.cliente.nombre }} {{ contratoAnalizado.cliente.apellido }}</li>
                    <li><strong>Cédula:</strong> {{ contratoAnalizado.cliente.idCliente }}</li>
                    <li><strong>Dirección:</strong> {{ contratoAnalizado.cliente.direccion }}</li>
                    <li><strong>Teléfono:</strong> {{ contratoAnalizado.cliente.telefono }}</li>
                    <li><strong>Correo:</strong> {{ contratoAnalizado.cliente.correo }}</li>
                </ul>
            </div>
            <div class="info-contrato-grid-content">
                <ul>
                    <!-- Información del Plan -->
                    <h3>Información del Plan</h3>
                    <li><strong>Plan:</strong> {{ contratoAnalizado.planSeguro.nombre }}</li>
                    <li><strong>Incluye Cobertura Pérdidas Parciales:</strong> {{ contratoAnalizado.planSeguro.perdidasParciales ? 'Sí' : 'No' }}</li>
                    <li><strong>Incluye Cobertura Pérdidas Totales:</strong> {{ contratoAnalizado.planSeguro.perdidasTotales ? 'Sí' : 'No' }}</li>
                    <li><strong>Incluye Cobertura Auxilio Mecánico:</strong> {{ contratoAnalizado.planSeguro.auxilioMecanico ? 'Sí' : 'No' }}</li>
                    <li><strong>Incluye Cobertura Mantenimiento Vehicular:</strong> {{ contratoAnalizado.planSeguro.mantenimientoVehicular ? 'Sí' : 'No' }}</li>
                    <li><strong>Valor Plan:</strong> ${{ contratoAnalizado.planSeguro.valorPlan }}</li>
                </ul>
            </div>
        </div>
    </div>

    <div class="evaluacion-section">
        <div class="evaluacion-acordeon">
            <div class="evaluacion-acordeon-header" @click="toggleEvaluacionAutomovil()">
                <h2>Evaluacion de Automóvil</h2>
                <ul>
                    <li>Año del Automóvil: {{ analisisContrato.calificacionAnioAutomovil }}%</li>
                    <li>Tipo de Automóvil: {{ analisisContrato.calificacionTipoAutomovil }}%</li>
                    <li>Ciudad de Circulacion del Automóvil: {{ analisisContrato.calificacionCiudadCirculacionAutomovil }}%</li>
                    <li>Uso Destinado del Automóvil: {{ analisisContrato.calificacionUsoDestinadoAutomovil }}%</li>
                </ul>
                <label>Evaluacion Total del Automóvil: {{ analisisContrato.evaluacionAutomovil }}%</label>
            </div>
            <div v-if="mostrarEvaluacionAutomovil" class="evaluacion-acordeon-content">
                <h2>Informacion del Automóvil</h2>
                <ul>
                    <li>Año del Automóvil: {{ contratoAnalizado.automovil.anio }}</li>
                    <li>Tipo de Automóvil: {{ contratoAnalizado.automovil.tipo }}</li>
                    <li>Ciudad de Circulacion del Automóvil: {{ contratoAnalizado.automovil.ciudadCirculacion }}</li>
                    <li>Uso Destinado del Automóvil: {{ contratoAnalizado.automovil.usoDestinado }}</li>
                </ul>
            </div>
        </div>
        
        <div class="evaluacion-acordeon">
            <div class="evaluacion-acordeon-header" @click="toggleEvaluacionHistorial()">
                <h2>Evaluacion del Historial de Usos del Seguro</h2>
                <ul>
                    <li>Total de Usos del Seguro: {{ analisisContrato.frecuenciaTotalUsos }}</li>
                    <li>Calificacion Frecuencia de Usos del Seguro: {{ analisisContrato.calificacionFrecuenciaUsoSeguro }}%</li>
                    <li>Total de Monto Aprobado: ${{ analisisContrato.montoTotal }}</li>
                    <li>Calificacion Montos Aprobados: {{ analisisContrato.calificacionMontoTotalAprobado }}%</li>
                    <li>Promedio de cada cuantos meses Usa el Seguro: {{ analisisContrato.tiempoMesesPromedio }}</li>
                    <li>Calificacio Promedio de Tiempo entre usos: {{ analisisContrato.calificacionPromedioTiempoEntreUso }}%</li>
                </ul>
                <label>Evaluacion Total del Historial de Usos del Seguro: {{ analisisContrato.evaluacionHistorialUsos }}%</label>
            </div>
            <div v-if="mostrarEvaluacionHistorial" class="evaluacion-acordeon-content">
                <h2>Informacion de Usos de Seguro</h2>
                <table class="usos-table">
                    <thead>
                        <tr>
                            <th>Id Uso</th>
                            <th>Fecha</th>
                            <th>Tipo de Uso</th>
                            <th>Monto Aprobado</th>
                            <th>Estado de Reclamo</th>
                            <th>Analizado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="uso in usosContrato" :key="uso.idUso" :class="{'analizado': uso.analizado}">
                            <td>{{ uso.idUso }}</td>
                            <td>{{ uso.fecha }}</td>
                            <td>{{ uso.tipoUso }}</td>
                            <td>${{ uso.montoAprobado }}</td>
                            <td>{{ uso.estadoReclamo }}</td>
                            <td>
                                <span v-if="uso.analizado">Sí</span>
                                <span v-else>No</span>
                            </td>
                        </tr>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3"><strong>Total Monto Abrobado Analizado:</strong></td>
                            <td>${{ totalMontoAnalizado }}</td>
                            <td colspan="2"><strong>Total Usos Analizados:</strong>  {{  totalUsosAnalizados }}</td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>

        <div class="evaluacion-acordeon">
            <div class="evaluacion-acordeon-header" @click="toggleEvaluacionSimilares()">
                <h2>Evaluacion con Clientes Similares</h2>
                <ul>
                    <li>Calificacion Promedio de usos Clientes Similares: {{ analisisContrato.calificacionPromedioUsosSimilares }}%</li>
                    <li>Usos del Cliente: {{ analisisContrato.usosCliente }}</li>
                    <li>Calificacion Promedio de Monto Aprobado Clientes Similares: {{ analisisContrato.calificacionPromedioMontoAprobadoSimilares }}%</li>
                    <li>Monto Abrobado al Cliente: ${{ analisisContrato.montoAprobadoCliente }}</li>
                </ul>
                <label>Evaluacion Total con Clientes Similares: {{ analisisContrato.evaluacionClientesSimilares }}%</label>
            </div>
            <div v-if="mostrarEvaluacionSimilares" class="evaluacion-acordeon-content">
                <!--Usos de Contratos Similares-->
                <h3>Contratos con Vehículos Similares</h3>
                <table class="contratos-similares-table">
                    <thead>
                        <tr>
                            <th>Id Contrato</th>
                            <th>Id Uso</th>
                            <th>Tipo de Uso</th>
                            <th>Monto Aprobado</th>
                            <th>Fecha</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- Iterar sobre contratos similares -->
                        <template v-for="(contrato, index) in contratosSimilares" :key="index">
                            <!-- Iterar sobre los usos de cada contrato -->
                            <tr v-for="(uso, usoIndex) in contrato.usos" :key="usoIndex">
                                <!-- Mostrar información del contrato y uso -->
                                <td v-if="usoIndex === 0" :rowspan="contrato.usos.length">{{ contrato.contratoId }}</td>
                                <td>{{ uso.idUso }}</td>
                                <td>{{ uso.tipoUso }}</td>
                                <td>${{ uso.montoAprobado.toFixed(2) }}</td>
                                <td>{{ uso.fecha }}</td>
                            </tr>
                            <!-- Totales por contrato -->
                            <tr>
                                <td colspan="3"><strong>Total del Contrato:</strong></td>
                                <td>${{ contrato.totalMonto.toFixed(2) }}</td>
                                <td><strong>Total Usos:</strong> {{ contrato.totalUsos }}</td>
                            </tr>
                        </template>
                    </tbody>
                    <tfoot>
                        <!-- Promedios generales -->
                        <tr>
                            <td colspan="3"><strong>Promedio de Usos Similares:</strong></td>
                            <td colspan="2">{{ promedioUsosSimilares.toFixed(2) }}</td>
                        </tr>
                        <tr>
                            <td colspan="3"><strong>Promedio de Monto Aprobado Similares:</strong></td>
                            <td colspan="2">${{ promedioMontoAprobadoSimilares.toFixed(2) }}</td>
                        </tr>
                    </tfoot>
                </table>
                <!--Usos del Contrato Analizado-->
                <h3>Contrato Analizado Actualmente</h3>
                <table class="contratos-similares-table">
                    <thead>
                        <tr>
                            <th>Id Uso</th>
                            <th>Tipo de Uso</th>
                            <th>Monto Aprobado</th>
                            <th>Fecha</th>
                            <th>Analizado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(uso, index) in usosContrato" :key="index">
                            <td>{{ uso.idUso }}</td>
                            <td>{{ uso.tipoUso }}</td>
                            <td>${{ uso.montoAprobado.toFixed(2) }}</td>
                            <td>{{ uso.fecha }}</td>
                            <td>
                                <span v-if="uso.analizado">Sí</span>
                                <span v-else>No</span>
                            </td>
                        </tr>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="2"><strong>Total de Usos Analizados:</strong></td>
                            <td colspan="2">{{ totalUsosAnalizados }}</td>
                        </tr>
                        <tr>
                            <td colspan="2"><strong>Total de Monto Aprobado:</strong></td>
                            <td colspan="2">${{ totalMontoAnalizado.toFixed(2) }}</td>
                        </tr>
                    </tfoot>
                </table>

            </div>
        </div>

        <div class="evaluacion-acordeon">
            <div class="evaluacion-acordeon-header" @click="toggleEvaluacionUmbral()">
                <h2>Evaluacion Final</h2>
                <ul>
                    <li>Calificacion Factor de Excelencia: {{ analisisContrato.calificacionFactorExcelencia }}</li>
                    <li>U Total del Cliente: {{ analisisContrato.valorTotalU }}%</li>
                    <li>Umbral de Aceptación: {{ analisisContrato.umbralAceptacion }}%</li>
                    <li>Porcentaje  de Penalidad: {{ analisisContrato.porcentajePenalidad }}%</li>
                </ul>
            </div>
            <div v-if="mostrarEvaluacionUmbral" class="evaluacion-acordeon-content">
                <h3>Detalles de los Cálculos</h3>

                <!-- Fórmula del Umbral -->
                <p>
                    <strong>Fórmula aplicada:</strong> 
                    <br>
                    <code>U = 0.30 × E_automovil + 0.40 × E_historial + 0.20 × E_comparacion − F_excelencia</code>
                </p>
                <!-- Explicación de cada término -->
                <table class="detalles-calculo-table">
                    <thead>
                        <tr>
                            <th>Término</th>
                            <th>Descripción</th>
                            <th>Valor</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>E_automovil</td>
                            <td>Evaluación del riesgo del automóvil</td>
                            <td>{{ analisisContrato.evaluacionAutomovil }}%</td>
                        </tr>
                        <tr>
                            <td>E_historial</td>
                            <td>Evaluación del historial de usos del seguro</td>
                            <td>{{ analisisContrato.evaluacionHistorialUsos }}%</td>
                        </tr>
                        <tr>
                            <td>E_comparacion</td>
                            <td>Evaluación comparativa con clientes similares</td>
                            <td>{{ analisisContrato.evaluacionClientesSimilares }}%</td>
                        </tr>
                        <tr>
                            <td>F_excelencia</td>
                            <td>Factor de excelencia</td>
                            <td>{{ analisisContrato.calificacionFactorExcelencia }}%</td>
                        </tr>
                    </tbody>
                </table>
                <!-- Sustitución con valores reales -->
                <p>
                    <strong>Valores reales utilizados:</strong>
                    <br>
                    <code>
                        U = 0.30 × {{ analisisContrato.evaluacionAutomovil }} + 
                        0.40 × {{ analisisContrato.evaluacionHistorialUsos }} +
                        0.20 × {{ analisisContrato.evaluacionClientesSimilares }} −
                        {{ analisisContrato.calificacionFactorExcelencia }}
                    </code>
                </p>
                <p><strong>Resultado U Total del Cliente = </strong> {{ analisisContrato.valorTotalU }}%</p>
                <p><strong>Umbral de Aceptación = </strong> {{ analisisContrato.umbralAceptacion }}%</p>

                <!-- Rangos del Umbral -->
                <h3>Rangos del Umbral de Aceptación</h3>
                <ul>
                    <li><strong>Penalidad del 1.1%:</strong> Si U está por encima del umbral entre un 1% y un 10%.</li>
                    <li><strong>Penalidad del 1.5%:</strong> Si U está por encima del umbral por más del 10%.</li>
                    <li><strong>Bonificación del 1.2%:</strong> Si U está por debajo del umbral por un 10% o más.</li>
                    <li><strong>Sin Ajuste:</strong> Si U no entra en los rangos anteriores.</li>
                </ul>

                <!-- Resultado final -->
                <h3>Resultado Final</h3>
                <p><strong>Porcentaje de Penalidad/Bonificación aplicado:</strong> {{ analisisContrato.porcentajePenalidad }}%</p>            </div>
        </div>
    </div>

    <div class="ajuste-contrato">
        <h2>Ajuste del Contrato Analizado</h2>
        <div class="ajuste-contrato-content">
            <p>
            <label for="penalidad" class="input-label">Porcentaje de Penalidad:</label>
            <input type="number" id="penalidad" v-model="analisisContrato.porcentajePenalidad" class="input-field" readonly/>
            </p>
            <p>
                <label for="motivoAgregados" class="input-label">Motivo del Ajuste:</label>
                <textarea id="motivoAgregados" v-model="nuevoAjusteContrato.motivoAgregado" class="input-field" placeholder="Ingrese el motivo del Ajuste" required></textarea>
            </p>
            <div v-if="contratoYaAjustado">           
                <p>
                    <strong>Valor Total Antes del Ajuste:</strong>
                    ${{ contratoAnalizado.valorsubtotal.toFixed(2) }}
                </p>
                <p>
                    <strong>Valor Total Ajustado:</strong>
                    ${{ valorTotalAjustado.toFixed(2) }}
                </p> 
                <p class="mensaje-ajustado">El contrato ya ha sido ajustado.</p>
            </div>
            <div v-else>
                <p>
                    <strong>Valor Total Antes del Ajuste:</strong>
                    ${{ contratoAnalizado.valortotal.toFixed(2) }}
                </p>
                <p>
                    <strong>Valor Total Ajustado:</strong>
                    ${{ valorTotalAjustado.toFixed(2) }}
                </p>
                <button @click="ajustarContrato" class="btn-guardar">Aplicar Ajuste</button>
            </div>
            <button @click="cancelarAjuste" class="btn-regresar">Cancelar</button>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        data(){
            return {
                analisisContrato: {},
                contratoAnalizado: {
                    idContrato: null,
                    automovil: {
                        idAutomovil: '',
                        anio: null,
                        tipo: '',
                        marca: '',
                        modelo: '',
                        ciudadCirculacion: '',
                        usoDestinado: '',
                    },
                    cliente: {
                        idCliente: '',
                        nombre: '',
                        apellido: '',
                        direccion: '',
                        telefono: '',
                        correo: '',
                        password: '',
                    },
                    planSeguro: {
                        idPlan: null,
                        nombre: '',
                        perdidasParciales: false,
                        valorPerdidasParciales: 0,
                        perdidasTotales: false,
                        valorPerdidasTotales: 0,
                        auxilioMecanico: false,
                        mantenimientoVehicular: false,
                        valorPlan: 0,
                    },
                    fechaInicio: '',
                    fechaFinalizacion: '',
                    renovacionContrato: false,
                    fechaRenovacion: null,
                    valoresAgregados: 0,
                    motivoAgregados: '',
                    valorsubtotal: 0,
                    valortotal: 0,
                },
                usosContrato: [],
                contratosSimilares: [],
                totalMontoAnalizado: 0,
                totalUsosAnalizados: 0,
                promedioUsosSimilares: 0,
                promedioMontoAprobadoSimilares: 0,
                totalUsosContratosSimilares: 0,
                totalMontosContratosSimilares: 0,
                mostrarEvaluacionAutomovil: false,
                mostrarEvaluacionHistorial: false,
                mostrarEvaluacionSimilares: false,
                mostrarEvaluacionUmbral: false,
                mostrarFormularioAjuste: true,
                nuevoAjusteContrato: {
                    idContrato: '',
                    motivoAgregado: '',
                    penalidad: '',
                },
                valorTotalAjustado: 0,
                contratoYaAjustado: false,
            };
        },
        methods: {
            fetchAnalisis(){
                const contrato = JSON.parse(localStorage.getItem("contratoAnalizar"));
                
                axios.get("http://localhost:8080/contrato/analisis", {
                    params: { idContrato: contrato.idContrato},
                })
                .then((response) => {
                    this.analisisContrato = response.data;
                    this.contratoAnalizado = contrato;
                    console.log("Contrato Analizado:", this.contratoAnalizado); // Aquí lo imprimes
                    this.nuevoAjusteContrato.idContrato = contrato.idContrato;
                    this.nuevoAjusteContrato.penalidad = this.analisisContrato.porcentajePenalidad;
                    this.valorTotalAjustado = (contrato.planSeguro.valorPlan + contrato.planSeguro.valorPlan * this.nuevoAjusteContrato.penalidad/100); 
                    if (this.contratoAnalizado.motivoAgregados && this.contratoAnalizado.valoresAgregados){
                        this.contratoYaAjustado = true;
                    }
                    this.fetchUsos();
                    this.fetchContratosSimilares();
                })
                .catch((error) => {
                    console.error("Error al obtener el analisis del contrato: ", error); 
                });
            },
            fetchUsos(){
                axios.get("http://localhost:8080/uso/filtrar/contrato", {
                    params: { idContrato: this.contratoAnalizado.idContrato },
                })
                .then((response) => {
                    // Proceso para determinar si un uso fue analizado
                    this.usosContrato = response.data.map((uso) => ({
                        ...uso,
                        analizado:
                            uso.estadoReclamo === "Aprobado" &&
                            uso.tipoUso !== "Mantenimiento" &&
                            uso.tipoUso !== "Auxilio mecanico",
                    }));

                    // Calcular la suma de los montos analizados
                    this.totalMontoAnalizado = this.usosContrato
                        .filter((uso) => uso.analizado)
                        .reduce((sum, uso) => sum + uso.montoAprobado, 0);
                    // Contar el total de usos analizados
                    this.totalUsosAnalizados = this.usosContrato.filter((uso) => uso.analizado).length;
                })
                .catch((error) => {
                    console.error("Error al obtener usos:", error);
                });       
            },
            fetchContratosSimilares() {
                const contratosSimilaresMap = this.analisisContrato.contratosSimilares;

                // Procesar el Map de contratos similares
                const contratosSimilaresArray = Object.entries(contratosSimilaresMap).map(([contratoId, usos]) => {
                    const montoTotal = usos.reduce((sum, uso) => sum + uso.montoAprobado, 0);
                    return {
                        contratoId,
                        usos,
                        totalUsos: usos.length,
                        totalMonto: montoTotal,
                    };
                });

                // Calcular totales generales
                const totalUsosSimilares = contratosSimilaresArray.reduce((sum, contrato) => sum + contrato.totalUsos, 0);
                const totalMontosSimilares = contratosSimilaresArray.reduce((sum, contrato) => sum + contrato.totalMonto, 0);
                const totalContratosConUsos = contratosSimilaresArray.length;

                // Calcular los promedios
                this.promedioUsosSimilares =
                    totalContratosConUsos > 0 ? totalUsosSimilares / totalContratosConUsos : 0;
                this.promedioMontoAprobadoSimilares =
                    totalContratosConUsos > 0 ? totalMontosSimilares / totalContratosConUsos : 0;

                // Actualizar la lista de contratos similares
                this.contratosSimilares = contratosSimilaresArray;
            },
            ajustarContrato(){
                axios.put("http://localhost:8080/contrato/analisis/ajuste", null,{
                    params: {
                        idContrato: this.nuevoAjusteContrato.idContrato,
                        motivoAgregado: this.nuevoAjusteContrato.motivoAgregado,
                        penalidad: this.nuevoAjusteContrato.penalidad
                    },
                })
                .then(() => {
                    alert("Ajuste aplicado exitosamente.");
                    this.$router.push("/empleado/contratos");
                })
                .catch((error) => {
                    console.error("Erro al aplicar el ajuste:", error);
                    alert("No se pudo aplicar el ajuste.");
                });
            },
            cancelarAjuste() {
                this.$router.push("/empleado/contratos");
            },
            toggleEvaluacionAutomovil(){
                this.mostrarEvaluacionAutomovil = !this.mostrarEvaluacionAutomovil;
            },
            toggleEvaluacionHistorial(){
                this.mostrarEvaluacionHistorial = !this.mostrarEvaluacionHistorial;
            },
            toggleEvaluacionSimilares(){
                this.mostrarEvaluacionSimilares = !this.mostrarEvaluacionSimilares;
            },
            toggleEvaluacionUmbral(){
                this.mostrarEvaluacionUmbral = !this.mostrarEvaluacionUmbral;
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
        mounted(){
            this.fetchAnalisis();
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

    /* Contenedores y Acordeones de Evaluacion */
    .info-contrato{
        border: 1px solid #ccc;
        border-radius: 20px;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        background-color: #333;
        color: white;
        margin: 20px;
        cursor: pointer;
        text-align: center;
        font-size: 15px;
    }
    .info-contrato h2{
        margin: 20px 0;
    }

    .info-contrato-grid{
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 20px;
        padding: 10px 30px;
    }

    .info-contrato-grid-content ul{
        list-style-type: none;
        margin: 0;
    }

    .info-contrato-grid-content li{
        margin-bottom: 10px;
    }

    .evaluacion-section {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
        padding: 20px;
    }

    .evaluacion-acordeon{
        height: auto;
        border: 1px solid #ccc;
        border-radius: 20px;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
    }

    .evaluacion-acordeon-header {
        height: -webkit-fill-available;
        background-color: #333;
        color: white;
        padding: 10px;
        cursor: pointer;
        text-align: center;
        font-weight: bold;
        min-height: 80px; /* Asegura que todos los encabezados tengan la misma altura */ 
    }

    .evaluacion-acordeon-header:hover {
        background-color: #615e5e;
    }

    .evaluacion-acordeon-content {
        background-color: #f4f4f4;
        padding: 10px;
    }

    /*Informacion Historial Usos */
    .usos-table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
        font-size: 16px;
        text-align: left;
    }

    .usos-table th, .usos-table td {
        border: 1px solid #ccc;
        padding: 10px;
    }

    .usos-table th {
        background-color: #333;
        color: white;
    }

    .usos-table tbody tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    .usos-table tbody tr:hover {
        background-color: #f1f1f1;
    }

    .usos-table tfoot td {
        font-weight: bold;
        background-color: #ddd;
    }

    /* Resalta los usos analizados */
    .analizado {
        background-color: #d4f8d4; /* Verde claro */
        font-weight: bold; /* Opcional: hace que el texto sea más destacado */
    }

    /* Asegura que el texto sea legible incluso con colores claros */
    .analizado td {
        color: #333; /* Color oscuro para el texto */
    }


    /*Informacion Contratos Similares*/
    .contratos-similares-table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
        font-size: 16px;
        text-align: left;
    }

    .contratos-similares-table th, .contratos-similares-table td {
        border: 1px solid #ccc;
        padding: 10px;
    }

    .contratos-similares-table th {
        background-color: #333;
        color: white;
        text-align: center;
    }

    .contratos-similares-table tbody tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    .contratos-similares-table tbody tr:hover {
        background-color: #f1f1f1;
    }

    .contratos-similares-table tfoot td {
        font-weight: bold;
        background-color: #ddd;
    }

    /*Evaluacion Final */
    .detalles-calculo-table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
        font-size: 16px;
        text-align: left;
    }

    .detalles-calculo-table th, .detalles-calculo-table td {
        border: 1px solid #ccc;
        padding: 10px;
    }

    .detalles-calculo-table th {
        background-color: #333;
        color: white;
        text-align: center;
    }

    .detalles-calculo-table tbody tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    .detalles-calculo-table tbody tr:hover {
        background-color: #f1f1f1;
    }


    .ajuste-contrato{
        border: 1px solid #ccc;
        border-radius: 20px;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        background-color: #333;
        color: white;
        margin: 20px;
        cursor: pointer;
        text-align: center;
        font-size: 15px;
        padding: 0 300px;

    }

    .ajuste-contrato-content{
        padding: 10px;
    }

    .input-label {
        display: block;
        font-weight: bold;
        margin-bottom: 5px;
    }
    
    .input-field {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 16px;
    }

    .btn-guardar,
    .btn-regresar {
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

</style>