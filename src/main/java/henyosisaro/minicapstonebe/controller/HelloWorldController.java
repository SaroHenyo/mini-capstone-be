package henyosisaro.minicapstonebe.controller;

import henyosisaro.minicapstonebe.model.HelloWorldRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/helloWorld")
public class HelloWorldController {

    @GetMapping("")
    public String getHelloWorldApi() {
        return "Hello World!";
    }

    @PostMapping("/custom")
    public String getHelloName(@RequestBody HelloWorldRequest helloWorldRequest) {
        return "Hello ".concat(helloWorldRequest.getFirstName()).concat(" ".concat(helloWorldRequest.getLastName()));
    }

    @PutMapping("/custom/{firstName}/{lastName}")
    public String getHelloName(@PathVariable String firstName, @PathVariable String lastName) {
        return "Hello ".concat(firstName).concat(" ".concat(lastName));
    }
}