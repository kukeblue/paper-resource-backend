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

    @GetMapping("/login")
    @PassToken
    public String login(@RequestParam(value = "name", defaultValue = "World") String name) throws Exception {
        //给分配一个token 然后返回
        String jwtToken = JwtUtils.createToken("1", "1", "1" );
//        Customer customer = new Customer("陈4", "焕" );
//        MongoDBUtil.saveOne("Department", customer);
        return jwtToken;
    }
}