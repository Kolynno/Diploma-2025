package itmo.nick.reports;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Настройки шрифта, цвета, фона и таблиц документа
 *
 * @author Николай Жмакин
 * @since 02.03.2025
 */
@Service
public class PDFTextSettings {

	private final static String FONT = "src/main/resources/static/files/reportFont/times.ttf";

	/**
	 * @return Главный заголовок
	 */
	public Font mainHeaderFont() {
		return fontCreator(FONT,BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLD, BaseColor.BLACK);
	}

	/**
	 * @return Основной заголовок
	 */
	public Font mainTitleFont() {
		return fontCreator(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.BOLD, BaseColor.BLACK);
	}

	/**
	 * @return Основной текст
	 */
	public Font mainTextFont() {
		return fontCreator(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.BLACK);
	}

	private Font fontCreator(String fontName, String encoding, boolean baseFont, int fontSize, int fontStyle, BaseColor fontColor) {
		try {
			return new Font(BaseFont.createFont(fontName, encoding, baseFont), fontSize, fontStyle, fontColor);
		} catch (DocumentException | IOException e) {
			throw new RuntimeException(e);
		}
	}


}

