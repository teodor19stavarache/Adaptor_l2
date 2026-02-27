package ADAPTOR;

public class Main {
    public static void main(String[] args) {
        GalleryGUI gui = new GalleryGUI();

        gui.addMedia(new ImageAdapter("foto_concediu.png"));
        gui.addMedia(new VideoAdapter("prezentare_video.mp4"));
        gui.addMedia(new WordAdapter("referat_final.docx"));
        gui.addMedia(new ExcelAdapter("Orar_angajati.xlsx"));
        gui.addMedia(new PDFAdapter("cursjava.pdf"));

        System.out.println("Aplicatia a pornit.");
    }
}