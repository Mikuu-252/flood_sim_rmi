package implementations;

import interfaces.IControlCenter;
import interfaces.IRetensionBasin;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ControlCenter extends UnicastRemoteObject implements IControlCenter {
    private Map<String, IRetensionBasin> rbMap = new HashMap<>();

    protected ControlCenter() throws RemoteException {
    }

    @Override
    public void assignRetensionBasin(IRetensionBasin irb, String name) {
        System.out.println("Assigning retension basin " + name);
        rbMap.put(name, irb);
    }


    public Map<String, IRetensionBasin> getRbMap() {
        return rbMap;
    }
}
