package com.xatkit.testing.recognition.dialogflow.model;

import com.google.gson.JsonElement;
import com.xatkit.core.XatkitBot;
import com.xatkit.execution.impl.ExecutionModelImpl;
import com.xatkit.intent.IntentDefinition;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.io.ReactEventProvider;
import com.xatkit.plugins.react.platform.io.ReactIntentProvider;
import com.xatkit.plugins.rest.platform.RestPlatform;
import com.xatkit.plugins.rest.platform.utils.ApiResponse;
import com.xatkit.testing.recognition.dialogflow.EnableIntentExamples;
import lombok.Getter;
import lombok.val;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.text.MessageFormat;
import java.util.*;

import static com.xatkit.dsl.DSL.*;

public class WeatherBotModel extends ExecutionModelImpl {

    @Getter
    private IntentDefinition howIsTheWeather;

    @Getter
    private IntentDefinition hello;

    @Getter
    private IntentDefinition bye;


    public WeatherBotModel(){
        val reactPlatform = new ReactPlatform();
        val restPlatform = new RestPlatform();


        val init = state("Init");
        val helloIntent = state("HelloIntent");
        val awaitingInput = state("AwaitingInput");
        val awaitingInputAfterHello = state("AwaitingInput");
        val badEducation = state("BadEducation");
        val byeState = state("BadEducation");
        val printWeather = state("PrintWeather");

        howIsTheWeather = intent("HowIsTheWeather")
                .trainingSentence("How is the weather in Barcelona?")
                .trainingSentence("What is the forecast for today in Barcelona?")
                .parameter("cityName")
                .fromFragment("Barcelona")
                .entity(
                        city()
                )
                .getIntentDefinition()
                ;

        hello = intent("HelloIntent")
                .trainingSentence("Hi")
                .trainingSentence("Greetings")
                .getIntentDefinition()
                ;

        bye = intent("ByeIntent")
                .trainingSentence("Bye")
                .trainingSentence("Goodbye")
                .getIntentDefinition()
                ;


        init
                .next()
                .when(eventIs(ReactEventProvider.ClientReady)).moveTo(awaitingInput);


        awaitingInput
                .body(
                        context -> reactPlatform.reply(context, "Hi, I am Xatkit. Glad to help you")
                )
                .next()
                .when(intentIs(hello))
                .moveTo(helloIntent)
                .when(intentIs(howIsTheWeather))
                .moveTo(badEducation);

        helloIntent.body(
                context ->
                        reactPlatform.reply(context, "Hi, you can ask me how it's the weather like")
        ).next().moveTo(awaitingInputAfterHello);

        badEducation.body(
                context ->
                        reactPlatform.reply(context, "It's good education to say hello before asking")
        ).next().moveTo(awaitingInput);

        byeState.body(
                context ->
                        reactPlatform.reply(context, "Goodbye")
        ).next().moveTo(awaitingInput);


        awaitingInputAfterHello
                .next()
                .when(intentIs(howIsTheWeather))
                .moveTo(printWeather)
                .when(intentIs(hello))
                .moveTo(helloIntent)
                .when(intentIs(bye))
                .moveTo(byeState);

        printWeather
                .body(context -> {
                    String cityName = (String) context.getIntent().getValue("cityName");
                    Map<String, Object> queryParameters = new HashMap<>();
                    queryParameters.put("q", cityName);
                    ApiResponse<JsonElement> response = restPlatform.getJsonRequest(context, "http://api" +
                                    ".openweathermap.org/data/2.5/weather", queryParameters, Collections.emptyMap(),
                            Collections.emptyMap());
                    if (response.getStatus() == 200) {
                        long temp = Math.round(response.getBody().getAsJsonObject().get("main").getAsJsonObject().get(
                                "temp").getAsDouble());
                        long tempMin =
                                Math.round(response.getBody().getAsJsonObject().get("main").getAsJsonObject().get(
                                        "temp_min").getAsDouble());
                        long tempMax =
                                Math.round(response.getBody().getAsJsonObject().get("main").getAsJsonObject().get(
                                        "temp_max").getAsDouble());
                        String weather =
                                response.getBody().getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();
                        String weatherIcon =
                                "http://openweathermap.org/img/wn/" + response.getBody().getAsJsonObject().get(
                                        "weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString() + ".png";
                        reactPlatform.reply(context, MessageFormat.format("The current weather is {0} &deg;C with " +
                                        "{1} ![{1}]({2}) and a high temperature of {3} &deg;C and a low of {4} &deg;C", temp,
                                weather,
                                weatherIcon, tempMax, tempMin));
                    } else if (response.getStatus() == 400) {
                        reactPlatform.reply(context, "Oops, I couldn't find this city");
                    } else {
                        reactPlatform.reply(context, "Sorry, an error " +  response.getStatus() + " " + response.getStatusText() + " occurred when accessing the openweathermap service");
                    }

                })
                .next()
                .moveTo(awaitingInputAfterHello);


        val defaultFallback = fallbackState()
                .body(context -> reactPlatform.reply(context, "Sorry, I didn't, get it"));


        ReactEventProvider reactEventProvider = reactPlatform.getReactEventProvider();
        ReactIntentProvider reactIntentProvider = reactPlatform.getReactIntentProvider();

        this.getUsedPlatforms().add(reactPlatform);
        this.getUsedPlatforms().add(restPlatform);
        this.getUsedProviders().add(reactEventProvider);
        this.getUsedProviders().add(reactIntentProvider);
        this.setInitState(init.getState());
        this.setDefaultFallbackState(defaultFallback.getState());
    }

}
