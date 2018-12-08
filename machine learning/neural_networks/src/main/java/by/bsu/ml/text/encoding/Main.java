package by.bsu.ml.text.encoding;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main implements LearningEventListener {
    public static final int NOISE_SIZE = 7;//6
    public static final int LETTER_TRAINING_COUNT = 200;//20
    public static final int LETTER_LENGTH = 48;
    public static final int LETTER_WIDTH = 6;
    public static final int LETTER_HEIGHT = LETTER_LENGTH / LETTER_WIDTH;
    private static HashMap<String, List<Double>> letterValue = new LinkedHashMap<>(26);

    private static Map<List<Double>, String> valueLetter;


    static {
        letterValue.put("A",
                        Arrays.asList(0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 1d,
                                      0d, 0d, 1d, 0d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d,
                                      0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d));
        letterValue.put("B",
                        Arrays.asList(1d, 1d, 1d, 1d, 0d, 0d, 1d, 0d, 0d, 0d, 1d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 1d,
                                      1d, 1d, 1d, 0d, 1d, 0d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d,
                                      0d, 1d, 1d, 1d, 1d, 1d, 1d, 0d));
        letterValue.put("C",
                        Arrays.asList(0d, 0d, 1d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d,
                                      0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 0d, 1d, 0d, 0d,
                                      1d, 0d, 0d, 0d, 1d, 1d, 0d, 0d));
        letterValue.put("D",
                        Arrays.asList(1d, 1d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d,
                                      0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 1d,
                                      1d, 0d, 1d, 1d, 1d, 0d, 0d, 0d));
        letterValue.put("E",
                        Arrays.asList(1d, 1d, 1d, 1d, 1d, 1d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 1d,
                                      1d, 1d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d,
                                      0d, 0d, 1d, 1d, 1d, 1d, 1d, 1d));
        letterValue.put("F",
                        Arrays.asList(1d, 1d, 1d, 1d, 1d, 1d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 1d,
                                      1d, 1d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d,
                                      0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d));
        letterValue.put("G",
                        Arrays.asList(0d, 0d, 1d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d,
                                      0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 1d, 1d, 0d, 1d, 0d, 0d,
                                      1d, 1d, 0d, 0d, 1d, 1d, 0d, 1d));
        letterValue.put("H",
                        Arrays.asList(1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 1d,
                                      1d, 1d, 1d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d,
                                      0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d));
        letterValue.put("I",
                        Arrays.asList(1d, 1d, 1d, 1d, 1d, 1d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d,
                                      1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d,
                                      0d, 0d, 1d, 1d, 1d, 1d, 1d, 1d));
        letterValue.put("J",
                        Arrays.asList(0d, 1d, 1d, 1d, 1d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d,
                                      0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 1d,
                                      0d, 0d, 0d, 1d, 1d, 1d, 0d, 0d));
        letterValue.put("K",
                        Arrays.asList(1d, 0d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 1d, 1d,
                                      0d, 0d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 0d,
                                      1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d));
        letterValue.put("L",
                        Arrays.asList(1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d,
                                      0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d,
                                      0d, 0d, 1d, 1d, 1d, 1d, 1d, 1d));
        letterValue.put("M",
                        Arrays.asList(0d, 1d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 1d, 0d, 1d, 1d, 0d, 1d, 1d, 0d,
                                      1d, 1d, 0d, 1d, 1d, 0d, 1d, 1d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d,
                                      0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d));
        letterValue.put("N",
                        Arrays.asList(1d, 0d, 0d, 0d, 0d, 1d, 1d, 1d, 0d, 0d, 0d, 1d, 1d, 0d, 1d, 0d, 0d, 1d, 1d, 0d,
                                      0d, 1d, 0d, 1d, 1d, 0d, 0d, 1d, 0d, 1d, 1d, 0d, 0d, 0d, 1d, 1d, 1d, 0d, 0d, 0d,
                                      1d, 1d, 1d, 0d, 0d, 0d, 0d, 1d));
        letterValue.put("O",
                        Arrays.asList(0d, 0d, 1d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d,
                                      0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 0d, 1d, 0d, 0d,
                                      1d, 0d, 0d, 0d, 1d, 1d, 0d, 0d));
        letterValue.put("P",
                        Arrays.asList(1d, 1d, 1d, 1d, 0d, 0d, 1d, 0d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d,
                                      0d, 0d, 1d, 1d, 1d, 1d, 1d, 1d, 0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 0d, 0d, 0d,
                                      0d, 0d, 1d, 0d, 0d, 0d, 0d, 0d));
        letterValue.put("Q",
                        Arrays.asList(0d, 0d, 1d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d,
                                      0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 1d, 0d, 1d, 0d, 1d, 0d, 0d,
                                      1d, 0d, 0d, 0d, 1d, 1d, 0d, 1d));
        letterValue.put("R",
                        Arrays.asList(1d, 1d, 1d, 1d, 0d, 0d, 1d, 0d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d,
                                      0d, 0d, 1d, 1d, 1d, 1d, 1d, 1d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 1d,
                                      0d, 0d, 1d, 0d, 0d, 0d, 1d, 0d));
        letterValue.put("S",
                        Arrays.asList(0d, 1d, 1d, 1d, 1d, 0d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 0d, 1d, 1d,
                                      0d, 0d, 0d, 0d, 0d, 0d, 1d, 1d, 1d, 0d, 0d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d,
                                      0d, 1d, 0d, 1d, 1d, 1d, 1d, 0d));
        letterValue.put("T",
                        Arrays.asList(1d, 1d, 1d, 1d, 1d, 1d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d,
                                      1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d,
                                      0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d));
        letterValue.put("U",
                        Arrays.asList(1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d,
                                      0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d,
                                      1d, 1d, 0d, 1d, 1d, 1d, 0d, 1d));
        letterValue.put("V",
                        Arrays.asList(1d, 0d, 0d, 0d, 0d, 1d, 1d, 1d, 0d, 0d, 1d, 1d, 1d, 1d, 0d, 0d, 1d, 1d, 0d, 1d,
                                      0d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d,
                                      0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d));
        letterValue.put("W",
                        Arrays.asList(1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d,
                                      1d, 1d, 0d, 1d, 1d, 0d, 1d, 1d, 0d, 1d, 1d, 0d, 1d, 1d, 0d, 1d, 0d, 1d, 0d, 0d,
                                      1d, 0d, 0d, 1d, 0d, 0d, 1d, 0d));
        letterValue.put("X",
                        Arrays.asList(1d, 0d, 0d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d,
                                      1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 1d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 0d,
                                      0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d));
        letterValue.put("Y",
                        Arrays.asList(1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 0d, 1d, 0d, 0d, 1d, 0d, 0d, 0d,
                                      1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d,
                                      0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d));
        letterValue.put("Z",
                        Arrays.asList(1d, 1d, 1d, 1d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d,
                                      1d, 1d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 1d, 1d, 0d, 0d, 0d, 0d, 1d, 1d, 0d, 0d,
                                      0d, 0d, 1d, 1d, 1d, 1d, 1d, 1d));

        valueLetter = letterValue.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        new Main().run();
    }

    public void run() throws IOException, URISyntaxException {
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(48, 48, 48);
((MultiLayerPerceptron) neuralNetwork).connectInputsToOutputs();
        LearningRule learningRule = neuralNetwork.getLearningRule();

        learningRule.addListener(this);

        System.out.println("Creating noised dataset");
        DataSet dataSet = getNoisedDataSet(NOISE_SIZE, LETTER_TRAINING_COUNT);
        System.out.println("Learning network");
        neuralNetwork.learn(dataSet);
        System.out.println("Learning is completed");

        StringBuilder decodedText = getEncodedText(neuralNetwork, "Neural_network_homework.txt");

        System.out.println(decodedText);
    }

    private static StringBuilder getEncodedText(final NeuralNetwork neuralNetwork, String filePath) throws IOException, URISyntaxException {
        Path encodedText = Paths.get(Thread.currentThread().getContextClassLoader().getResource(filePath).toURI());
        StringBuilder decodedText = new StringBuilder();
        try (Stream<String> textItems = Files.lines(encodedText)) {
            textItems.forEach(encodedWord -> {
//                System.out.println("Read : \"" + encodedWord + "\"");
                if (encodedWord.length() > 1) {
                    double[] numberRepresentation = convertNumbersToDoubleArray(encodedWord);

//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    vizualize(Arrays.asList(ArrayUtils.toObject(numberRepresentation)));

                    System.out.println();
                    System.out.println("||||||");
                    System.out.println("VVVVVV");

                    double[] result = getNeuralNetworkResult(neuralNetwork, numberRepresentation);

                    String letter = getLetter(result);
                    if (letter != null) {
                        decodedText.append(letter);
                    }
                } else {
                    decodedText.append(encodedWord);
                }
            });
        }
        return decodedText;
    }

    private static double[] convertNumbersToDoubleArray(final String encodedWord) {
        double[] result = new double[LETTER_LENGTH];
        String[] digit = encodedWord.split("");
        for (int i = 0; i < encodedWord.length(); i++) {
            result[i] = Double.parseDouble(digit[i]);
        }

        if (result.length != LETTER_LENGTH) {
            result[LETTER_LENGTH - 1] = 1;
        }

        return result;
    }

    private static DataSet getNoisedDataSet(final int noiseSize, final int noiseCount) {
        DataSet dataSet = new DataSet(LETTER_LENGTH, LETTER_LENGTH);
        //make noise
        try (FileWriter fileWriter = new FileWriter(
                "D:\\workspace\\bybsutexttencoding\\src\\main\\resources\\nosiedMapping.csv");
             FileWriter vizualWriter = new FileWriter(
                     "D:\\workspace\\bybsutexttencoding\\src\\main\\resources\\visualization.txt")) {


            try (PrintWriter printWriter = new PrintWriter(fileWriter);
                 PrintWriter visualPrintWriter = new PrintWriter(vizualWriter)) {
                letterValue.forEach((letter, expected) -> {
//            vizualize(expected);


                    for (int j = 0; j < noiseCount; j++) {
                        List<Double> noisedLetterNumbers = getNoisedLetterNumbers(expected, noiseSize);

                        String csvString = StringUtils.join(noisedLetterNumbers, " ")
                                + ","
                                + StringUtils.join(expected, " ");

                        dataSet.addRow(noisedLetterNumbers.stream().mapToDouble(Double::doubleValue).toArray(),
                                       expected.stream().mapToDouble(Double::doubleValue).toArray());

                        printWriter.println(csvString);
//                        vizualize(noisedLetterNumbers);
                        visualPrintWriter.print(geetVizualizationString(noisedLetterNumbers));

                    }

                    String coorectValue = StringUtils.join(expected, " ")
                            + ","
                            + StringUtils.join(expected, " ");

                    printWriter.println(coorectValue);
                    visualPrintWriter.print(geetVizualizationString(expected));
                    dataSet.addRow(expected.stream().mapToDouble(Double::doubleValue).toArray(),
                                   expected.stream().mapToDouble(Double::doubleValue).toArray());

                    printWriter.println(coorectValue);
                    visualPrintWriter.print(geetVizualizationString(expected));
                    dataSet.addRow(expected.stream().mapToDouble(Double::doubleValue).toArray(),
                                   expected.stream().mapToDouble(Double::doubleValue).toArray());

                    printWriter.println(coorectValue);
                    visualPrintWriter.print(geetVizualizationString(expected));
                    dataSet.addRow(expected.stream().mapToDouble(Double::doubleValue).toArray(),
                                   expected.stream().mapToDouble(Double::doubleValue).toArray());

                    printWriter.println(coorectValue);
                    visualPrintWriter.print(geetVizualizationString(expected));
                    dataSet.addRow(expected.stream().mapToDouble(Double::doubleValue).toArray(),
                                   expected.stream().mapToDouble(Double::doubleValue).toArray());

                    printWriter.println(coorectValue);
//                    vizualize(expected);
                    visualPrintWriter.print(geetVizualizationString(expected));
                    dataSet.addRow(expected.stream().mapToDouble(Double::doubleValue).toArray(),
                                   expected.stream().mapToDouble(Double::doubleValue).toArray());


//            for (int j = 0; j < noiseCount; j++) {
//                dataSet.addRow(value.stream().mapToDouble(Double::doubleValue).toArray(),
//                               value.stream().mapToDouble(Double::doubleValue).toArray());
//            }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    private static void vizualize(final List<Double> value) {
        for (int i = 0; i < value.size(); i++) {
            if (i % 6 == 0) {
                System.out.println();
            }
            if (value.get(i) > 0) {
                System.out.print("#");
            } else {
                System.out.print("-");
            }
        }
        System.out.println();
    }

    private static String geetVizualizationString(final List<Double> value) {
        StringBuilder result = new StringBuilder(48);
        for (int i = 0; i < value.size(); i++) {
            if (i % 6 == 0) {
                result.append("\n");
            }
            if (value.get(i) > 0) {
                result.append("#");
            } else {
                result.append("-");
            }
        }
        result.append("\n");

        return result.toString();
    }

    private static double[] getNeuralNetworkResult(final NeuralNetwork neuralNetwork, final double[] numberRepresentation) {
        neuralNetwork.setInput(numberRepresentation);
        neuralNetwork.calculate();
        double[] result = neuralNetwork.getOutput();
//        printCalculatedResult(result);
        return result;
    }

    private static String getLetter(final double[] result) {
        List<Double> resultList = getRoundedResult(result);

        String resultLetter = valueLetter.get(resultList);
        vizualize(resultList);
//        if (resultLetter != null) {
//            System.out.println("This is matches : " + resultLetter);
//            vizualize(resultList);
//        } else {
////            System.out.print("Ha-ha gotcha ");
////            vizualize(resultList);
//        }

        System.out.println();
        return resultLetter;
    }

    private static List<Double> getRoundedResult(final double[] result) {
        List<Double> resultList = new ArrayList<>();

//        System.out.println("Rounded result:");
        for (final double aResult : result) {
            double roundedResult = Math.round(aResult);
//            System.out.print((int) roundedResult + " ");
            resultList.add(roundedResult);
        }
//        System.out.println();
        return resultList;
    }

    private static void printCalculatedResult(final double[] result) {
        //Original
        System.out.println("Calculated result:");
        Arrays.stream(result).forEach(number -> System.out.printf("%.4f ", number));
        System.out.println();
    }

    private static List<Double> getNoisedLetterNumbers(final List<Double> value, int noiseSize) {
        List<Double> numbers = new ArrayList<>(value);
        for (int i = 0; i < noiseSize; i++) {
            int index = new Random().nextInt(LETTER_LENGTH);
            if (numbers.get(index) > 0) {
                numbers.set(index, 0d);
            } else {
                numbers.set(index, 1d);
            }
        }
        return numbers;
    }

    @Override
    public void handleLearningEvent(final LearningEvent learningEvent) {
        BackPropagation bp = (BackPropagation)learningEvent.getSource();
        if (learningEvent.getEventType() != LearningEvent.Type.LEARNING_STOPPED)
            System.out.println("Iiteration : " + bp.getCurrentIteration() + ".Erorr: " + bp.getTotalNetworkError());
    }
}
