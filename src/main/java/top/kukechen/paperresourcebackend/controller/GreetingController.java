package top.kukechen.paperresourcebackend.controller;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kukechen.paperresourcebackend.restservice.Greeting;
import top.kukechen.paperresourcebackend.units.JwtUtils;
import top.kukechen.paperresourcebackend.units.PassToken;


@RestController
@RequestMapping("/index")
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) throws Exception {
//        if(!StringUtils.isEmpty(name)) throw new Exception("Error occurred");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}