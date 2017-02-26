package akka.devoxx2017;

import akka.actor.ActorSystem;

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
     * - Actors
     * - Cluster
     * - Persistence
     * - Distributed data
     * - Streams
     * - HTTP
     *
     * @param args
     */
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MyActorSystem");

    }

}