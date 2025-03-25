package implementations;

import interfaces.ITailor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class PControlCenter extends JPanel {

    private ControlCenter cc = null;

    private JTextField txtPort, txtName, txtIp;
    private int rbNumber = 0;

    private List<String> rbNames = new ArrayList<String>();
    private List<JLabel> lblRbWaterPersent = new ArrayList<JLabel>();
    private List<JLabel> lblWaterDischarge = new ArrayList<JLabel>();
    private List<JTextField> txtZ = new ArrayList<JTextField>();

    /**
     * Create the panel.
     */
    public PControlCenter() {

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

        txtName = new JTextField("Kontroler 1");
        txtName.setBounds(265, 5, 120, 20);
        add(txtName);

        JToggleButton btnListen = new JToggleButton("Listen");

        btnListen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    cc = new ControlCenter();
                    Registry registry = LocateRegistry.getRegistry(txtIp.getText(), Integer.parseInt(txtPort.getText()));
                    ITailor it = (ITailor) registry.lookup("Tailor");

                    it.register(cc,txtName.getText());

                    for(String key : cc.getRbMap().keySet()) {
                        addRetensionBasinDisplay(key);
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

        JLabel lblNumber = new JLabel("Zb.:");
        lblNumber.setBounds(5, 35, 50, 20);
        add(lblNumber);

        JLabel lblBasinName = new JLabel("Name");
        lblBasinName.setBounds(40, 35, 70, 20);
        add(lblBasinName);

        JLabel lblRbWaterPersent = new JLabel("Stan wody:");
        lblRbWaterPersent.setBounds(140, 35, 70, 20);
        add(lblRbWaterPersent);

        JLabel LblSWD = new JLabel("Nastawy:");
        LblSWD.setBounds(210, 35, 70, 20);
        add(LblSWD);

    }

    public void addRetensionBasinDisplay(String name) {
        System.out.println("Added display retension basin " + name);

        JLabel lblZ = new JLabel("Z " + rbNumber + ":");
        lblZ.setBounds(5, 60 + (25*rbNumber), 30, 20);
        add(lblZ);

        JLabel lblName = new JLabel(name);
        lblName.setBounds(35, 60 + (25*rbNumber), 135, 20);
        add(lblName);

        JLabel newLblRbWaterPersent = new JLabel("0%");
        newLblRbWaterPersent.setBounds(170, 60 + (25*rbNumber), 60, 20);
        add(newLblRbWaterPersent);

        JLabel newLblSWD = new JLabel("100");
        newLblSWD.setBounds(240, 60 + (25*rbNumber), 40, 20);
        add(newLblSWD);

        JTextField newTxtZ = new JTextField();
        newTxtZ.setBounds(280, 60 + (25*rbNumber), 35, 20);
        add(newTxtZ);

        JButton btnSWD = new JButton("Ustaw nastawe");
        btnSWD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    cc.getRbMap().get(name).setWaterDischarge(Integer.parseInt(newTxtZ.getText()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnSWD.setBounds(330, 60 + (25*rbNumber), 125, 20);
        add(btnSWD);

        rbNames.add(name);

        lblRbWaterPersent.add(newLblRbWaterPersent);
        lblWaterDischarge.add(newLblSWD);
        txtZ.add(newTxtZ);

        rbNumber += 1;

        revalidate();
        repaint();
    }

    public void update() throws RemoteException {
        if (cc != null) {
            System.out.println(rbNumber);
            System.out.println(cc.getRbMap().size());


            if (rbNumber != cc.getRbMap().size()) {
                for (String key : cc.getRbMap().keySet()) {
                    if (!rbNames.contains(key)) {
                        addRetensionBasinDisplay(key);
                        rbNames.add(key);
                    }
                }
            }


            for (int i = 0; i < rbNumber; i++) {
                System.out.println("RB " + i);

                String rbName = rbNames.get(i);

                long rbWaterPersent = cc.getRbMap().get(rbName).getFillingPercentage();
                int waterDischarge = cc.getRbMap().get(rbName).getWaterDischarge();

                lblRbWaterPersent.get(i).setText(rbWaterPersent + "%");
                lblWaterDischarge.get(i).setText(String.valueOf(waterDischarge));

                System.out.println(txtZ.get(i));
            }
        }
    }
}
