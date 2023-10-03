package itmo.nick.tests;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstTestController {

    @GetMapping("/first")
    public String firstTest() {
        return "firstTest";
    }
}
