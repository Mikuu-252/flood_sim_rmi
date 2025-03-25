package implementations;

import interfaces.IEnvironment;
import interfaces.IRetensionBasin;
import interfaces.IRiverSection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Environment extends UnicastRemoteObject implements IEnvironment {
    private Map<String, IRiverSection> rivMap = new HashMap<>();

    protected Environment() throws RemoteException {
    }

    @Override
    public void assignRiverSection(IRiverSection irs, String name) {
        rivMap.put(name, irs);
    }


    public Map<String, IRiverSection> getRivMap() {
        return rivMap;
    }
}
