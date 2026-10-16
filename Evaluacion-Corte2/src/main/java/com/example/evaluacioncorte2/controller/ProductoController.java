package com.example.evaluacioncorte2.controller;

import com.example.evaluacioncorte2.DAO.ProductoDAO;
import com.example.evaluacioncorte2.model.Producto;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ProductoController {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ObservableList<Producto> listaObservable = FXCollections.observableArrayList();

    // Tabla y Columnas (@FXML)
    @FXML private TableView<Producto> tabla;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    // Etiquetas de estado
    @FXML private Label lblContador;
    @FXML private Label lblEstado;

    // Filtros
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroCategoria;
    @FXML private CheckBox chkSoloExistencias;

    // Formulario
    @FXML private TextField txtId;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    private Integer idSeleccionado = null;
    private static final Set<String> CATEGORIAS_VALIDAS = Set.of("Papelería", "Tecnología", "Accesorios");

    @FXML
    public void initialize() {
        // Configuracion de ComboBoxes
        cmbFiltroCategoria.getItems().addAll("Todas", "Papelería", "Tecnología", "Accesorios");
        cmbFiltroCategoria.setValue("Todas");

        cmbCategoria.getItems().addAll("Papelería", "Tecnología", "Accesorios");

        // Configuracion de las 6 columnas del TableView
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getIdProducto()));
        colCodigo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCodigo()));
        colNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colCategoria.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoria()));
        colPrecio.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPrecio()));
        colStock.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getStock()));

        tabla.setItems(listaObservable);
        tabla.setPlaceholder(new Label("Sin coincidencias"));

        // Evento al seleccionar una fila de la tabla
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, ant, actual) -> {
            if (actual != null) {
                idSeleccionado = actual.getIdProducto();
                txtId.setText(String.valueOf(actual.getIdProducto()));
                txtCodigo.setText(actual.getCodigo());
                txtNombre.setText(actual.getNombre());
                cmbCategoria.setValue(actual.getCategoria());
                txtPrecio.setText(actual.getPrecio().toPlainString());
                txtStock.setText(String.valueOf(actual.getStock()));
            }
        });

        // Cargar listado inicial desde PostgreSQL al abrir la ventana
        ejecutarConsulta(false);
    }

   //Acciones de los filtros

    @FXML
    private void onBuscar() {
        ejecutarConsulta(true);
    }

    @FXML
    private void onLimpiarFiltros() {
        txtBuscar.clear();
        cmbFiltroCategoria.setValue("Todas");
        chkSoloExistencias.setSelected(false);
        ejecutarConsulta(false);
    }

    @FXML
    private void onRecargar() {
        ejecutarConsulta(false);
    }

    private void ejecutarConsulta(boolean mostrarAvisoSinCoincidencias) {
        try {
            String texto = txtBuscar.getText();
            String categoria = cmbFiltroCategoria.getValue();
            boolean soloStock = chkSoloExistencias.isSelected();

            List<Producto> resultados = productoDAO.buscarProductos(texto, categoria, soloStock);
            listaObservable.setAll(resultados);
            lblContador.setText("Registros visibles: " + resultados.size());

            if (resultados.isEmpty()) {
                lblEstado.setText("Sin coincidencias");
                if (mostrarAvisoSinCoincidencias) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sin coincidencias",
                            "No se encontraron productos que coincidan con los filtros indicados (0 filas).");
                }
            } else {
                lblEstado.setText("Consulta completada.");
            }
        } catch (SQLException e) {
            lblContador.setText("Error de conexión");
            lblEstado.setText("Fallo al consultar la base de datos.");
            mostrarErrorSQL("Error al consultar el catálogo en PostgreSQL", e);
        }
    }

    // Acciones CRUD

    @FXML
    private void onNuevo() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtId.clear();
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.setValue(null);
        txtPrecio.clear();
        txtStock.clear();
        tabla.getSelectionModel().clearSelection();
        lblEstado.setText("Formulario limpio.");
    }

    @FXML
    private void onGuardar() {
        Producto validado = validarYConstruirProducto(null);
        if (validado == null) return;

        try {
            int filas = productoDAO.insertar(validado);
            if (filas == 1) {
                limpiarFormulario();
                ejecutarConsulta(false);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Guardado exitoso",
                        "El producto fue registrado correctamente en PostgreSQL.");
            }
        } catch (SQLException e) {
            mostrarErrorSQL("No se pudo guardar el producto", e);
        }
    }

    @FXML
    private void onActualizar() {
        if (idSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida (V10)",
                    "Debe seleccionar un producto de la tabla antes de actualizar.");
            return;
        }

        Producto validado = validarYConstruirProducto(idSeleccionado);
        if (validado == null) return;

        try {
            int filas = productoDAO.actualizar(validado);
            if (filas == 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Producto no encontrado (E3)",
                        "El producto seleccionado ya no existe en la base de datos. Se recargará el listado.");
                limpiarFormulario();
                ejecutarConsulta(false);
                return;
            }
            limpiarFormulario();
            ejecutarConsulta(false);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Actualización exitosa",
                    "El producto fue actualizado correctamente.");
        } catch (SQLException e) {
            mostrarErrorSQL("No se pudo actualizar el producto", e);
        }
    }

    @FXML
    private void onEliminar() {
        if (idSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida (V10)",
                    "Debe seleccionar un producto de la tabla antes de eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Desea eliminar el producto seleccionado?");
        confirmacion.setContentText("ID: " + idSeleccionado + " | Código: " + txtCodigo.getText());

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
            lblEstado.setText("Eliminación cancelada.");
            return;
        }

        try {
            int filas = productoDAO.eliminar(idSeleccionado);
            if (filas == 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Producto no encontrado (E3)",
                        "El producto seleccionado ya no existe en la base de datos. Se recargará el listado.");
                limpiarFormulario();
                ejecutarConsulta(false);
                return;
            }
            limpiarFormulario();
            ejecutarConsulta(false);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminación exitosa",
                    "El producto fue eliminado correctamente.");
        } catch (SQLException e) {
            mostrarErrorSQL("No se pudo eliminar el producto", e);
        }
    }



    private Producto validarYConstruirProducto(Integer idActual) {
        String codigo = (txtCodigo.getText() == null) ? "" : txtCodigo.getText().trim().toUpperCase();
        String nombre = (txtNombre.getText() == null) ? "" : txtNombre.getText().trim();
        String categoria = cmbCategoria.getValue();
        String precioStr = (txtPrecio.getText() == null) ? "" : txtPrecio.getText().trim();
        String stockStr = (txtStock.getText() == null) ? "" : txtStock.getText().trim();

        if (codigo.isEmpty() || codigo.length() > 10) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Código (V1)",
                    "El código es obligatorio y debe tener entre 1 y 10 caracteres tras quitar espacios laterales.");
            return null;
        }

        if (nombre.length() < 3 || nombre.length() > 80) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Nombre (V2)",
                    "El nombre es obligatorio y debe tener entre 3 y 80 caracteres tras quitar espacios laterales.");
            return null;
        }

        if (categoria == null || !CATEGORIAS_VALIDAS.contains(categoria)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Categoría (V3)",
                    "Debe seleccionar una categoría válida: Papelería, Tecnología o Accesorios.");
            return null;
        }

        if (!precioStr.matches("^-?\\d+(\\.\\d+)?$")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V4)",
                    "El precio debe ser un número válido utilizando punto decimal (ejemplo: 65.00).");
            return null;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(precioStr);
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V4)",
                    "Formato numérico de precio inválido.");
            return null;
        }

        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V5)",
                    "El precio debe ser mayor que 0 (mínimo 0.01).");
            return null;
        }

        if (precio.scale() > 2) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V6)",
                    "El precio no puede tener más de 2 decimales.");
            return null;
        }

        if (precio.compareTo(new BigDecimal("99999999.99")) > 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Precio (V6)",
                    "El precio excede el máximo permitido de 99999999.99.");
            return null;
        }

        if (!stockStr.matches("^-?\\d+$")) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Stock (V7)",
                    "El stock debe ser un número entero válido sin decimales.");
            return null;
        }

        int stock;
        try {
            long stockLong = Long.parseLong(stockStr);
            if (stockLong < 0 || stockLong > Integer.MAX_VALUE) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación de Stock (V8)",
                        "El stock debe ser un entero entre 0 y 2147483647.");
                return null;
            }
            stock = (int) stockLong;
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Stock (V8)",
                    "El stock está fuera del rango permitido (0 a 2147483647).");
            return null;
        }

        try {
            if (productoDAO.existeCodigo(codigo, idActual)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado (V9)",
                        "Ya existe otro producto registrado con el código '" + codigo + "'.");
                return null;
            }
        } catch (SQLException e) {
            mostrarErrorSQL("Error al verificar disponibilidad del código", e);
            return null;
        }

        txtCodigo.setText(codigo);
        txtNombre.setText(nombre);

        return new Producto(idActual, codigo, nombre, categoria, precio, stock);
    }

    private void mostrarErrorSQL(String contexto, SQLException e) {
        String sqlState = (e.getSQLState() != null) ? e.getSQLState() : "Desconocido";
        String mensajeUsuario;

        if ("23505".equals(sqlState)) {
            mensajeUsuario = "Violación de unicidad: Ya existe un producto con ese código.";
        } else if ("23514".equals(sqlState)) {
            mensajeUsuario = "Los datos enviados incumplen una restricción CHECK de integridad en la base de datos.";
        } else if (sqlState.startsWith("08")) {
            mensajeUsuario = "No se pudo establecer conexión con PostgreSQL. Verifique que el servidor esté activo y que config.properties sea correcto.";
        } else {
            mensajeUsuario = "Ocurrió un error en la base de datos al procesar la operación (Estado SQL: " + sqlState + ").";
        }

        mostrarAlerta(Alert.AlertType.ERROR, contexto, mensajeUsuario);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}