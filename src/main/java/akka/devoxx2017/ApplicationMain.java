package akka.devoxx2017;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.devoxx2017.actors.BillMurray;
import akka.devoxx2017.messages.Messages;
import akka.devoxx2017.utils.LucBessonScenarioGenerator;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import de.heikoseeberger.akkasse.japi.EventStreamMarshalling;
import de.heikoseeberger.akkasse.japi.ServerSentEvent;

import java.util.concurrent.CompletionStage;

import static akka.pattern.PatternsCS.ask;

public class ApplicationMain extends AllDirectives {

    /**
     * Alexandre Delègue
     *
     * SERLI : www.serli.com / @SerliFr
     *
     * @chanksleroux
     * https://github.com/larousso
     *
     * AKKA :
     * - Actors: concurrence, parallélisation, résistance aux pannes, asynchrone, non bloquant
     * - Cluster: Messaging à travers le réseau, singleton, sharding ...
     * - Persistence: Event sourcing / CQRS
     * - Distributed data: CRDT
     * - Streams: reactive streams
     * - HTTP: REST / WebSocket
     * - Alpakka: Connecteurs pour akka streams, kafka, AMQP, JMS, fichiers, mongo, cassandra ...
     *
     * @param args
     */
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MyActorSystem");


//        Source<ServerSentEvent, NotUsed> films = Source
//                .repeat(Messages.FaitMoiUnFilm)
//                .map(f -> Messages.Scenario(LucBessonScenarioGenerator.nextScenario()))
//                .map(s -> Messages.NouveauMessageSurRepondeur("Hey bill", s))
//                .mapAsyncUnordered(10, m ->
//                        demanderABillMurray(repondeur, m)
//                                .thenApply(e -> Messages.Film(m.scenario, "Bill Murray"))
//                                .exceptionally(e -> Messages.Film(m.scenario, "Ben affleck"))
//                )
//                .map(f -> ServerSentEvent.create(f.json()));
//
//        ActorMaterializer materializer = ActorMaterializer.create(system);
//
//        // curl http://localhost:8080/films
//        ApplicationMain app = new ApplicationMain();
//
//        final Http http = Http.get(system);
//        http.bindAndHandle(
//                app.createRoute(films).flow(system, materializer),
//                ConnectHttp.toHost("localhost", 8080),
//                materializer
//        );
//


    }

    private Route createRoute(Source<ServerSentEvent, ?> events) {
        return route(
                path("films", () ->
                    get(() ->
                            completeOK(events, EventStreamMarshalling.toEventStream())
                    )
                )
        );
    }

    private static CompletionStage<Object> demanderABillMurray(ActorRef repondeur, Messages.NouveauMessageSurRepondeur message) {
        return ask(
                repondeur,
                message,
                1000
        );
    }

}