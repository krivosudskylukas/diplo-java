package fei.stu.billing.web.dto;

public record ResponseBody(String encryptedResponse, String signature) {
}
