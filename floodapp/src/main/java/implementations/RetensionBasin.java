package implementations;

import interfaces.IRetensionBasin;
import interfaces.IRiverSection;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RetensionBasin extends UnicastRemoteObject implements IRetensionBasin {
    private String name = null;
    private int waterDischarge = 0;
    private int water = 0;
    private int waterIn = 0;
    private int waterOut = 0;
    private int capacity;
    private boolean simStart;

    private List<String> riverInName = new ArrayList<String>();
    private List<Integer> riverInFlows = new ArrayList<Integer>();

    int riverNumber = 0;
    private Map<String, IRiverSection> riverMap = new HashMap<>();

    // End river
    private IRiverSection endRiver = null;

    protected RetensionBasin(String name, int capacity) throws RemoteException {
        this.name = name;
        this.capacity = capacity;
        simStart = true;
    }

    public int getWater() {
        return water;
    }

    public boolean isSimStart() {
        return simStart;
    }

    public int getRiverNumber() {
        return riverNumber;
    }

    public Map<String, IRiverSection> getRiverMap() {
        return riverMap;
    }

    public List<Integer> getRiverInFlows() {
        return riverInFlows;
    }

    public int getWaterIn() {
        return waterIn;
    }

    public int getWaterOut() {
        return waterOut;
    }

    public List<String> getRiverInName() {
        return riverInName;
    }

    @Override
    public int getWaterDischarge() throws RemoteException {
        return waterDischarge;
    }

    @Override
    public long getFillingPercentage() throws RemoteException {
        double res = ((double)water / (double)capacity) * 100;
        return (long)res;
    }

    @Override
    public void setWaterDischarge(int waterDischarge) throws RemoteException {
        this.waterDischarge = waterDischarge;
    }

    @Override
    public void setWaterInflow(int waterInflow, String name) throws RemoteException {
        System.out.println("Dostaje wody: " + waterInflow + " z rzeki: " + name);

        int index = -1;

        for (int i = 0; i < riverInName.size(); i++) {
            if (Objects.equals(name, riverInName.get(i))) index = i;
        }

        if (index != -1) {
            riverInFlows.set(index, waterInflow);
        } else {
            riverInName.add(name);
            riverInFlows.add(waterInflow);

            riverNumber += 1;
        }
    }

    @Override
    public void assignRiverSection(IRiverSection irs, String name) throws RemoteException {
        endRiver = irs;
    }

    public void update() throws RemoteException {
        if(simStart) {
            waterIn = 0;

            for (Integer riverInFlow : riverInFlows) {
                waterIn += riverInFlow;
            }

            //Fill
            water += waterIn;

            //Out
            waterOut = waterDischarge;
            int overflow = water - capacity;
            if (overflow > waterDischarge) {
                waterOut = overflow;
            } else if (waterOut > water) {
                waterOut = water;
            }

            water -= waterOut;

            if (endRiver != null) {
                endRiver.setRealDischarge(waterOut);
            }
        }
    }
}
