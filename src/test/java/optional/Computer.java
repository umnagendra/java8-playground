package optional;

import java.util.Optional;

public class Computer {
    private Optional<SoundCard> soundCard;

    public Optional<SoundCard> getSoundCard() {
        return soundCard;
    }

    public void setSoundCard(Optional<SoundCard> soundCard) {
        this.soundCard = soundCard;
    }
}

class SoundCard {
    private Optional<USB> usb;

    public Optional<USB> getUSB() {
        return usb;
    }

    public void setUSB(Optional<USB> usb) {
        this.usb = usb;
    }
}

class USB {
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}