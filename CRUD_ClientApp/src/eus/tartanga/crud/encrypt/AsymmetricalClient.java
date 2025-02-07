package eus.tartanga.crud.encrypt;
import static com.google.common.io.ByteStreams.toByteArray;
import eus.tartanga.crud.exception.EncryptException;
import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * La clase AsymmetricalClient cifra una contraseña utilizando una clave pública RSA cargada desde un archivo de recursos.
 * Utiliza el algoritmo RSA con el esquema de padding PKCS1.
 * En caso de error, lanza una EncryptException.
 * 
 * @author Irati y Meylin
 */
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
