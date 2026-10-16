
package com.example.evaluacioncorte2.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {

    private static final String ARCHIVO_CONFIG = "db.properties";

    private ConexionDB() {
    }

    private static Properties cargarConfig() throws SQLException {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(ARCHIVO_CONFIG)) {
            props.load(fis);
        } catch (IOException e) {
            throw new SQLException(
                    "No se pudo leer db.properties. " +
                            "Coloca el archivo en la carpeta raíz del proyecto.",
                    e
            );
        }

        return props;
    }

    public static Connection getConexion() throws SQLException {
        Properties props = cargarConfig();

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        if (url == null || url.isBlank()
                || user == null || user.isBlank()
                || password == null) {
            throw new SQLException(
                    "Faltan datos en db.properties: " +
                            "db.url, db.user o db.password."
            );
        }

        return DriverManager.getConnection(url, user, password);
    }
}
