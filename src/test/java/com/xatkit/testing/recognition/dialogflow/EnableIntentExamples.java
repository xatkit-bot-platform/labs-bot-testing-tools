package com.xatkit.testing.recognition.dialogflow;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.recognition.IntentRecognitionProvider;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.core.recognition.dialogflow.DialogFlowIntentRecognitionProvider;
import com.xatkit.core.recognition.dialogflow.DialogFlowStateContext;
import com.xatkit.intent.IntentDefinition;
import com.xatkit.intent.RecognizedIntent;
import com.xatkit.testing.recognition.dialogflow.model.GreetingsBotModel;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class EnableIntentExamples {

    private static GreetingsBotModel botModel;

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
        botModel = new GreetingsBotModel();

        Configurations configurations = new Configurations();
        Configuration botConfiguration = configurations.properties(EnableIntentExamples.class.getClassLoader().getResource(
                "greetings-bot.properties"));

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

    /**
     * Test that the Greetings intent is properly matched if it is enabled.
     */
    @Test
    public void testGreetingsEnabled() throws IntentRecognitionProviderException {
        DialogFlowTestingContext testingContext = getTestingContext();
        testingContext.enableIntents(botModel.getGreetings());
        RecognizedIntent recognizedIntent = dialogFlowProvider.getIntent("Hello", testingContext);
        assertThat(recognizedIntent.getDefinition()).isEqualTo(botModel.getGreetings());
    }

    /**
     * Test that the Greetings intent is not matched if no intent is enabled in the current context.
     * <p>
     * This test case relies on {@link IntentRecognitionProvider#DEFAULT_FALLBACK_INTENT}: the default
     * {@link com.xatkit.intent.IntentDefinition} returned by the provider when no intent matches the provided input.
     */
    @Test
    public void testGreetingsNotEnabled() throws IntentRecognitionProviderException {
        DialogFlowTestingContext testingContext = getTestingContext();
        RecognizedIntent recognizedIntent = dialogFlowProvider.getIntent("Hello", testingContext);
        assertThat(recognizedIntent.getDefinition()).isEqualTo(IntentRecognitionProvider.DEFAULT_FALLBACK_INTENT);
    }

    /**
     * Test that the Greetings intent is not matched if it is not part of the enabled intents in the current context.
     * <p>
     * This test case relies on {@link IntentRecognitionProvider#DEFAULT_FALLBACK_INTENT}: the default
     * {@link com.xatkit.intent.IntentDefinition} returned by the provider when no intent matches the provided input.
     */
    @Test
    public void testGreetingsOtherIntentEnabled() throws IntentRecognitionProviderException {
        DialogFlowTestingContext testingContext = getTestingContext();
        testingContext.enableIntents(botModel.getFine(), botModel.getHowAreYou());
        RecognizedIntent recognizedIntent = dialogFlowProvider.getIntent("Hello", testingContext);
        assertThat(recognizedIntent.getDefinition()).isEqualTo(IntentRecognitionProvider.DEFAULT_FALLBACK_INTENT);
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
