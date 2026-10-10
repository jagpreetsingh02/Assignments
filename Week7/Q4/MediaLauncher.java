interface Playable {
    String play();

    String play(int fromSecond);

    String pause();
}

abstract class MediaFile {
    private static int counter = 1000;

    private final String fileId;

    protected MediaFile() {
        counter++;
        this.fileId = "MF-" + counter;
    }

    String getFileId() {
        return fileId;
    }

    public abstract String getFormatInfo();
}

// AudioFile IS-A MediaFile: it is a stored file with an ID and a format, and it can also be played.
class AudioFile extends MediaFile implements Playable {
    private final String title;

    public AudioFile(String title) {
        this.title = title;
    }

    @Override
    public String play() {
        return "Playing audio: " + title;
    }

    @Override
    public String play(int fromSecond) {
        return "Playing audio: " + title + " from " + (fromSecond / 60) + ":" + String.format("%02d", fromSecond % 60);
    }

    @Override
    public String pause() {
        return "Paused audio: " + title;
    }

    @Override
    public String getFormatInfo() {
        return "Audio file, ID: " + getFileId();
    }
}

// Podcast only CAN-DO Playable: it is streamed live, so a stored fileId and a
// format description would mean nothing for it. It shares the behaviour, not the identity.
class Podcast implements Playable {
    private final String showName;
    private final int episodeNumber;

    public Podcast(String showName, int episodeNumber) {
        this.showName = showName;
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String play() {
        return "Streaming episode " + episodeNumber + " of " + showName;
    }

    @Override
    public String play(int fromSecond) {
        return play() + " from " + (fromSecond / 60) + ":" + String.format("%02d", fromSecond % 60);
    }

    @Override
    public String pause() {
        return "Paused stream of " + showName;
    }
}

public class MediaLauncher {

    static void launchAll(Playable[] items) {
        for (Playable item : items) {
            System.out.println(item.play());
        }
    }

    public static void main(String[] args) {
        AudioFile a = new AudioFile("Morning Jazz");
        System.out.println(a.play());
        System.out.println(a.play(30));
        System.out.println(a.getFormatInfo());

        Podcast p = new Podcast("Tech Talk", 12);
        System.out.println(p.play());
        System.out.println(p.play(95));
        System.out.println(p.pause());

        // Upcasting: an AudioFile stored as the Playable interface type.
        Playable ref = a;
        System.out.println(ref.play());

        System.out.println();
        launchAll(new Playable[]{ref, p});
    }
}
