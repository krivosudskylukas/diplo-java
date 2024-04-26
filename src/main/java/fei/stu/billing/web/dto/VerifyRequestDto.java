package fei.stu.billing.web.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;

public record VerifyRequestDto(Integer customerId, @JsonRawValue String data, @JsonRawValue String signature) {

}
