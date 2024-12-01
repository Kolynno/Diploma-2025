package itmo.nick.controllers;

import itmo.nick.database.entities.PersonTable;
import itmo.nick.database.PersonTableService;
import itmo.nick.person.Person;
import itmo.nick.person.PersonState;
import itmo.nick.test.attention.TestOne;
import itmo.nick.test.attention.TestOneData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Основной контроллер
 */
@Controller
public class TestController {

    @Autowired
    private PersonTableService personTableService;

    /**
     * Основные страницы
     */
    //Страница регистрации нового участника
    @GetMapping("/r")
    public String registration() {
        return "registration";
    }

    //Страница оценки первичного состояния
    @GetMapping("/s")
    public String personStateAnalyze() {
        return "personStateAnalyze";
    }

    //Страница всех тестов
    @GetMapping("/t")
    public String testsPage() {
        return "testsPage";
    }

    /**
     * Тесты
     */
    //Тест на внимательность 1
    @GetMapping("/t/a/1")
    public String attentionTestOne(@RequestParam("s") int stage) {
        TestOne testOne = TestOne.getInstance();
        stage = testOne.CorrectNextStage(stage);
        return "attention/attentionTestOneStage" + stage;
    }

    /**
     * Реация на действия
     */
    //Данные регистрации участника
    @PostMapping("/register")
    public String registerPerson(@RequestBody Person person) {
        PersonTable.getInstance().setPerson(person);
        return "personStateAnalyze";
    }

    //Данные состояния участника
    @PostMapping("/stateAnalyze")
    public String personState(@RequestBody PersonState personState) {
        PersonTable.getInstance().setPersonState(personState);
        personTableService.save(PersonTable.getInstance());
        return "personStateAnalyze";
    }

    //Данные этапа теста на внимательность 1
    @PostMapping("/attentionTestOneStageData")
    public ResponseEntity<Void> attentionTestOneStageData(@RequestBody TestOneData data) {
        //System.out.println(data);
        return ResponseEntity.ok().build();
    }
}

