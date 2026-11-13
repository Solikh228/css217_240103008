package task16;

class AssetLoader {
    public void loadGeometry(int levelNumber) {
        System.out.println("[AssetLoader] Loaded 3D mesh geometry for Level " + levelNumber + ".");
    }
}

class TextureCache {
    public void cacheTextures(int levelNumber) {
        System.out.println("[TextureCache] Uploaded Level " + levelNumber + " textures to VRAM.");
    }
    public void clearCache() {
        System.out.println("[TextureCache] Cleared VRAM texture cache.");
    }
}

class PhysicsEngine {
    public void initWorld() {
        System.out.println("[Physics] Gravity (-9.81 m/s^2) and collision bounds initialized.");
    }
    public void reset() {
        System.out.println("[Physics] World state cleared.");
    }
}

class AudioMixer {
    public void playTrack(int levelNumber) {
        System.out.println("[AudioMixer] Playing ambient soundtrack for Level " + levelNumber + ".");
    }
    public void stopAll() {
        System.out.println("[AudioMixer] Faded out all audio channels.");
    }
}

class SaveStateReader {
    public String readCheckpoint(String saveSlot) {
        String coords = "(X: 142.0, Y: 12.5, Z: -88.0)";
        System.out.println("[SaveReader] Slot '" + saveSlot + "' checkpoint at " + coords);
        return coords;
    }
}

class GameLevelFacade {
    private final AssetLoader assets;
    private final TextureCache textures;
    private final PhysicsEngine physics;
    private final AudioMixer audio;
    private final SaveStateReader saveReader;

    public GameLevelFacade(AssetLoader assets, TextureCache textures, PhysicsEngine physics,
                           AudioMixer audio, SaveStateReader saveReader) {
        this.assets = assets;
        this.textures = textures;
        this.physics = physics;
        this.audio = audio;
        this.saveReader = saveReader;
    }

    public void loadLevel(int levelNumber, String saveSlot) {
        System.out.println("=== Loading Level " + levelNumber + " ===");
        assets.loadGeometry(levelNumber);
        textures.cacheTextures(levelNumber);
        physics.initWorld();
        saveReader.readCheckpoint(saveSlot);
        audio.playTrack(levelNumber);
        System.out.println("Level " + levelNumber + " ready!\n");
    }

    public void unloadCurrentLevel() {
        System.out.println("--- Unloading Current Level ---");
        audio.stopAll();
        physics.reset();
        textures.clearCache();
        System.out.println("Memory freed.\n");
    }
}

public class Main {
    public static void main(String[] args) {
        GameLevelFacade game = new GameLevelFacade(
                new AssetLoader(), new TextureCache(), new PhysicsEngine(),
                new AudioMixer(), new SaveStateReader()
        );

        game.loadLevel(1, "slot_auto");
        game.unloadCurrentLevel();
        game.loadLevel(2, "slot_manual_1");
    }
}