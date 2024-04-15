package fei.stu.billing.domain;

import java.time.LocalDateTime;

public record CustomerComputer(
        Integer id,
        Customer customer,
        String url,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
