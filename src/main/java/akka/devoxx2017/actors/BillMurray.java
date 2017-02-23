package akka.devoxx2017.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.devoxx2017.messages.Messages;

/**
 * Created by adelegue on 22/02/2017.
 */
public class BillMurray extends AbstractLoggingActor {

    private final ActorRef repondeur;

    public static Props props(ActorRef repondeur) {
        return Props.create(BillMurray.class, () -> new BillMurray(repondeur));
    }

    private BillMurray(ActorRef repondeur) {
        this.repondeur = repondeur;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.MessageSurRepondeur.class, m -> {
                    log().info("J'ai un nouveau message {}", m.message);
                    m.numeroTel.tell(Messages.JeSuisDAccord(m.scenario), self());
                    repondeur.tell(Messages.MessageSuivant, self());
                })
                .matchEquals(Messages.PasDeMessage, m -> {
                    //TODO : faire qqc
                })
                .build();
    }


    @Override
    public void preStart() throws Exception {
        //TODO : faire qqc
    }
}
