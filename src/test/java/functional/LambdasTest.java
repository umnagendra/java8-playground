package functional;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class LambdasTest {

    @Test
    public void testLambda1() {
        MyPrinter myPrinter = System.out::println;
        myPrinter.print("hello");
        assertTrue(true);
    }

    @Test
    public void testLambda2() {
        List<Person> allPersons = new ArrayList<>();
        allPersons.add(new Person("Person1", Person.Sex.MALE, 43));
        allPersons.add(new Person("Person2", Person.Sex.FEMALE, 20));
        allPersons.add(new Person("Person3", Person.Sex.FEMALE, 32));
        allPersons.add(new Person("Person4", Person.Sex.MALE, 19));
        allPersons.add(new Person("Person5", Person.Sex.MALE, 24));
        allPersons.add(new Person("Person6", Person.Sex.FEMALE, 21));
        allPersons.add(new Person("Person7", Person.Sex.MALE, 22));

        processElements(allPersons, p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() < 25, p -> p.getName(), System.out::println);
        allPersons.stream()
                .filter(p -> p.getGender() == Person.Sex.MALE && p.getAge() >=18 && p.getAge() < 25)
                .map(p -> p.getName())
                .forEach(System.out::println);
    }

    private <X, Y> void processElements(Iterable<X> roster, Predicate<X> predicate, Function<X, Y> mapper, Consumer<Y> consumer) {
        for (X x : roster) {
            if (predicate.test(x)) {
                Y data = mapper.apply(x);
                consumer.accept(data);
            }
        }
    }
}

interface MyPrinter {
    void print(String s);
}