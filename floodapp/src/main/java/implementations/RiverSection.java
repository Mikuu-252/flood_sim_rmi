package implementations;

import interfaces.IRetensionBasin;
import interfaces.IRiverSection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RiverSection extends UnicastRemoteObject implements IRiverSection {
    private String name = null;
    private int waterRainfall = 0;
    private int waterBasinIn = 0;
    private int waterOut;
    private List<Integer> waterSegments = new ArrayList<Integer>();
    private int segments = 0;
    private boolean simStart = false;

    // End basin
    private IRetensionBasin endBasin = null;


    protected RiverSection(String name, int segments) throws RemoteException {
        this.name = name;
        this.segments = segments;

        for (int i = 0; i < segments; i++) {
            waterSegments.add(0);
        }

        simStart = true;
    }

    public int getWaterRainfall() {
        return waterRainfall;
    }

    public int getWaterBasinIn() {
        return waterBasinIn;
    }

    public List<Integer> getWaterSegments() {
        return waterSegments;
    }

    public int getSegments() {
        return segments;
    }

    public boolean isSimStart() {
        return simStart;
    }

    public int getWaterOut() {
        return waterOut;
    }

    public IRetensionBasin getEndBasin() {
        return endBasin;
    }

    @Override
    public void setRealDischarge(int realDischarge) throws RemoteException{
        waterBasinIn = realDischarge;
    }

    @Override
    public void setRainfall(int rainfall) throws RemoteException{
        waterRainfall = rainfall;
    }

    @Override
    public void assignRetensionBasin(IRetensionBasin irb, String name) throws RemoteException{
            endBasin = irb;
    }

    public void update() throws RemoteException {
        if(simStart) {
            int waterIn = waterBasinIn + waterRainfall;

            //Fill and flow
            int nextWaterSegment = 0;
            waterOut = 0;

            for (int i = 0; i <= segments; i++) {
                if(i == 0) {
                    nextWaterSegment = waterSegments.get(i);
                    waterSegments.set(i, waterIn);
                } else if(i == segments) {
                    waterOut = nextWaterSegment;
                } else {
                    int waterTemp = waterSegments.get(i);
                    waterSegments.set(i, nextWaterSegment);
                    nextWaterSegment = waterTemp;
                }
            }

            if (endBasin != null) {
                endBasin.setWaterInflow(waterOut, name);
            }
        }
    }
}
