package itmo.nick.reports;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Создание графиков
 *
 * @author Николай Жмакин
 * @since 13.04.2025
 */
@Service
public class PDFCharts {

	public static final int CHART_WIDTH_IN_DOCUMENT = 640;
	public static final int CHART_HEIGHT_TO_DOCUMENT = 480;

	public static final String CHARTS_PATH = "src/main/resources/static/files/charts/";
	public static String CHARTS_DIR = "test1/";

	/**
	 * @param document pdf-файл
	 * @param allPersonData значение для таблички "Все данные по датам"
	 */
	public void addAllDataCharts(Document document, LinkedList<String> allPersonData) throws DocumentException {
		LinkedList<JFreeChart> barCharts = createCharts(allPersonData);
		saveChartsToFiles(barCharts);
		addAllDataCharts(document);
	}

	/**
	 * @param document pdf-файл
	 * @param allBestData значение для таблички "Лучшее значение"
	 */
	public void addAllBestCharts(Document document, LinkedList<String> allBestData) throws DocumentException {
		LinkedList<JFreeChart> barCharts = createCharts(allBestData);
		saveChartsToFiles(barCharts);
		addAllDataCharts(document);
	}

	/**
	 * Создает графики
	 * @param allPersonData - данные для графиков конкретного теста
	 * @return список графиков
	 */
	private LinkedList<JFreeChart> createCharts(LinkedList<String> allPersonData) {
		DefaultCategoryDataset p1Dataset = new DefaultCategoryDataset();
		p1Dataset.addValue(Double.parseDouble(allPersonData.get(2)), "П1", allPersonData.get(1));
		p1Dataset.addValue(Double.parseDouble(allPersonData.get(8)), "П1", allPersonData.get(7));
		p1Dataset.addValue(Double.parseDouble(allPersonData.get(14)), "П1", allPersonData.get(13));

		DefaultCategoryDataset p2Dataset = new DefaultCategoryDataset();
		p2Dataset.addValue(Double.parseDouble(allPersonData.get(3)), "П2", allPersonData.get(1));
		p2Dataset.addValue(Double.parseDouble(allPersonData.get(9)), "П2", allPersonData.get(7));
		p2Dataset.addValue(Double.parseDouble(allPersonData.get(15)), "П2", allPersonData.get(13));

		DefaultCategoryDataset p3Dataset = new DefaultCategoryDataset();
		p3Dataset.addValue(Double.parseDouble(allPersonData.get(4)), "П3", allPersonData.get(1));
		p3Dataset.addValue(Double.parseDouble(allPersonData.get(10)), "П3", allPersonData.get(7));
		p3Dataset.addValue(Double.parseDouble(allPersonData.get(16)), "П3", allPersonData.get(13));

		DefaultCategoryDataset p4Dataset = new DefaultCategoryDataset();
		p4Dataset.addValue(Double.parseDouble(allPersonData.get(5)), "П4", allPersonData.get(1));
		p4Dataset.addValue(Double.parseDouble(allPersonData.get(11)), "П4", allPersonData.get(7));
		p4Dataset.addValue(Double.parseDouble(allPersonData.get(17)), "П4", allPersonData.get(13));

		JFreeChart p1Chart = ChartFactory.createBarChart(
			"Результаты П1 по датам",
			"Дата",
			"Значение",
			p1Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		JFreeChart p2Chart = ChartFactory.createBarChart(
			"Результаты П2 по датам",
			"Дата",
			"Значение",
			p2Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		JFreeChart p3Chart = ChartFactory.createBarChart(
			"Результаты П3 по датам",
			"Дата",
			"Значение",
			p3Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		JFreeChart p4Chart = ChartFactory.createBarChart(
			"Результаты П4 по датам",
			"Дата",
			"Значение",
			p4Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		LinkedList<JFreeChart> barCharts = new LinkedList<>();
		barCharts.add(p1Chart);
		barCharts.add(p2Chart);
		barCharts.add(p3Chart);
		barCharts.add(p4Chart);
		return barCharts;
	}

	/**
	 * Сохраняет графики на диск
	 * @param barCharts все созданные графики
	 */
	private void saveChartsToFiles(LinkedList<JFreeChart> barCharts) {
		int chartNumber = 1;
		for (JFreeChart chart : barCharts) {
			File chartFile = new File(CHARTS_PATH + CHARTS_DIR + "chart" + chartNumber + ".png");
			chartNumber++;
			try {
				ChartUtils.saveChartAsPNG(chartFile, chart, CHART_WIDTH_IN_DOCUMENT, CHART_HEIGHT_TO_DOCUMENT);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Берет все картники графиков с диска и вставляет в документ
	 */
	private  void addAllDataCharts(Document document) throws DocumentException {
		int chartsCount =
			Objects.requireNonNull(new File(CHARTS_PATH + CHARTS_DIR).list()).length;

		for (int i = 1; i <= chartsCount; i++) {
			Image chartImage = null;
			try {
				chartImage = Image.getInstance(CHARTS_PATH + CHARTS_DIR + "chart" + i + ".png");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			chartImage.scaleToFit(500, 400);
			chartImage.setAlignment(Element.ALIGN_CENTER);
			document.add(chartImage);
		}
	}
}
