package itmo.nick.controllers;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.database.entities.PersonTable;
import itmo.nick.database.PersonTableService;
import itmo.nick.person.Person;
import itmo.nick.person.PersonState;
import itmo.nick.test.attention.AttentionTestOne;
import itmo.nick.test.attention.AttentionTestOneData;
import itmo.nick.test.memory.MemoryTestOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static itmo.nick.test.SimpleTest.setNotFinished;
import static itmo.nick.test.attention.AttentionTestOne.getInstance;

/**
 * Основной контроллер
 */
@Controller
public class TestController {

    @Autowired
    private PersonTableService personTableService;
    @Autowired
    private ResultTableService resultTableService;
    @Autowired
    private TestTableService testTableService;

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
    public String testsPage(Model model) {
		model.addAttribute("t1", testTableService.getNameById(1));
        return "testsPage";
    }

    /**
     * Тесты
     */
    //Тест на внимательность 1
    @GetMapping("/t/a/1")
    public String attentionTestOne(@RequestParam("s") int stage, Model model) {
        AttentionTestOne attentionTestOne = getInstance();
        stage = attentionTestOne.CorrectNextStage(stage);
        if (stage == 0) {
            model.addAttribute("desc", testTableService.getDescById(1));
            model.addAttribute("name", testTableService.getNameById(1));
        }
        if (stage == 5) {
            model.addAttribute("result",
                attentionTestOne.resultSec(testTableService.getResultsById(1).split(";"))
            );
        }
        return "attention/attentionTestOneStage" + stage;
    }

	//Тест на память 1
	@GetMapping("/t/m/1")
	public String memoryTestOne(@RequestParam("s") int stage,
                                @RequestParam(value = "t", required = false) Integer timeout, Model model) {
		MemoryTestOne memoryTestOne = MemoryTestOne.getInstance();
		stage = memoryTestOne.CorrectNextStage(stage);
        if (timeout != null) {
            if (timeout == 1) {
                System.out.println("TIME");
            } else if (timeout == 0) {
                System.out.println("SPACE");
            }
        }

        if (stage == 0) {
            model.addAttribute("desc", testTableService.getDescById(2));
            model.addAttribute("name", testTableService.getNameById(2));
        }
        if (stage == 2) {
        }
        model.addAttribute("imagePath", "/img/memtrax/"+ memoryTestOne.getNextPicture() +".jpg");
		return "memory/memoryTestOneStage" + stage;
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
        PersonTable.delete();
        setNotFinished();
        return "personStateAnalyze";
    }

    //Данные этапа теста на внимательность 1
    @PostMapping("/attentionTestOneStageData")
    public ResponseEntity<Void> attentionTestOneStageData(@RequestBody AttentionTestOneData data) {
        System.out.println(data);
        AttentionTestOne attentionTestOne = getInstance();

        if (data.getStage() != 0) {
            attentionTestOne.getTestData()[data.getStage()] = new AttentionTestOneData(data.getStage(), data.getTime(), data.getErrors());
        }

        if (data.getStage() == 4) {
            resultTableService.saveTestOne(attentionTestOne.getTestData());
        }
        return ResponseEntity.ok().build();
    }
}

