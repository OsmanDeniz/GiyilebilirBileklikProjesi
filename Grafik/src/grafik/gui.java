package grafik;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.fazecast.jSerialComm.SerialPort;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class gui extends JPanel {

    private static final int AMOUNTOFLEDS=14, AMOUNTOFRECEIVERS=14;
    public int referenceValues[][] = new int[AMOUNTOFLEDS][AMOUNTOFRECEIVERS];
    private BufferedImage image;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int values[][] = new int[AMOUNTOFLEDS][AMOUNTOFRECEIVERS];

        try {
            referenceValues = readValuesFromFile("referans.txt");
            writeArduinoDataToFile();
            values = readValuesFromFile("sensorVerileri.txt");
            if (isItMatched(values, referenceValues)) {
                image = ImageIO.read(getClass().getResourceAsStream("acikEl.jpg"));
                g.drawImage(image, 750, 200, null);
            } else {
                image = ImageIO.read(getClass().getResourceAsStream("kapaliEl.jpg"));
                g.drawImage(image, 750, 200, null);
            }
        } catch (IOException ex) {
            Logger.getLogger(gui.class.getName()).log(Level.SEVERE, null, ex);
        }
        g.setColor(Color.BLACK);
        g.drawLine(30, 650, 640, 650);
        g.drawLine(40, 30, 40, 670);
        for (int index = 0; index < AMOUNTOFLEDS; index++) {
            g.drawString(String.valueOf(index), 70 + index * 40, 665);
        }
        for (int index = 0; index < AMOUNTOFRECEIVERS; index++) {
            g.drawString(String.valueOf(index), 15, 620 - index * 40);
        }
        try {
            int lineSpace, columnSpace = 0;
            for (int columnIndex = 0; columnIndex < AMOUNTOFLEDS; columnIndex++) {
                lineSpace = 0;
                for (int rowIndex = 0; rowIndex < AMOUNTOFRECEIVERS; rowIndex++) {
                    g.setColor(setColorFix(values[columnIndex][rowIndex]));
                    g.fillRect((50 + rowIndex * 40 + lineSpace), (600 - columnIndex * 40 - columnSpace), 40, 40);
                    lineSpace++;
                }
                columnSpace++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Color setColorFix(int value) throws IOException {

        Color myColor;
        if (value < 20) {
            myColor = Color.BLACK;
        } else if (value < 40) {
            myColor = Color.RED;
        } else if (value < 60) {
            System.setProperty("myColor", "#00FF00"); //yesil
            myColor = Color.getColor("myColor");
        } else if (value < 80) {
            System.setProperty("myColor", "#0000FF");//mavi
            myColor = Color.getColor("myColor");
        } else if (value < 100) {
            System.setProperty("myColor", "#FFFF00");//sarı
            myColor = Color.getColor("myColor");
        } else if (value < 120) {
            System.setProperty("myColor", "#00FFFF");//turkuaz
            myColor = Color.getColor("myColor");
        } else if (value < 140) {
            System.setProperty("myColor", "#FF00FF");//pembe
            myColor = Color.getColor("myColor");
        } else if (value < 160) {
            System.setProperty("myColor", "#C0C0C0");//gri
            myColor = Color.getColor("myColor");
        } else if (value < 180) {
            System.setProperty("myColor", "#800000");//kahverengi
            myColor = Color.getColor("myColor");
        } else {
            System.setProperty("myColor", "#000080");//lacivert
            myColor = Color.getColor("myColor");
        }
        return myColor;
    }

    boolean isItMatched(int sensorDataArray[][], int referenceArray[][]) {
        int tolerance=35;
        for (int ledIndex = 0; ledIndex < AMOUNTOFLEDS; ledIndex++) {
            for (int receiverIndex = 0; receiverIndex < AMOUNTOFRECEIVERS; receiverIndex++) {
                if (Math.abs(sensorDataArray[ledIndex][receiverIndex] - referenceArray[ledIndex][receiverIndex]) < tolerance) {
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void writeArduinoDataToFile() throws IOException {

        SerialPort ports[] = SerialPort.getCommPorts();
        int portIndex = 0, dataIndex = 0;
        for (SerialPort port : ports) {
            System.out.println(portIndex++ + " " + port.getSystemPortName());
            SerialPort port1 = ports[0];
            port1.setBaudRate(9600);
            if (port1.openPort()) {
                System.out.println("Bağlantı Başarılı");
            } else {
                System.out.println("Bağlantı Başarısız");
                return;
            }
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10000, 0);
            Scanner data = new Scanner(port1.getInputStream());
            RandomAccessFile raf = new RandomAccessFile("sensorVerileri.txt", "rw");
            raf.setLength(0);
            //İlk dönen değerlerin doğruluk payı düşük olduğu için aşağıdaki
            //kodda değerler 2 kez alınarak 2. alınan değerler 1. değerlerin üstüne yazılmış.
            //Böylece hata oranı düşürülmüştür.
            while (data.hasNextLine()) {
                try {
                    raf.writeBytes(data.nextLine() + "\n");
                    if (dataIndex == 13) {
                        dataIndex = 0;
                        raf = new RandomAccessFile("sensorVerileri.txt", "rw");
                        raf.setLength(0);
                        while (data.hasNextLine()) {
                            raf.writeBytes(data.nextLine() + "\n");
                            if (dataIndex == 13) {
                                break;
                            }
                            dataIndex++;
                        }
                        break;
                    }
                    dataIndex++;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            port1.closePort();
            break;
        }
    }

    public int[][] readValuesFromFile(String fileName) throws FileNotFoundException, IOException {
        try {
            int[][] allReadValues = new int[AMOUNTOFLEDS][AMOUNTOFRECEIVERS];
            File file = new File(fileName);
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] lineValues = null;

            for (int ledIndex = 0; ledIndex< AMOUNTOFLEDS; ledIndex++) {
                lineValues = line.split(" - ");
                for (int receiverIndex = 0; receiverIndex < AMOUNTOFRECEIVERS; receiverIndex++) {
                    if (line != null) {
                        allReadValues[ledIndex][receiverIndex] = Integer.parseInt(lineValues[receiverIndex]);
                    }
                }
                lineValues = null;
                line = reader.readLine();
            }
            return allReadValues;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
