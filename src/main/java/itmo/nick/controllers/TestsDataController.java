package itmo.nick.controllers;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.entities.PersonTable;
import itmo.nick.database.PersonTableService;
import itmo.nick.database.repositories.PersonTableRepository;
import itmo.nick.person.Person;
import itmo.nick.person.PersonState;
import itmo.nick.test.attention.AttentionTestOne;
import itmo.nick.test.attention.AttentionTestOneData;
import itmo.nick.test.attention.AttentionTestTwo;
import itmo.nick.test.attention.AttentionTestTwoData;
import itmo.nick.test.memory.MemoryTestOne;
import itmo.nick.test.memory.MemoryTestOneData;
import itmo.nick.test.memory.MemoryTestTwo;
import itmo.nick.test.memory.MemoryTestTwoData;
import itmo.nick.test.processing.ProcessingTestOne;
import itmo.nick.test.processing.ProcessingTestOneData;
import itmo.nick.test.processing.ProcessingTestTwo;
import itmo.nick.test.processing.ProcessingTestTwoData;
import itmo.nick.test.reaction.ReactionTestOne;
import itmo.nick.test.reaction.ReactionTestOneData;
import itmo.nick.test.reaction.ReactionTestTwo;
import itmo.nick.test.reaction.ReactionTestTwoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static itmo.nick.test.attention.AttentionTestOne.getInstance;

/**
 * Контроллер данныз
 */
@Controller
public class TestsDataController {

    @Autowired
    private PersonTableService personTableService;
    @Autowired
    private ResultTableService resultTableService;

    /**
     * Получение данных регистрации участника
     * @param person участник
     */
    @PostMapping("/register")
    public String registerPerson(@RequestBody Person person) {
        //Сначала очистить старые данные, потом создать новую PersonTable
        PersonTable.delete();
        PersonTable.getInstance().setPerson(person);
        return "personStateAnalyze";
    }

    @PostMapping("/login")
    public String login(@RequestBody Person person) {
        //Если есть хотя бы одна запись с таким id
        if (!"0".equals(personTableService.getPersonTableRepository().findPersonById(person.getLoginId()))) {
            PersonTable.getInstance().setPerson_id(Long.valueOf(person.getLoginId()));
            return "personStateAnalyze";
        } else {
            //временная заплатка
            return "/r";
        }
    }

    /**
     * Получение данных состояния участника
     * @param personState состояние участника
     */
    @PostMapping("/stateAnalyze")
    public String personState(@RequestBody PersonState personState) {
        //Если регистрация, то как обычно, иначе же просто оставить PersonTable с id
        if (PersonTable.getInstance().getFio() != null) {
            PersonTable.getInstance().setPersonState(personState);
            personTableService.save(PersonTable.getInstance());
        }
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
     * Получение данных этапа теста на внимательность 2
     * @param data данные
     */
    @PostMapping("/attentionTestTwoStageData")
    public ResponseEntity<Void> attentionTestTwoStageData(@RequestBody AttentionTestTwoData data) {
        AttentionTestTwo attentionTestTwo = AttentionTestTwo.getInstance();
        attentionTestTwo.setData(data);
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
     * Получение данных этапа теста на реакцию 2
     * @param data данные
     */
    @PostMapping("/reactionTestTwoStageData")
    public ResponseEntity<Void> reactionTestTwoStageData(@RequestBody Double[] reactionTime) {
        ReactionTestTwo reactionTestTwo = ReactionTestTwo.getInstance();
        reactionTestTwo.addData(reactionTime);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение данных этапа теста на память 1
     * @param data данные
     */
    @PostMapping("/memoryTestOneStageData")
    public ResponseEntity<Void> memoryTestOneStageData(@RequestBody MemoryTestOneData data) {
        MemoryTestOne memoryTestOne = MemoryTestOne.getInstance();
        memoryTestOne.addData(data);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение данных этапа теста на память 2
     * @param data данные
     */
    @PostMapping("/memoryTestTwoStageData")
    public ResponseEntity<Void> memoryTestTwoStageData(@RequestBody MemoryTestTwoData data) {
        MemoryTestTwo memoryTestTwo = MemoryTestTwo.getInstance();
        memoryTestTwo.updateMap(data);
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

    /**
     * Получение данных этапа теста на обработку информации 2
     * @param data данные
     */
    @PostMapping("/processingTestTwoStageData")
    public ResponseEntity<Void> processingTestTwoStageData(@RequestBody ProcessingTestTwoData data) {
        ProcessingTestTwo processingTestTwo = ProcessingTestTwo.getInstance();
        processingTestTwo.addData(data);
        return ResponseEntity.ok().build();
    }
}

