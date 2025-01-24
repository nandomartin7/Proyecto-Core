<template>
  <div>
    <div>
      <!-- Botón para mostrar u ocultar el contenedor principal -->
      <button @click="toggleContainerAutomovil" class="toggleButton">
        {{ showContainerAutomovil ? 'Ocultar Automóviles' : 'Automóviles' }}
      </button>

      <button @click="toggleContainerCliente" class="toggleButton">
        {{ showContainerCliente ? 'Ocultar Clientes' : 'Clientes' }}
      </button>

      <button @click="toggleContainerContrato" class="toggleButton">
        {{ showContainerContrato ? 'Ocultar Contratos' : 'Contratos' }}
      </button>

      <button @click="toggleContainerEmpleado" class="toggleButton">
        {{ showContainerEmpleado ? 'Ocultar Empleados' : 'Empleados' }}
      </button>

      <button @click="toggleContainerPlanSeguro" class="toggleButton">
        {{ showContainerPlanSeguro ? 'Plan Seguro' : 'Plan Seguro'}}
      </button>

      <button @click="toggleContainerUsoSeguro" class="toggleButton">
        {{ showContainerUsosSeguro ? 'Ocultar Usos del Seguro' : 'Usos del Seguro' }}
      </button>

      <button @click="logout">Cerrar Sesión</button>
    </div>

    <!-- Contenedor Clientes-->
    <div v-if="showContainerCliente">
      <h2>Lista de Clientes</h2>

      <button @click="toggleAgregarCliente" class="btnAgregarCliente">
        {{ showAgregarCliente ? 'Cancelar Registro de Cliente' : 'Registrar Cliente'}}
      </button>

      <div v-if="showAgregarCliente">
        <h2>Agregar Cliente</h2>
        <form @submit.prevent="submitCliente" class="formAgregarCliente">
          <p><input v-model="nuevoCliente.idCliente" class="input-field" placeholder="Numero de Cédula" required/></p>
          <p><input v-model="nuevoCliente.nombre" class="input-field" placeholder="Nombre" required/></p>
          <p><input v-model="nuevoCliente.apellido" class="input-field" placeholder="Apellido" required/></p>
          <p><input v-model="nuevoCliente.direccion" class="input-field" placeholder="Dirección" required/></p>
          <p><input v-model="nuevoCliente.telefono" class="input-field" placeholder="Teléfono" required/></p>
          <p><input v-model="nuevoCliente.correo" class="input-field" placeholder="Correo" required/></p>
          <p><input type="password" v-model="nuevoCliente.password" class="input-field" placeholder="Contraseña" required/></p>
          <button type="submit" class="btnGuardar">Guardar</button>
        </form>
      </div>

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Correo</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cliente in clientes" :key="cliente.idCliente">
            <td>{{ cliente.idCliente }}</td>
            <td>{{ cliente.nombre }}</td>
            <td>{{ cliente.apellido }}</td>
            <td>{{ cliente.correo }}</td>
            <td>
              <button @click="verCliente(cliente)" class="btnVerCliente">Ver</button>
              <button @click="editarCliente(cliente)" class="btnEditarCliente">Editar</button>
              <button @click="eliminarCliente(cliente.idCliente)" class="btnEliminarCliente">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
  
      <!-- Modal para ver toda la información del cliente -->
      <div v-if="clienteSeleccionado">
        <h3>Detalles del Cliente</h3>
        <p>ID: {{ clienteSeleccionado.idEmpleado }}</p>
        <p>Nombre: {{ clienteSeleccionado.nombre }}</p>
        <p>Apellido: {{ clienteSeleccionado.apellido }}</p>
        <p>Correo: {{ clienteSeleccionado.correo }}</p>
        <p>Dirección: {{ clienteSeleccionado.direccion }}</p>
        <p>Teléfono: {{ clienteSeleccionado.telefono }}</p>
        <button @click="cerrarModal">Cerrar</button>
      </div>
  
      <!-- Modal para editar cliente -->
      <div v-if="clienteEditando">
        <h3>Editar Cliente</h3>
        <p><label>Nombre: <input v-model="clienteEditando.nombre" required/></label></p>
        <p><label>Apellido: <input v-model="clienteEditando.apellido" required/></label></p>
        <p><label>Correo: <input v-model="clienteEditando.correo" required/></label></p>
        <p><label>Dirección: <input v-model="clienteEditando.direccion" required/></label></p>
        <p><label>Teléfono: <input v-model="clienteEditando.telefono" required/></label></p>  
        <button @click="guardarCambiosCliente">Guardar</button>
        <button @click="cancelarEdicion">Cancelar</button>
      </div>
    </div>

    <!--Contenedor Automoviles-->
    <div v-if="showContainerAutomovil">
      <h2>Lista de Automoviles</h2>

      <button @click="toggleAgregarAutomovil" class="btnAgregarAutomovil">
        {{ showAgregarAutomovil ? 'Cancelar Registro de Automovil' : 'Registrar Automovil'}}
      </button>

      <div v-if="showAgregarAutomovil">
        <h2>Agregar Automovil</h2>
        <form @submit.prevent="submitAutomovil" class="formAgregarAutomovil">
          <p><input v-model="nuevoAutomovil.idAutomovil" class="input-field" placeholder="Placa del Automovil" required/></p>
          <p><input type="number" v-model.number="nuevoAutomovil.anio" class="input-field" placeholder="Año" required min="1990"/></p>
          <p>    
            <select v-model="nuevoAutomovil.tipo" class="input-select" required>
              <option value="" disabled>Selecciona un tipo de automovil</option>
              <option value="Automóvil">Automovil</option>
              <option value="Jeep / Caminoneta">Jeep / Camioneta</option>
              <option value="Deportivo">Deportivo</option>
            </select>
          </p>
          
          <p><input v-model="nuevoAutomovil.marca" class="input-field" placeholder="Marca" required/></p>
          <p><input v-model="nuevoAutomovil.modelo" class="input-field" placeholder="Modelo" required/></p>
          <p>    
            <select v-model="nuevoAutomovil.ciudadCirculacion" class="input-select" required>
              <option value="" disabled>Selecciona una ciudad de circulacion</option>
              <option value="Quito">Quito</option>
              <option value="Guayaquil">Guayaquil</option>
            </select>
          </p>
          <p>    
            <select v-model="nuevoAutomovil.usoDestinado" class="input-select" required>
              <option value="" disabled>Selecciona un uso destinado</option>
              <option value="Familiar">Familiar</option>
              <option value="Trabajo">Trabajo</option>
              <option value="Carga / Comercio">Carga / Comercio</option>
            </select>
          </p>
          <button type="submit" class="btnGuardar">Guardar</button>
        </form>
      </div>

      <!--Tabla para mostrar Automoviles-->
      <table>
        <thead>
          <tr>
            <th>Placa</th>
            <th>Marca</th>
            <th>Modelo</th>
            <th>Ciudad</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="automovil in automoviles" :key="automovil.idAutomovil">
            <td>{{ automovil.idAutomovil }}</td>
            <td>{{ automovil.marca }}</td>
            <td>{{ automovil.modelo }}</td>
            <td>{{ automovil.ciudadCirculacion }}</td>
            <td>
              <button @click="verAutomovil(automovil)" class="btnVerAutomovil">Ver</button>
              <button @click="editarAutomovil(automovil)" class="btnEditarAutomovil">Editar</button>
              <button @click="eliminarAutomovil(automovil.idAutomovil)" class="btnEliminarAutomovil">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!--Modal para Ver Automovil-->
      <div v-if="automovilSeleccionado">
        <h3>Detalles del Automovil</h3>
        <p>Placa: {{ automovilSeleccionado.idAutomovil }}</p>
        <p>Año: {{ automovilSeleccionado.anio }}</p>
        <p>Tipo de Automovil: {{ automovilSeleccionado.tipo }}</p>
        <p>Marca: {{ automovilSeleccionado.marca }}</p>
        <p>Modelo: {{ automovilSeleccionado.modelo }}</p>
        <p>Ciudad de Circulacion: {{ automovilSeleccionado.ciudadCirculacion }}</p>
        <p>Uso Destinado: {{ automovilSeleccionado.usoDestinado }}</p>
        <button @click="cerrarModal">Cerrar</button>
      </div>
      <!--Modal para Editar Automovil-->
      <div v-if="automovilEditando">
        <h3>Editar Automovil</h3>
        <p>Placa: {{ automovilEditando.idAutomovil }}</p>
        <p><label>Año: <input type="number" v-model="automovilEditando.anio" required/></label></p>
        <p>
          <label>Tipo de Automovil : 
            <select v-model="automovilEditando.tipo" required>
              <option value="Automóvil">Automovil</option>
              <option value="Jeep / Camioneta">Jeep / Camioneta</option>
              <option value="Deportivo">Deportivo</option>
            </select>
          </label>
        </p>                    
        <p><label>Marca: <input v-model="automovilEditando.marca" required/></label></p>
        <p><label>Modelo: <input v-model="automovilEditando.modelo" required/></label></p>
        <p>
          <label>Ciudad de Circulacion : 
            <select v-model="automovilEditando.ciudadCirculacion" required>
              <option value="Quito">Quito</option>
              <option value="Guayaquil">Guayaquil</option>
            </select>
          </label>
        </p>
        <p>
          <label>Uso Destinado : 
            <select v-model="automovilEditando.usoDestinado" required>
              <option value="Familiar">Familiar</option>
              <option value="Trabajo">Trabajo</option>
              <option value="Carga / Comercio">Carga / Comercio</option>
            </select>
          </label>
        </p>
        <button @click="guardarCambiosAutomovil">Guardar</button>
        <button @click="cancelarEdicion">Cancelar</button>
      </div>
    </div>

    <!--Contenedor de Contrato-->
    <div v-if="showContainerContrato">
      <h2>Lista de Contratos</h2>
      <button @click="toggleAgregarContrato" class="btnAgregarContrato">
        {{ showAgregarContrato ? 'Cancelar Registro de Contrato' : 'Registrar Contrato'}}
      </button>

      <div v-if="showAgregarContrato">
        <h2>Agregar Contrato</h2>
        <form @submit.prevent="submitContrato" class="formAgregarContrato">
          <!--Agregar Registro de AUTOMOVIL-->
          <p>
            <label>Automovil</label>
            <select v-model="nuevoContrato.automovil" class="input-select" required>
              <option v-for="auto in automoviles" :key="auto.idAutomovil" :value="auto">
                {{ auto.marca }} - {{ auto.modelo }} ({{ auto.idAutomovil }})
              </option>
            </select>
          </p>
          <!--Agregar Registro de CLIENTE-->
          <p>
            <label>Cliente:</label>
            <select v-model="nuevoContrato.cliente" class="input-select" required>
              <option v-for="cliente in clientes" :key="cliente.idCliente" :value="cliente">
                {{ cliente.nombre }} {{ cliente.apellido }} ({{ cliente.idCliente }})
              </option>
            </select>
          </p>
          <!--Agregar Registro de PLAN-->
          <p>
            <label>Plan de Seguro:</label>
            <select v-model="nuevoContrato.planSeguro" class="input-select" required @change="actualizarValores">
              <option v-for="plan in planes" :key="plan.idPlan" :value="plan">
                {{ plan.nombre }} - ${{ plan.valorPlan }}
              </option>
            </select>
          </p>
          <p>
            <label>Fecha de Inicio</label>
            <input type="date" v-model="nuevoContrato.fechaInicio" class="input-field" required/>
          </p>
          <p>
            <label>Fecha de Finalizacion</label>
            <input type="date" v-model="nuevoContrato.fechaFinalizacion" class="input-field" required/>
          </p>       
          <p><input type="number" v-model="nuevoContrato.valorsubtotal" class="input-field" readonly/></p>            
          <p><input type="number" v-model="nuevoContrato.valortotal" class="input-field" readonly/></p>            
          <button type="submit" class="btnGuardar">Guardar</button>
        </form>
      </div>

      <!--Lista para mostrar Contratos-->
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Automovil</th>
            <th>Cliente</th>
            <th>Plan de Seguro</th>
            <th>Valor Total</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="contrato in contratos" :key="contrato.idContrato">
            <td>{{ contrato.idContrato }}</td>
            <td>{{ contrato.automovil.idAutomovil }}</td>
            <td>cedula: {{ contrato.cliente.idCliente }}  nombre: {{ contrato.cliente.nombre }} {{ contrato.cliente.apellido }}</td>
            <td>{{ contrato.planSeguro.nombre }}</td>
            <td>{{ contrato.valortotal }}</td>
            <td>
              <button @click="verContrato(contrato)" class="btnVerContrato">Ver</button>
              <button @click="editarContrato(contrato)" class="btnEditarContrato">Editar</button>
              <button @click="eliminarContrato(contrato.idContrato)" class="btnEliminarContrato">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!--Modal para Ver Contrato-->
      <div v-if="contratoSeleccionado">
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

      <!--Modal para Editar Contrato-->
      <div v-if="contratoEditando">
        <h3>Detalle Contrato</h3>
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

    <!--Contenedor de Empleados-->
    <div v-if="showContainerEmpleado">
      <h2>Lista de Empleados</h2>

      <button @click="toggleAgregarEmpleado" class="btnAgregarEmpleado">
        {{ showAgregarEmpleado ? 'Cancelar Registro de Empleado' : 'Registrar Empleado'}}
      </button>

      <div v-if="showAgregarEmpleado">
        <h2>Agregar Empleado</h2>
        <form @submit.prevent="submitEmpleado" class="formAgregarEmpleado">
          <p><input v-model="nuevoEmpleado.idEmpleado" class="input-field" placeholder="Numero de Cédula" required/></p>
          <p><input v-model="nuevoEmpleado.nombre" class="input-field" placeholder="Nombre" required/></p>
          <p><input v-model="nuevoEmpleado.apellido" class="input-field" placeholder="Apellido" required/></p>
          <p><input v-model="nuevoEmpleado.direccion" class="input-field" placeholder="Dirección" required/></p>
          <p><input v-model="nuevoEmpleado.telefono" class="input-field" placeholder="Teléfono" required/></p>
          <p><input v-model="nuevoEmpleado.correo" class="input-field" placeholder="Correo" required/></p>
          <p><input type="password" v-model="nuevoEmpleado.password" class="input-field" placeholder="Contraseña" required/></p>
          <button type="submit" class="btnGuardar">Guardar</button>
        </form>
      </div>

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Correo</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="empleado in empleados" :key="empleado.idEmpleado">
            <td>{{ empleado.idEmpleado }}</td>
            <td>{{ empleado.nombre }}</td>
            <td>{{ empleado.apellido }}</td>
            <td>{{ empleado.correo }}</td>
            <td>
              <button @click="verEmpleado(empleado)" class="btnVerEmpleado">Ver</button>
              <button @click="editarEmpleado(empleado)" class="btnEditarEmpleado">Editar</button>
              <button @click="eliminarEmpleado(empleado.idEmpleado)" class="btnEliminarEmpleado">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Modal para ver empleado -->
      <div v-if="empleadoSeleccionado">
        <h3>Detalles del Empleado</h3>
        <p>ID: {{ empleadoSeleccionado.idEmpleado }}</p>
        <p>Nombre: {{ empleadoSeleccionado.nombre }}</p>
        <p>Apellido: {{ empleadoSeleccionado.apellido }}</p>
        <p>Correo: {{ empleadoSeleccionado.correo }}</p>
        <p>Dirección: {{ empleadoSeleccionado.direccion }}</p>
        <p>Teléfono: {{ empleadoSeleccionado.telefono }}</p>
        <button @click="cerrarModal">Cerrar</button>
      </div>
  
      <!-- Modal para editar cliente -->
      <div v-if="empleadoEditando">
        <h3>Editar Empleado</h3>
        <p><label>Nombre: <input v-model="empleadoEditando.nombre" required/></label></p>
        <p><label>Apellido: <input v-model="empleadoEditando.apellido" required/></label></p>
        <p><label>Correo: <input v-model="empleadoEditando.correo" required/></label></p>
        <p><label>Dirección: <input v-model="empleadoEditando.direccion" required/></label></p>
        <p><label>Teléfono: <input v-model="empleadoEditando.telefono" required/></label></p>  
        <button @click="guardarCambiosEmpleado">Guardar</button>
        <button @click="cancelarEdicion">Cancelar</button>
      </div>
    </div>

    <!--Contenedor Plan Seguro-->
    <div v-if="showContainerPlanSeguro">
      <h2>Lista de Planes de Seguro</h2>

      <button @click="toggleAgregarPlanSeguro" class="btnAgregarPlanSeguro">
        {{ showAgregarPlanSeguro ? 'Cancelar Registro de Plan de Seguro' : 'Registrar Plan de Seguro'}}
      </button>

      <div v-if="showAgregarPlanSeguro">
        <h2>Agregar Plan de Seguro</h2>
        <form @submit.prevent="submitPlan" class="formAgregarAutomovil">
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

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Valor Plan</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="plan in planes" :key="plan.idPlan">
            <td>{{ plan.idPlan }}</td>
            <td>{{ plan.nombre }}</td>
            <td>{{ plan.valorPlan }}</td>
            <td>
              <button @click="verPlan(plan)" class="btnVerPlan">Ver</button>
              <button @click="editarPlan(plan)" class="btnEditarPlan">Editar</button>
              <button @click="eliminarPlan(plan.idPlan)" class="btnEliminarPlan">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Modal para ver Plan -->
      <div v-if="planSeleccionado">
        <h3>Detalles del Plan</h3>
        <p>Nombre: {{ planSeleccionado.nombre }}</p>
        <p>Incluye Perdidas Parciales: {{ planSeleccionado.perdidasParciales ? 'SI' : 'NO' }}</p>
        <p>Valor de Perdidas Parciales: {{ planSeleccionado.valorPerdidasParciales }}</p>
        <p>Incluye Perdidas Totales: {{ planSeleccionado.perdidasTotales ? 'SI' : 'NO' }}</p>
        <p>Valor de Perdidas Totales: {{ planSeleccionado.valorPerdidasTotales }}</p>
        <p>Incluye Auxilio Mecanico: {{ planSeleccionado.auxilioMecanico ? 'SI' : 'NO' }}</p>
        <p>Incluye Mantenimiento Vehicular: {{ planSeleccionado.mantenimientoVehicular ? 'SI' : 'NO' }}</p>
        <p>Valor del Plan: {{ planSeleccionado.valorPlan }}</p>
        <button @click="cerrarModal">Cerrar</button>
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

    <!-- Contenedor Usos de Seguro-->
    <div v-if="showContainerUsosSeguro">
      <h2>Lista de Usos del Seguro</h2>
      <button @click="toggleAgregarUsoSeguro" class="btnAgregarUsoSeguro">
        {{ showAgregarUsoSeguro ? 'Cancelar Registro de Uso del Seguro' : 'Agregar Uso del Seguro'}}
      </button>

      <div v-if="showAgregarUsoSeguro">
        <h2>Agregar Cliente</h2>
        <form @submit.prevent="submitUso" class="formAgregarCliente">
          <!--Agregar Registro del CONTRATO-->
          <p>
            <label>Contrato:</label>
            <select v-model="nuevoUso.contrato" class="input-select" required>
              <option v-for="contrato in contratos" :key="contrato.idContrato" :value="contrato">
                #Contrato: {{ contrato.idContrato }}   Placa del Auto: {{ contrato.automovil.idAutomovil }}  Cliente: {{ contrato.cliente.nombre }} {{ contrato.cliente.apellido }}
              </option>
            </select>
          </p>
          <p>    
            <select v-model="nuevoUso.tipoUso" class="input-select" required>
              <option value="" disabled>Tipo de Uso del Seguro</option>
              <option value="Choque grave">Choque Grave</option>
              <option value="Choque leve">Choque Leve</option>
              <option value="Daño electrico">Daño Electrico</option>
              <option value="Daño mecanico no grave">Daño Mecanico</option>
              <option value="Mantenimiento">Mantenimiento</option>
              <option value="Auxilio Mecánico">Auxilio Mecanico</option>
            </select>
          </p>            
          <p><input v-model="nuevoUso.descripcion" class="input-field" placeholder="Descripcion" required/></p>
          <p><input type="number" v-model.number="nuevoUso.montoAprobado" class="input-field" placeholder="Monto Aprobado" required step="0.01"/></p>            
          <button type="submit" class="btnGuardar">Guardar</button>
        </form>
      </div>

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Contrato</th>
            <th>Fecha</th>
            <th>Tipo de Uso</th>
            <th>Monto Aprobado</th>
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
            <td>{{ uso.montoAprobado }}</td>
            <td>
              <button @click="verUso(uso)" class="btnVerUso">Ver</button>
              <button @click="editarUso(uso)" class="btnEditarUso">Editar</button>
              <button @click="eliminarUso(uso.idUso)" class="btnEliminarUso">Eliminar</button>
            </td>
          </tr>
        </tbody>    
      </table>

      <!--Modal para ver Uso del Seguro-->
      <div v-if="usoSeleccionado">
        <h3>Detalles del Uso del Seguro</h3>
        <p>ID: {{ usoSeleccionado.idUso }}</p>
        <p>Contrato: {{ usoSeleccionado.contrato.idContrato }}
          Placa: {{ usoSeleccionado.contrato.automovil.idAutomovil }}  
          Cliente: {{ usoSeleccionado.contrato.cliente.nombre }} {{ usoSeleccionado.contrato.cliente.apellido }} 
          Plan: {{ usoSeleccionado.contrato.planSeguro.nombre }}
        </p>
        <p>Fecha: {{ usoSeleccionado.fecha }}</p>
        <p>Tipo de Uso: {{ usoSeleccionado.tipoUso }}</p>
        <p>Descripcion: {{ usoSeleccionado.descripcion }}</p>
        <p>Monto Aprobado: {{ usoSeleccionado.montoAprobado }}</p>
        <p>Estado de Reclamo: {{ usoSeleccionado.estadoReclamo }}</p>
        <button @click="cerrarModal">Cerrar</button>
      </div>

      <!--Modal para editar Uso del Seguro-->
      <div v-if="usoEditando">
        <h3>Detalle Uso del Seguro</h3>
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
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      //Contenedores de cada Objeto
      showContainerAutomovil: true,
      showContainerCliente: false,
      showContainerContrato: false,
      showContainerEmpleado: false, 
      showContainerPlanSeguro: false,
      showContainerUsosSeguro: false,// Variable para controlar la visibilidad del contenedor
      
      //Contenedor para crear cada objeto
      showAgregarCliente: false,
      showAgregarAutomovil: false,
      showAgregarContrato: false,
      showAgregarEmpleado: false,
      showAgregarPlanSeguro: false,
      showAgregarUsoSeguro: false,

      nuevoCliente: {
        idCliente: '',
        nombre: '',
        apellido: '',
        direccion: '',
        telefono: '',
        correo: '',
        password: '',
      },
      clientes: [], // Lista de clientes para la tabla
      clienteSeleccionado: null, // Cliente seleccionado para ver detalles
      clienteEditando: null, // Cliente seleccionado para editar
      
      nuevoAutomovil: {
        idAutomovil: '',
        anio: '',
        tipo: '',
        marca: '',
        modelo: '',
        ciudadCirculacion: '',
        usoDestinado: '',
      },
      automoviles: [],
      automovilSeleccionado: null,
      automovilEditando: null,

      nuevoContrato: {
        automovil: null,
        cliente: null,
        planSeguro: null,
        fechaInicio: '',
        fechaFinalizacion: '',
        /*renovacionContrato: false,
        fechaRenovacion: '',
        valoresAgregados: '',
        motivoAgregados: '',*/
        valorsubtotal: '',
        valortotal: '',
      },
      contratos: [],
      contratoSeleccionado: null,
      contratoEditando: null,

      nuevoEmpleado: {
        idEmpleado: '',
        nombre: '',
        apellido: '',
        direccion: '',
        telefono: '',
        correo: '',
        password: '',
      },
      empleados: [],
      empleadoSeleccionado: null,
      empleadoEditando: null,

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
      planes: [],
      planSeleccionado: null,
      planEditando: null,

      nuevoUso: {
        contrato: null,
        tipoUso: '',
        descripcion: '',
        montoAprobado: '',
        estadoReclamo: 'Pendiente',
      },
      usos: [],
      usoSeleccionado: null,
      usoEditando: null,
    };
  },
  watch:{
    'contratoEditando.renovacionContrato'(newValue) {
      // Verificar si la renovación está activada y la fecha de renovación ya tiene un valor
      if (this.contratoEditando && newValue) {
        if (this.contratoEditando.fechaRenovacion) {
          // Si la fecha de renovación ya existe, solo actualiza si la fecha de finalización ha cambiado
          if (this.contratoEditando.fechaFinalizacion !== this.contratoEditando.fechaRenovacion) {
            this.contratoEditando.fechaRenovacion = this.contratoEditando.fechaFinalizacion;
          }
        } else {
          // Si no hay fecha de renovación, asigna la fecha de finalización
          this.contratoEditando.fechaRenovacion = this.contratoEditando.fechaFinalizacion;
        }
      }
    },
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
    toggleContainerAutomovil(){
      this.showContainerAutomovil = !this.showContainerAutomovil;
      this.showContainerCliente = false;
      this.showContainerContrato = false
      this.showContainerEmpleado = false;
      this.showContainerPlanSeguro = false;
      this.showContainerUsosSeguro = false;
    },

    toggleContainerCliente() {
      this.showContainerCliente = !this.showContainerCliente;
      this.showContainerAutomovil = false;
      this.showContainerContrato = false;
      this.showContainerEmpleado = false;
      this.showContainerPlanSeguro = false;
      this.showContainerUsosSeguro = false;
    },

    toggleContainerContrato(){
      this.showContainerContrato = !this.showContainerContrato;
      this.showContainerAutomovil = false;
      this.showContainerCliente = false;
      this.showContainerEmpleado = false;
      this.showContainerPlanSeguro = false;
      this.showContainerUsosSeguro = false;
    },

    toggleContainerEmpleado(){
      this.showContainerEmpleado = !this.showContainerEmpleado;
      this.showContainerAutomovil = false;
      this.showContainerCliente = false;
      this.showContainerContrato = false;
      this.showContainerPlanSeguro = false;
      this.showContainerUsosSeguro = false;
    },

    toggleContainerPlanSeguro(){
      this.showContainerPlanSeguro = !this.showContainerPlanSeguro;
      this.showContainerAutomovil = false;
      this.showContainerCliente = false;
      this.showContainerContrato = false;
      this.showContainerEmpleado = false;
      this.showContainerUsosSeguro = false;
    },

    toggleContainerUsoSeguro(){
      this.showContainerUsosSeguro = !this.showContainerUsosSeguro;
      this.showContainerAutomovil = false;
      this.showContainerCliente = false;
      this.showContainerContrato = false;
      this.showContainerEmpleado = false;
      this.showContainerPlanSeguro = false;
    },

    //Metodos para abrir formulario de registro:
    toggleAgregarCliente(){
      this.showAgregarCliente = !this.showAgregarCliente;
    },
    toggleAgregarAutomovil(){
      this.showAgregarAutomovil = !this.showAgregarAutomovil;
    },
    toggleAgregarContrato(){
      this.showAgregarContrato = !this.showAgregarContrato;
    },
    toggleAgregarEmpleado(){
      this.showAgregarEmpleado = !this.showAgregarEmpleado;
    },
    toggleAgregarPlanSeguro(){
      this.showAgregarPlanSeguro = !this.showAgregarPlanSeguro;
    },
    toggleAgregarUsoSeguro(){
      this.showAgregarUsoSeguro = !this.showAgregarUsoSeguro;
    },
    
    //Metodo fetch Objetos
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
    fetchEmpleados(){
      axios.get('http://localhost:8080/empleado')
        .then(response => {
          this.empleados = response.data;
        })
        .catch(error => {
          console.error("Error fetching empleados:", error);
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
    fetchUsos(){
      axios.get('http://localhost:8080/uso')
        .then(response => {
          this.usos = response.data;
        })
        .catch(error => {
          console.error("Error fetching usos de seguro:", error);
        })
    },

    actualizarValores(){
      if(this.nuevoContrato.planSeguro){
        this.nuevoContrato.valorsubtotal = this.nuevoContrato.planSeguro.valorPlan;
        this.nuevoContrato.valortotal = this.nuevoContrato.valorsubtotal
      }
    },

    //Metodos para enviar formulario y agregar un nuevo objeto
    submitAutomovil(){
      axios.post('http://localhost:8080/automovil', this.nuevoAutomovil)
        .then(() => {
          this.fetchAutomoviles();
          this.resetAtomovil();
          this.$router.push('/admin/inicio');
        })
        .catch(error => {
          console.error(error);
        });
    },
    submitCliente(){
      axios.post('http://localhost:8080/cliente', this.nuevoCliente)
        .then(() => {
          this.fetchClientes();
          this.resetCliente();
          this.$router.push('/admin/inicio');
        })
        .catch(error => {
          console.error(error);
        });
    },
    submitContrato(){
      if (this.nuevoContrato.fechaInicio && this.nuevoContrato.fechaFinalizacion) {
        // Convertir las fechas a strings en formato "YYYY-MM-DD"
        const fechaInicio = new Date(this.nuevoContrato.fechaInicio).toISOString().split('T')[0];
        const fechaFinalizacion = new Date(this.nuevoContrato.fechaFinalizacion).toISOString().split('T')[0];
        this.nuevoContrato.fechaInicio = fechaInicio.toString();
        this.nuevoContrato.fechaFinalizacion = fechaFinalizacion.toString();
      }

      axios.post('http://localhost:8080/contrato', this.nuevoContrato)
        .then(() => {
            this.fetchContratos();
            this.resetContrato();
            this.$router.push('/admin/inicio');
          })
          .catch(error => {
            console.error(error);
          });
    },
    submitEmpleado(){
      axios.post('http://localhost:8080/empleado', this.nuevoEmpleado)
        .then(() => {
          this.fetchEmpleados();
          this.resetEmpleado();
          this.$router.push('/admin/inicio');
        })
        .catch(error => {
          console.error(error);
        });
    },
    submitPlan(){
      axios.post('http://localhost:8080/plan', this.nuevoPlan)
        .then(() => {
          this.fetchPlanes();
          this.resetPlan();
          this.$router.push('/admin/inicio');
        })
        .catch(error => {
          console.error(error);
        });
    },
    submitUso(){
      axios.post('http://localhost:8080/uso', this.nuevoUso)
        .then(() => {
            this.fetchUsos();
            this.resetUso();
            this.$router.push('/admin/inicio');
          })
          .catch(error => {
            console.error(error);
          });

    },

    //Metodod para resetear objeto
    resetCliente(){
      this.nuevoCliente = {
        idCliente: '',
        nombre: '',
        apellido: '',
        direccion: '',
        telefono: '',
        correo: '',
        password: '',
      };
    },
    resetAtomovil(){
      this.nuevoAutomovil= {
        idAutomovil: '',
        anio: '',
        tipo: '',
        marca: '',
        modelo: '',
        ciudadCirculacion: '',
        usoDestinado: '',
      };
    },
    resetContrato(){
      this.nuevoContrato= {
        automovil: null,
        cliente: null,
        planSeguro: null,
        fechaInicio: '',
        fechaFinalizacion: '',
        /*renovacionContrato: '',
        fechaRenovacion: '',
        valoresAgregados: '',
        motivoAgregados: '',*/
        valorsubtotal: '',
        valortotal: '',
      };
    },
    resetEmpleado(){
      this.nuevoEmpleado = {
        idEmpleado: '',
        nombre: '',
        apellido: '',
        direccion: '',
        telefono: '',
        correo: '',
        password: '',
      };
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
    resetUso(){
      this.nuevoUso= {
        contrato: null,
        tipoUso: '',
        descripcion: '',
        montoAprobado: '',
        estadoReclamo: '',
      };
    },

    //Metodos para ver objeto en especifico
    verAutomovil(automovil) {
      this.automovilSeleccionado = automovil;
    },
    verCliente(cliente) {
      this.clienteSeleccionado = cliente;
    },
    verContrato(contrato) {
      this.contratoSeleccionado = contrato;
    },
    verEmpleado(empleado){
      this.empleadoSeleccionado = empleado;
    },
    verPlan(plan){
      this.planSeleccionado = plan;
    },
    verUso(uso){  
      this.usoSeleccionado = uso;
    },
    
    actualizarFechas(){
      if (this.contratoEditando.renovacionContrato && this.contratoEditando.fechaRenovacion == null ){
        this.contratoEditando.fechaRenovacion = this.contratoEditando.fechaFinalizacion;
      } else {
        this.contratoEditando.fechaRenovacion = '';
        this.contratoEditando.fechaFinalizacion = ''; 
      }
    },

    //Metodos para editar objeto en especifico
    editarAutomovil(automovil){
      this.automovilEditando = { ...automovil };
    },
    editarCliente(cliente) {
      this.clienteEditando = { ...cliente };
    },
    editarContrato(contrato) {
      this.contratoEditando = { ...contrato};
    },
    editarEmpleado(empleado){
      this.empleadoEditando = { ...empleado};
    },
    editarPlan(plan){
      this.planEditando = { ...plan};
    },
    editarUso(uso){
      this.usoEditando = { ...uso};
    },

    //Metodo para guardar cambios de actualizacion de datos
    guardarCambiosAutomovil(){  
      axios.put(`http://localhost:8080/automovil/${this.automovilEditando.idAutomovil}`, this.automovilEditando)
        .then(response => {
          this.fetchAutomoviles();
          this.cancelarEdicion();
          const index =  this.automovilEditando.idAutomovil;
          if (index !== -1){
            this.automoviles[index] = response.data;
          }
          this.automovilEditando = null;
        })
        .catch(error => {
          console.error("Error actualizando automovil:", error);
        });      
    },
    guardarCambiosCliente() {
      axios.put(`http://localhost:8080/cliente/${this.clienteEditando.idCliente}`, this.clienteEditando)
        .then(response => {
          this.fetchClientes();
          this.cancelarEdicion();
          const index =  this.clienteEditando.idCliente;
          if (index !== -1){
            this.clientes[index] = response.data;
          }
          this.clienteEditando = null;
        })
        .catch(error => {
          console.error("Error actualizando cliente:", error);
        });  
    },
    guardarCambiosContrato(){
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
    guardarCambiosEmpleado(){
      axios.put(`http://localhost:8080/empleado/${this.empleadoEditando.idEmpleado}`, this.empleadoEditando)
        .then(response => {
          this.fetchEmpleados();
          this.cancelarEdicion();
          const index =  this.empleadoEditando.idEmpleado;
          if (index !== -1){
            this.empleados[index] = response.data;
          }
          this.empleadoEditando = null;
        })
        .catch(error => {
          console.error("Error actualizando empleado:", error);
        });  
    },
    guardarCambiosPlan(){
      axios.put(`http://localhost:8080/plan/${this.planEditando.idPlan}`, this.planEditando)
        .then(response => {
          this.fetchPlanes();
          this.cancelarEdicion();
          const index =  this.planEditando.idPlan;
          if (index !== -1){
            this.planes[index] = response.data;
          }
          this.planEditando = null;
        })
        .catch(error => {
          console.error("Error actualizando plan de seguro:", error);
        });  
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

    //Metodo para cancelar la edicion de un objeto
    cancelarEdicion() {
      this.clienteEditando = null;
      this.automovilEditando = null;
      this.contratoEditando = null;
      this.empleadoEditando = null;
      this.planEditando = null;
      this.usoEditando = null;
    },

    //Metodos para elimianr objeto en especifico
    eliminarAutomovil(idAutomovil){
      axios.delete(`http://localhost:8080/automovil/${idAutomovil}`)
        .then(() => {
          this.automoviles = this.automoviles.filter(automovil => automovil.idAutomovil !== idAutomovil);
        })
        .catch( error => {
          console.error("Error eliminando automovil:", error);          
        });
    },
    eliminarCliente(idCliente) {
      axios.delete(`http://localhost:8080/cliente/${idCliente}`)
        .then(() => {
          this.clientes = this.clientes.filter(cliente => cliente.idCliente !== idCliente);
        })
        .catch( error => {
          console.error("Error eliminando cliente:", error);          
        });
    },
    eliminarContrato(idContrato) {
      axios.delete(`http://localhost:8080/contrato/${idContrato}`)
        .then(() => {
          this.contratos = this.contratos.filter(contrato => contrato.idContrato !== idContrato);
        })
        .catch( error => {
          console.error("Error eliminando contrato:", error);          
        });
    },
    eliminarEmpleado(idEmpleado) {
      axios.delete(`http://localhost:8080/empleado/${idEmpleado}`)
        .then(() => {
          this.empleados = this.empleados.filter(empleado => empleado.idEmpleado !== idEmpleado);
        })
        .catch( error => {
          console.error("Error eliminando empleado:", error);          
        });
    },
    eliminarPlan(idPlan) {
      axios.delete(`http://localhost:8080/plan/${idPlan}`)
        .then(() => {
          this.planes = this.planes.filter(plan => plan.idPlan !== idPlan);
        })
        .catch( error => {
          console.error("Error eliminando plan de seguro:", error);          
        });
    },
    eliminarUso(idUso) {
      axios.delete(`http://localhost:8080/uso/${idUso}`)
        .then(() => {
          this.usos = this.usos.filter(uso => uso.idUso !== idUso);
        })
        .catch( error => {
          console.error("Error eliminando uso de seguro:", error);          
        });
    },

    //Metodo para cerrar el modal
    cerrarModal() {
      this.clienteSeleccionado = null;
      this.automovilSeleccionado = null;
      this.contratoSeleccionado = null;
      this.empleadoSeleccionado = null;
      this.planSeleccionado = null;
      this.usoSeleccionado = null;
    },

    logout(){
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      this.$router.push('/');
    },

  },
  mounted(){
    this.fetchAutomoviles();
    this.fetchClientes();
    this.fetchContratos();
    this.fetchEmpleados();
    this.fetchPlanes();
    this.fetchUsos();
  }
};
</script>

<style scoped>
.toggleButton {
  margin-bottom: 10px;
}
</style>
