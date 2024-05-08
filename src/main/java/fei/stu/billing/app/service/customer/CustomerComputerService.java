package fei.stu.billing.app.service.customer;

import fei.stu.billing.domain.Customer;
import fei.stu.billing.domain.CustomerComputer;
import fei.stu.billing.infra.customer.entity.CustomerComputerEntity;
import fei.stu.billing.infra.customer.repository.CustomerComputerEntityMapper;
import fei.stu.billing.infra.customer.mapper.CustomerComputerJpaRepository;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class CustomerComputerService {

    private final CustomerComputerJpaRepository repository;
    private final CustomerComputerEntityMapper mapper;

    public CustomerComputerService(CustomerComputerJpaRepository repository, CustomerComputerEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CustomerComputer getCustomerComputerInfo(Integer customerId){
        return mapper.mapFromEntity(
                repository
                        .findByCustomerId(customerId)
                        .orElseThrow(() -> new IllegalArgumentException("No customer info!"))
        );
    }

    public PublicKey getSigningKey(Integer customerId) throws Exception {
        CustomerComputerEntity entity = repository.findByCustomerId(customerId).orElseThrow(() -> new IllegalArgumentException("No customer info!"));

        // Read all bytes from the public key file
        String publicKeyPEM = entity.getSigningKey();

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

    public PublicKey getEncryptionKey(Integer customerId) throws Exception {
        CustomerComputerEntity entity = repository.findByCustomerId(customerId).orElseThrow(() -> new IllegalArgumentException("No customer info!"));

        // Read all bytes from the public key file
        String publicKeyPEM = entity.getEncryptionKey();

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
