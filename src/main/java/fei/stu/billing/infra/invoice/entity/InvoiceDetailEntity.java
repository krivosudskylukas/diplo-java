package fei.stu.billing.infra.invoice.entity;

import fei.stu.billing.infra.AbstractEntity;
import fei.stu.billing.infra.product.entity.ProductEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_invoice_detail")
public class InvoiceDetailEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_detail_id_seq")
    @SequenceGenerator(name = "invoice_detail_id_seq", sequenceName = "invoice_detail_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    private InvoiceEntity invoice;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductEntity product;

    public InvoiceDetailEntity() {
    }

    public Integer getId() {
        return id;
    }

    public InvoiceEntity getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceEntity invoice) {
        this.invoice = invoice;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}