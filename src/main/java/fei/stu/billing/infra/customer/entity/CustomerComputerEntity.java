package fei.stu.billing.infra.customer.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_customer_computer")
public class CustomerComputerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_computer_id_seq")
    @SequenceGenerator(name = "customer_computer_id_seq", sequenceName = "customer_computer_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    CustomerEntity customer;

    @Column(name = "public_encryption_key")
    String encryptionKey;

    @Column(name = "public_signing_key")
    String signingKey;

    public CustomerComputerEntity() {
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
}
