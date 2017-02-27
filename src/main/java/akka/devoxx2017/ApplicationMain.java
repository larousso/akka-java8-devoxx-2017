package akka.devoxx2017;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.devoxx2017.actors.Producteur;
import akka.devoxx2017.messages.Messages;
import akka.japi.Pair;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.*;
import sun.net.www.http.KeepAliveStream;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static akka.pattern.PatternsCS.ask;

public class ApplicationMain {

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

//        Source<Messages.FaitMoiUnFilm, NotUsed> range = Source.repeat(Messages.FaitMoiUnFilm);
//
//        Flow<Messages.FaitMoiUnFilm, Messages.Film, NotUsed> films = Flow.<Messages.FaitMoiUnFilm>create()
//                .take(20)
//                .mapAsync(10, m ->
//                        ask(producteur, m, 10000).thenApply(Messages.Film.class::cast)
//                );
//
//        Sink<Messages.Film, CompletionStage<List<Messages.Film>>> filmsList = Sink.seq();
//
//        RunnableGraph<CompletionStage<List<Messages.Film>>> runnableGraph = range.via(films).toMat(filmsList, Keep.right());
//
//        CompletionStage<List<Messages.Film>> result = runnableGraph.run(ActorMaterializer.create(system));
//
//        result.whenComplete((f, e) -> {
//            if(e != null) {
//                e.printStackTrace();
//            }
//            System.out.println("Voici les Films : ");
//            f.forEach(System.out::println);
//        });

    }

}