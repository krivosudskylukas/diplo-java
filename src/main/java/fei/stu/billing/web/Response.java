package fei.stu.verifier.ui;

import java.util.List;

public record Response(Long expiration_date, String name, Long starting_date, List<String> functionality) { 

}
