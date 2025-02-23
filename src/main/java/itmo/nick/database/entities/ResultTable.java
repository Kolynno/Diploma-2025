package itmo.nick.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

/**
 * Класс таблицы результатов в БД
 *
 * @author Николай Жмакин
 * @since 07.12.2024
 */

@Entity
@Table(name = "result")
@Getter
@Setter
@ToString
public class ResultTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long result_id;
	private String person_id;
	private String test_id;
	private LocalDate result_date;

	private String p1;
	private String p2;
	private String p3;
	private String p4;
	private String p5;

	static ResultTable resultTable;

	public ResultTable() {}

	public static ResultTable getInstance() {
		if (resultTable == null) {
			resultTable = new ResultTable();
			return resultTable;
		} else {
			return resultTable;
		}
	}
}
