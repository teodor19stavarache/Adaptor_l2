package ADAPTOR;


import javax.swing.ImageIcon;

class ImageAdapter implements MediaFile {
    private String name;
    private RealImageLoader loader = new RealImageLoader();

    public ImageAdapter(String name) { this.name = name; }

    @Override
    public void openFile() { loader.openImageInViewer(name); }

    @Override
    public String getFileName() { return name; }

    @Override
    public ImageIcon getThumbnail() { return loader.processImageFile(name); }
}

class PDFAdapter implements MediaFile {
    private String name;
    private AdvancedPDFEngine pdfEngine = new AdvancedPDFEngine();

    public PDFAdapter(String name) { this.name = name; }

    @Override
    public void openFile() { pdfEngine.launchPDFReader(name); }

    @Override
    public String getFileName() { return name; }

    @Override
    public ImageIcon getThumbnail() { return pdfEngine.renderFirstPage(name); }
}

class WordAdapter implements MediaFile {
    private String name;
    private WordProcessorEngine wordEngine = new WordProcessorEngine();

    public WordAdapter(String name) { this.name = name; }

    @Override
    public void openFile() { wordEngine.startWordApp(name); }

    @Override
    public String getFileName() { return name; }

    @Override
    public ImageIcon getThumbnail() { return wordEngine.extractTextPreview(name); }
}

class ExcelAdapter implements MediaFile {
    private String name;
    private ExcelSpreadsheetEngine excelEngine = new ExcelSpreadsheetEngine();

    public ExcelAdapter(String name) { this.name = name; }

    @Override
    public void openFile() { excelEngine.startExcelApp(name); }

    @Override
    public String getFileName() { return name; }

    @Override
    public ImageIcon getThumbnail() { return excelEngine.generateGridPreview(name); }
}

class VideoAdapter implements MediaFile {
    private String name;
    private VideoCodecEngine videoEngine = new VideoCodecEngine();

    public VideoAdapter(String name) { this.name = name; }

    @Override
    public void openFile() { videoEngine.playMovie(name); }

    @Override
    public String getFileName() { return name; }

    @Override
    public ImageIcon getThumbnail() { return videoEngine.grabFrame(name); }
}