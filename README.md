# Sistema de Gestión de Inventario — Papelería Campus

Aplicación de escritorio desarrollada en Java para gestionar el inventario de Papelería Campus. El sistema permite registrar, consultar, buscar, filtrar, actualizar y eliminar productos mediante una interfaz gráfica desarrollada con JavaFX y una base de datos PostgreSQL.

Este proyecto fue desarrollado como parte del Corte Evaluativo 2 de Ingeniería en Sistemas de la Universidad Americana (UAM), Managua, Nicaragua.

## Tabla de contenido

- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Requisitos previos](#requisitos-previos)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Configuración de PostgreSQL](#configuración-de-postgresql)
- [Configuración de la conexión](#configuración-de-la-conexión)
- [Operaciones de acceso a datos](#operaciones-de-acceso-a-datos)
- [Validaciones del sistema](#validaciones-del-sistema)
- [Solución de problemas](#solución-de-problemas)
- [Seguridad y buenas prácticas](#seguridad-y-buenas-prácticas)
- [Autor](#autor)

## Características principales

### Gestión de productos

- Registrar nuevos productos en el inventario.
- Consultar los productos almacenados en PostgreSQL.
- Actualizar la información de productos existentes.
- Eliminar productos mediante su identificador.
- Mostrar información de productos en una tabla interactiva.
- Administrar el código, nombre, categoría, precio y existencias de cada producto.

### Búsqueda y filtrado

- Buscar productos por código o nombre.
- Filtrar productos por categoría.
- Consultar todas las categorías mediante la opción `Todas`.
- Mostrar únicamente los productos que tengan existencias disponibles.
- Ordenar los resultados por código de producto.

### Validación de datos

- Comprobar si un código de producto ya existe.
- Detectar códigos duplicados ignorando mayúsculas, minúsculas y espacios al principio o al final.
- Utilizar consultas SQL preparadas mediante `PreparedStatement`.
- Gestionar errores relacionados con las operaciones de base de datos.
- Registrar información de diagnóstico para facilitar la identificación de errores SQL.

## Tecnologías utilizadas

| Tecnología | Descripción |
|---|---|
| Java 21 | Lenguaje de programación principal. |
| JavaFX 21 | Desarrollo de la interfaz gráfica. |
| FXML | Definición de los componentes visuales. |
| Apache Maven | Gestión de dependencias y compilación. |
| PostgreSQL | Sistema de gestión de bases de datos relacionales. |
| PostgreSQL JDBC Driver 42.7.2 | Conexión entre Java y PostgreSQL. |
| IntelliJ IDEA | Entorno de desarrollo recomendado. |

## Arquitectura del proyecto

El sistema organiza el código mediante una estructura basada en los patrones Modelo-Vista-Controlador (MVC) y Data Access Object (DAO), separando la interfaz gráfica, el control de eventos, los modelos y el acceso a los datos.

- **Modelo (`model`):** representa la información de los productos.
- **Vista (`resources`):** contiene los archivos FXML de la interfaz gráfica.
- **Controlador (`controller`):** gestiona los eventos y la interacción del usuario.
- **Acceso a datos (`dao`):** contiene las consultas SQL y las operaciones sobre los productos.
- **Conexión (`connection`):** centraliza el establecimiento de conexiones con PostgreSQL.

### Estructura principal del proyecto

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
├── db.properties
├── pom.xml
└── README.md
```

El archivo `db.properties` es una configuración local que debe crearse en la raíz del proyecto. No debe publicarse con contraseñas reales.

## Requisitos previos

Antes de ejecutar la aplicación, es necesario contar con:

1. JDK 21 instalado y configurado.
2. Apache Maven.
3. PostgreSQL instalado y ejecutándose.
4. Una base de datos creada para el inventario.
5. Un usuario de PostgreSQL con los permisos necesarios.
6. IntelliJ IDEA u otro entorno compatible con Maven y JavaFX.

Para verificar las instalaciones de Java y Maven, ejecutá:

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
2. Seleccioná la opción **Open**.
3. Elegí la carpeta donde se encuentra `pom.xml`.
4. Esperá a que Maven descargue las dependencias.
5. Verificá que el proyecto utilice JDK 21.

### 3. Configurar PostgreSQL

Iniciá el servicio de PostgreSQL y creá una base de datos para el inventario. El nombre de la base de datos debe coincidir con el configurado en el archivo `db.properties`.

### 4. Configurar las credenciales

Creá un archivo llamado `db.properties` en la raíz del proyecto, al mismo nivel que `pom.xml`.

Utilizá la siguiente estructura de ejemplo:

```properties
db.url=jdbc:postgresql://localhost:5432/tu_base_de_datos
db.user=postgres
db.password=tu_contraseña
```

Reemplazá los valores de ejemplo por los datos de tu instalación de PostgreSQL.

### 5. Compilar el proyecto

Desde la terminal, en la raíz del proyecto, ejecutá:

```bash
mvn clean compile
```

Para limpiar, compilar y empaquetar el proyecto, ejecutá:

```bash
mvn clean package
```

### 6. Ejecutar la aplicación

Desde IntelliJ IDEA, ejecutá la clase principal `HelloApplication.java` o la configuración de ejecución correspondiente al proyecto.

La aplicación necesita una conexión válida con PostgreSQL para consultar y modificar los productos del inventario.

## Configuración de PostgreSQL

La aplicación utiliza una tabla denominada `producto`. Las consultas del DAO esperan las siguientes columnas:

| Columna | Descripción |
|---|---|
| `id_producto` | Identificador del producto. |
| `codigo` | Código único utilizado para identificar el producto según las reglas de la aplicación. |
| `nombre` | Nombre del producto. |
| `categoria` | Categoría del producto. |
| `precio` | Precio del producto. |
| `stock` | Cantidad disponible en el inventario. |

Si todavía no existe la tabla, el siguiente SQL sirve como estructura inicial. Ajustalo si el proyecto requiere restricciones adicionales.

```sql
CREATE TABLE producto (
    id_producto INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    precio NUMERIC(10, 2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0
);
```

Para comprobar que la tabla existe y consultar sus registros:

```sql
SELECT
    id_producto,
    codigo,
    nombre,
    categoria,
    precio,
    stock
FROM producto
ORDER BY codigo ASC;
```

El usuario configurado en `db.properties` debe contar con permisos para consultar, insertar, actualizar y eliminar registros.

**Importante:** la estructura de la tabla debe coincidir con las consultas implementadas en `ProductoDAO.java`.

## Configuración de la conexión

La clase `ConexionDB.java`, ubicada en el paquete `com.example.evaluacioncorte2.connection`, centraliza la conexión con PostgreSQL.

### Responsabilidades de `ConexionDB`

- Leer la configuración desde `db.properties`.
- Obtener la URL JDBC, el usuario y la contraseña.
- Comprobar que los datos de conexión necesarios estén presentes.
- Crear conexiones mediante `DriverManager`.
- Informar de errores al leer el archivo de configuración o conectarse a la base de datos.

El método utilizado por las clases que necesitan una conexión es:

```java
public static Connection getConexion() throws SQLException
```

La llamada desde otras clases debe escribirse exactamente así:

```java
Connection conn = ConexionDB.getConexion();
```

### Corrección del nombre del método

Para evitar errores de compilación, el nombre del método debe ser consistente en todas las clases.

En `ProductoDAO.java`, las llamadas a la conexión deben utilizar:

```java
ConexionDB.getConexion()
```

No debe utilizarse `ConexionDB.getConnection()` a menos que ese método también esté definido en `ConexionDB.java`.

La clase de conexión utiliza `java.util.Properties` para cargar los datos del archivo de configuración.

### Manejo de errores de configuración

Si el archivo no se puede leer, la aplicación genera una excepción `SQLException` con un mensaje que indica dónde debe colocarse `db.properties`.

Si faltan los valores requeridos, la clase informa de que deben configurarse `db.url`, `db.user` y `db.password`.

La ruta del archivo es relativa al directorio de trabajo desde el que se ejecuta la aplicación. Por ello, debe verificarse la configuración de ejecución de IntelliJ IDEA si el archivo existe y aun así no se encuentra.

## Operaciones de acceso a datos

La clase `ProductoDAO.java` contiene las operaciones de persistencia sobre la tabla `producto`.

### Métodos principales

| Método | Función |
|---|---|
| `buscarProductos(String texto, String categoria, boolean soloConExistencias)` | Busca por código o nombre, filtra por categoría y permite mostrar únicamente productos con stock disponible. |
| `existeCodigo(String codigo, Integer idExcluir)` | Comprueba si existe un código y permite excluir un producto durante una actualización. |
| `insertar(Producto p)` | Inserta un nuevo producto. |
| `actualizar(Producto p)` | Actualiza la información de un producto existente. |
| `eliminar(int idProducto)` | Elimina un producto mediante su identificador. |

### Uso de conexiones y consultas preparadas

Las operaciones de acceso a datos utilizan `Connection`, `PreparedStatement` y `ResultSet` cuando corresponde.

Ejemplo del patrón de conexión empleado:

```java
try (Connection conn = ConexionDB.getConexion();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {

    // Asignar los parámetros de la consulta.
    // Ejecutar la operación SQL.
}
```

El uso de `try-with-resources` permite cerrar automáticamente los recursos al finalizar cada operación.

### Búsqueda y filtrado de productos

El método `buscarProductos()` permite combinar los siguientes filtros:

- Texto parcial del código o nombre.
- Categoría seleccionada.
- Disponibilidad de existencias, mediante `stock > 0`.

La consulta emplea `ILIKE`, una característica de PostgreSQL que permite buscar texto sin distinguir entre mayúsculas y minúsculas.

Los valores se asignan mediante parámetros de `PreparedStatement`, en lugar de concatenar directamente el texto del usuario en la consulta SQL.

### Verificación de códigos duplicados

El método `existeCodigo()` utiliza `UPPER()` y `BTRIM()` para comparar códigos ignorando las diferencias de mayúsculas, minúsculas y espacios al principio o al final.

Cuando se actualiza un producto, `idExcluir` permite excluir el identificador del producto actual de la búsqueda de duplicados.

### Registro de errores SQL

Las operaciones de búsqueda, verificación de códigos, inserción, actualización y eliminación gestionan excepciones `SQLException`.

El diagnóstico registra:

- La operación que produjo el error.
- El estado SQL (`SQLState`).
- El código del error.
- El mensaje proporcionado por la excepción.

Después de registrar el diagnóstico, la excepción se vuelve a lanzar para que la capa que llamó al DAO pueda gestionarla.

## Validaciones del sistema

Las validaciones deben coincidir con las reglas implementadas en el controlador, el modelo y la base de datos.

| Campo | Validación documentada |
|---|---|
| Código | Obligatorio, máximo 10 caracteres y comprobación de duplicados. |
| Nombre | Obligatorio, entre 3 y 80 caracteres. |
| Categoría | Debe coincidir con las categorías admitidas por la aplicación. |
| Precio | Valor numérico mayor que cero y con un máximo de dos decimales. |
| Stock | Número entero no negativo. |

Las validaciones de la interfaz gráfica no sustituyen las restricciones de la base de datos. Se recomienda definir restricciones apropiadas en PostgreSQL para proteger la integridad de la información.

## Solución de problemas

### Error: no se encuentra `getConexion()`

Verificá que `ConexionDB.java` declare el método `getConexion()` y que `ProductoDAO.java` utilice el mismo nombre:

```java
ConexionDB.getConexion()
```

Los nombres deben coincidir exactamente, incluyendo mayúsculas y minúsculas.

### Error: no se encuentra `db.properties`

- Confirmá que el archivo exista.
- Verificá que se llame exactamente `db.properties`.
- Colocalo en la raíz del proyecto, al mismo nivel que `pom.xml`.
- Revisá que contenga `db.url`, `db.user` y `db.password`.
- Comprobá que el directorio de trabajo de la ejecución sea correcto.

### Error: conexión rechazada

- Verificá que PostgreSQL esté ejecutándose.
- Comprobá el host y el puerto configurados en `db.url`.
- Confirmá que el servidor acepte conexiones desde tu equipo.
- Revisá el nombre de la base de datos.

### Error: autenticación fallida

Comprobá el usuario y la contraseña de `db.properties`. Asegurate de que el usuario tenga permiso para conectarse a la base de datos.

### Error: la base de datos no existe

Verificá el nombre de la base de datos en la URL JDBC y confirmá que haya sido creada en PostgreSQL.

### Error: no existe la tabla `producto`

Comprobá que la tabla exista en el esquema utilizado por la conexión y que contenga las columnas requeridas por `ProductoDAO.java`.

### Error relacionado con `ILIKE`

`ILIKE` es compatible con PostgreSQL. Si se cambia a otro motor de base de datos, las consultas deberán adaptarse a la sintaxis correspondiente.

### Error al compilar

Ejecutá:

```bash
mvn clean compile
```

Si el error continúa, revisá la consola de IntelliJ IDEA, el JDK configurado, las dependencias de Maven y los nombres de los métodos utilizados en las clases.

### Maven no descarga las dependencias

- Comprobá la conexión a Internet.
- Verificá que Maven esté instalado correctamente.
- Recargá el proyecto Maven desde IntelliJ IDEA.
- Revisá las dependencias declaradas en `pom.xml`.

## Seguridad y buenas prácticas

- No publicar contraseñas reales ni credenciales privadas en GitHub.
- Mantener `db.properties` fuera del control de versiones cuando contenga información sensible.
- Utilizar consultas preparadas para ejecutar operaciones SQL.
- Cerrar automáticamente las conexiones y los recursos mediante `try-with-resources`.
- Mantener consistentes los nombres de los métodos entre las clases.
- Gestionar las excepciones sin ocultar los detalles necesarios para la depuración.
- Validar los datos tanto en la aplicación como en la base de datos.

## Autor

**Roger Marcel Sequeira Espinoza**

- **Universidad:** Universidad Americana (UAM).
- **Carrera:** Ingeniería en Sistemas.
- **Proyecto:** Corte Evaluativo 2.
