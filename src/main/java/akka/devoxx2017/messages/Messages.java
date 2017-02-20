package akka.devoxx2017.messages;

import akka.actor.ActorRef;

/**
 * Created by adelegue on 20/02/2017.
 */
public interface Messages {


    static AMovie AMovie(String scenario, String actor) {
        return new AMovie(scenario, actor);
    }

    class AMovie {

        public final String scenario;

        public final String actor;

        public AMovie(String scenario, String actor) {
            this.scenario = scenario;
            this.actor = actor;
        }
    }

    static PhoneMessage PhoneMessage(String message, ActorRef number) {
        return new PhoneMessage(message, number);
    }

    class PhoneMessage {

        public final String message;
        public final ActorRef number;
        public PhoneMessage(String message, ActorRef number) {
            this.message = message;
            this.number = number;
        }

    }

    NoMessage NoMessage = new NoMessage();

    class NoMessage {
    }


}
