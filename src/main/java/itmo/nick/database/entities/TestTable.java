package itmo.nick.database.entities;

import itmo.nick.person.Person;
import itmo.nick.person.PersonState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

/**
 * Класс таблицы тестов в БД
 *
 * @author Николай Жмакин
 * @since 07.12.2024
 */

@Entity
@Table(name = "test")
@Getter
@Setter
@ToString
public class TestTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long test_id;

	private String test_name;
	private String test_start_desc;
	private String test_original_result;
	private String test_param_count;

	static TestTable testTable;

	public TestTable() {}

	public static TestTable getInstance() {
		if (testTable == null) {
			testTable = new TestTable();
			return testTable;
		} else {
			return testTable;
		}
	}
}
