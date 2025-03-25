package implementations;

import interfaces.ITailor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class PRiverSection extends JPanel {
    private RiverSection riv = null;

    // Display
    private List<JLabel> lblWaterSegments = new ArrayList<JLabel>();
    JLabel lblBasinIn;
    JLabel lblRainfall;
    JLabel lblOut;

    // Serv
    private JTextField txtName, txtIp, txtPort;
    private JTextField txtMySegments;

    private JTextField txtBasinName;


    /**
     * Create the panel.
     */

    public PRiverSection() {
        setLayout(null);
        // --- My serv
        JLabel lblMySegments = new JLabel("Segmenty:");
        lblMySegments.setBounds(5, 5, 60, 20);
        add(lblMySegments);

        txtMySegments = new JTextField("5");
        txtMySegments.setBounds(70, 5, 20, 20);
        add(txtMySegments);

        JLabel lblName = new JLabel("Nazwa:");
        lblName.setBounds(110, 5, 55, 20);
        add(lblName);

        txtName = new JTextField("Rzeka 1");
        txtName.setBounds(160, 5, 120, 20);
        add(txtName);

        JLabel lblIp = new JLabel("Ip:");
        lblIp.setBounds(285, 5, 20, 20);
        add(lblIp);

        txtIp = new JTextField("localhost");
        txtIp.setBounds(310, 5, 100, 20);
        add(txtIp);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(415, 5, 40, 20);
        add(lblPort);

        txtPort = new JTextField("2000");
        txtPort.setBounds(450, 5, 40, 20);
        add(txtPort);


        JToggleButton btnListen = new JToggleButton("Listen");

        btnListen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    btnListen.setEnabled(false);

                    int segments = Integer.parseInt(txtMySegments.getText());
                    txtMySegments.setEditable(false);

                    riv = new RiverSection(txtName.getText(), segments);

                    generateRiver();

                    Registry registry = LocateRegistry.getRegistry(txtIp.getText(), Integer.parseInt(txtPort.getText()));
                    ITailor it = (ITailor) registry.lookup("Tailor");

                    it.register(riv, txtName.getText());

                } catch (RemoteException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnListen.setBounds(495, 5, 80, 20);
        add(btnListen);

        JSeparator s1 = new JSeparator();
        s1.setBounds(0, 30, 625, 1);
        s1.setOrientation(SwingConstants.HORIZONTAL);
        add(s1);
    }

    public void generateRiver() {
        System.out.println("Generowanie rzeczki z " + riv.getSegments() + " segmentami." );

        for (int i = 0; i < riv.getSegments(); i++) {
            JSeparator riverWall = new JSeparator();
            riverWall.setBounds(60 + (50 * i), 120, 1, 60);
            riverWall.setOrientation(SwingConstants.VERTICAL);
            add(riverWall);

            JLabel newLblWaterSegment = new JLabel("00000");
            newLblWaterSegment.setBounds(70 + (50 * i), 145, 50, 20);
            add(newLblWaterSegment);
            lblWaterSegments.add(newLblWaterSegment);
        }

        JSeparator riverEnd = new JSeparator();
        riverEnd.setBounds(60 + (50 * riv.getSegments()), 120, 1, 60);
        riverEnd.setOrientation(SwingConstants.VERTICAL);
        add(riverEnd);

        JLabel lblArrowBasinIn = new JLabel(">>>>>");
        lblArrowBasinIn.setBounds(5, 145, 40, 20);
        add(lblArrowBasinIn);

        lblBasinIn = new JLabel("Bi");
        lblBasinIn.setBounds(15, 125, 40, 20);
        add(lblBasinIn);


        JLabel lblArrowRainfall = new JLabel("v");
        lblArrowRainfall.setBounds(80, 100, 40, 20);
        add(lblArrowRainfall);

        lblRainfall = new JLabel("Rf");
        lblRainfall.setBounds(80, 80, 40, 20);
        add(lblRainfall);

        JLabel lblArrowOut = new JLabel(">>>>>");
        lblArrowOut.setBounds(80 + (50 * riv.getSegments()), 145, 40, 20);
        add(lblArrowOut);

        lblOut = new JLabel("10000");
        lblOut.setBounds(80 + (50 * riv.getSegments()), 125, 40, 20);
        add(lblOut);

        revalidate();
        repaint();
    }

    public void update() throws RemoteException {
        if (riv != null) {
            if (riv.isSimStart()) {
                riv.update();

                for (int i = 0; i < riv.getWaterSegments().size(); i++) {
                    lblWaterSegments.get(i).setText(riv.getWaterSegments().get(i).toString());
                }

                lblBasinIn.setText(String.valueOf(riv.getWaterBasinIn()));
                lblRainfall.setText(String.valueOf(riv.getWaterRainfall()));
                lblOut.setText(String.valueOf(riv.getWaterOut()));
            }
        }
    }
}
