package itmo.nick.controllers;

import itmo.nick.database.TestTableService;
import itmo.nick.database.entities.PersonTable;
import itmo.nick.database.repositories.PersonTableRepository;
import itmo.nick.test.SimpleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static itmo.nick.test.SimpleTest.deleteTestsData;

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
	@Autowired
	private PersonTableRepository personTableRepository;

	/**
	 * Страница регистрации
	 */
	@GetMapping("/r")
	public String registration() {
		deleteTestsData();
		return "registration";
	}

	/**
	 * Страница оценки состояния
	 */
	@GetMapping("/s")
	public String personStateAnalyze(Model model) {
		model.addAttribute("title", getTitle());
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
		model.addAttribute("title", getTitle());
		return "testsPage";
	}

	private String getTitle() {
		if (PersonTable.getInstance().getPerson_id() != null) {
			String code = String.valueOf(PersonTable.getInstance().getPerson_id());
			return personTableRepository.getFioInitials(code) + "[Код:" + code + "]";
		} else {
			//При регистрации id может быть еще null, поэтому без него. А вот ФИО доступно через класс, а не через БД
			return PersonTable.getInstance().getFioInitials();
		}
	}
}
