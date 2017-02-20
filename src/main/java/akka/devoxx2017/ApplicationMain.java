package akka.devoxx2017;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.devoxx2017.actors.AnswerPhone;
import akka.devoxx2017.actors.BillMurray;
import akka.devoxx2017.actors.Producer;
import akka.devoxx2017.actors.Scenarist;
import akka.devoxx2017.messages.Messages;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static akka.pattern.PatternsCS.ask;

public class ApplicationMain {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MyActorSystem");

        ActorRef scenarist = system.actorOf(Scenarist.props(), "scenarist");
        ActorRef answerPhone = system.actorOf(AnswerPhone.props(), "answerPhone");
        system.actorOf(BillMurray.props(answerPhone), "billMurray");


        ActorRef producer = system.actorOf(Producer.props(scenarist, answerPhone), "producer");

        CompletionStage<List<Messages.AMovie>> movies = Source.range(0, 20)
                .mapAsyncUnordered(10, i ->
                        ask(producer, Producer.CreateMovie, 5000).thenApply(Messages.AMovie.class::cast)
                )
                .runWith(Sink.seq(), ActorMaterializer.create(system));

        movies.whenComplete((l, e ) -> {
            javaslang.collection.List.ofAll(l).forEach(m ->
                    System.out.println(m.actor + " in " + m.scenario)
            );
        });
    }

}