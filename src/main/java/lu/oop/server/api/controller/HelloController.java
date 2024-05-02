package lu.oop.server.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    // Since Uncle Bob said that comments should be used carefully, I will disregard his advice
    // So basically this is Spring Boot
    // And in Spring boot everything is done through magical decorators
    // How is this controller registered?
    // Well rather simple - the @RestController
    // It should be obvious to the untrained eye
    // Ok next we do the "Hello worlds"
    // We do it by giving request param
    // And then we return the string
    // TODO how to send JSON
    @GetMapping("/")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
}
