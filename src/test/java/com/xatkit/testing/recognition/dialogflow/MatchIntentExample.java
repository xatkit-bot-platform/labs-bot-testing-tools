package com.xatkit.testing.recognition.dialogflow;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.core.recognition.dialogflow.DialogFlowIntentRecognitionProvider;
import com.xatkit.core.recognition.dialogflow.DialogFlowStateContext;
import com.xatkit.execution.ExecutionModel;
import com.xatkit.intent.IntentDefinition;
import com.xatkit.testing.recognition.dialogflow.model.DummiestBotModel;
import com.xatkit.testing.recognition.dialogflow.model.WeatherBotModel;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class MatchIntentExample {

    private static ExecutionModel botModel = new DummiestBotModel();

    private static Thread botThread;

    private static DialogFlowIntentRecognitionProvider dialogFlowProvider;

    /**
     * Starts the bot used to run the test cases.
     * <p>
     * Note that the bot is started once and is not reset between test cases. This shouldn't be an issue if the
     * test cases do not alter the bot and/or bot model. The bot can be accessed and queried through its
     * <a href="http://localhost:5000">web interface</a>.
     * <p>
     * This method ensures that {@link #dialogFlowProvider} is set when it returns.
     * <p>
     * <b>Note</b>: make sure you properly configured {@code /src/test/resources/greetings-bot.properties} with valid
     * DialogFlow credentials before you run this class.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws InterruptedException, ConfigurationException {
        Configurations configurations = new Configurations();
        Configuration botConfiguration = configurations.properties(MatchIntentExample.class.getClassLoader().getResource("greetings-bot.properties"));

        XatkitBot xatkitBot = new XatkitBot(botModel, botConfiguration);
        botThread = new Thread(xatkitBot);
        botThread.start();
        while (isNull(dialogFlowProvider)) {
            Thread.sleep(1000);
            if (nonNull(xatkitBot.getXatkitServer()) && xatkitBot.getXatkitServer().isStarted()) {
                /*
                 * The Xatkit server is started. At this point the DialogFlow connector should be fully initialized.
                 * (And yes, this is a ugly fix)
                 */
                dialogFlowProvider = (DialogFlowIntentRecognitionProvider) xatkitBot.getIntentRecognitionProvider();
            }
        }
    }

    @AfterClass
    public static void tearDownAfterClass() {
        /*
         * Stop the bot, this may take a few seconds.
         */

        botThread.interrupt();
    }


    @Test
    public void testAll() throws IntentRecognitionProviderException {
        DialogFlowIntentMatcher dialogFlowIntentMatcher = new DialogFlowIntentMatcher(botModel, getTestingContext(), dialogFlowProvider);
        List<IntentMatch> matchingIntents = dialogFlowIntentMatcher.getMatchingIntents();
        for (IntentMatch im : matchingIntents){
            System.out.println("Intent " + im.getExpectedIntent() + " was confused with intent " + im.getActualIntent() + " with a confidence of " + im.getConfidence());
        }

    }

    /**
     * Returns an empty {@link DialogFlowTestingContext}.
     * <p>
     * The returned {@link DialogFlowTestingContext} is properly initialized but does not enable any intent, use
     * {@link DialogFlowTestingContext#enableIntents(IntentDefinition...)} to configure it.
     *
     * @return the created {@link DialogFlowTestingContext}
     * @throws IntentRecognitionProviderException if the DialogFlow connector fails to create the underlying
     *                                            {@link DialogFlowStateContext}
     */
    private DialogFlowTestingContext getTestingContext() throws IntentRecognitionProviderException {
        DialogFlowStateContext context = (DialogFlowStateContext) dialogFlowProvider.createContext("TestContext");
        return new DialogFlowTestingContext(context);
    }

}
