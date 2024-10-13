package itmo.nick.tests;

import itmo.nick.database.PersonDatabaseActions;
import itmo.nick.person.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {

    @GetMapping("/r")
    public String registration() {
        return "registration";
    }

    @PostMapping("/register")
    public String registerPerson(@RequestBody Person person) {
        PersonDatabaseActions personDatabaseActions = new PersonDatabaseActions();
        personDatabaseActions.save(person);
        return "personStateAnalyze";
    }

    @GetMapping("/s")
    public String personStateAnalyze() {
        return "personStateAnalyze";
    }
}
