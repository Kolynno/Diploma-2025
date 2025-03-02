package itmo.nick.reports;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import org.springframework.stereotype.Service;

/**
 * Настройки шрифта, цвета, фона и таблиц документа
 *
 * @author Николай Жмакин
 * @since 02.03.2025
 */
@Service
public class PDFTextSettings {

	/**
	 * @return Главный заголовок
	 */
	public Font mainHeaderFont() {
		return FontFactory.getFont("fonts/times.ttf", 16, 1, BaseColor.BLACK);
	}

	/**
	 * @return Основной заголовок
	 */
	public Font mainTitleFont() {
		return FontFactory.getFont("fonts/times.ttf", 14, 1, BaseColor.BLACK);
	}

	/**
	 * @return Основной текст
	 */
	public Font mainTextFont() {
		return FontFactory.getFont("fonts/times.ttf", 12, BaseColor.BLACK);
	}



}

