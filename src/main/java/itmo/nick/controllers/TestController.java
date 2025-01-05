package itmo.nick.controllers;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.database.entities.PersonTable;
import itmo.nick.database.PersonTableService;
import itmo.nick.person.Person;
import itmo.nick.person.PersonState;
import itmo.nick.test.SimpleTest;
import itmo.nick.test.attention.AttentionTestOne;
import itmo.nick.test.attention.AttentionTestOneData;
import itmo.nick.test.memory.MemoryTestOne;
import itmo.nick.test.processing.ProcessingTestOne;
import itmo.nick.test.processing.ProcessingTestOneData;
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
     * Страница регистрации
     */
    @GetMapping("/r")
    public String registration() {
        deleteTestAndData();
        return "registration";
    }

    /**
     * Страница оценки состояния
     */
    @GetMapping("/s")
    public String personStateAnalyze() {
        return "personStateAnalyze";
    }

    /**
     * Страница всех тестов
     */
    @GetMapping("/t")
    public String testsPage(Model model) {
        for(int i = 1; i <= SimpleTest.TEST_AMOUNT; i++) {
            model.addAttribute("t" + i, testTableService.getNameById(i));
        }
        return "testsPage";
    }

    /**
     * Тест на внимательность 1
     * @param stage этап
     */
    @GetMapping("/t/a/1")
    public String attentionTestOne(@RequestParam("s") int stage, Model model) {
        AttentionTestOne attentionTestOne = getInstance();
        stage = attentionTestOne.CorrectNextStage(stage);

        if (stage == AttentionTestOne.DESCRIPTION_STAGE) {
            model.addAttribute("desc", testTableService.getDescById(1));
            model.addAttribute("name", testTableService.getNameById(1));
        }

        if (stage == AttentionTestOne.LAST_STAGE) {
            model.addAttribute("result",
                attentionTestOne.resultSec(testTableService.getResultsById(1).split(";"))
            );
        }
        return "attention/attentionTestOneStage" + stage;
    }

    /**
     * Тест на память 1
     * @param stage этап
     * @param timeout время реакции
     */
	@GetMapping("/t/m/1")
	public String memoryTestOne(@RequestParam("s") int stage,
                                @RequestParam(value = "t", required = false) Double timeout, Model model) {
 		MemoryTestOne memoryTestOne = MemoryTestOne.getInstance();
		stage = memoryTestOne.CorrectNextStage(stage);

        if (timeout != null) {
            if (timeout > MemoryTestOne.TIMEOUT_IN_SEC) {
                memoryTestOne.answer("0", 0);
            } else {
                //только время реации распознавания
                memoryTestOne.answer("1", timeout);
            }
        }

        if (stage == MemoryTestOne.DESCRIPTION_STAGE) {
            model.addAttribute("desc", testTableService.getDescById(2));
            model.addAttribute("name", testTableService.getNameById(2));
        }

        String picture = memoryTestOne.getNextPicture();
        if ("-1".equals(picture)) {
            stage = MemoryTestOne.LAST_STAGE;
        } else {
            model.addAttribute("imagePath", "/img/memtrax/" + picture + ".jpg");
        }

        if (stage == MemoryTestOne.LAST_STAGE) {
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

    /**
     * Тест на реацию 1
     * @param stage этап
     */
    @GetMapping("/t/r/1")
    public String reactionTestOne(@RequestParam("s") int stage, Model model) {
        ReactionTestOne reactionTestOne = ReactionTestOne.getInstance();
        stage = reactionTestOne.CorrectNextStage(stage);

        if (reactionTestOne.timeIsUp()) {
            stage = ReactionTestOne.LAST_STAGE;
        }

        if (stage == ReactionTestOne.DESCRIPTION_STAGE) {
            model.addAttribute("desc", testTableService.getDescById(3));
            model.addAttribute("name", testTableService.getNameById(3));
        }

        if (stage == ReactionTestOne.LAST_STAGE) {
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
     * Тест на обработку информации 1
     * @param stage этап
     */
    @GetMapping("/t/p/1")
    public String processingTestOne(@RequestParam("s") int stage, Model model) {
        ProcessingTestOne processingTestOne = ProcessingTestOne.getInstance();
        stage = processingTestOne.CorrectNextStage(stage);

        if (stage == ReactionTestOne.DESCRIPTION_STAGE) {
            model.addAttribute("desc", testTableService.getDescById(4));
            model.addAttribute("name", testTableService.getNameById(4));
        }

        String numbers = processingTestOne.getNextNumbers();
        if ("-1".equals(numbers)) {
            stage = MemoryTestOne.LAST_STAGE;
        } else {
            model.addAttribute("numbers", numbers);
        }

        if (stage == ReactionTestOne.LAST_STAGE) {
            if (!processingTestOne.isFinished()) {

                resultTableService.saveTestFourth();
            }
            model.addAttribute("result",
                processingTestOne.result(testTableService.getResultsById(4).split(";"))
            );
        }

        return "processing/processingTestOneStage" + stage;
    }


    /**
     * Получение данных регистрации участника
     * @param person участник
     */
    @PostMapping("/register")
    public String registerPerson(@RequestBody Person person) {
        PersonTable.getInstance().setPerson(person);
        return "personStateAnalyze";
    }

    /**
     * Получение данных состояния участника
     * @param personState состояние участника
     */
    @PostMapping("/stateAnalyze")
    public String personState(@RequestBody PersonState personState) {
        PersonTable.getInstance().setPersonState(personState);
        personTableService.save(PersonTable.getInstance());
        PersonTable.delete();
        return "personStateAnalyze";
    }

    /**
     * Получение данных этапа теста на внимательность 1
     * @param data данные
     */
    @PostMapping("/attentionTestOneStageData")
    public ResponseEntity<Void> attentionTestOneStageData(@RequestBody AttentionTestOneData data) {
        AttentionTestOne attentionTestOne = getInstance();

        if (data.getStage() != AttentionTestOne.DESCRIPTION_STAGE) {
            attentionTestOne.getTestData()[data.getStage()] = new AttentionTestOneData(data.getStage(), data.getTime(), data.getErrors());
        }

        if (data.getStage() == AttentionTestOne.LAST_STAGE - 1) {
            resultTableService.saveTestOne(attentionTestOne.getTestData());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Получение данных этапа теста на реакцию 1
     * @param data данные
     */
    @PostMapping("/reactionTestOneStageData")
    public ResponseEntity<Void> reactionTestOneStageData(@RequestBody ReactionTestOneData data) {
        ReactionTestOne reactionTestOne = ReactionTestOne.getInstance();
        reactionTestOne.addData(data);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение данных этапа теста на обработку информации 1
     * @param data данные
     */
    @PostMapping("/processingTestOneStageData")
    public ResponseEntity<Void> processingTestOneStageData(@RequestBody ProcessingTestOneData data) {
        ProcessingTestOne processingTestOne = ProcessingTestOne.getInstance();
        processingTestOne.addData(data);
        return ResponseEntity.ok().build();
    }
}

