package itmo.nick.controllers;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import itmo.nick.test.attention.AttentionTestOne;
import itmo.nick.test.memory.MemoryTestOne;
import itmo.nick.test.processing.ProcessingTestOne;
import itmo.nick.test.processing.ProcessingTestTwo;
import itmo.nick.test.reaction.ReactionTestOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static itmo.nick.test.attention.AttentionTestOne.getInstance;

/**
 * Контроллер показа тестов
 *
 * @author Николай Жмакин
 * @since 07.01.2025
 */
@Controller
public class TestsController {

	@Autowired
	private ResultTableService resultTableService;
	@Autowired
	private TestTableService testTableService;

	/**
	 * Тест на внимательность 1
	 * @param stage этап
	 */
	@GetMapping("/t/a/1")
	public String attentionTestOne(@RequestParam("s") int stage, Model model) {
		AttentionTestOne attentionTestOne = getInstance();
		stage = attentionTestOne.CorrectNextStage(stage);

		loadIfDescriptionStage(stage, model, 1);

		if (stage == AttentionTestOne.LAST_STAGE) {
			model.addAttribute("result", getOriginalResults(attentionTestOne, 1));
		}
		return "attention/attentionTestOneStage" + stage;
	}

	/**
	 * Тест на память 1
	 * @param stage этап
	 */
	@GetMapping("/t/m/1")
	public String memoryTestOne(@RequestParam("s") int stage, Model model) {
		MemoryTestOne memoryTestOne = MemoryTestOne.getInstance();
		stage = memoryTestOne.CorrectNextStage(stage);

		loadIfDescriptionStage(stage, model, 2);

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
			model.addAttribute("result", getOriginalResults(memoryTestOne, 2));
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

		loadIfDescriptionStage(stage, model, 3);

		if (stage == ReactionTestOne.LAST_STAGE) {
			if (!reactionTestOne.isFinished()) {
				reactionTestOne.getReactionTestOneDataList().remove(0); //первое ложное при старте
				resultTableService.saveTestThree(String.valueOf(reactionTestOne.getReactionTime()).substring(0,5),
					String.valueOf(reactionTestOne.getErrors()), reactionTestOne.getFalseStarts());
				reactionTestOne.setFinished(true);
			}
			model.addAttribute("result", getOriginalResults(reactionTestOne, 3));
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

		loadIfDescriptionStage(stage, model, 4);

		String numbers = processingTestOne.getNextNumbers();
		if ("-1".equals(numbers)) {
			stage = ProcessingTestOne.LAST_STAGE;
		} else {
			model.addAttribute("numbers", numbers);
		}

		if (stage == ProcessingTestOne.LAST_STAGE) {
			model.addAttribute("result", getOriginalResults(processingTestOne, 4));
			if (!processingTestOne.isFinished()) {
				resultTableService.saveTestFourth(processingTestOne);
			}
		}

		return "processing/processingTestOneStage" + stage;
	}

	/**
	 * Тест на обработку информации 2
	 * @param stage этап
	 */
	@GetMapping("/t/p/2")
	public String processingTestTwo(@RequestParam("s") int stage, Model model) {
		ProcessingTestTwo processingTestTwo = ProcessingTestTwo.getInstance();
		stage = processingTestTwo.CorrectNextStage(stage);

		loadIfDescriptionStage(stage, model, 5);

		String tone = processingTestTwo.getNextTone();
		if ("-1".equals(tone)) {
			stage = ProcessingTestOne.LAST_STAGE;
		} else {
			model.addAttribute("tone", tone);
		}

		if (stage == ProcessingTestOne.LAST_STAGE) {
			model.addAttribute("result", getOriginalResults(processingTestTwo, 5));
			if (!processingTestTwo.isFinished()) {
				//resultTableService.saveTestFive(processingTestTwo);
			}
		}
		return "processing/processingTestTwoStage" + stage;
	}

	/**
	 * Загрузить данные для описания, если страница с описанием
	 * @param stage этап
	 * @param model страница
	 * @param id идентификатор теста
	 */
	private void loadIfDescriptionStage(int stage, Model model, int id) {
		if (stage == SimpleTest.DESCRIPTION_STAGE) {
			model.addAttribute("desc", testTableService.getDescById(id));
			model.addAttribute("name", testTableService.getNameById(id));
		}
	}

	/**
	 * Получить результаты оригинального тестирования
	 * @param test наследник класса (сам тест)
	 * @param id номер теста
	 */
	private String getOriginalResults(SimpleTest test, int id) {
		return test.result(testTableService.getResultsById(id).split(";"));
	}
}