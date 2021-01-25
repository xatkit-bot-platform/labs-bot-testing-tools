package com.xatkit.testing.recognition.dialogflow.model;

import com.xatkit.execution.impl.ExecutionModelImpl;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.io.ReactEventProvider;
import com.xatkit.plugins.react.platform.io.ReactIntentProvider;
import lombok.val;

import static com.xatkit.dsl.DSL.*;

public class ChatBotCorpusBotModel extends ExecutionModelImpl {


    public ChatBotCorpusBotModel(){
        ReactPlatform reactPlatform = new ReactPlatform();
        ReactEventProvider reactEventProvider = reactPlatform.getReactEventProvider();
        ReactIntentProvider reactIntentProvider = reactPlatform.getReactIntentProvider();
        val init = state("Init");
        val awaitingInput = state("AwaitingInput");
        val printHello = state("PrintHello");

        val StationDest = mapping("StationDest")
                .entry().value("hbf")
                .entry().value("frötmaning")
                .entry().value("laim")
                .entry().value("rotkreuzplatz")
                .entry().value("munich east")
                .entry().value("münchner freiheit")
                .entry().value("hauptbahnhof")
                .entry().value("boltzmannstraße")
                .entry().value("poccistraße")
                .entry().value("milbertshofen")
                .entry().value("glockenbachviertel")
                .entry().value("neuperlach süd")
                .entry().value("perlach")
                .entry().value("marienplatz")
                .entry().value("nordfriedhof")
                .entry().value("kieferngarte")
                .entry().value("assling")
                .entry().value("klinikum")
                .entry().value("petershausen")
                .entry().value("garching")
                .entry().value("karlsplatz")
                .entry().value("prinzregentenplatz")
                .entry().value("freimann")
                .entry().value("scheidplatz")
                .entry().value("garching forschungszentrum")
                .entry().value("alte heide")
                .entry().value("karl-preis-platz")
                .entry().value("hohenlindenerstr")
                .entry().value("moosach")
                .entry().value("neuperlach sued")
                .entry().value("odeonsplatz")
                .entry().value("airport")
                .entry().value("lehel")
                .entry().value("untere straussäcker")
                .entry().value("pasing")
                .entry().value("quiddestraße")
                .entry().value("kurt-eisner-straße")
                .entry().value("hohenlindenerstraße")
                .entry().value("studentenstadt")
                .entry().value("fröttmaning")
                .entry().value("sendlinger tor")
                .entry().value("ostbahnhof")
                .entry().value("hackerbrücke")
                .entry().value("kieferngarten")
                ;
        val StationStart = mapping("StationStart")
                .entry().value("röblingweg")
                .entry().value("odeonsplatz")
                .entry().value("muncher freiheit")
                .entry().value("garching")
                .entry().value("garching forschungzentrum")
                .entry().value("münchner freiheit")
                .entry().value("central station")
                .entry().value("hauptbahnhof")
                .entry().value("quiddestraße")
                .entry().value("romanplatz")
                .entry().value("munchner freiheit")
                .entry().value("michaelibad")
                .entry().value("mariahilfplatz")
                .entry().value("muenchen freicheit")
                .entry().value("sendlinger tor")
                .entry().value("ostbahnhof")
                .entry().value("nordfriedhof")
                .entry().value("garching forschungszentrum")
                .entry().value("alte heide")
                .entry().value("garching,forschungszentrum")
                ;
        val Vehicle = mapping("Vehicle")
                .entry().value("rocket")
                .entry().value("bus")
                .entry().value("tram")
                .entry().value("subway")
                .entry().value("s-bahn")
                .entry().value("u-bahn")
                .entry().value("train")
                ;
        val Criterion = mapping("Criterion")
                .entry().value("next")
                ;
        val TimeStartTime = mapping("TimeStartTime")
                .entry().value("5")
                .entry().value("2 pm")
                .entry().value("8 am")
                .entry().value("3 pm")
                ;
        val TimeEndTime = mapping("TimeEndTime")
                .entry().value("9")
                .entry().value("1 pm")
                ;
        val Line = mapping("Line")
                .entry().value("u6")
                ;
        val FindConnection = intent("FindConnection")
                .trainingSentence("i need a connection from StationStart to StationDest at TimeStartTime.")
                .trainingSentence("StationStart to StationDest")
                .trainingSentence("can you give me a connection from StationStart to StationDest?")
                .trainingSentence("how do i get to StationDest?")
                .trainingSentence("is there a Vehicle from StationStart to StationDest at TimeStartTime?")
                .trainingSentence("can you find a connection from StationStart to StationDest?")
                .trainingSentence("what's the Criterion way between StationStart and StationDest?")
                .trainingSentence("is there a train from StationStart to StationDest at TimeStartTime?")
                .trainingSentence("what's the Criterion connection between StationStart and StationDest?")
                .trainingSentence("when is the Criterion Vehicle from StationStart to StationDest")
                .trainingSentence("how can i get from StationStart to StationStart?")
                .trainingSentence("can you tell me the Criterion way from StationStart to StationDest?")
                .trainingSentence("take me from StationStart to StationDest")
                .trainingSentence("take me to the StationDest")
                .trainingSentence("i want to go StationDest")
                .trainingSentence("connection from StationStart to StationDest")
                .trainingSentence("how to get from StationStart to StationDest")
                .trainingSentence("find connection from StationStart to StationDest")
                .trainingSentence("i need to be in StationDest at TimeEndTime")
                .trainingSentence("can i take a Vehicle from StationStart to StationDest?")
                .trainingSentence("hello munich city bot! how do i get from StationStart to StationDest?")
                .trainingSentence("can you find a connection from StationStart to StationDest at TimeStartTime?")
                .trainingSentence("what's the Criterion way from StationStart to StationDest?")
                .trainingSentence("connection from StationStart to StationDest?")
                .trainingSentence("from StationStart to StationDest?")
                .trainingSentence("start: StationStart end:StationDest")
                .trainingSentence("when is the Criterion Vehicle to StationDest")
                .trainingSentence("how can i get to StationDest")
                .trainingSentence("can you find a Vehicle from StationStart to StationDest?")
                .trainingSentence("can you find the Criterion way from StationStart to StationDest?")
                .trainingSentence("from StationStart to StationDest")
                .trainingSentence("i want to travel from StationStart to StationDest?")
                .trainingSentence("when is the Criterion Vehicle from untere StationStart to StationDest")
                .trainingSentence("is there a Vehicle from StationStart to StationDest at around TimeStartTime?")
                .trainingSentence("how can i get from StationStart to StationDest?")
                .trainingSentence("what is the Criterion connection from StationStart to StationDest?")
                .trainingSentence("how can i get from StationStart StationDest?")
                .trainingSentence("could you give me the Criterion connection between StationStart and StationDest?")
                .trainingSentence("what is the Criterion connection between StationStart and StationDest?")
                .trainingSentence("how to get from StationStart to StationDest ?")
                .trainingSentence("in need to be at StationDest at TimeEndTime, can you search a connection from StationStart?")
                .trainingSentence("how can i go from StationStart to StationDest")
                .trainingSentence("how can i get to StationDest?")
                .trainingSentence("how do i get from StationStart to StationDest")
                .trainingSentence("how i can get from StationStart to StationDest")
                .trainingSentence("how can i get from StationStart to StationDest as Criterion as possible?")
                .trainingSentence("how to get from StationStart to StationDest?")
                .trainingSentence("when is the Vehicle from StationStart to StationDest")
                .trainingSentence("how can i get to StationDest from StationStart?")
                .trainingSentence("how can i get from StationStart to StationDest")
                .trainingSentence("is there a Vehicle from StationStart to StationDest?")
                .trainingSentence("how do i get from StationStart zu StationDest")
                .trainingSentence("how i can get from StationStart to StationDest?")
                .trainingSentence("i want to go StationDest from StationStart")
                .trainingSentence("i want to go to StationDest from StationStart")
                .trainingSentence("how do i get from StationStart to StationDest?")
                .parameter("StationDest").fromFragment("StationDest").entity(StationDest)
                .parameter("StationStart").fromFragment("StationStart").entity(StationStart)
                .parameter("Vehicle").fromFragment("Vehicle").entity(Vehicle)
                .parameter("Criterion").fromFragment("Criterion").entity(Criterion)
                .parameter("TimeStartTime").fromFragment("TimeStartTime").entity(TimeStartTime)
                .parameter("TimeEndTime").fromFragment("TimeEndTime").entity(TimeEndTime)
                .getIntentDefinition();
        val DepartureTime = intent("DepartureTime")
                .trainingSentence("when does the Criterion Vehicle leaves at StationStart?")
                .trainingSentence("when is the Criterion Line?")
                .trainingSentence("when does the Criterion Vehicle starts from StationStart?")
                .trainingSentence("when does the Criterion Vehicle departs from StationStart?")
                .trainingSentence("the Criterion Vehicle from StationStart")
                .trainingSentence("when is the Criterion Vehicle")
                .trainingSentence("when is the Criterion Vehicle from StationStart")
                .trainingSentence("when is the Vehicle from StationStart?")
                .trainingSentence("when does the Criterion Vehicle come at StationStart")
                .trainingSentence("Criterion Vehicle from StationStart")
                .trainingSentence("when does the Criterion Vehicle leaves from StationStart?")
                .trainingSentence("when is the next Line leaving from StationStart?")
                .trainingSentence("tell me the Criterion Vehicle from StationStart")
                .trainingSentence("when the Criterion Vehicle in StationStart is leaving?")
                .trainingSentence("when does the Vehicle leaving in StationStart")
                .trainingSentence("when does the Criterion Vehicle departs at StationStart?")
                .trainingSentence("Criterion Vehicle from StationStart?")
                .trainingSentence("when is the Criterion Vehicle leaving at StationStart?")
                .trainingSentence("when is the Vehicle from StationStart")
                .trainingSentence("what is the Criterion Vehicle from StationStart")
                .trainingSentence("when does the Criterion Vehicle departs at StationStart")
                .trainingSentence("Criterion Vehicle from StationStart.")
                .trainingSentence("Criterion Vehicle in StationStart")
                .trainingSentence("when comes the Criterion Vehicle")
                .trainingSentence("when does the Criterion Vehicle departes from StationStart?")
                .trainingSentence("depart in StationStart, i assume")
                .trainingSentence("when is the Vehicle leaving in StationStart?")
                .trainingSentence("hey bot, when does the Criterion Vehicle starts at StationStart?")
                .trainingSentence("when does the Criterion Line leave from StationStart")
                .trainingSentence("when is the Criterion Vehicle in StationStart")
                .trainingSentence("when will the Criterion Vehicle depart from StationStart?")
                .trainingSentence("when is the Criterion Vehicle in StationStart?")
                .trainingSentence("when does the Criterion Vehicle depart at StationStart?")
                .trainingSentence("Criterion Vehicle from StationStart, please.")
                .trainingSentence("when does the next Vehicle leaves?")
                .trainingSentence("when does the Criterion Vehicle leave in StationStart?")
                .trainingSentence("when can i get a Vehicle at StationStart?")
                .trainingSentence("when does the Vehicle to StationStart starts?")
                .trainingSentence("when is the Criterion Vehicle leaving in StationStart")
                .trainingSentence("show me the Criterion Vehicle from StationStart.")
                .trainingSentence("when is the Vehicle leaving in StationStart")
                .trainingSentence("show me the Criterion Vehicle from StationStart!")
                .trainingSentence("when is adrians Criterion Vehicle leaving at StationStart?")
                .trainingSentence("when is the Criterion Vehicle leaving from StationStart?")
                .trainingSentence("or depart from StationStart")
                .trainingSentence("when does the Criterion Vehicle starts at StationStart?")
                .trainingSentence("when does the Criterion Vehicle leave from StationStart")
                .parameter("Vehicle").fromFragment("Vehicle").entity(Vehicle)
                .parameter("Criterion").fromFragment("Criterion").entity(Criterion)
                .parameter("StationStart").fromFragment("StationStart").entity(StationStart)
                .parameter("Line").fromFragment("Line").entity(Line)
                .getIntentDefinition();
        awaitingInput.next()
                .when(intentIs(FindConnection)).moveTo(printHello)
                .when(intentIs(DepartureTime)).moveTo(printHello)
        ;
        printHello.body(context -> {
            reactPlatform.reply(context, "Hey!");
        }).next().moveTo(awaitingInput);init.next().when(eventIs(ReactEventProvider.ClientReady)).moveTo(awaitingInput);
        val defaultFallback = fallbackState().body(context -> reactPlatform.reply(context, "Sorry, I didn't, get it"));


        this.getUsedPlatforms().add(reactPlatform);
        this.getUsedProviders().add(reactPlatform.getReactEventProvider());
        this.getUsedProviders().add(reactPlatform.getReactIntentProvider());
        this.setInitState(init.getState());
        this.setDefaultFallbackState(defaultFallback.getState());
    }
}