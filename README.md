# Sistema de Gestión de Inventario — Papelería Campus

Aplicación de escritorio desarrollada para gestionar el inventario de **Papelería Campus**. Permite registrar, consultar, buscar, filtrar, actualizar y eliminar productos mediante una interfaz gráfica y una base de datos PostgreSQL.

Este proyecto fue desarrollado como parte del **Corte Evaluativo 2** de Ingeniería en Sistemas en la Universidad Americana (UAM), Managua, Nicaragua.

## Tabla de contenido

- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Requisitos previos](#requisitos-previos)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Configuración de la base de datos](#configuración-de-la-base-de-datos)
- [Validaciones del sistema](#validaciones-del-sistema)
- [Solución de problemas](#solución-de-problemas)
- [Autor](#autor)
- [Licencia](#licencia)

## Características principales

### Gestión de productos

- Registro de nuevos productos en el inventario.
- Consulta de productos almacenados en PostgreSQL.
- Actualización de la información de los productos existentes.
- Eliminación de productos con confirmación previa.
- Visualización de los datos mediante una tabla interactiva (`TableView`).
- Presentación del código, nombre, categoría, precio en córdobas (C$) y cantidad disponible.

### Búsqueda y filtrado

- Búsqueda de productos por código o nombre.
- Filtrado por categoría.
- Categorías disponibles: Papelería, Tecnología y Accesorios.
- Opción para visualizar únicamente los productos que tienen existencias disponibles.
- Actualización de los resultados según los criterios de búsqueda.

### Validación de datos

- Verificación de campos obligatorios.
- Validación del formato y longitud del código y nombre.
- Comprobación de precios válidos y cantidades enteras no negativas.
- Prevención de códigos de productos duplicados.
- Control de errores relacionados con las operaciones de base de datos.
- Uso de consultas preparadas para mejorar la seguridad de las consultas SQL.

## Tecnologías utilizadas

| Tecnología | Descripción |
|---|---|
| Java 21 | Lenguaje de programación principal. |
| JavaFX 21 | Desarrollo de la interfaz gráfica. |
| FXML | Definición de los componentes visuales. |
| Apache Maven | Gestión de dependencias y compilación. |
| PostgreSQL 18 | Sistema de gestión de bases de datos relacionales. |
| PostgreSQL JDBC Driver 42.7.2 | Conexión entre Java y PostgreSQL. |
| IntelliJ IDEA | Entorno de desarrollo recomendado. |

## Arquitectura del proyecto

El sistema utiliza una organización basada en los patrones **MVC (Modelo-Vista-Controlador)** y **DAO (Data Access Object)**, con el objetivo de separar las responsabilidades de la aplicación y facilitar su mantenimiento.

- **Modelo (`model`):** representa la información de los productos.
- **Vista (`resources`):** contiene la interfaz gráfica definida mediante FXML.
- **Controlador (`controller`):** gestiona los eventos de la interfaz.
- **DAO (`dao`):** administra las consultas SQL y las operaciones de persistencia.
- **Conexión (`connection`):** gestiona el acceso a la base de datos PostgreSQL.

### Estructura principal

```text
Evaluacion-Corte2/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/evaluacioncorte2/
│       │       ├── HelloApplication.java
│       │       ├── Launcher.java
│       │       ├── connection/
│       │       │   └── ConexionDB.java
│       │       ├── controller/
│       │       │   └── ProductoController.java
│       │       ├── dao/
│       │       │   └── ProductoDAO.java
│       │       └── model/
│       │           └── Producto.java
│       └── resources/
│           └── com/example/evaluacioncorte2/
│               └── Campus-View.fxml
├── pom.xml
└── README.md
```

*Nota: esta estructura resume los componentes principales. La ubicación exacta de las carpetas debe coincidir con la estructura actual del repositorio.*

## Requisitos previos

Antes de ejecutar el proyecto, es necesario contar con:

1. **JDK 21:** para compilar y ejecutar la aplicación.
2. **Apache Maven:** para gestionar las dependencias del proyecto.
3. **PostgreSQL:** para almacenar la información del inventario.
4. **IntelliJ IDEA:** recomendado para abrir, configurar y ejecutar el proyecto.
5. Una base de datos PostgreSQL con la estructura requerida por la aplicación.

Para verificar las instalaciones de Java y Maven, ejecutá los siguientes comandos en una terminal:

```bash
java -version
mvn -version
```

## Instalación y ejecución

### 1. Clonar el repositorio

Abrí una terminal y ejecutá:

```bash
git clone https://github.com/RMSE233/Evaluacion-Corte2.git
```

Ingresá a la carpeta del proyecto:

```bash
cd Evaluacion-Corte2
```

### 2. Abrir el proyecto en IntelliJ IDEA

1. Abrí IntelliJ IDEA.
2. Seleccioná **Open**.
3. Elegí la carpeta que contiene el archivo `pom.xml`.
4. Esperá a que Maven descargue y configure las dependencias.
5. Verificá que el proyecto utilice el JDK 21.

### 3. Configurar la conexión a PostgreSQL

Abrí la clase `ConexionDB.java` y revisá la configuración de conexión a la base de datos.

Asegurate de establecer correctamente:

- Host del servidor.
- Puerto de PostgreSQL.
- Nombre de la base de datos.
- Usuario.
- Contraseña.

Los valores deben corresponder a la configuración de tu equipo.

**Importante:** no publiques contraseñas reales ni credenciales privadas en el repositorio. Para otros entornos, es recomendable utilizar variables de entorno o archivos de configuración excluidos del control de versiones.

### 4. Compilar el proyecto

Desde la carpeta que contiene el archivo `pom.xml`, ejecutá:

```bash
mvn clean package
```

Este comando limpia los archivos de compilaciones anteriores y genera una nueva compilación del proyecto.

### 5. Ejecutar la aplicación

Desde IntelliJ IDEA, ejecutá la clase principal `HelloApplication.java`, utilizando el JDK 21 y las dependencias configuradas en Maven.

La aplicación necesita una conexión válida con PostgreSQL para consultar y modificar el inventario.

## Configuración de la base de datos

La aplicación utiliza PostgreSQL para almacenar y recuperar los productos.

Para preparar el entorno:

1. Iniciá el servicio de PostgreSQL.
2. Creá una base de datos para el sistema.
3. Prepará la tabla de productos con las columnas y restricciones que utiliza el proyecto.
4. Configurá las credenciales en `ConexionDB.java` o en el mecanismo de configuración correspondiente.
5. Confirmá que el usuario tenga permisos para consultar, insertar, actualizar y eliminar registros.
6. Ejecutá la aplicación y verificá que los productos se carguen correctamente.

Las categorías contempladas por el sistema son Papelería, Tecnología y Accesorios.

**Importante:** la estructura SQL debe coincidir con las consultas implementadas en `ProductoDAO.java`. Revisá ese archivo antes de crear la tabla o cargar datos iniciales.

## Validaciones del sistema

El sistema incorpora controles para mantener la integridad de la información.

| Campo | Validaciones |
|---|---|
| Código del producto | Obligatorio, máximo 10 caracteres y sin duplicados. |
| Nombre del producto | Obligatorio, entre 3 y 80 caracteres. |
| Categoría | Debe pertenecer a las categorías admitidas. |
| Precio | Debe ser numérico, mayor que cero y tener como máximo dos decimales. |
| Stock | Debe ser un número entero no negativo. |

Además, la validación del código contempla la detección de duplicados incluso cuando existen diferencias de mayúsculas, minúsculas o espacios, de acuerdo con la lógica implementada.

## Solución de problemas

### La aplicación no inicia

- Verificá que esté configurado el JDK 21.
- Recargá las dependencias de Maven.
- Revisá los errores mostrados en la consola de IntelliJ IDEA.
- Comprobá las rutas de la clase principal y del archivo FXML.

### Error de conexión con PostgreSQL

- Confirmá que el servidor esté activo.
- Revisá el host, puerto, nombre de la base de datos y credenciales.
- Verificá que el usuario tenga los permisos necesarios.
- Comprobá que el controlador JDBC esté declarado en el archivo `pom.xml`.

### La tabla no muestra productos

- Confirmá que existan registros en la tabla correspondiente.
- Revisá la conexión con PostgreSQL.
- Verificá las consultas definidas en `ProductoDAO.java`.
- Consultá los mensajes de error de la aplicación.

### Maven no descarga las dependencias

- Comprobá la conexión a Internet.
- Verificá que Maven esté instalado y correctamente configurado.
- Recargá el proyecto Maven desde IntelliJ IDEA.
- Revisá la configuración del archivo `pom.xml`.

## Autor

**Roger Marcel Sequeira Espinoza**

- **Universidad:** Universidad Americana (UAM).
- **Carrera:** Ingeniería en Sistemas.
- **Ubicación:** Managua, Nicaragua.
- **Proyecto:** Corte Evaluativo 2.

### Repositorio

[https://github.com/RMSE233/Evaluacion-Corte2](https://github.com/RMSE233/Evaluacion-Corte2)

## Licencia

Este repositorio no especifica una licencia de distribución en esta documentación. Si se desea permitir formalmente la reutilización, modificación o distribución del código, se recomienda añadir un archivo `LICENSE` con la licencia elegida.
