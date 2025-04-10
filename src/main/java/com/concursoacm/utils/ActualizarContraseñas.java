package com.concursoacm.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ActualizarContraseñas {

    private static final String URL = "jdbc:mysql://localhost:3306/concurso_acm"; // Cambia la URL si es necesario
    private static final String USER = "root"; // Usuario de la base de datos
    private static final String PASSWORD = ""; // Contraseña de la base de datos

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Obtener todas las filas de la tabla jefes_delegacion
            String selectQuery = "SELECT id_jefe, contraseña FROM jefes_delegacion";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();

            // Preparar la consulta de actualización
            String updateQuery = "UPDATE jefes_delegacion SET contraseña = ? WHERE id_jefe = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

            while (resultSet.next()) {
                int idJefe = resultSet.getInt("id_jefe");
                String contraseñaActual = resultSet.getString("contraseña");

                // Codificar la contraseña
                String contraseñaCodificada = passwordEncoder.encode(contraseñaActual);

                // Actualizar la contraseña en la base de datos
                updateStatement.setString(1, contraseñaCodificada);
                updateStatement.setInt(2, idJefe);
                updateStatement.executeUpdate();

                System.out.println("Contraseña actualizada para el ID_Jefe: " + idJefe);
            }

            System.out.println("Actualización de contraseñas completada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
