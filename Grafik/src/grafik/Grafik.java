package grafik;

import javax.swing.JFrame;

public class Grafik {

    public static void main(String[] args) throws InterruptedException {
        JFrame graphic = new JFrame("Led-Alıcı Grafiği");

        graphic.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphic.setSize(1366, 768);
        graphic.setVisible(true);
        gui shape = new gui();
        graphic.add(shape);

        while (true) {
            graphic.invalidate();
            graphic.validate();
            graphic.repaint();
        }
    }
}
