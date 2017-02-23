package akka.devoxx2017.utils;

import javaslang.collection.List;

import java.util.Random;

/**
 * Created by adelegue on 20/02/2017.
 */
public class LucBessonScenarioGenerator {

    private static List<String> list1 = List.of("Un taxi", "Un chinois", "Un yamakazi");
    private static List<String> list2 = List.of("protege", "pête la gueule à");
    private static List<String> list3 = List.of("un flic", "un yamakazi", "une pute");
    private static List<String> list4 = List.of("en banlieu", "en audi");

    private static Random random = new Random();

    public static String nextScenario() {
        return list1.get(random.nextInt(3)) + " " + list2.get(random.nextInt(2)) + " " + list3.get(random.nextInt(3)) + " " + list4.get(random.nextInt(2));
    }

}
