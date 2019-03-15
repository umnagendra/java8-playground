package functional;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

public class AggregateOperationsTest {

    private List<Person> allPersons;

    public AggregateOperationsTest() {
        allPersons = new ArrayList<>();
        allPersons.add(new Person("Person1", Person.Sex.MALE, 43));
        allPersons.add(new Person("Person2", Person.Sex.FEMALE, 20));
        allPersons.add(new Person("Person3", Person.Sex.FEMALE, 32));
        allPersons.add(new Person("Person4", Person.Sex.MALE, 19));
        allPersons.add(new Person("Person5", Person.Sex.MALE, 24));
        allPersons.add(new Person("Person6", Person.Sex.FEMALE, 21));
        allPersons.add(new Person("Person7", Person.Sex.MALE, 22));
    }

    @Test
    public void testAggregateOperations() {
        // all males
        allPersons
                .stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .forEach(p -> p.printPerson());

        // average age of all males
        double averageAgeOfAllMales = allPersons
                .stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .mapToInt(p -> p.getAge())
                .average().orElse(-1);
        System.out.println("Average age of all males = " + averageAgeOfAllMales + " years");

        // total age of all males
        int totalAgeOfAllMales = allPersons
                .stream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .mapToInt(p -> p.getAge())
                .reduce(0, (a, b) -> a + b);
        System.out.println("Total age of all males = " + totalAgeOfAllMales + " years");

        // average age of all males (using collect)
        Averager averager = allPersons
                                .stream()
                                .filter(p -> p.getGender() == Person.Sex.MALE)
                                .mapToInt(Person::getAge)
                                .collect(Averager::new, Averager::accept, Averager::combine);
        System.out.println("Average age of all males = " + averager.average() + " years");

        // grouping members by gender
        Map<Person.Sex, List<Person>> byGender = allPersons
                                                    .stream()
                                                    .collect(Collectors.groupingBy(Person::getGender));
        System.out.println("Persons grouped by gender = ");
        byGender.forEach((gender, persons) -> {
            System.out.println("\n" + gender + ":");
            persons.forEach(p -> p.printPerson());
        });

        // total age of each gender
        Map<Person.Sex, Integer> totalAgeByGender = allPersons
                                                        .stream()
                                                        .collect(Collectors.groupingBy(Person::getGender, Collectors.reducing(0, Person::getAge, Integer::sum)));
        System.out.println("Total age by gender = ");
        totalAgeByGender.forEach((gender, totalAge) -> System.out.println(gender + ": " + totalAge));

        // average age of each gender
        Map<Person.Sex, Double> averageAgeByGender = allPersons
                                                        .stream()
                                                        .collect(Collectors.groupingBy(Person::getGender, Collectors.averagingInt(Person::getAge)));
        System.out.println("Average age by gender = ");
        averageAgeByGender.forEach((gender, averageAge) -> System.out.println(gender + ": " + averageAge));

        List<String> allNamesUpper = allPersons
                .parallelStream()
                .map(p -> p.getName().toUpperCase())
                .collect(Collectors.toList());
        System.out.println(allNamesUpper);
    }
}

class Averager implements IntConsumer {

    private int total = 0;
    private int count = 0;

    public double average() {
        return count > 0 ? ((double) total)/count : 0;
    }

    public void accept(int i) {
        total += i;
        count++;
    }

    public void combine(Averager other) {
        total += other.total;
        count += other.count;
    }
}
