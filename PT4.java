import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;

public class PT4 {

	public static void main(String[] args) throws Exception {

		DataSource trainSource = new DataSource("rawdata/Final files/train3.csv");
		DataSource testSource = new DataSource("rawdata/Final files/test3.csv");

		String[] instruments = { "Accordian", "Clarinet", "Trumpet", "DoubleBass", "Oboe", "Piano", "Saxophone",
				"Violin", "Cello", "Tuba", "Viola", "Trombone" };

		Instances train = trainSource.getDataSet();
		Instances test = testSource.getDataSet();

		for (String instrument : instruments) {

			Add addFilter = new Add();
			addFilter.setAttributeIndex("last");
			addFilter.setNominalLabels("false, true");
			addFilter.setAttributeName("is" + instrument);
			addFilter.setInputFormat(train);
			Instances newData = Filter.useFilter(train, addFilter);

			addFilter.setAttributeIndex("last");
			addFilter.setNominalLabels("false, true");
			addFilter.setAttributeName("is" + instrument);
			addFilter.setInputFormat(test);
			Instances newData2 = Filter.useFilter(test, addFilter);

			for (int i = 0; i < newData.numInstances(); i++) {

				String rows = newData.instance(i).toString();

				if (rows.toLowerCase().contains(instrument.toLowerCase())) {

					newData.instance(i).setValue(newData.numAttributes() - 1, "true");

				} else {
					newData.instance(i).setValue(newData.numAttributes() - 1, "false");

				}

			}

			Remove removeFilter = new Remove();
			String toDelete = Integer.toString(newData.numAttributes() - 2) + ","
					+ Integer.toString(newData.numAttributes() - 1);
			removeFilter.setAttributeIndices(toDelete);
			removeFilter.setInvertSelection(false);
			removeFilter.setInputFormat(newData);
			Instances noData = Filter.useFilter(newData, removeFilter);

			Remove removeFilter2 = new Remove();
			String toDelete2 = Integer.toString(newData2.numAttributes() - 5) + ","
					+ Integer.toString(newData2.numAttributes() - 4) + ","
					+ Integer.toString(newData2.numAttributes() - 3) + ","
					+ Integer.toString(newData2.numAttributes() - 2) + ","
					+ Integer.toString(newData2.numAttributes() - 1);
			removeFilter2.setAttributeIndices(toDelete2);
			removeFilter2.setInvertSelection(false);
			removeFilter2.setInputFormat(newData2);
			Instances noData2 = Filter.useFilter(newData2, removeFilter2);

			Instances dataSet = noData;
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataSet);
			saver.setFile(new File("newdata/PT4/" + instrument + ".arff"));
			saver.writeBatch();

			Instances dataSet2 = noData2;
			ArffSaver saver2 = new ArffSaver();
			saver2.setInstances(dataSet2);
			saver2.setFile(new File("newdata/PT4/test/testWith" + instrument + ".arff"));
			saver2.writeBatch();

		}

	}

}
