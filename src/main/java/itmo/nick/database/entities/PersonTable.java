package itmo.nick.database.entities;

import itmo.nick.person.Person;
import itmo.nick.person.PersonState;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Класс таблицы участника в БД
 *
 * @author Николай Жмакин
 * @since 01.12.2024
 */

@Entity
@Table(name = "person")
@Getter
@Setter
@ToString
public class PersonTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long person_id;
	private String fio;
	@Temporal(TemporalType.DATE)
	private Date birthday;
	private String sex;
	private String education;
	private String joy;
	private String calm;
	private String dominance;

	static PersonTable personTable;

	public PersonTable() {}

	public static PersonTable getInstance() {
		if (personTable == null) {
			personTable = new PersonTable();
			return personTable;
		} else {
			return personTable;
		}
	}

	public void setPerson(Person person) {
		this.fio = person.getLastname() + " " + person.getFirstname() + " " + person.getMiddlename();
		this.birthday = person.getBirthday();
		this.sex = person.getSex();
		this.education = person.getEducation();
	}
	public void setPersonState(PersonState personState) {
		this.joy = personState.getJoy();
		this.calm = personState.getCalm();
		this.dominance = personState.getDominance();
	}
}
