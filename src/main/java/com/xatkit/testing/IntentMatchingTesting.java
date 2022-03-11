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
     * The thread where the bot is running
     */
    private static Thread botThread;

    /**
     * The intent recognition provider of the chatbot
     */
    private static IntentRecognitionProvider intentRecognitionProvider;

    /**
     * The name of the file containing the examples to be tested
     */
    private static String intentsFileName;


    /**
     * Creates a "testing" or "temporal" state context
     * @param intents the intents that the state of the state context will contain (i.e. the intents that can be
     *                matched through this state)
     * @return the new fake state context
     * @throws IntentRecognitionProviderException
     */
    private static StateContext createTestingStateContext(List<IntentDefinition> intents)
            throws IntentRecognitionProviderException {
        StateContext stateContext =  intentRecognitionProvider.createContext("TestContext");
        FakeState fakeState = new FakeState();
        fakeState.setIntents(intents);
        stateContext.setState(fakeState);
        return stateContext;
    }

    /**
     * Starts the bot and initializes {@link #intentRecognitionProvider}
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
     */
    private static void runIntentMatching(StateContext testingStateContext) {
        String outputFilePath =
                "src/test/resources/" + intentsFileName.split("\\.")[0] + "Result." + intentsFileName.split("\\.")[1];
        try {
            String testDocPath =
                    Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(intentsFileName)).getPath();
            CSVReader reader = new CSVReaderBuilder(new FileReader(testDocPath))
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build();
            List<String[]> csv = reader.readAll();
            String[] header = csv.get(0);
            csv.remove(0);

            CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath));
            writer.writeNext(header);
            for (String[] row : csv) {
                String input = row[0];
                String expectedIntent = row[1];
                RecognizedIntent recognizedIntent = intentRecognitionProvider.getIntent(input, testingStateContext);
                String[] newRow = {input, expectedIntent, recognizedIntent.getDefinition().getName()};
                writer.writeNext(newRow);
            }
            writer.close();
        } catch (IOException | CsvException | IntentRecognitionProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of the Intent Matching test.
     *
     * @param xatkitBot the xatkit bot
     * @param fileName  the name of the file containing the table with the utterances and expected intents
     * @param intents   the intents to use in the test
     */
    public static void testIntentMatching(XatkitBot xatkitBot, String fileName, IntentDefinition... intents) {
        intentsFileName = fileName;
        try {
            startBotThread(xatkitBot);
            StateContext testingStateContext = createTestingStateContext(Arrays.asList(intents));
            runIntentMatching(testingStateContext);
            botThread.interrupt();
        } catch (InterruptedException | IntentRecognitionProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of the Intent Matching test.
     *
     * @param xatkitBot the xatkit bot
     * @param fileName  the name of the file containing the table with the utterances and expected intents
     * @param state     the state containing the intents to use in the test
     */
    public static void testIntentMatching(XatkitBot xatkitBot, String fileName, State state) {
        intentsFileName = fileName;
        try {
            startBotThread(xatkitBot);
            StateContext testingStateContext = createTestingStateContext(new ArrayList<>(state.getAllAccessedIntents()));
            runIntentMatching(testingStateContext);
            botThread.interrupt();
        } catch (InterruptedException | IntentRecognitionProviderException e) {
            e.printStackTrace();
        }
    }
}
