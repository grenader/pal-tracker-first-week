package io.pivotal.pal.tracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String sayHello() {
        return "hello from Igor";
    }

    @GetMapping("/java")
    public String printJavaVersion() {
        return "System.getProperty(\"java.version\") =" + System.getProperty("java.version");
    }
}
