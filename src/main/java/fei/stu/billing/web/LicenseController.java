package fei.stu.billing.web;

import fei.stu.billing.app.service.customer.CustomerService;
import fei.stu.billing.web.dto.CustomerInfoDto;
import fei.stu.billing.web.dto.VerifyRequestDto;
import fei.stu.billing.web.dto.Response;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import fei.stu.billing.app.service.invoice.InvoiceService;

import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;


@RestController
@RequestMapping("/api")
public class LicenseController {

    private final InvoiceService invoiceService;
    private final CustomerService customerService;

    public LicenseController(InvoiceService invoiceService, CustomerService customerService) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @PostMapping("/license")
    public String hellos(@RequestBody VerifyRequestDto verifyRequestDto) throws Exception {
        System.out.println(verifyRequestDto);

        boolean isValid = verifySignature(verifyRequestDto.data(), verifyRequestDto.signature());

        if (isValid) {
            PublicKey publicKey = loadPublicKeyFromFile("publicKey.pem");
            Response response = invoiceService.checkIfInvoiceIsPaid(verifyRequestDto.customerId());


            // Convert the response to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);

            return encryptData(jsonResponse, publicKey);

        } else {
            throw new RuntimeException("Signature is incorrect");
        }

    }

    @PostMapping("/notify")
    public void notifyMe(@RequestBody CustomerInfoDto customerInfoDto){
        customerService.notifyMe(customerInfoDto);
    }


    private boolean verifySignature(String jsonData, String base64Signature) throws Exception {
        // Convert the public key from a stored string or file
        byte[] signatureBytes = Base64.getDecoder().decode(base64Signature.replace("\n", "").replace("\r", ""));
        byte[] data = jsonData.getBytes();

        PublicKey publicKey = loadPublicKeyFromFile("publicKey.pem");

        Signature sig = Signature.getInstance("SHA256withRSA/PSS");
        sig.initVerify(publicKey);
        sig.update(data);

        return sig.verify(signatureBytes);
    }

    private String encryptData(String jsonData, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepParams);
        byte[] encryptedBytes = cipher.doFinal(jsonData.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private PublicKey loadPublicKeyFromFile(String filePath) throws Exception {
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


}

