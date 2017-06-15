package akka.devoxx2017;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.devoxx2017.actors.BillMurray;
import akka.devoxx2017.actors.Producteur;
import akka.devoxx2017.actors.Repondeur;
import akka.devoxx2017.messages.Messages;
import akka.devoxx2017.utils.LucBessonScenarioGenerator;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.*;
import de.heikoseeberger.akkasse.japi.EventStreamMarshalling;
import de.heikoseeberger.akkasse.japi.ServerSentEvent;
import javaslang.API;
import javaslang.collection.List;

import java.util.concurrent.CompletionStage;

import static akka.pattern.PatternsCS.ask;
import static javaslang.API.*;
import static javaslang.Predicates.*;

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

        ActorRef repondeur = system.actorOf(Repondeur.props(), "repondeur");

        system.actorOf(BillMurray.props(repondeur), "BillMurray");

        ActorRef producteur = system.actorOf(Producteur.props(repondeur), "Producteur");

        Source<ServerSentEvent, NotUsed> films = Source
                .repeat(Messages.FaitMoiUnFilm)
                .mapAsync(2, m -> ask(producteur, m, 20000)
                        .thenApply(Messages.Film.class::cast)
                        .exceptionally(e -> {
                            System.out.println(e.getMessage());
                            return Messages.Film(Messages.Scenario("----"), "Ben Affleck");
                        })
                )
                .map(f -> ServerSentEvent.create(f.json()));

        ActorMaterializer materializer = ActorMaterializer.create(system);

        // curl http://localhost:8080/films
        ApplicationMain app = new ApplicationMain();

        final Http http = Http.get(system);
        http.bindAndHandle(
                app.createRoute(films).flow(system, materializer),
                ConnectHttp.toHost("localhost", 8080),
                materializer
        );

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

    private static CompletionStage<Messages.Film> demanderABillMurray(ActorRef repondeur, Messages.NouveauMessageSurRepondeur message) {
        return ask(repondeur, message, 1000)
                .thenApply(m -> Match(m).of(
                    Case(instanceOf(Messages.JeSuisDAccord.class), Messages.Film(message.scenario, "Bill Murray")),
                    Case(instanceOf(Messages.AllezVousFaire.class), Messages.Film(message.scenario, "Ben Affleck"))
                ))
                .exceptionally(e -> Messages.Film(message.scenario, "Ben Affleck"));
    }


    private static void printFilms(java.util.List<Messages.Film> f) {
        System.out.println("Films : \n" + List.ofAll(f).mkString("\n"));
    }


}
