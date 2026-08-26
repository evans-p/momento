package gr.evansp.momento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MomentoPosts {
  public static void main(String[] args) {
    SpringApplication.run(MomentoPosts.class, args);
  }
}
