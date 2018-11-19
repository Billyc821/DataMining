import java.io.BufferedWriter;
import java.io.FileWriter;  
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;
import weka.classifiers.functions.SimpleLogistic;


public class SimpleLogisticClassifier {

	public static void main(String[] args) throws Exception {

		String[] instruments = { "Accordian", "Clarinet", "Trumpet", "DoubleBass", "Oboe", "Piano", "Saxophone",
				"Violin", "Cello", "Tuba", "Viola", "Trombone" };

		final String[][] csvFile = new String[11001][instruments.length];

		int count = 0;

		for (String instrument : instruments) {
			System.out.println(instrument);
			DataSource trainSource = new DataSource("newData/PT4/" + instrument + ".arff");
			DataSource testSource = new DataSource("newData/PT4/test/testWith" + instrument + ".arff");

			Instances train = trainSource.getDataSet();
			Instances test = testSource.getDataSet();

			train.setClassIndex(train.numAttributes() - 1);

			SMOTE smote = new SMOTE();
			smote.setInputFormat(train);
			Instances smote_data;

			switch (instrument) {

			case ("Accordian"):
				smote.setPercentage(670);

			case ("Cello"):
				smote.setPercentage(1500);

			case ("Clarinet"):
				smote.setPercentage(720);

			case ("DoubleBass"):
				smote.setPercentage(660);

			case ("Saxophone"):
				smote.setPercentage(20);

			case ("Oboe"):
				smote.setPercentage(350);

			case ("Trumpet"):
				smote.setPercentage(180);

			case ("Tuba"):
				smote.setPercentage(920);

			case ("Viola"):
				smote.setPercentage(840);

			case ("Violin"):
				smote.setPercentage(1950);

			case ("Piano"):
				smote.setPercentage(800);

			default:
				smote.setPercentage(0);

			}

			smote_data = Filter.useFilter(train, smote);

			smote_data.setClassIndex(smote_data.numAttributes() - 1);
			test.setClassIndex(test.numAttributes() - 1);

			Classifier cls = new SimpleLogistic();

//			Evaluation eval = new Evaluation(smote_data);
//			eval.crossValidateModel(cls, smote_data, 10, new Random(1));
//			System.out.println(eval.toSummaryString("\nResults\n======\n", false));

			cls.buildClassifier(smote_data);

			csvFile[0][count] = instrument;

			for (int j = 0; j < test.numInstances(); j++) {

				double[] clsLabel = cls.distributionForInstance(test.get(j));
				csvFile[j + 1][count] = Double.toString(clsLabel[1]);

			}

			count++;
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < csvFile.length; i++) {
			for (int j = 0; j < csvFile[0].length; j++) {
				builder.append(csvFile[i][j] + "");
				if (j < csvFile[0].length - 1)
					builder.append(",");
			}
			builder.append("\n");
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter("newData/PT4/Predictions/predictions.csv"));
		writer.write(builder.toString());
		writer.close();

	}

}
