package akka.devoxx2017.actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.devoxx2017.messages.Messages;
import javaslang.Tuple2;
import javaslang.collection.List;

/**
 * Created by adelegue on 06/04/2017.
 */
public class Repondeur extends AbstractLoggingActor {

    List<Messages.MessageSurRepondeur> messages = List.empty();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.NouveauMessageSurRepondeur.class, m -> {
                    log().info("Nouveau message {}", m);
                    messages = messages.append(Messages.MessageSurRepondeur(m.message, m.scenario, sender()));
                })
                .matchEquals(Messages.MessageSuivant, m -> messages.isEmpty(), m -> {
                    sender().tell(Messages.PasDeMessage, self());
                })
                .matchEquals(Messages.MessageSuivant, m -> {
                    Tuple2<Messages.MessageSurRepondeur, List<Messages.MessageSurRepondeur>> pair = messages.pop2();
                    messages = pair._2;
                    sender().tell(pair._1, self());
                })
                .build();
    }

    public static Props props() {
        return Props.create(Repondeur.class, () -> new Repondeur());
    }
}
