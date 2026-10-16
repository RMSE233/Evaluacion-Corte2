# 🛒 Sistema de Gestión de Inventario — Papelería Campus

Aplicación de escritorio multiplataforma desarrollada en **Java 21** y **JavaFX**, integrada con una base de datos relacional en **PostgreSQL**. Este proyecto implementa el patrón arquitectónico **MVC + DAO (Modelo-Vista-Controlador y Data Access Object)** para garantizar una separación limpia de responsabilidades, alta mantenibilidad y cumplimiento riguroso de reglas de negocio y validaciones de integridad.

---

## 👥 Autores y Colaboradores
* **Estudiante:** Roger Marcel Sequeira Espinoza
* **Universidad:** Universidad Americana (UAM) — Managua, Nicaragua
* **Carrera:** Ingeniería en Sistemas
* **Asignatura:** Programación / Desarrollo de Software (Corte Evaluativo 2)

---

## 🚀 Características y Funcionalidades del Sistema

1. **Catálogo de Productos en Tiempo Real (`TableView`)**:
   * Visualización estructurada de ID, Código, Nombre, Categoría, Precio (en Córdobas - C$) y Stock.
   * Carga automática de datos directamente desde la base de datos PostgreSQL al iniciar la aplicación.

2. **Módulo de Búsqueda y Filtros Dinámicos**:
   * Buscador por texto con coincidencia flexible (`ILIKE`) para códigos y nombres de productos.
   * Filtro por categoría mediante un `ComboBox` (*Papelería, Tecnología, Accesorios* o *Todas*).
   * Checkbox de filtrado rápido para mostrar únicamente productos con existencias en stock (`stock > 0`).

3. **Operaciones CRUD Completas**:
   * **Crear (Insertar)**: Registro de nuevos artículos validando restricciones de formato y duplicidad.
   * **Leer (Consultar)**: Actualización de vistas mediante consultas preparadas (`PreparedStatement`).
   * **Actualizar**: Selección directa desde la tabla al formulario lateral para modificar datos de manera intuitiva.
   * **Eliminar**: Eliminación segura con cuadro de diálogo de confirmación previa.

4. **Validaciones Estrictas de Entrada (V1 a V9)**:
   * **V1 - V2 (Código y Nombre)**: Obligatoriedad, control de espacios vacíos y límites de longitud de caracteres (Código: máx 10 chars; Nombre: 3 a 80 chars).
   * **V3 (Categoría)**: Restricción estricta a categorías permitidas del dominio del negocio.
   * **V4 - V6 (Precio)**: Validación numérica estricta con punto decimal, valor estrictamente mayor a 0, máximo 2 decimales y tope límite de valor (`99999999.99`).
   * **V7 - V8 (Stock)**: Validación de números enteros no negativos dentro del rango válido de enteros de Java.
   * **V9 (Duplicidad de Código)**: Verificación en tiempo real en la base de datos para evitar códigos de producto repetidos (incluso ignorando mayúsculas/minúsculas y espacios).

5. **Manejo de Diagnóstico y Errores SQL**:
   * Captura centralizada de excepciones de base de datos con traducción de estados SQL (`SQLState`) a mensajes claros para el usuario final (ej. violaciones de restricciones `CHECK`, pérdida de conexión o duplicidad).

---

## 🛠️ Stack Tecnológico y Requisitos del Entorno

* **Lenguaje**: Java SE (JDK 21)
* **Interfaz Gráfica**: JavaFX 21 (`javafx-controls`, `javafx-fxml`)
* **Gestor de Dependencias y Compilación**: Apache Maven
* **Base de Datos**: PostgreSQL 18
* **Driver JDBC**: PostgreSQL JDBC Driver (`org.postgresql:postgresql` v42.7.2)
* **IDE Recomendado**: IntelliJ IDEA 2025+

---

## 📁 Estructura del Proyecto

```text
Evaluacion-Corte2/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/evaluacioncorte2/
│       │       ├── HelloApplication.java    # Clase principal de JavaFX
│       │       ├── Launcher.java            # Lanzador auxiliar para empaquetado
│       │       ├── connection/
│       │       │   └── ConexionDB.java      # Gestión de conexión JDBC a PostgreSQL
│       │       ├── controller/
│       │       │   └── ProductoController.java # Controlador de la UI y lógica de eventos
│       │       ├── dao/
│       │       │   └── ProductoDAO.java     # Consultas SQL, persistencia y transacciones
│       │       └── model/
│       │           └── Producto.java        # Entidad POJO de productos
│       │
│       └── resources/
│           └── com/example/evaluacioncorte2/
│               └── Campus-View.fxml         # Diseño de la interfaz gráfica
│
├── target/                                  # Clases compiladas por Maven
├── pom.xml                                  # Configuración de dependencias Maven
└── README.md                                # Documentación oficial del proyecto
