import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class IftarSayaciGUI {
    private JFrame frame;
    private JLabel label;
    private JPanel textPanel;
    private Timer timer;
    private LocalTime iftarVakti;

    public IftarSayaciGUI() {
        frame = new JFrame("İftar Sayacı");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        BackgroundPanel panel = new BackgroundPanel("src/blue-mosque-istanbul.jpg");
        panel.setLayout(new GridBagLayout());

        textPanel = new JPanel();
        textPanel.setBackground(new Color(0, 0, 0, 150)); // Siyah arka plan, transparan %60
        textPanel.setLayout(new BorderLayout());

        label = new JLabel("İftara kalan süre hesaplanıyor...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);

        textPanel.add(label, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(250, 50));

        panel.add(textPanel);
        frame.add(panel);

        frame.setLocationRelativeTo(null); // Ekranın ortasında açılmasını sağlar

        frame.setVisible(true);
        getIftarVakti(); // API'den veriyi al
        baslatGeriSayim();
    }

    private void getIftarVakti() {
        HttpResponse<JsonNode> response = Unirest.get("https://api.collectapi.com/pray/all?data.city=istanbul")
                .header("content-type", "application/json")
                .header("authorization", "apikey your_token")
                .asJson();

        if (response.isSuccess()) {
            // API'den gelen cevabı al ve parse et
            String iftarTimeStr = response.getBody()
                    .getObject()
                    .getJSONArray("result")
                    .getJSONObject(0)
                    .getString("iftar");

            // API saati genellikle "HH:mm" formatında verir, buna göre parse ediyoruz.
            String[] timeParts = iftarTimeStr.split(":");
            iftarVakti = LocalTime.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
        } else {
            System.out.println("API'den veri alınamadı.");
            iftarVakti = LocalTime.of(19, 30); // Varsayılan olarak 19:30
        }
    }

    private void baslatGeriSayim() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime simdi = LocalDateTime.now();
                LocalDateTime iftarTarihi = LocalDateTime.of(simdi.toLocalDate(), iftarVakti);

                Duration kalanZaman = Duration.between(simdi, iftarTarihi);

                if (!kalanZaman.isNegative()) {
                    long saat = kalanZaman.toHours();
                    long dakika = kalanZaman.toMinutes() % 60;
                    long saniye = kalanZaman.getSeconds() % 60;

                    label.setText("İftara: " + saat + " saat " + dakika + " dk " + saniye + " sn");
                } else {
                    label.setText("İftar vakti geldi! Hayırlı iftarlar.");
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IftarSayaciGUI());
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String filePath) {
        backgroundImage = new ImageIcon(filePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
