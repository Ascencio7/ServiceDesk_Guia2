/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.edu.itca.servicedesk360.service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 * @author Vladimir Ascencio
 */
public class SeguridadClave {
    private SeguridadClave() {
 }
 public static String generarHash(String clave) {
     try {
 MessageDigest digest = MessageDigest.getInstance("SHA-256");
 byte[] bytes = digest.digest(
 clave.getBytes(StandardCharsets.UTF_8));
 return Base64.getEncoder().encodeToString(bytes);
 } catch (NoSuchAlgorithmException ex) {
 throw new IllegalStateException(
 "No fue posible transformar la contraseña.", ex);
 }
 }
}