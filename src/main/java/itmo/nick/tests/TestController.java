package itmo.nick.tests;

import itmo.nick.person.Person;
import itmo.nick.person.PersonState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {

    @GetMapping("/r")
    public String registration() {
        return "registration";
    }

    @GetMapping("/s")
    public String personStateAnalyze() {
        return "personStateAnalyze";
    }

    @GetMapping("/t")
    public String testsPage() {
        return "testsPage";
    }

    @PostMapping("/register")
    public String registerPerson(@RequestBody Person person) {
        return "personStateAnalyze";
    }

    @PostMapping("/stateAnalyze")
    public String personState(@RequestBody PersonState personState) {
        return "personStateAnalyze";
    }
}

