package implementations;

import interfaces.ITailor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class PEnvironment extends JPanel {
    private Environment environment = null;

    private JTextField txtName, txtIp, txtPort;
    private int rNumber = 0;

    private List<String> rivNames = new ArrayList<String>();
    private List<JLabel> lblRainfall = new ArrayList<JLabel>();
    private List<JTextField> txtZ = new ArrayList<JTextField>();

    /**
     * Create the panel.
     */
    public PEnvironment() {

        setLayout(null);

        JLabel lblIp = new JLabel("Ip:");
        lblIp.setBounds(5, 5, 30, 20);
        add(lblIp);

        txtIp = new JTextField("localhost");
        txtIp.setBounds(25, 5, 100, 20);
        add(txtIp);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(125, 5, 40, 20);
        add(lblPort);

        txtPort = new JTextField("2000");
        txtPort.setBounds(165, 5, 50, 20);
        add(txtPort);

        JLabel lblName = new JLabel("Nazwa:");
        lblName.setBounds(215, 5, 50, 20);
        add(lblName);

        txtName = new JTextField("Srodowisko 1");
        txtName.setBounds(265, 5, 120, 20);
        add(txtName);

        JToggleButton btnListen = new JToggleButton("Listen");

        btnListen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    environment = new Environment();
                    Registry registry = LocateRegistry.getRegistry(txtIp.getText(), Integer.parseInt(txtPort.getText()));
                    ITailor it = (ITailor) registry.lookup("Tailor");

                    it.register(environment, txtName.getText());

                    for(String key : environment.getRivMap().keySet()) {
                        addRiverDisplay(key);
                    }

                } catch (RemoteException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnListen.setBounds(390, 5, 80, 20);
        add(btnListen);

        JSeparator s = new JSeparator();
        s.setBounds(0, 30, 1000, 1);
        s.setOrientation(SwingConstants.HORIZONTAL);
        add(s);

        JLabel lblRiverNumber = new JLabel("Lp");
        lblRiverNumber.setBounds(5, 35, 50, 20);
        add(lblRiverNumber);

        JLabel lblRiverName = new JLabel("Nazwa rzeki");
        lblRiverName.setBounds(40, 35, 70, 20);
        add(lblRiverName);

        JLabel LblSRF = new JLabel("Opady");
        LblSRF.setBounds(140, 35, 70, 20);
        add(LblSRF);

    }

    public void addRiverDisplay(String name) {
        System.out.println("Added display " + name);


        JLabel lblZ = new JLabel("R " + rNumber + ":");
        lblZ.setBounds(5, 60 + (25*rNumber), 30, 20);
        add(lblZ);

        JLabel lblName = new JLabel(name);
        lblName.setBounds(35, 60 + (25*rNumber), 135, 20);
        add(lblName);

        JLabel newLblSRF = new JLabel("0");
        newLblSRF.setBounds(150, 60 + (25*rNumber), 40, 20);
        add(newLblSRF);

        JTextField newTxtZ = new JTextField();
        newTxtZ.setBounds(190, 60 + (25*rNumber), 35, 20);
        add(newTxtZ);

        JButton btnSRF = new JButton("Ustaw opady");
        btnSRF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String mess = "srf:" + newTxtZ.getText();
                    System.out.println(mess);
                    environment.getRivMap().get(name).setRainfall(Integer.parseInt(newTxtZ.getText()));
                    newLblSRF.setText(newTxtZ.getText());

                } catch (NumberFormatException | RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        btnSRF.setBounds(240, 60 + (25*rNumber), 125, 20);
        add(btnSRF);

        rivNames.add(name);

        lblRainfall.add(newLblSRF);
        txtZ.add(newTxtZ);

        rNumber += 1;

        revalidate();
        repaint();
    }

    public void update() throws RemoteException {
        if (environment != null) {
            if (rNumber != environment.getRivMap().size()) {
                for (String key : environment.getRivMap().keySet()) {
                    if (!rivNames.contains(key)) {
                        addRiverDisplay(key);
                        rivNames.add(key);
                    }
                }
            }

            for (int i = 0; i < rNumber; i++) {
                System.out.println("Riv " + i);

//            String rbName = rivNames.get(i);
//            int rainfall = environment.getRivMap().get(rbName).getRainfall();
//            lblRainfall.get(i).setText(String.valueOf(rainfall));

                System.out.println(txtZ.get(i));
            }
        }
    }
}
