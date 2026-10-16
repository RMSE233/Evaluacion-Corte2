# Sistema de Gestión de Inventario — Papelería Campus

Aplicación de escritorio desarrollada en Java para administrar el inventario de Papelería Campus. El sistema permite registrar, consultar, buscar, filtrar, actualizar y eliminar productos mediante una interfaz gráfica construida con JavaFX y una base de datos PostgreSQL.

Este proyecto fue desarrollado como parte del Corte Evaluativo 2 de Ingeniería en Sistemas de la Universidad Americana (UAM), Managua, Nicaragua.

## Tabla de contenido

- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Requisitos previos](#requisitos-previos)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Configuración de PostgreSQL](#configuración-de-postgresql)
- [Configuración de la conexión](#configuración-de-la-conexión)
- [Acceso a datos y operaciones SQL](#acceso-a-datos-y-operaciones-sql)
- [Validaciones del sistema](#validaciones-del-sistema)
- [Solución de problemas](#solución-de-problemas)
- [Autor](#autor)

## Características principales

### Gestión de productos

- Registrar nuevos productos en el inventario.
- Consultar los productos almacenados en PostgreSQL.
- Actualizar la información de productos existentes.
- Eliminar productos mediante su identificador.
- Mostrar la información de los productos en una tabla interactiva.
- Administrar el código, nombre, categoría, precio y existencias de cada producto.

### Búsqueda y filtrado

- Buscar productos por código o nombre.
- Filtrar productos por categoría.
- Consultar todas las categorías mediante la opción `Todas`.
- Mostrar únicamente productos que tengan existencias disponibles.
- Ordenar los resultados por código de producto.

### Validación e integridad de los datos

- Verificar la existencia de códigos de producto duplicados.
- Comparar códigos sin distinguir entre mayúsculas y minúsculas.
- Ignorar espacios al principio y al final al comprobar códigos duplicados.
- Utilizar consultas SQL preparadas mediante `PreparedStatement`.
- Gestionar excepciones relacionadas con las operaciones de base de datos.
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

El sistema utiliza una organización basada en el patrón Modelo-Vista-Controlador (MVC) y el patrón Data Access Object (DAO), separando la interfaz gráfica, la lógica de interacción, los modelos y el acceso a los datos.

- **Modelo (`model`):** representa los productos y sus atributos.
- **Vista (`resources`):** contiene los archivos FXML de la interfaz.
- **Controlador (`controller`):** administra los eventos de la interfaz gráfica.
- **Acceso a datos (`dao`):** ejecuta las consultas SQL y las operaciones sobre los productos.
- **Conexión (`connection`):** establece la conexión con PostgreSQL y lee la configuración externa.

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
├── db.properties
├── pom.xml
└── README.md
```

El archivo `db.properties` es una configuración local que debe crearse en la raíz del proyecto. No debe incluirse en el repositorio con contraseñas reales.

## Requisitos previos

Antes de ejecutar el sistema, se necesita:

1. JDK 21 instalado y configurado.
2. Apache Maven.
3. PostgreSQL instalado y ejecutándose.
4. Una base de datos creada para el inventario.
5. Un usuario de PostgreSQL con los permisos necesarios.
6. IntelliJ IDEA u otro entorno compatible con proyectos Maven y JavaFX.

Para verificar las instalaciones, ejecutá:

```bash
java -version
mvn -version
```

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/RMSE233/Evaluacion-Corte2.git
cd Evaluacion-Corte2
```

### 2. Abrir el proyecto

1. Abrí IntelliJ IDEA.
2. Seleccioná la opción `Open`.
3. Elegí la carpeta que contiene `pom.xml`.
4. Esperá a que Maven descargue las dependencias.
5. Verificá que el proyecto utilice JDK 21.

### 3. Configurar PostgreSQL

Iniciá el servicio de PostgreSQL y creá una base de datos para el sistema. El nombre elegido deberá coincidir con el configurado en `db.properties`.

### 4. Crear el archivo de configuración

Creá un archivo llamado `db.properties` en la raíz del proyecto, al mismo nivel que `pom.xml`.

Configuración de ejemplo:

```properties
db.url=jdbc:postgresql://localhost:5432/tu_base_de_datos
db.user=postgres
db.password=tu_contraseña
```

Reemplazá los valores de ejemplo por los de tu instalación de PostgreSQL.

### 5. Compilar el proyecto

Desde la terminal, en la carpeta raíz del proyecto, ejecutá:

```bash
mvn clean compile
```

Para generar el paquete de compilación:

```bash
mvn clean package
```

### 6. Ejecutar la aplicación

Desde IntelliJ IDEA, ejecutá la clase principal `HelloApplication.java` o la configuración de ejecución que utilice el proyecto.

La aplicación requiere que PostgreSQL esté disponible y que los datos de conexión sean correctos para realizar operaciones sobre el inventario.

## Configuración de PostgreSQL

El sistema utiliza una tabla denominada `producto`. Las consultas del DAO requieren las siguientes columnas:

| Columna | Uso |
|---|---|
| `id_producto` | Identificador del producto. |
| `codigo` | Código del producto. |
| `nombre` | Nombre del producto. |
| `categoria` | Categoría a la que pertenece. |
| `precio` | Precio del producto. |
| `stock` | Cantidad disponible en inventario. |

La estructura de la tabla en PostgreSQL debe coincidir con los nombres y tipos de datos esperados por el código Java.

Ejemplo básico de creación de tabla, si todavía no existe:

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

Este ejemplo es una estructura inicial; debe ajustarse a las restricciones que requiera el proyecto.

Para verificar que la tabla exista y tenga datos, ejecutá:

```sql
SELECT id_producto, codigo, nombre, categoria, precio, stock
FROM producto
ORDER BY codigo ASC;
```

## Configuración de la conexión

La clase `ConexionDB.java`, ubicada en el paquete `com.example.evaluacioncorte2.connection`, centraliza el acceso a PostgreSQL.

### Responsabilidades de `ConexionDB`

- Cargar las propiedades desde el archivo `db.properties`.
- Obtener la URL JDBC, el usuario y la contraseña.
- Validar que los datos de conexión necesarios estén presentes.
- Crear la conexión utilizando `DriverManager`.
- Informar mediante excepciones cuando no se pueda leer la configuración o establecer la conexión.

El método público utilizado por el resto del sistema es:

```java
public static Connection getConexion() throws SQLException
```

Las clases que necesiten conectarse a la base de datos deben llamar a este método:

```java
Connection conn = ConexionDB.getConexion();
```

**Importante:** el nombre `getConexion()` debe coincidir exactamente con el utilizado en `ProductoDAO.java`. No debe confundirse con `getConnection()` si ese método no está definido en la clase.

La clase utiliza `java.util.Properties` para leer el archivo de configuración.

## Acceso a datos y operaciones SQL

La clase `ProductoDAO.java` centraliza las operaciones de persistencia sobre la tabla `producto`.

### Métodos implementados

| Método | Función |
|---|---|
| `buscarProductos(String texto, String categoria, boolean soloConExistencias)` | Busca productos por código o nombre, filtra por categoría y permite mostrar solo productos con stock disponible. |
| `existeCodigo(String codigo, Integer idExcluir)` | Comprueba si un código ya existe y permite excluir el identificador de un producto durante una actualización. |
| `insertar(Producto p)` | Registra un nuevo producto. |
| `actualizar(Producto p)` | Actualiza los datos de un producto existente. |
| `eliminar(int idProducto)` | Elimina un producto según su identificador. |

### Conexión desde `ProductoDAO`

Todos los métodos que consultan o modifican la base de datos deben utilizar el mismo método de conexión:

```java
try (Connection conn = ConexionDB.getConexion();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    // Ejecutar la consulta SQL.
}
```

Este patrón utiliza `try-with-resources` para cerrar automáticamente la conexión y los recursos asociados al finalizar la operación.

### Búsqueda de productos

La búsqueda utiliza `ILIKE`, una característica de PostgreSQL que permite realizar comparaciones de texto sin distinguir entre mayúsculas y minúsculas.

La consulta puede combinar tres criterios:

- Texto de búsqueda en el código o nombre.
- Categoría seleccionada.
- Disponibilidad de existencias (`stock > 0`).

Los valores se envían mediante parámetros de `PreparedStatement`, evitando concatenar directamente el texto ingresado por el usuario en la consulta.

### Verificación de códigos duplicados

El método `existeCodigo()` utiliza `UPPER()` y `BTRIM()` para comparar los códigos ignorando diferencias de mayúsculas, minúsculas y espacios al principio o al final.

Cuando se actualiza un producto, el parámetro `idExcluir` permite que el código actual del mismo producto no se considere un duplicado de sí mismo.

### Diagnóstico de errores SQL

Las operaciones de búsqueda, verificación de duplicados, inserción, actualización y eliminación capturan excepciones `SQLException` para registrar información útil durante la depuración.

El diagnóstico incluye:

- Operación que produjo el error.
- Estado SQL (`SQLState`).
- Código del error.
- Mensaje de la excepción.

Después de registrar el error, la excepción se vuelve a lanzar para que la capa que invocó el DAO pueda gestionarla.

## Validaciones del sistema

Las validaciones de entrada deben mantenerse coordinadas con las reglas implementadas en el controlador y el modelo.

| Campo | Validación documentada |
|---|---|
| Código | Obligatorio, máximo 10 caracteres y sin duplicados según la lógica de comparación del DAO. |
| Nombre | Obligatorio, entre 3 y 80 caracteres. |
| Categoría | Debe coincidir con las categorías admitidas por la aplicación. |
| Precio | Valor numérico mayor que cero y con un máximo de dos decimales. |
| Stock | Número entero no negativo. |

Las validaciones de interfaz no sustituyen las restricciones de la base de datos. Es recomendable que PostgreSQL también proteja la integridad de los datos mediante restricciones apropiadas.

## Solución de problemas

### Error: no se encuentra `getConexion()`

Verificá que `ConexionDB.java` declare el método `getConexion()` y que `ProductoDAO.java` lo invoque con el mismo nombre:

```java
ConexionDB.getConexion()
```

Si el método se llama de otra manera, los nombres deben unificarse.

### Error: no se puede leer `db.properties`

- Confirmá que el archivo exista.
- Verificá que se llame exactamente `db.properties`.
- Colocalo en la raíz del proyecto, al mismo nivel que `pom.xml`.
- Revisá que incluya `db.url`, `db.user` y `db.password`.
- Comprobá que los valores no estén vacíos.

La ruta relativa del archivo depende del directorio de trabajo con el que se inicia la aplicación.

### Error: conexión rechazada

- Confirmá que PostgreSQL esté ejecutándose.
- Verificá el host y el puerto configurados en `db.url`.
- Confirmá que el servidor acepte conexiones desde el equipo.
- Comprobá que el nombre de la base de datos sea correcto.

### Error: autenticación fallida

Verificá el usuario y la contraseña definidos en `db.properties`. Confirmá también que el usuario tenga permiso para conectarse a la base de datos.

### Error: la base de datos no existe

Comprobá el nombre indicado en la URL JDBC y asegurate de que la base de datos haya sido creada en PostgreSQL.

### Error: no existe la tabla `producto`

Verificá que la tabla se encuentre en el esquema utilizado por la conexión y que tenga las columnas esperadas por `ProductoDAO.java`.

### Error relacionado con `ILIKE`

`ILIKE` funciona en PostgreSQL. Si se cambia a otro motor de base de datos, será necesario adaptar las consultas SQL.

### Error al compilar el proyecto

Ejecutá:

```bash
mvn clean compile
```

Si persiste el problema, revisá la consola de IntelliJ IDEA, la configuración del JDK y las dependencias declaradas en `pom.xml`.

## Seguridad y buenas prácticas

- No subir contraseñas reales ni credenciales privadas a GitHub.
- Mantener `db.properties` fuera del control de versiones cuando contenga credenciales.
- Utilizar consultas preparadas para las operaciones SQL.
- Cerrar automáticamente conexiones y recursos mediante `try-with-resources`.
- Gestionar las excepciones SQL sin ocultar los detalles necesarios para la depuración.
- Mantener los nombres de los métodos de conexión consistentes en todo el proyecto.

## Autor

**Roger Marcel Sequeira Espinoza**

- Universidad: Universidad Americana (UAM).
- Carrera: Ingeniería en Sistemas.
- Proyecto: Corte Evaluativo 2.
