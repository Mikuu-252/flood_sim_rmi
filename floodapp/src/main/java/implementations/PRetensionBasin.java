package implementations;

import interfaces.ITailor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PRetensionBasin extends JPanel {
    private RetensionBasin rb = null;

    // Display
    int riverNumber = 0;
    private List<JLabel> lblRivers = new ArrayList<JLabel>();
    JLabel lblWater;
    JLabel lblCapacity;
    JLabel lblIn;
    JLabel lblOut;

    private JTextField txtName, txtIp, txtPort;
    private JTextField txtMyCapacity;
    private JTextField txtRiverName;

    /**
     * Create the panel.
     */

    public PRetensionBasin() {
        setLayout(null);
        // --- My serv
        JLabel lblMyCapacity = new JLabel("Poj:");
        lblMyCapacity.setBounds(5, 5, 35, 20);
        add(lblMyCapacity);

        txtMyCapacity = new JTextField("100");
        txtMyCapacity.setBounds(30, 5, 40, 20);
        add(txtMyCapacity);

        JLabel lblMyPort = new JLabel("Nazwa:");
        lblMyPort.setBounds(75, 5, 50, 20);
        add(lblMyPort);

        txtName = new JTextField("Zbiornik 1");
        txtName.setBounds(125, 5, 80, 20);
        add(txtName);

        JLabel lblIp = new JLabel("Ip:");
        lblIp.setBounds(210, 5, 20, 20);
        add(lblIp);

        txtIp = new JTextField("localhost");
        txtIp.setBounds(230, 5, 100, 20);
        add(txtIp);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(335, 5, 40, 20);
        add(lblPort);

        txtPort = new JTextField("2000");
        txtPort.setBounds(370, 5, 40, 20);
        add(txtPort);

        JToggleButton btnListen = new JToggleButton("Listen");

        btnListen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    btnListen.setEnabled(false);

                    int capacity = Integer.parseInt(txtMyCapacity.getText());
                    lblCapacity.setText("/" + capacity);
                    txtMyCapacity.setEditable(false);

                    rb = new RetensionBasin(txtName.getText(), capacity);
                    Registry registry = LocateRegistry.getRegistry(txtIp.getText(), Integer.parseInt(txtPort.getText()));
                    ITailor it = (ITailor) registry.lookup("Tailor");

                    it.register(rb, txtName.getText());

                } catch (RemoteException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnListen.setBounds(415, 5, 80, 20);
        add(btnListen);

        JSeparator s1 = new JSeparator();
        s1.setBounds(0, 30, 520, 1);
        s1.setOrientation(SwingConstants.HORIZONTAL);
        add(s1);

        // Stan
        JSeparator tankBot = new JSeparator();
        tankBot.setBounds(250, 205, 100, 1);
        tankBot.setOrientation(SwingConstants.HORIZONTAL);
        add(tankBot);

        JSeparator tankLeft = new JSeparator();
        tankLeft.setBounds(250, 120, 1, 85);
        tankLeft.setOrientation(SwingConstants.VERTICAL);
        add(tankLeft);

        JSeparator tankRight = new JSeparator();
        tankRight.setBounds(350, 120, 1, 85);
        tankRight.setOrientation(SwingConstants.VERTICAL);
        add(tankRight);

        lblWater = new JLabel("0");
        lblWater.setBounds(265, 142, 40, 20);
        add(lblWater);

        lblCapacity = new JLabel("/0");
        lblCapacity.setBounds(303, 142, 40, 20);
        add(lblCapacity);

        JLabel lblArrowIn = new JLabel(">>>>>");
        lblArrowIn.setBounds(200, 142, 40, 20);
        add(lblArrowIn);

        lblIn = new JLabel("10000");
        lblIn.setBounds(200, 122, 40, 20);
        add(lblIn);

        JLabel lblArrowOut = new JLabel(">>>>>");
        lblArrowOut.setBounds(370, 142, 40, 20);
        add(lblArrowOut);

        lblOut = new JLabel("10000");
        lblOut.setBounds(370, 122, 40, 20);
        add(lblOut);
    }
    public void updateInFlowDisplay() {

        if(riverNumber != rb.getRiverInName().size()) {
            for (int i = riverNumber; i < rb.getRiverInName().size(); i++) {
                JLabel lblRiver = new JLabel("Rzeka " + rb.getRiverInName().get(i) + " >>");
                lblRiver.setBounds(5, 90 + (20 * riverNumber), 150, 20);
                add(lblRiver);

                JLabel newLblRiverIn = new JLabel("0");
                newLblRiverIn.setBounds(125, 90 + (20 * riverNumber), 40, 20);
                add(newLblRiverIn);

                lblRivers.add(newLblRiverIn);

                riverNumber += 1;
                revalidate();
                repaint();
            }
        }

        for (int i = 0; i < riverNumber; i++) {
            lblRivers.get(i).setText(String.valueOf(rb.getRiverInFlows().get(i)));

        }
    }
    public void update() throws RemoteException {
        if(rb != null) {
            if (rb.isSimStart()) {
                rb.update();
                updateInFlowDisplay();

                lblWater.setText(String.valueOf(rb.getWater()));
                lblIn.setText(String.valueOf(rb.getWaterIn()));
                lblOut.setText(String.valueOf(rb.getWaterOut()));


            }
        }
    }
}

