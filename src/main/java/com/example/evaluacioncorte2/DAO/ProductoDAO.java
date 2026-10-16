package com.example.evaluacioncorte2.DAO;

import com.example.evaluacioncorte2.connection.ConexionDB;
import com.example.evaluacioncorte2.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> buscarProductos(String texto, String categoria, boolean soloConExistencias) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id_producto, codigo, nombre, categoria, precio, stock FROM producto WHERE 1=1"
        );
        List<Object> parametros = new ArrayList<>();

        if (texto != null && !texto.trim().isEmpty()) {
            sql.append(" AND (codigo ILIKE ? OR nombre ILIKE ?)");
            String patron = "%" + texto.trim() + "%";
            parametros.add(patron);
            parametros.add(patron);
        }

        if (categoria != null && !categoria.equals("Todas")) {
            sql.append(" AND categoria = ?");
            parametros.add(categoria);
        }

        if (soloConExistencias) {
            sql.append(" AND stock > 0");
        }

        sql.append(" ORDER BY codigo ASC");

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                pstmt.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock")
                    );
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            registrarDiagnostico("CONSULTA / FILTRO", e);
            throw e;
        }
        return lista;
    }

    public boolean existeCodigo(String codigo, Integer idExcluir) throws SQLException {
        String sql = (idExcluir == null)
                ? "SELECT COUNT(*) FROM producto WHERE UPPER(BTRIM(codigo)) = UPPER(BTRIM(?))"
                : "SELECT COUNT(*) FROM producto WHERE UPPER(BTRIM(codigo)) = UPPER(BTRIM(?)) AND id_producto <> ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            if (idExcluir != null) {
                pstmt.setInt(2, idExcluir);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            registrarDiagnostico("VERIFICAR DUPLICADO", e);
            throw e;
        }
        return false;
    }

    public int insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO producto (codigo, nombre, categoria, precio, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getCodigo());
            pstmt.setString(2, p.getNombre());
            pstmt.setString(3, p.getCategoria());
            pstmt.setBigDecimal(4, p.getPrecio());
            pstmt.setInt(5, p.getStock());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            registrarDiagnostico("INSERTAR PRODUCTO", e);
            throw e;
        }
    }

    public int actualizar(Producto p) throws SQLException {
        String sql = "UPDATE producto SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ? WHERE id_producto = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getCodigo());
            pstmt.setString(2, p.getNombre());
            pstmt.setString(3, p.getCategoria());
            pstmt.setBigDecimal(4, p.getPrecio());
            pstmt.setInt(5, p.getStock());
            pstmt.setInt(6, p.getIdProducto());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            registrarDiagnostico("ACTUALIZAR PRODUCTO", e);
            throw e;
        }
    }

    public int eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            registrarDiagnostico("ELIMINAR PRODUCTO", e);
            throw e;
        }
    }

    private void registrarDiagnostico(String operacion, SQLException e) {
        System.err.printf("[DIAGNÓSTICO SQL] Operación: %s | SQLState: %s | Código Error: %d | Detalle: %s%n",
                operacion, e.getSQLState(), e.getErrorCode(), e.getMessage());
    }
}