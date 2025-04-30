package com.nogueira.git.actions.demogitactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoGitActionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGitActionsApplication.class, args);
    }

    @RestController
    class HelloController {
        @GetMapping("/")
        public String hello() {
            return "Hello, Client!";
        }
    }
}
