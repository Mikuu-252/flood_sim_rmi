package implementations;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.rmi.RemoteException;

public class MainRetensionBasin extends JFrame {

    private JPanel contentPanel=null;
    static PRetensionBasin rb = new PRetensionBasin();

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws RemoteException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainRetensionBasin frame = new MainRetensionBasin();
                    frame.setTitle("Retension Basin");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        while(true){
            rb.update();

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
    public MainRetensionBasin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 530, 350);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        rb.setBorder(new LineBorder(new Color(0, 0, 0)));
        rb.setBounds(5, 5, 505, 300);
        contentPanel.add(rb);

    }
}
