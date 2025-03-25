package implementations;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.rmi.RemoteException;

public class MainTailor extends JFrame {

    private JPanel tailorPanel=null;
    static PTailor tr = new PTailor();
    /**
     * Launch the application.
     */
    public static void main(String[] args) throws RemoteException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainTailor frame = new MainTailor();
                    frame.setTitle("Tailor");
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        while(true){
            tr.update();

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
    public MainTailor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 500, 400);
        tailorPanel = new JPanel();
        tailorPanel.setBackground(Color.WHITE);
        tailorPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(tailorPanel);
        tailorPanel.setLayout(null);


        tr.setBorder(new LineBorder(new Color(0, 0, 0)));
        tr.setBounds(5, 5, 475, 350);
        tailorPanel.add(tr);

    }
}
