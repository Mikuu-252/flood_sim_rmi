package implementations;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.rmi.RemoteException;

public class MainEnvironment extends JFrame {

    private JPanel contentPanel=null;
    static PEnvironment e = new PEnvironment();
    /**
     * Launch the application.
     */
    public static void main(String[] args) throws RemoteException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainEnvironment frame = new MainEnvironment();
                    frame.setTitle("Environment");
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        while(true){
            e.update();

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
    public MainEnvironment() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 500, 400);
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);


        e.setBorder(new LineBorder(new Color(0, 0, 0)));
        e.setBounds(5, 5, 475, 350);
        contentPanel.add(e);

    }
}
