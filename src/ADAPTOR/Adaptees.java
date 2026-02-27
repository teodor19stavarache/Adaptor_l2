package ADAPTOR;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

// Biblioteci Externe
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

class RealImageLoader {
    public void openImageInViewer(String path) {
        try { Desktop.getDesktop().open(new File(path)); } catch (Exception e) {}
    }
    public ImageIcon processImageFile(String path) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            return resize(img);
        } catch (Exception e) { return null; }
    }
    public static ImageIcon resize(BufferedImage img) {
        if (img == null) return null;
        Image tmp = img.getScaledInstance(180, 160, Image.SCALE_SMOOTH);
        return new ImageIcon(tmp);
    }
}

class AdvancedPDFEngine {
    public void launchPDFReader(String path) {
        try { Desktop.getDesktop().open(new File(path)); } catch (Exception e) {}
    }
    public ImageIcon renderFirstPage(String path) {
        try (PDDocument document = Loader.loadPDF(new File(path))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage img = renderer.renderImageWithDPI(0, 72);
            return RealImageLoader.resize(img);
        } catch (Exception e) { return null; }
    }
}

class WordProcessorEngine {
    public void startWordApp(String path) {
        try { Desktop.getDesktop().open(new File(path)); } catch (Exception e) {}
    }

    // AUTOMATIZARE: Citește textul din Word și îl randează pe o foaie virtuală
    public ImageIcon extractTextPreview(String path) {
        try (FileInputStream fis = new FileInputStream(path);
             XWPFDocument doc = new XWPFDocument(fis)) {
            
            BufferedImage img = new BufferedImage(180, 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Desenăm "foaia" de hârtie
            g2.setColor(Color.WHITE); g2.fillRect(0, 0, 180, 200);
            g2.setColor(new Color(43, 87, 154)); g2.fillRect(0, 0, 180, 40); // Antet albastru Word
            
            g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString("WORD PREVIEW", 10, 25);
            
            g2.setColor(new Color(51, 51, 51)); g2.setFont(new Font("Serif", Font.ITALIC, 10));
            
            int y = 60;
            // Extragem primele paragrafe reale din document
            for (XWPFParagraph p : doc.getParagraphs()) {
                String text = p.getText();
                if (text != null && !text.trim().isEmpty()) {
                    // Tăiem textul dacă e prea lung pentru a nu ieși din card
                    if (text.length() > 30) text = text.substring(0, 28) + "...";
                    g2.drawString(text, 15, y);
                    y += 18;
                    if (y > 180) break;
                }
            }
            g2.dispose();
            return new ImageIcon(img);
        } catch (Exception e) { 
            return null; 
        }
    }
}

class ExcelSpreadsheetEngine {
    public void startExcelApp(String path) {
        try { Desktop.getDesktop().open(new File(path)); } catch (Exception e) {}
    }

    // AUTOMATIZARE: Citește celulele reale din Excel și desenează tabelul
    public ImageIcon generateGridPreview(String path) {
        try (FileInputStream fis = new FileInputStream(path);
             XSSFWorkbook wb = new XSSFWorkbook(fis)) {
            
            BufferedImage img = new BufferedImage(180, 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = img.createGraphics();
            g2.setColor(new Color(245, 245, 245)); g2.fillRect(0, 0, 180, 200);
            
            // Fundal antet Excel (Verde)
            g2.setColor(new Color(33, 115, 70)); g2.fillRect(0, 0, 180, 40);
            g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString("EXCEL DATA", 10, 25);

            g2.setColor(new Color(200, 200, 200));
            // Desenăm liniile de tabel
            for(int i=40; i<200; i+=25) g2.drawLine(0, i, 180, i);
            for(int i=0; i<180; i+=60) g2.drawLine(i, 40, i, 200);
            
            g2.setColor(Color.BLACK); g2.setFont(new Font("Arial", Font.PLAIN, 10));
            
            Sheet sheet = wb.getSheetAt(0);
            int y = 57;
            int rowsProcessed = 0;
            for (Row row : sheet) {
                if (rowsProcessed++ > 5) break;
                for (Cell cell : row) {
                    if (cell.getColumnIndex() > 2) break;
                    String val = cell.toString();
                    if (val.length() > 8) val = val.substring(0, 7);
                    g2.drawString(val, (cell.getColumnIndex() * 60) + 5, y);
                }
                y += 25;
            }
            g2.dispose();
            return new ImageIcon(img);
        } catch (Exception e) { return null; }
    }
}

class VideoCodecEngine {
    public void playMovie(String path) {
        try { Desktop.getDesktop().open(new File(path)); } catch (Exception e) {}
    }

    // AUTOMATIZARE: JCodec "îngheață" un cadru real din video
    public ImageIcon grabFrame(String path) {
        try {
            Picture picture = FrameGrab.getFrameFromFile(new File(path), 1);
            BufferedImage frame = AWTUtil.toBufferedImage(picture);
            
            BufferedImage finalImg = new BufferedImage(180, 160, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = finalImg.createGraphics();
            g2.drawImage(frame, 0, 0, 180, 160, null);
            
            // Simbol Play Estetic
            g2.setColor(new Color(0, 0, 0, 120)); g2.fillOval(65, 55, 50, 50);
            g2.setColor(Color.WHITE); 
            int[] x = {82, 82, 102}; int[] y = {67, 93, 80};
            g2.fillPolygon(x, y, 3);
            
            g2.dispose();
            return new ImageIcon(finalImg);
        } catch (Exception e) { return null; }
    }
}