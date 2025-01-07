package itmo.nick.controllers;

import itmo.nick.database.TestTableService;
import itmo.nick.test.SimpleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static itmo.nick.test.SimpleTest.deleteTestAndData;

/**
 * Класс контроллер обработки основых страниц
 *
 * @author Николай Жмакин
 * @since 07.01.2025
 */
@Controller
public class MainPageController {
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
}
