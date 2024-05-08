package fei.stu.billing.web;

import fei.stu.billing.app.service.customer.CustomerComputerService;
import fei.stu.billing.app.service.customer.CustomerService;
import fei.stu.billing.web.dto.CustomerInfoDto;
import fei.stu.billing.web.dto.ResponseBody;
import fei.stu.billing.web.dto.VerifyRequestDto;
import fei.stu.billing.web.dto.Response;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import fei.stu.billing.app.service.invoice.InvoiceService;

import java.io.IOException;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
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
    private final CustomerComputerService customerComputerService;


    public LicenseController(InvoiceService invoiceService, CustomerService customerService, CustomerComputerService customerComputerService) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
        this.customerComputerService = customerComputerService;
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @PostMapping("/license")
    public ResponseBody hellos(@RequestBody VerifyRequestDto verifyRequestDto) throws Exception {
        System.out.println(verifyRequestDto);

        boolean isValid = verifySignature(verifyRequestDto.data(), verifyRequestDto.signature(),  verifyRequestDto.customerId());

        if (isValid) {
            PublicKey publicKey = customerComputerService.getEncryptionKey(verifyRequestDto.customerId());
            Response response = invoiceService.checkIfInvoiceIsPaid(verifyRequestDto.customerId());


            // Convert the response to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            String encryptedResponse =  encryptData(jsonResponse, publicKey);

            PrivateKey privateKey = loadPrivateKeyFromFile("privateCompanyKey.pem");
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(privateKey);
            signature.update(jsonResponse.getBytes("UTF-8"));
            byte[] signatureData = signature.sign();
            return new ResponseBody(encryptedResponse, Base64.getEncoder().encodeToString(signatureData));
        } else {
            throw new RuntimeException("Signature is incorrect");
        }

    }

    @PostMapping("/notify")
    public void notifyMe(@RequestBody CustomerInfoDto customerInfoDto){
        customerService.notifyMe(customerInfoDto);
    }


    private boolean verifySignature(String jsonData, String base64Signature, Integer customerId) throws Exception {
        // Convert the public key from a stored string or file
        byte[] signatureBytes = Base64.getDecoder().decode(base64Signature.replace("\n", "").replace("\r", ""));
        byte[] data = jsonData.getBytes();

        PublicKey publicKey = customerComputerService.getSigningKey(customerId);

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

    private PublicKey loadKeyFromFile(String filePath) throws Exception {
        // Read all bytes from the public key file
        String publicKeyPEM = new String(Files.readAllBytes(Paths.get(filePath)));

        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");

        publicKeyPEM = publicKeyPEM.replaceAll("\\s", "");

        // Decode the Base64 encoded string
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

        // Generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private PrivateKey loadPrivateKeyFromFile(String filePath) throws Exception {
        String privateKeyPEM = new String(Files.readAllBytes(Paths.get(filePath)));
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

}

