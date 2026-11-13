package task01;

class Amplifier {
    public void on() { System.out.println("[Amplifier] Powering on."); }
    public void setSurroundSound() { System.out.println("[Amplifier] Surround sound mode enabled."); }
    public void setVolume(int level) { System.out.println("[Amplifier] Volume set to " + level + "."); }
    public void off() { System.out.println("[Amplifier] Powering off."); }
}

class Projector {
    public void on() { System.out.println("[Projector] Warming up lamp."); }
    public void wideScreenMode() { System.out.println("[Projector] Aspect ratio set to 16:9 widescreen."); }
    public void off() { System.out.println("[Projector] Cooling down and shutting off."); }
}

class StreamingPlayer {
    public void on() { System.out.println("[StreamingPlayer] Ready."); }
    public void play(String movie) { System.out.println("[StreamingPlayer] Now playing: \"" + movie + "\""); }
    public void stop() { System.out.println("[StreamingPlayer] Playback stopped."); }
    public void off() { System.out.println("[StreamingPlayer] Powered off."); }
}

class TheaterLights {
    public void dim(int level) { System.out.println("[TheaterLights] Dimming lights to " + level + "%."); }
    public void on() { System.out.println("[TheaterLights] Lights restored to 100%."); }
}

class HomeTheaterFacade {
    private final Amplifier amplifier;
    private final Projector projector;
    private final StreamingPlayer player;
    private final TheaterLights lights;
    
    public HomeTheaterFacade(Amplifier amplifier, Projector projector, StreamingPlayer player, TheaterLights lights) {
        this.amplifier = amplifier;
        this.projector = projector;
        this.player = player;
        this.lights = lights;
    }

    public void watchMovie(String movie) {
        System.out.println("=== Preparing Home Theater ===");
        lights.dim(15);
        projector.on();
        projector.wideScreenMode();
        amplifier.on();
        amplifier.setSurroundSound();
        amplifier.setVolume(65);
        player.on();
        player.play(movie);
    }

    public void endMovie() {
        System.out.println("=== Shutting Down Home Theater ===");
        player.stop();
        player.off();
        amplifier.off();
        projector.off();
        lights.on();
    }
}

public class Main {
    public static void main(String[] args) {
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
                new Amplifier(), new Projector(), new StreamingPlayer(), new TheaterLights()
        );

        homeTheater.watchMovie("The Java Chronicles");
        System.out.println();
        homeTheater.endMovie();

        System.out.println("\n--- Next Session ---\n");

        homeTheater.watchMovie("Design Patterns: The Movie");
        System.out.println();
        homeTheater.endMovie();
    }
}