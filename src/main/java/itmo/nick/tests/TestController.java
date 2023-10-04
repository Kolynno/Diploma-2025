package itmo.nick.tests;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/first")
    public String firstTest() {
        return "firstTest";
    }

    @GetMapping("/second")
    public String secondTest() {
        return "secondTest";
    }

    @GetMapping("/third")
    public String thirdTest() {
        return "thirdTest";
    }
}
