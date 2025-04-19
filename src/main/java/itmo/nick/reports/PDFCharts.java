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
		LinkedList<JFreeChart> barCharts = createAllDataCharts(allPersonData);
		saveChartsToFiles(barCharts, 1);
		addAllDataCharts(document, 1, 4);
	}

	/**
	 * Создает графики
	 * @param allPersonData - данные для графиков конкретного теста
	 * @return список графиков
	 */
	private LinkedList<JFreeChart> createAllDataCharts(LinkedList<String> allPersonData) {
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
	private void saveChartsToFiles(LinkedList<JFreeChart> barCharts, int startNumber) {
		int chartNumber = startNumber;
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
	private  void addAllDataCharts(Document document, int startNumber, int endNumber) throws DocumentException {
		for (int i = startNumber; i <= endNumber; i++) {
			Image chartImage = null;
			try {
				chartImage = Image.getInstance(CHARTS_PATH + CHARTS_DIR + "chart" + i + ".png");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			chartImage.scaleToFit(300, 250);
			chartImage.setAlignment(Element.ALIGN_CENTER);
			document.add(chartImage);
		}
	}


	/**
	 * @param document pdf-файл
	 * @param allBestData значение для таблички "Лучшее значение"
	 */
	public void addAllBestCharts(Document document, LinkedList<String> allBestData) throws DocumentException {
		LinkedList<JFreeChart> barCharts = createBestDataCharts(allBestData);
		saveChartsToFiles(barCharts, 5);
		addAllDataCharts(document, 5, 8);
	}

	private LinkedList<JFreeChart> createBestDataCharts(LinkedList<String> allBestData) {
		DefaultCategoryDataset p1Dataset = new DefaultCategoryDataset();
		p1Dataset.addValue(Double.parseDouble(allBestData.get(0)), "П1Л", "");
		p1Dataset.addValue(Double.parseDouble(allBestData.get(1)), "П1Э", "");

		DefaultCategoryDataset p2Dataset = new DefaultCategoryDataset();
		p2Dataset.addValue(Double.parseDouble(allBestData.get(2)), "П2Л", "");
		p2Dataset.addValue(Double.parseDouble(allBestData.get(3)), "П2Э", "");

		DefaultCategoryDataset p3Dataset = new DefaultCategoryDataset();
		p3Dataset.addValue(Double.parseDouble(allBestData.get(4)), "П3Л", "");
		p3Dataset.addValue(Double.parseDouble(allBestData.get(5)), "П3Э", "");

		DefaultCategoryDataset p4Dataset = new DefaultCategoryDataset();
		p4Dataset.addValue(Double.parseDouble(allBestData.get(6)), "П4Л", "");
		p4Dataset.addValue(Double.parseDouble(allBestData.get(7)), "П4Э", "");

		JFreeChart p1Chart = ChartFactory.createBarChart(
			"Сравнение лучших и эталонных значений П1",
			"",
			"Значение",
			p1Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		JFreeChart p2Chart = ChartFactory.createBarChart(
			"Сравнение лучших и эталонных значений П2",
			"",
			"Значение",
			p2Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		JFreeChart p3Chart = ChartFactory.createBarChart(
			"Сравнение лучших и эталонных значений П3",
			"",
			"Значение",
			p3Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		JFreeChart p4Chart = ChartFactory.createBarChart(
			"Сравнение лучших и эталонных значений П4",
			"",
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
	 * Создание графиков сравнения
	 */
	public void addAllCompareCharts(Document document, LinkedList<String> allCompareData) throws DocumentException {
		LinkedList<JFreeChart> barCharts = createCompareDataCharts(allCompareData);
		saveChartsToFiles(barCharts, 9);
		addAllDataCharts(document, 9, 12);
	}

	private LinkedList<JFreeChart> createCompareDataCharts(LinkedList<String> allCompareData) {
		DefaultCategoryDataset p1Dataset = new DefaultCategoryDataset();
		p1Dataset.addValue(Double.parseDouble(allCompareData.get(0)), "П1С%", "");
		p1Dataset.addValue(Double.parseDouble(allCompareData.get(1)), "П1Э%", "");

		DefaultCategoryDataset p2Dataset = new DefaultCategoryDataset();
		p2Dataset.addValue(Double.parseDouble(allCompareData.get(2)), "П2С%", "");
		p2Dataset.addValue(Double.parseDouble(allCompareData.get(3)), "П2Э%", "");

		DefaultCategoryDataset p3Dataset = new DefaultCategoryDataset();
		p3Dataset.addValue(Double.parseDouble(allCompareData.get(4)), "П3С%", "");
		p3Dataset.addValue(Double.parseDouble(allCompareData.get(5)), "П3Э%", "");

		DefaultCategoryDataset p4Dataset = new DefaultCategoryDataset();
		p4Dataset.addValue(Double.parseDouble(allCompareData.get(6)), "П4С%", "");
		p4Dataset.addValue(Double.parseDouble(allCompareData.get(7)), "П4Э%", "");

		JFreeChart p1Chart = ChartFactory.createBarChart(
			"Сравнение в % между результатами участника и других (П1С%), и эталонных (П1Э%)",
			"",
			"Значение",
			p1Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		JFreeChart p2Chart = ChartFactory.createBarChart(
			"Сравнение в % между результатами участника и других (П2С%), и эталонных (П2Э%)",
			"",
			"Значение",
			p2Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		JFreeChart p3Chart = ChartFactory.createBarChart(
			"Сравнение в % между результатами участника и других (П3С%), и эталонных (П3Э%)",
			"",
			"Значение",
			p3Dataset,
			PlotOrientation.VERTICAL,
			true, true, false
		);

		JFreeChart p4Chart = ChartFactory.createBarChart(
			"Сравнение в % между результатами участника и других (П4С%), и эталонных (П4Э%)",
			"",
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
}
