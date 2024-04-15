package fei.stu.billing.domain;


import java.time.LocalDateTime;
import java.util.List;

public record Invoice(
        Integer id,
        Customer customer,
        LocalDateTime invoiceDate,
        LocalDateTime dueDate,
        Double totalAmount,
        List<Product> products,
        boolean paid,
        int numberOfTries
) {
}
