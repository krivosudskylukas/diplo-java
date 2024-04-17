package fei.stu.billing.infra.invoice.entity;

import fei.stu.billing.infra.AbstractEntity;
import fei.stu.billing.infra.customer.entity.CustomerEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "t_invoice")
public class InvoiceEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_seq")
    @SequenceGenerator(name = "invoice_id_seq", sequenceName = "invoice_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "paid", nullable = false)
    private boolean paid;

    @Column(name = "number_of_tries", nullable = false)
    private int numberOfTries;

    @Column(name = "license_file_transferred", nullable = false)
    private boolean fileTransferred;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvoiceDetailEntity> invoiceDetails;

    public InvoiceEntity() {
    }

    public Integer getId() {
        return id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<InvoiceDetailEntity> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<InvoiceDetailEntity> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getNumberOfTries() {
        return numberOfTries;
    }

    public void setNumberOfTries(int numberOfTries) {
        this.numberOfTries = numberOfTries;
    }

    public boolean isFileTransferred() {
        return fileTransferred;
    }

    public void setFileTransferred(boolean fileTransferred) {
        this.fileTransferred = fileTransferred;
    }
}
