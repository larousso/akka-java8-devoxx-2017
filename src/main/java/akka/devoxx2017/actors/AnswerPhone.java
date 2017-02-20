package akka.devoxx2017.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.devoxx2017.messages.Messages;
import javaslang.Tuple2;
import javaslang.collection.List;

/**
 * Created by adelegue on 20/02/2017.
 */
public class AnswerPhone extends AbstractLoggingActor {

    private List<Messages.PhoneMessage> messages = List.empty();

    public static GiveMeLastMessage GiveMeLastMessage = new GiveMeLastMessage();

    public static LeaveAMessage LeaveAMessage(Messages.PhoneMessage phoneMessage) {
        return new LeaveAMessage(phoneMessage);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(LeaveAMessage.class, msg -> {
                    messages = messages.append(msg.message);
                })
                .match(GiveMeLastMessage.class, msg -> {
                    if(!messages.isEmpty()) {
                        Tuple2<Messages.PhoneMessage, List<Messages.PhoneMessage>> pair = messages.pop2();
                        messages = pair._2;
                        sender().tell(pair._1, self());
                    } else {
                        sender().tell(Messages.NoMessage, self());
                    }
                })
                .build();
    }

    public static Props props() {
        return Props.create(AnswerPhone.class);
    }

    public static class LeaveAMessage {
        public final Messages.PhoneMessage message;

        public LeaveAMessage(Messages.PhoneMessage message) {
            this.message = message;
        }
    }

    public static class GiveMeLastMessage {}

}
