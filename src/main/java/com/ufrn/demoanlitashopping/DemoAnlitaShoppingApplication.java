package com.ufrn.demoanlitashopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class DemoAnlitaShoppingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoAnlitaShoppingApplication.class, args);
    }

}
