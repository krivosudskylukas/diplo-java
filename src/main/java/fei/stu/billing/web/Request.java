package fei.stu.verifier.ui;

import com.fasterxml.jackson.annotation.JsonRawValue;

public record Request(@JsonRawValue String data, @JsonRawValue String signature) {

}
