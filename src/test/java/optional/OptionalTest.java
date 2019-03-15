package optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OptionalTest {

    @Test
    @DisplayName("Basic Test for Optional")
    public void testOptional1() {
        Computer computer = new Computer();
        assertFalse(computer.getSoundCard().isPresent());
    }

    @Test
    @DisplayName("Check for USB 3.0")
    public void testUSB3() {
        USB usb = new USB();
        usb.setVersion("3.0");

        SoundCard soundCard = new SoundCard();
        soundCard.setUSB(Optional.of(usb));

        Computer computer = new Computer();
        computer.setSoundCard(Optional.of(soundCard));

        computer.getSoundCard()
                .ifPresent(sc -> sc.getUSB()
                        .filter(usb1 -> "3.0".equals(usb1.getVersion()))
                        .ifPresent(usb2 -> System.out.println(usb2.getVersion())));

        Optional<Computer> computero = Optional.of(computer);
        computero.flatMap(Computer::getSoundCard)
                .flatMap(SoundCard::getUSB)
                .ifPresent(usb1 -> System.out.println(usb1.getVersion()));
    }
}
