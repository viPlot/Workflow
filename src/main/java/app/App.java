package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
   // @Bean
    //public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
    //}
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
