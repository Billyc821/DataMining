import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.HashMap;

import java.util.Map;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class modifier {

	public static void main(String[] args) throws Exception {

		String[] classes = { "aero_double-reed", "aero_free-reed", "aero_lip-vibrated", "aero_side", "aero_single-reed",
				"chrd_composite", "chrd_simple" };

		DataSource source = new DataSource("newData/PT4/Predictions/predictions.csv");
		Instances predictions = source.getDataSet();

		source = new DataSource("rawData/test.csv");
		Instances testData = source.getDataSet();

		source = new DataSource("rawdata/test-withPitch.csv");
		Instances pitch = source.getDataSet();

		StringBuilder builder = new StringBuilder();

		Map<String, Double> map = new HashMap<String, Double>();

		for (int i = 0; i < pitch.numInstances(); i++) {

			Double class2;

			for (int j = 0; j < pitch.numAttributes(); j++) {

				class2 = pitch.get(i).value(j);
				map.put(classes[j], class2);

			}

		}

		for (int i = 0; i < predictions.numInstances(); i++) {
			
			double max = 0;
			String predictedInstrument = "";
			for (int j = 0; j < predictions.numAttributes() - 1; j++) {
				if (predictions.instance(i).value(j) > max) {
					max = predictions.instance(i).value(j);
					predictedInstrument = predictions.attribute(j).name();

				}

			}

			if (max < 0.6) {

				if (testData.instance(i).stringValue(testData.numAttributes() - 1).equals("chrd_simple")) {

					if (testData.instance(i).value(testData.attribute(19)) > map.get("chrd_simple")) {
						predictedInstrument = "Piano";
					} else {
						predictedInstrument = "SynthBass";
					}

				}

				if (testData.instance(i).stringValue(testData.numAttributes() - 1).equals("aero_double-reed")) {

					if (testData.instance(i).value(testData.attribute(19)) > map.get("aero_double-reed")) {
						predictedInstrument = "Oboe";

					} else {
						predictedInstrument = "Bassoon";
					}

				}

				if (testData.instance(i).stringValue(testData.numAttributes() - 1).equals("aero_single-reed")) {

					if (testData.instance(i).value(testData.attribute(19)) > map.get("aero_single-reed") - 200) {
						predictedInstrument = "Clarinet";

					} else {
						predictedInstrument = "Saxophone";
					}

				}

				if (testData.instance(i).stringValue(testData.numAttributes() - 1).equals("aero_side")) {

					if (testData.instance(i).value(testData.attribute(19)) > map.get("aero_side")) {
						predictedInstrument = "Piccolo";

					} else {
						predictedInstrument = "Flute";
					}

				}

				if (testData.instance(i).stringValue(testData.numAttributes() - 1).equals("aero_lip-vibrated")) {

					predictedInstrument = "FrenchHorn";

					if (testData.instance(i).value(testData.attribute(19)) > map.get("aero_lip-vibrated") + 400) {
						predictedInstrument = "EnglishHorn";
					}

					if (testData.instance(i).value(testData.attribute(19)) < map.get("aero_lip-vibrated") - 100) {
						predictedInstrument = "Trumpet";
					}

					if (testData.instance(i).value(testData.attribute(19)) < map.get("aero_lip-vibrated") - 300) {
						predictedInstrument = "Trombone";
					}

				}

				if (testData.instance(i).stringValue(testData.numAttributes() - 1).equals("chrd_composite")) {

					predictedInstrument = "Cello";

					if (testData.instance(i).value(testData.attribute(19)) > map.get("chrd_composite") + 150) {
						predictedInstrument = "Violin";

						if (testData.instance(i).value(testData.attribute(19)) > map.get("chrd_composite") + 250) {
							predictedInstrument = "Viola";
						}

					} else {

						if (testData.instance(i).value(testData.attribute(19)) < map.get("chrd_composite") - 50) {
							predictedInstrument = "DoubleBass";
						}
					}

				}

			}

			if (testData.instance(i).stringValue(testData.numAttributes() - 1).equals("aero_free-reed")) {
				predictedInstrument = "Accordian";
			}

			for (int j = 0; j < 1; j++) {
				builder.append(predictedInstrument + "\n");// append to the output string

			}

		}

		BufferedWriter writer = new BufferedWriter(new FileWriter("newData/PT4/Predictions/predictionsNew.csv"));
		writer.write(builder.toString());// save the string representation of the board
		writer.close();

	}

}
