/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.encrypt;

/**
 *
 * @author Irati y Meylin
 */
import static com.google.common.io.ByteStreams.toByteArray;
import eus.tartanga.crud.exception.EncryptException;
import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

//Este metodo se usa para cifrar la contraseña usando una clave publica RSA
public class AsymmetricalClient {

    public byte[] encryptedData(String passwd) throws EncryptException {
        byte[] encryptedData;
        // Cargar la clave pública desde un recurso del classpath
        try {
            //lee la clave publica
            InputStream keyInputStream = AsymmetricalClient.class.getResourceAsStream("Client_Public.key");
            //convierte el contenido en un arreglo de bytes
            
            if (keyInputStream == null) {
                throw new FileNotFoundException("No se encontró el archivo de clave publica.");
            }
            
            byte publicKeyBytes[] = toByteArray(keyInputStream);
            //cierra el fichero
            keyInputStream.close();

            //Reconstruir la clave pública. Osease, convertir los bytes de la clave publica a un objeto publickey
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //crear un objeto con la clave en formato x509
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            //genera la clave publica
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            //inicializar el cifrado con el algoritmo RSA y el modo padding
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //configurar el cipher para cifrar
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            /*-cifrar la contraseña
              -Convertir la contraseña en bytes*/
            encryptedData = cipher.doFinal(passwd.getBytes());

        } catch (Exception e) {
            throw new EncryptException();
        }
        return encryptedData;
    }
}
