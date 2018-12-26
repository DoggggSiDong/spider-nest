package spider.nest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpiderNestApplication {
    public static void main(String args[]){
        new SpringApplicationBuilder(SpiderNestApplication.class).run(args);
    }
}
