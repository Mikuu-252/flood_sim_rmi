package implementations;

import interfaces.ITailor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PTailor extends JPanel {

    private Tailor tailor = null;

    private JTextField txtPort, txtName1, txtName2;
    private int rbNumber, rivNumber, ccNumber, envNumber;
    List<String> addedNames = new ArrayList<>();

    /**
     * Create the panel.
     */
    public PTailor() {
        addedNames.add("");
        setLayout(null);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(5, 5, 40, 20);
        add(lblPort);

        txtPort = new JTextField("2000");
        txtPort.setBounds(45, 5, 50, 20);
        add(txtPort);

        JToggleButton btnListen = new JToggleButton("Listen");

        btnListen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                tailor = new Tailor(Integer.parseInt(txtPort.getText()));
            }
        });

        btnListen.setBounds(100, 5, 80, 20);
        add(btnListen);

        JSeparator s = new JSeparator();
        s.setBounds(0, 30, 1000, 1);
        s.setOrientation(SwingConstants.HORIZONTAL);
        add(s);

        JLabel lblName1 = new JLabel("Połącz:");
        lblName1.setBounds(5, 35, 60, 20);
        add(lblName1);

        txtName1 = new JTextField("");
        txtName1.setBounds(65, 35, 120, 20);
        add(txtName1);

        JLabel lblName2 = new JLabel("  Z  ");
        lblName2.setBounds(200, 35, 60, 20);
        add(lblName2);

        txtName2 = new JTextField("");
        txtName2.setBounds(250, 35, 120, 20);
        add(txtName2);

        JButton btnConn = new JButton("Połącz");

        btnConn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println(txtName1.getText()  + " z " + txtName2.getText());

                try {
                    tailor.connect(txtName1.getText(), txtName2.getText());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnConn.setBounds(375, 35, 80, 20);
        add(btnConn);

        JLabel lblNumber = new JLabel("Zbiorniki:");
        lblNumber.setBounds(5, 55, 70, 20);
        add(lblNumber);

        JLabel lblBasinName = new JLabel("Rzeki:");
        lblBasinName.setBounds(125, 55, 70, 20);
        add(lblBasinName);

        JLabel lblRbWaterPersent = new JLabel("Centrale:");
        lblRbWaterPersent.setBounds(240, 55, 70, 20);
        add(lblRbWaterPersent);

        JLabel LblSWD = new JLabel("Środowiska:");
        LblSWD.setBounds(360, 55, 90, 20);
        add(LblSWD);
    }

    public void addItem(String name, String t) {
        int x = switch (t) {
            case "b" -> 5;
            case "r" -> 125;
            case "c" -> 240;
            case "e" -> 360;
            default -> 0;
        };

        if(x != 0) {
            int y = 0;
            switch (t) {
                case "b" -> {
                    y = 80 + (25*rbNumber);
                    rbNumber++;
                }
                case "r" -> {
                    y = 80 + (25*rivNumber);
                    rivNumber++;
                }
                case "c" -> {
                    y = 80 + (25*ccNumber);
                    ccNumber++;
                }
                case "e" -> {
                    y = 80 + (25*envNumber);
                    envNumber++;
                }
            };

            if(y != 0) {
                JLabel lblZ = new JLabel(name);
                lblZ.setBounds(x, y, 120, 20);
                add(lblZ);
                revalidate();
                repaint();
            }
        }
    }

    public void update() throws RemoteException {
        if (tailor != null) {
            if (rbNumber != tailor.getRbmap().size()) {
                for (Map.Entry<String, Remote> entry : tailor.getRbmap().entrySet()) {
                    if (!addedNames.contains(entry.getKey())) {
                        addItem(entry.getKey(), "b");
                        addedNames.add(entry.getKey());
                    }
                }
            }

            if (rivNumber != tailor.getRivmap().size()) {
                for (Map.Entry<String, Remote> entry : tailor.getRivmap().entrySet()) {
                    if (!addedNames.contains(entry.getKey())) {
                        addItem(entry.getKey(), "r");
                        addedNames.add(entry.getKey());
                    }
                }
            }

            if (ccNumber != tailor.getCcmap().size()) {
                for (Map.Entry<String, Remote> entry : tailor.getCcmap().entrySet()) {
                    if (!addedNames.contains(entry.getKey())) {
                        addItem(entry.getKey(), "c");
                        addedNames.add(entry.getKey());
                    }
                }
            }

            if (envNumber != tailor.getEnvmap().size()) {
                for (Map.Entry<String, Remote> entry : tailor.getEnvmap().entrySet()) {
                    if (!addedNames.contains(entry.getKey())) {
                        addItem(entry.getKey(), "e");
                        addedNames.add(entry.getKey());
                    }
                }
            }
        }
    }
}
