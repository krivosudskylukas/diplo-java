package fei.stu.billing.web;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import fei.stu.billing.app.service.invoice.InvoiceService;

import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;


@RestController
@RequestMapping("/api")
public class LicenseController{

    private final InvoiceService invoiceService;
    
    public LicenseController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }   

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @PostMapping("/license")
    public String hellos(@RequestBody Request request) throws Exception{
        System.out.println(request);

        boolean isValid = verifySignature(request.data(), request.signature());

        if (isValid) {
            PublicKey publicKey = loadPublicKeyFromFile("publicKey.pem");
            Response response = new Response(1739919600L, "Kramare", 1713024184L, List.of("Scan", "Xray", "Messages"));


            // Convert the response to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            System.out.println(jsonResponse);
            return encryptData(jsonResponse, publicKey);
            // return "Signature is correct";
         } else {
             throw new RuntimeException("Signature is incorrect");
         }

    }


public boolean verifySignature(String jsonData, String base64Signature) throws Exception {
    // Convert the public key from a stored string or file
    /*String publicKeyPEM = new String(Files.readAllBytes(Paths.get("publicKey.pem")));
    publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "")
                                .replaceAll("\\s", "");
    byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);*/
    byte[] signatureBytes = Base64.getDecoder().decode(base64Signature.replace("\n", "").replace("\r", ""));
    //byte[] data = Base64.getDecoder().decode(jsonData.replace("\n", "").replace("\r", ""));
    byte[] data = jsonData.getBytes();

    /*X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(keySpec);*/
    PublicKey publicKey = loadPublicKeyFromFile("publicKey.pem");

    Signature sig = Signature.getInstance("SHA256withRSA/PSS");
    sig.initVerify(publicKey);
    sig.update(data);

    return sig.verify(signatureBytes);
}

public String encryptData(String jsonData, PublicKey publicKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
    cipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepParams);
    byte[] encryptedBytes = cipher.doFinal(jsonData.getBytes());
    return Base64.getEncoder().encodeToString(encryptedBytes);
    
    /*Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encryptedBytes = cipher.doFinal("{\"CustomerId\":\"Kramare\",\"Request date\":1713724944}".getBytes());
    return Base64.getEncoder().encodeToString(encryptedBytes);*/
}

public PublicKey loadPublicKeyFromFile(String filePath) throws Exception {
    // Read all bytes from the public key file
    String publicKeyPEM = new String(Files.readAllBytes(Paths.get(filePath)));

    // Remove the first and last lines
    publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "");

    // Remove newline characters
    publicKeyPEM = publicKeyPEM.replaceAll("\\s", "");

    // Decode the Base64 encoded string
    byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

    // Generate public key
    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(spec);
}

public long getNextMonth(){
    // Get the current date and time
    LocalDateTime now = LocalDateTime.now();

    // Adjust to the first of the next month
    LocalDateTime firstOfNextMonth = now.with(TemporalAdjusters.firstDayOfNextMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0);

    // Convert LocalDateTime to ZonedDateTime to include the time zone
    ZonedDateTime zonedDateTime = firstOfNextMonth.atZone(ZoneId.systemDefault());

    // Convert to epoch seconds
    return zonedDateTime.toEpochSecond();
}

}

