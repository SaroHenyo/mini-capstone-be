package henyosisaro.minicapstonebe.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HelloWorldRequest {

    private String firstName;
    private String lastName;
}