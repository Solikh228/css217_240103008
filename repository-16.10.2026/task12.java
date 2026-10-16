interface MediaPlayer {
    void playAudio(String filename);
}

class VlcEngine {
    public void startPlayback(String filepath, int volume) {
        System.out.println("Playing " + filepath + " vol " + volume);
    }
}

class VlcAdapter implements MediaPlayer {
    VlcEngine vlc; 

    public VlcAdapter(VlcEngine vlc) {
        this.vlc = vlc;
    }

    public void playAudio(String filename) {
        vlc.startPlayback(filename, 80);
    }
}