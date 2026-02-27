package ADAPTOR;
import javax.swing.ImageIcon;

public interface MediaFile {
    void openFile();
    String getFileName();
    ImageIcon getThumbnail();
}