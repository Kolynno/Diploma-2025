package itmo.nick.controllers;

import itmo.nick.database.ResultTableService;
import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import itmo.nick.test.attention.AttentionTestOne;
import itmo.nick.test.attention.AttentionTestTwo;
import itmo.nick.test.memory.MemoryTestOne;
import itmo.nick.test.memory.MemoryTestTwo;
import itmo.nick.test.processing.ProcessingTestOne;
import itmo.nick.test.processing.ProcessingTestTwo;
import itmo.nick.test.reaction.ReactionTestOne;
import itmo.nick.test.reaction.ReactionTestTwo;
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
		int testId = attentionTestOne.getTestId();

		if (resultTableService.getStatus(testId)) {
			stage = AttentionTestOne.LAST_STAGE;
		} else {
			stage = attentionTestOne.CorrectNextStage(stage);
		}

		loadIfDescriptionStage(stage, model, testId);

		if (stage == AttentionTestOne.LAST_STAGE) {
			model.addAttribute("result", getOriginalResults(attentionTestOne, testId));
		}
		return "attention/attentionTestOneStage" + stage;
	}

	/**
	 * Тест на внимательность 2
	 * @param stage этап
	 */
	@GetMapping("/t/a/2")
	public String attentionTestTwo(@RequestParam("s") int stage, Model model) {
		AttentionTestTwo attentionTestTwo = AttentionTestTwo.getInstance();
		int testId = attentionTestTwo.getTestId();

		if (resultTableService.getStatus(testId)) {
			stage = AttentionTestTwo.LAST_STAGE;
		} else {
			stage = attentionTestTwo.CorrectNextStage(stage);
		}

		loadIfDescriptionStage(stage, model, testId);

		if (stage == AttentionTestTwo.LAST_STAGE) {
			if (!attentionTestTwo.isFinished()) {
				resultTableService.saveTestSix(attentionTestTwo);
				attentionTestTwo.setFinished(true);
			}
			model.addAttribute("result", getOriginalResults(attentionTestTwo, testId));
		}
		return "attention/attentionTestTwoStage" + stage;
	}

	/**
	 * Тест на память 1
	 * @param stage этап
	 */
	@GetMapping("/t/m/1")
	public String memoryTestOne(@RequestParam("s") int stage, Model model) {
		MemoryTestOne memoryTestOne = MemoryTestOne.getInstance();
		int testId = memoryTestOne.getTestId();

		if (resultTableService.getStatus(testId)) {
			stage = MemoryTestOne.LAST_STAGE;
		} else {
			stage = memoryTestOne.CorrectNextStage(stage);
		}

		loadIfDescriptionStage(stage, model, testId);

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
			model.addAttribute("result", getOriginalResults(memoryTestOne, testId));
		}

		return "memory/memoryTestOneStage" + stage;
	}

	/**
	 * Тест на память 2
	 * @param stage этап
	 */
	@GetMapping("/t/m/2")
	public String memoryTestTwo(@RequestParam("s") int stage, Model model) {
		MemoryTestTwo memoryTestTwo = MemoryTestTwo.getInstance();
		int testId = memoryTestTwo.getTestId();

		if (resultTableService.getStatus(testId)) {
			stage = MemoryTestTwo.LAST_STAGE;
		} else {
			stage = memoryTestTwo.CorrectNextStage(stage);
		}

		loadIfDescriptionStage(stage, model, testId);

		model.addAttribute("words", memoryTestTwo.getWords());

		if (stage == MemoryTestTwo.LAST_STAGE) {
			if (!memoryTestTwo.isFinished()) {
				resultTableService.saveTestSeven(memoryTestTwo.getCorrectWords());
				memoryTestTwo.setFinished(true);
			}
			model.addAttribute("result", getOriginalResults(memoryTestTwo, testId));
		}

		return "memory/memoryTestTwoStage" + stage;
	}

	/**
	 * Тест на реацию 1
	 * @param stage этап
	 */
	@GetMapping("/t/r/1")
	public String reactionTestOne(@RequestParam("s") int stage, Model model) {
		ReactionTestOne reactionTestOne = ReactionTestOne.getInstance();
		int testId = reactionTestOne.getTestId();

		if (resultTableService.getStatus(testId)) {
			stage = ReactionTestOne.LAST_STAGE;
		} else {
			stage = reactionTestOne.CorrectNextStage(stage);
		}

		if (reactionTestOne.timeIsUp()) {
			stage = ReactionTestOne.LAST_STAGE;
		}

		loadIfDescriptionStage(stage, model, testId);

		if (stage == ReactionTestOne.LAST_STAGE) {
			if (!reactionTestOne.isFinished()) {
				reactionTestOne.getReactionTestOneDataList().remove(0); //первое ложное при старте
				resultTableService.saveTestThree(String.valueOf(reactionTestOne.getReactionTime()).substring(0,5),
					String.valueOf(reactionTestOne.getErrors()), reactionTestOne.getFalseStarts());
				reactionTestOne.setFinished(true);
			}
			model.addAttribute("result", getOriginalResults(reactionTestOne, testId));
		}

		return "reaction/reactionTestOneStage" + stage;
	}

	/**
	 * Тест на реацию 2
	 * @param stage этап
	 */
	@GetMapping("/t/r/2")
	public String reactionTestTwo(@RequestParam("s") int stage, Model model) {
		ReactionTestTwo reactionTestTwo = ReactionTestTwo.getInstance();
		int testId = reactionTestTwo.getTestId();

		if (resultTableService.getStatus(testId)) {
			stage = ReactionTestTwo.LAST_STAGE;
		} else {
			stage = reactionTestTwo.CorrectNextStage(stage);
		}

		loadIfDescriptionStage(stage, model, testId);

		if (stage == ReactionTestTwo.LAST_STAGE) {
			if (!reactionTestTwo.isFinished()) {
				resultTableService.saveTestEight(reactionTestTwo.getReactionTestTwoDataList());
				reactionTestTwo.setFinished(true);
			}
			model.addAttribute("result", getOriginalResults(reactionTestTwo, testId));
		}

		return "reaction/reactionTestTwoStage" + stage;
	}

	/**
	 * Тест на обработку информации 1
	 * @param stage этап
	 */
	@GetMapping("/t/p/1")
	public String processingTestOne(@RequestParam("s") int stage, Model model) {
		ProcessingTestOne processingTestOne = ProcessingTestOne.getInstance();
		int testId = processingTestOne.getTestId();

		if (resultTableService.getStatus(testId)) {
			stage = ProcessingTestOne.LAST_STAGE;
		} else {
			stage = processingTestOne.CorrectNextStage(stage);
		}

		loadIfDescriptionStage(stage, model, testId);

		String numbers = processingTestOne.getNextNumbers();
		if ("-1".equals(numbers)) {
			stage = ProcessingTestOne.LAST_STAGE;
		} else {
			model.addAttribute("numbers", numbers);
		}

		if (stage == ProcessingTestOne.LAST_STAGE) {
			model.addAttribute("result", getOriginalResults(processingTestOne, testId));
			if (!processingTestOne.isFinished()) {
				resultTableService.saveTestFourth(processingTestOne);
				processingTestOne.setFinished(true);
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
		int testId = processingTestTwo.getTestId();

		if (resultTableService.getStatus(testId)) {
			stage = ProcessingTestTwo.LAST_STAGE;
		} else {
			stage = processingTestTwo.CorrectNextStage(stage);
		}

		loadIfDescriptionStage(stage, model, testId);

		String tone = "5";
		if (stage == 1) {
			tone = processingTestTwo.getNextTone();
		}
		if ("-1".equals(tone)) {
			stage = ProcessingTestOne.LAST_STAGE;
		} else {
			model.addAttribute("tone", tone);
		}

		if (stage == ProcessingTestOne.LAST_STAGE) {
			model.addAttribute("result", getOriginalResults(processingTestTwo, testId));
			if (!processingTestTwo.isFinished()) {
				resultTableService.saveTestFive(processingTestTwo);
				processingTestTwo.setFinished(true);
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