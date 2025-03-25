package implementations;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.rmi.RemoteException;

public class MainRiverSection extends JFrame {

    private JPanel contentPanel=null;
    static PRiverSection rs = new PRiverSection();

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws RemoteException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainRiverSection frame = new MainRiverSection();
                    frame.setTitle("River");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        while(true){
            rs.update();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Create the frame.
     */
    public MainRiverSection() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 250);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        rs.setBorder(new LineBorder(new Color(0, 0, 0)));
        rs.setBounds(5, 5, 625, 200);
        contentPanel.add(rs);

    }
}
