# Aplicación Core Seguros de Automóviles

## Descripción

El objetivo del proyecto es desarrollar un sistema que automatice el análisis de los contratos de seguros de automóviles, permitiendo evaluar el nivel de riesgo de cada cliente. El sistema identifica a los clientes que hacen un uso excesivo de su seguro para aplicar penalidades a sus contratos, y recompensa a aquellos con un comportamiento ejemplar con incentivos. El enfoque está en ajustar la prima del seguro de manera justa y transparente, considerando las características del vehículo, el historial de uso del seguro y comparaciones con clientes similares.

## Tecnologías Utilizadas

- **Backend**: Spring Boot, Java (versión 17), MySQL (Base de Datos)
- **Frontend**: Vue.js
- **Despliegue**: Railway (Backend), Vercel (Frontend)
- **Control de versiones**: Git y GitHub

## Funcionalidades Principales

### Core del Sistema
- **Cálculo de primas ajustadas**:
  - Evaluación del riesgo basado en el vehículo (año, tipo, ciudad de circulación, uso destinado).
  - Análisis del historial de uso del seguro, considerando la frecuencia, monto total aprobado y promedio ponderado de tiempo entre usos.
  - Comparación con clientes similares según el promedio de uso del seguro y montos aprobados.
- **Ajustes en contratos**:
  - Penalidades o bonificaciones automáticas basadas en el nivel de riesgo.
  - Registro de ajustes con razones específicas y valores actualizados.

### Funciones Generales
- **Para los Clientes**:
  - Registro y autenticación.
  - Contratación de seguros.
  - Gestión de contratos: crear, actualizar y visualizar contratos (sin opción de eliminar).
  - Solicitudes de uso del seguro: registrar nuevas solicitudes con detalles del siniestro.
- **Para los Empleados**:
  - Registro y autenticación.
  - Gestión de planes de seguro (CRUD).
  - Gestión de contratos: administración completa, incluyendo ajustes de primas.
  - Visualización y gestión del historial de uso del seguro.
  - Ejecución y aplicación del análisis del Core para ajustes en contratos.

## Guía del Backend

El backend está desarrollado en **Spring Boot** y consta de varios componentes clave que permiten cumplir con el propósito del proyecto:

- **Modelos**:
  - `Contrato`: Representa los contratos de seguro, almacenando detalles como cliente, plan, vehículo, y ajustes aplicados.
  - `Plan`: Define las características de los diferentes planes de seguro disponibles.
  - `HistorialUso`: Registra cada vez que un cliente hace uso de su seguro, permitiendo analizar patrones de uso.
  - `AnalisisContrato`: Encargado de calcular el nivel de riesgo de cada contrato, aplicando las fórmulas del Core para determinar penalidades o bonificaciones.
  - `Cliente`: Representa a los clientes registrados, asociando sus datos personales y contratos.
  - `Empleado`: Representa a los empleados, quienes tienen permisos para gestionar contratos, planes y ejecutar análisis.

- **Servicios**:
  - `ContratoService`: Contiene la lógica principal del sistema Core. Realiza el cálculo de primas ajustadas, gestiona la creación y actualización de contratos, y ejecuta el análisis de riesgo.
  - `PlanService`: Permite la administración de los planes de seguro, asegurando que los datos estén disponibles para los clientes y empleados.
  - `ClienteService`: Gestiona el registro, autenticación y operaciones relacionadas con los clientes.
  - `HistorialUsoService`: Realiza operaciones sobre los registros de uso del seguro y proporciona datos al Core para el análisis.
  - `AnalisisService`: Específicamente diseñado para ejecutar las fórmulas del Core, evaluando los factores del cliente, vehículo y comparación con otros clientes.

- **Controladores**:
  - `ContratoController`: Expone endpoints para la gestión de contratos y la ejecución del análisis del Core. Permite a los empleados realizar ajustes y consultar resultados.
  - `PlanController`: Proporciona endpoints para la gestión de planes, tanto para empleados como para clientes.
  - `ClienteController`: Gestiona las operaciones de registro y autenticación de clientes.
  - `HistorialUsoController`: Expone los datos históricos de uso del seguro y permite registrar nuevos eventos.

- **Configuraciones**:
  - **application.properties**: Configuración de conexión a MySQL.


 
## Despliegue del Proyecto

El proyecto está desplegado en las siguientes plataformas:

- **Frontend**: [Vercel – Acceder a la aplicación](#)

- **Backend**: [Railway – Repositorio del backend](#)

Link de accedo a la pagina web desplegada: https://proyecto-core-frontend.vercel.app/


## Despliegue en Railway y Vercel

Para desplegar el proyecto, se siguieron los siguientes tutoriales:

- **Desplegar Frontend en Vercel**: https://www.youtube.com/watch?v=Ez2VnA2OaII
- **Desplegar Backend en Railway**: https://www.youtube.com/watch?v=UsbqrZueoro

## Video Demostrativo

Puedes ver una demostración del proyecto en el siguiente video:  
https://youtu.be/eRB7hWLAZec

## Instalación y Configuración

### Backend

1. Clonar el repositorio del backend:

    ```bash
    git clone https://github.com/nandomartin7/AplicacionEjercicioMVCBackend.git
    ```

2. Configurar la base de datos en `application.properties` con las credenciales de MySQL.
3. Ejecutar el backend usando Maven:

    ```bash
    mvn spring-boot:run
    ```

### Frontend

1. Clonar el repositorio del frontend:

    ```bash
    git clone https://github.com/nandomartin7/AplicacionEjercicioMVCFrontend.git
    ```

2. Instalar las dependencias:

    ```bash
    npm install
    ```

3. Ejecutar el frontend en modo desarrollo:

    ```bash
    npm run serve
    ```

## Contacto
Fernando Camacho  
Telf: 0984147484     Email: fernando.camacho@ulda.edu.ec
