package ADAPTOR;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryGUI {
    private JFrame frame;
    private JPanel galleryGrid;
    private JTextField searchField;
    private List<MediaFile> allFiles = new ArrayList<>();
    private String currentCategory = "Toate fisierele";

    // --- PALETA ROYAL BLUE ---
    private final Color GRADIENT_START = new Color(0, 20, 60);
    private final Color GRADIENT_END = new Color(0, 50, 150);
    private final Color SIDEBAR_BG = new Color(0, 15, 45);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color ACCENT_COLOR = new Color(100, 180, 255);
    private final Color SEARCH_BAR_BG = new Color(255, 255, 255, 220);

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gp = new GradientPaint(0, 0, GRADIENT_START, 0, getHeight(), GRADIENT_END);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public GalleryGUI() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        frame = new JFrame("My Gallery Dashboard v7.0 - Organized");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1250, 900);
        frame.setLayout(new BorderLayout());

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(280, 0)); // Putin mai lat pentru noile denumiri
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 12));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(50, 50, 100)));

        JLabel logo = new JLabel("MultiGallery");
        logo.setForeground(TEXT_COLOR);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setBorder(new EmptyBorder(30, 0, 30, 0));
        sidebar.add(logo);

        JButton addBtn = new JButton("➕ Adauga Fisier");
        addBtn.setPreferredSize(new Dimension(230, 50));
        addBtn.setBackground(new Color(0, 70, 180));
        addBtn.setForeground(TEXT_COLOR);
        addBtn.setFocusPainted(false);
        addBtn.setBorder(null);
        addBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> openFilePicker());
        sidebar.add(addBtn);

        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- CATEGORII DESPARTITE ---
        addSidebarButton(sidebar, "Toate fisierele", "🏠");
        sidebar.add(Box.createRigidArea(new Dimension(0, 10))); // Spatiu mic
        
        addSidebarButton(sidebar, "Documente Word", "📝");
        addSidebarButton(sidebar, "Tabele Excel", "📊");
        addSidebarButton(sidebar, "Fisiere PDF", "📕");
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        addSidebarButton(sidebar, "Clipuri Video", "🎬");
        addSidebarButton(sidebar, "Imagini / Foto", "🖼️");

        frame.add(sidebar, BorderLayout.WEST);

        // --- MAIN CONTENT ---
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25));
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(0, 110));

        JPanel roundedSearch = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SEARCH_BAR_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
                g2.dispose();
            }
        };
        roundedSearch.setOpaque(false);
        roundedSearch.setPreferredSize(new Dimension(750, 55));
        roundedSearch.setBorder(new EmptyBorder(5, 25, 5, 25));

        JLabel searchIconLabel = new JLabel("🔍");
        searchIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        searchIconLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
        
        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshUI(); }
            public void removeUpdate(DocumentEvent e) { refreshUI(); }
            public void changedUpdate(DocumentEvent e) { refreshUI(); }
        });

        roundedSearch.add(searchIconLabel, BorderLayout.WEST);
        roundedSearch.add(searchField, BorderLayout.CENTER);
        topBar.add(roundedSearch);
        mainPanel.add(topBar, BorderLayout.NORTH);

        galleryGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 35, 35));
        galleryGrid.setOpaque(false);
        galleryGrid.setBorder(new EmptyBorder(10, 40, 10, 40));

        JScrollPane scroll = new JScrollPane(galleryGrid);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        mainPanel.add(scroll, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addSidebarButton(JPanel sidebar, String title, String emoji) {
        JLabel btn = new JLabel(emoji + "   " + title);
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setForeground(TEXT_COLOR);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentCategory = title;
                refreshUI();
                for(Component c : sidebar.getComponents()) {
                    if(c instanceof JLabel && c != sidebar.getComponent(0)) {
                        c.setForeground(TEXT_COLOR);
                    }
                }
                btn.setForeground(ACCENT_COLOR);
            }
            public void mouseEntered(MouseEvent e) { if(!currentCategory.equals(title)) btn.setForeground(ACCENT_COLOR); }
            public void mouseExited(MouseEvent e) { if(!currentCategory.equals(title)) btn.setForeground(TEXT_COLOR); }
        });
        sidebar.add(btn);
    }

    private void openFilePicker() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            for (File file : selectedFiles) {
                MediaFile adaptedFile = autoAdapt(file.getAbsolutePath());
                if (adaptedFile != null) addMedia(adaptedFile);
            }
        }
    }

    private MediaFile autoAdapt(String path) {
        String lp = path.toLowerCase();
        if (lp.endsWith(".png") || lp.endsWith(".jpg")) return new ImageAdapter(path);
        if (lp.endsWith(".pdf")) return new PDFAdapter(path);
        if (lp.endsWith(".docx")) return new WordAdapter(path);
        if (lp.endsWith(".xlsx")) return new ExcelAdapter(path);
        if (lp.endsWith(".mp4")) return new VideoAdapter(path);
        return null;
    }

    public void addMedia(MediaFile file) {
        allFiles.add(file);
        refreshUI();
    }

    private void refreshUI() {
        galleryGrid.removeAll();
        String query = searchField.getText().toLowerCase().trim();
        for (MediaFile f : allFiles) {
            if (f.getFileName().toLowerCase().contains(query) && filterCategory(f)) {
                galleryGrid.add(createModernCard(f));
            }
        }
        galleryGrid.revalidate();
        galleryGrid.repaint();
    }

    // --- LOGICA DE FILTRARE PE TIPURI ---
    private boolean filterCategory(MediaFile f) {
        if (currentCategory.contains("Toate")) return true;
        String n = f.getFileName().toLowerCase();
        
        if (currentCategory.contains("Word")) return n.endsWith(".docx");
        if (currentCategory.contains("Excel")) return n.endsWith(".xlsx");
        if (currentCategory.contains("PDF")) return n.endsWith(".pdf");
        if (currentCategory.contains("Video")) return n.endsWith(".mp4");
        if (currentCategory.contains("Imagini")) return n.endsWith(".png") || n.endsWith(".jpg");
        
        return false;
    }

    private JPanel createModernCard(MediaFile file) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(220, 300));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        ImageIcon thumb = file.getThumbnail();
        JLabel preview = new JLabel(thumb != null ? thumb : new ImageIcon());
        preview.setHorizontalAlignment(JLabel.CENTER);
        card.add(preview, BorderLayout.CENTER);

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        String displayName = new File(file.getFileName()).getName();
        JLabel name = new JLabel(displayName);
        name.setFont(new Font("Segoe UI Bold", Font.PLAIN, 14));
        name.setForeground(new Color(30, 30, 30));
        name.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel meta = new JLabel("Status: Activ • Adaptat");
        meta.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        meta.setForeground(new Color(100, 100, 100));
        meta.setHorizontalAlignment(JLabel.CENTER);

        info.add(name); info.add(meta);
        card.add(info, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { file.openFile(); }
            public void mouseEntered(MouseEvent e) { 
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(ACCENT_COLOR, 2, true),
                    new EmptyBorder(13, 13, 13, 13)
                ));
            }
            public void mouseExited(MouseEvent e) { card.setBorder(new EmptyBorder(15, 15, 15, 15)); }
        });

        return card;
    }
}