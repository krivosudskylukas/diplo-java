package fei.stu.billing.domain;

import jakarta.persistence.Column;

public record Customer(
        Integer id,
        String companyName,
        String email,
        String phoneNumber,
        String address) {
}
