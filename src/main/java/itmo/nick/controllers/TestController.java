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
import itmo.nick.test.reaction.ReactionTestOne;
import itmo.nick.test.reaction.ReactionTestOneData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static itmo.nick.test.SimpleTest.deleteTestAndData;
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
        deleteTestAndData();
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
        model.addAttribute("t2", testTableService.getNameById(2));
        model.addAttribute("t3", testTableService.getNameById(3));
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
                                @RequestParam(value = "t", required = false) Double timeout, Model model) {
 		MemoryTestOne memoryTestOne = MemoryTestOne.getInstance();
		stage = memoryTestOne.CorrectNextStage(stage);
        if (timeout != null) {
            if (timeout > 3.0) {
                memoryTestOne.answer("0", 0);
            } else {
                //только время реации распознавания
                memoryTestOne.answer("1", timeout);
            }
        }

        if (stage == 0) {
            model.addAttribute("desc", testTableService.getDescById(2));
            model.addAttribute("name", testTableService.getNameById(2));
        }
        String picture = memoryTestOne.getNextPicture();
        if ("-1".equals(picture)) {
            stage = 2;
        } else {
            model.addAttribute("imagePath", "/img/memtrax/" + picture + ".jpg");
        }
        if (stage == 2) {
            if (!memoryTestOne.isFinished()) {
                resultTableService.saveTestTwo(memoryTestOne.getErrorPercent(), memoryTestOne.getAnswerMs());
                memoryTestOne.setFinished(true);
            }
            model.addAttribute("result",
                memoryTestOne.result(testTableService.getResultsById(2).split(";"))
            );

        }

		return "memory/memoryTestOneStage" + stage;
	}

    //Тест на реацию 1
    @GetMapping("/t/r/1")
    public String reactionTestOne(@RequestParam("s") int stage, Model model) {
        ReactionTestOne reactionTestOne = ReactionTestOne.getInstance();
        stage = reactionTestOne.CorrectNextStage(stage);

        if (reactionTestOne.timeIsUp()) {
            stage = 2;
        }

        if (stage == 0) {
            model.addAttribute("desc", testTableService.getDescById(3));
            model.addAttribute("name", testTableService.getNameById(3));
        }

        if (stage == 2) {
            if (!reactionTestOne.isFinished()) {
                reactionTestOne.getReactionTestOneDataList().remove(0); //первое ложное при старте
                resultTableService.saveTestThree(String.valueOf(reactionTestOne.getReactionTime()).substring(0,5),
                    String.valueOf(reactionTestOne.getErrors()), reactionTestOne.getFalseStarts());
                reactionTestOne.setFinished(true);
            }
            model.addAttribute("result",
                reactionTestOne.result(testTableService.getResultsById(3).split(";"))
            );
        }

        return "reaction/reactionTestOneStage" + stage;
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
        return "personStateAnalyze";
    }

    //Данные этапа теста на внимательность 1
    @PostMapping("/attentionTestOneStageData")
    public ResponseEntity<Void> attentionTestOneStageData(@RequestBody AttentionTestOneData data) {
        AttentionTestOne attentionTestOne = getInstance();

        if (data.getStage() != 0) {
            attentionTestOne.getTestData()[data.getStage()] = new AttentionTestOneData(data.getStage(), data.getTime(), data.getErrors());
        }

        if (data.getStage() == 4) {
            resultTableService.saveTestOne(attentionTestOne.getTestData());
        }
        return ResponseEntity.ok().build();
    }

    //Данные этапа теста на внимательность 1
    @PostMapping("/reactionTestOneStageData")
    public ResponseEntity<Void> reactionTestOneStageData(@RequestBody ReactionTestOneData data) {
        ReactionTestOne reactionTestOne = ReactionTestOne.getInstance();
        reactionTestOne.addData(data);
        return ResponseEntity.ok().build();
    }
}

