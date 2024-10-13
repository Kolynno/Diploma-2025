package itmo.nick.person;

import lombok.*;
import java.util.Date;

/**
 * Участник тестирования
 *
 * @author Николай Жмакин
 * @since 13.10.2024
 */
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@ToString
public class Person {
	private final String firstname;
	private final String lastname;
	private final String middlename;
	private final Date birthday;
	private final String sex;
	private final String education;
}
