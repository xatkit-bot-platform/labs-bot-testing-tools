package com.xatkit.testing;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.xatkit.core.XatkitBot;
import com.xatkit.core.recognition.IntentRecognitionProvider;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.execution.State;
import com.xatkit.execution.StateContext;
import com.xatkit.intent.ContextParameterValue;
import com.xatkit.intent.IntentDefinition;
import com.xatkit.intent.RecognizedIntent;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * This class tests that the expected intents for a set of utterances are detected properly by a chatbot state.
 */
public class IntentMatchingTesting {

    /**
     * The thread where the bot is running.
     */
    private static Thread botThread;

    /**
     * The intent recognition provider of the chatbot.
     */
    private static IntentRecognitionProvider intentRecognitionProvider;

    /**
     * The name of the file containing the examples to be tested.
     * <p>
     * The structure of the csv file must be the following (i.e. the following must be the header of the csv file):
     * <p>
     * {@code utterance,expected_intent,detected_intent,expected_parameters,detected_parameters}, where the
     * "detected" columns are empty since they will be filled through this process.
     */
    private static String intentsCsvFileName;

    /**
     * Creates a "testing" or "temporal" state context.
     * @param stateName the name of the testing state
     * @param intents   the intents that the state of the state context will contain (i.e. the intents that can be
     *                  matched through this state)
     * @return the new fake state context
     * @throws IntentRecognitionProviderException
     */
    private static StateContext createTestingStateContext(String stateName, List<IntentDefinition> intents)
            throws IntentRecognitionProviderException {
        StateContext stateContext =  intentRecognitionProvider.createContext("TestContext");
        FakeState fakeState = new FakeState();
        fakeState.setIntents(intents);
        fakeState.setName(stateName);
        stateContext.setState(fakeState);
        return stateContext;
    }

    /**
     * Starts the bot and initializes {@link #intentRecognitionProvider}.
     * @param xatkitBot the Xatkit bot that will be executed
     * @throws InterruptedException
     */
    private static void startBotThread(XatkitBot xatkitBot) throws InterruptedException {
        botThread = new Thread(xatkitBot);
        botThread.start();
        while (isNull(intentRecognitionProvider)) {
            Thread.sleep(1000);
            if (nonNull(xatkitBot.getXatkitServer()) && xatkitBot.getXatkitServer().isStarted()) {
                /*
                 * The Xatkit server is started. At this point the intent recognition provider should be fully
                 * initialized. (And yes, this is a ugly fix)
                 */
                intentRecognitionProvider = xatkitBot.getIntentRecognitionProvider();
            }
        }
    }

    /**
     * Creates a new file that contains the results of the intent matching test.
     *
     * @param testingStateContext the state context that contains a fake state with a set of intents
     * @return the path to the generated file
     */
    private static String runIntentMatching(StateContext testingStateContext) {
        // Rename "path/to/file/my_file.csv" to "path/to/file/my_fileResult.csv"
        String outputFilePath =
                "src/test/resources/" + intentsCsvFileName.split("\\.")[0] + "Result." + intentsCsvFileName.split("\\.")[1];
        try {
            String intentsCsvFilePath =
                    Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(intentsCsvFileName)).getPath();
            CSVReader reader = new CSVReaderBuilder(new FileReader(intentsCsvFilePath))
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build();
            List<String[]> csv = reader.readAll();
            String[] header = csv.get(0);
            csv.remove(0);

            CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath));
            writer.writeNext(header);
            for (String[] row : csv) {
                String utterance = row[0];
                String expectedIntent = row[1];
                String expectedParameters = row[3];
                RecognizedIntent recognizedIntent = intentRecognitionProvider.getIntent(utterance, testingStateContext);
                List<String> parameters = new ArrayList<>();
                List<ContextParameterValue> contextParameterValues = recognizedIntent.getValues();
                for (ContextParameterValue contextParameterValue : contextParameterValues) {
                    String parameterName = contextParameterValue.getContextParameter().getName();
                    String parameterValue = contextParameterValue.getValue().toString();
                    parameters.add(parameterName + " = " + parameterValue);
                }
                String[] newRow = {
                        utterance,
                        expectedIntent,
                        recognizedIntent.getDefinition().getName(),
                        expectedParameters,
                        String.join("; ", parameters),
                        "",
                        ""
                };
                writer.writeNext(newRow);
            }
            writer.close();
            return outputFilePath;
        } catch (IOException | CsvException | IntentRecognitionProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The entry point of the Intent Matching test.
     *
     * @param xatkitBot the xatkit bot
     * @param fileName  the name of the file containing the table with the utterances and expected intents
     * @param intents   the intents to use in the test
     * @return the path to the generated file
     */
    public static String testIntentMatching(XatkitBot xatkitBot, String fileName,
                                            String stateName, IntentDefinition... intents) {
        intentsCsvFileName = fileName;
        try {
            startBotThread(xatkitBot);
            StateContext testingStateContext = createTestingStateContext(stateName, Arrays.asList(intents));
            String outputFilePath = runIntentMatching(testingStateContext);
            botThread.interrupt();
            return outputFilePath;
        } catch (InterruptedException | IntentRecognitionProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The entry point of the Intent Matching test.
     *
     * @param xatkitBot the xatkit bot
     * @param fileName  the name of the file containing the table with the utterances and expected intents
     * @param state     the state containing the intents to use in the test
     * @return the path to the generated file
     */
    public static String testIntentMatching(XatkitBot xatkitBot, String fileName, State state) {
        intentsCsvFileName = fileName;
        try {
            startBotThread(xatkitBot);
            StateContext testingStateContext = createTestingStateContext(state.getName(), new ArrayList<>(state.getAllAccessedIntents()));
            String outputFilePath = runIntentMatching(testingStateContext);
            botThread.interrupt();
            return outputFilePath;
        } catch (InterruptedException | IntentRecognitionProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
