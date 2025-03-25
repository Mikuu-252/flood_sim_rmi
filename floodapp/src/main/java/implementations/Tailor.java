package implementations;

import interfaces.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tailor implements ITailor {
    Map<String,Remote> ccmap = new HashMap<>();
    Map<String,Remote> envmap = new HashMap<>();
    Map<String,Remote> rbmap = new HashMap<>();
    Map<String,Remote> rivmap = new HashMap<>();

    @Override
    public boolean register(Remote r, String name) throws RemoteException {
        /*
        // informację na temat tego na jakim hoście i porcie rzeczywiście działa namiastka
        // chyba najłatwiej uzyskać parsując wynik metody toString()
        // metoda ta zwraca ciąg znaków podobny do poniższego:
        // Proxy[IControlCenter,RemoteObjectInvocationHandler[UnicastRef [liveRef: [endpoint:[192.168.1.153:3000](remote),objID:[-50fb9f25:1945c684c6d:-7fff, 7487353482432237380]]]]]
        // wystarczy więc wyciągnąć z niego podciąg korzystając z regexp
        Pattern pattern = Pattern.compile(".*endpoint:\\[(.*)\\]\\(remote.*");
        Matcher matcher = pattern.matcher(r.toString());
        if (matcher.find())
        {
            System.out.println(matcher.group(1)); //
        }
        czyli zamiast name można byłoby użyć wyciągnięty ciąg znaków host:port
        ale to byłoby mało czytelne
        */

        if(r instanceof IControlCenter) {
            if(!ccmap.containsKey(name)) {
                ccmap.put(name,r);
                System.out.println("registration of control center named: " + name);
                return true;
            }
            else return false;
        }

        if(r instanceof IEnvironment) {
            if(!envmap.containsKey(name)) {
                envmap.put(name,r);
                System.out.println("registration of environment named: " + name);
                return true;
            }
            else return false;
        }

        if(r instanceof IRetensionBasin) {
            if(!rbmap.containsKey(name) && !rivmap.containsKey(name)) {
                rbmap.put(name,r);
                System.out.println("registration of retension basin named: " + name);
                return true;
            }
            return false;
        }

        if(r instanceof IRiverSection) {
            if(!rivmap.containsKey(name) && !rbmap.containsKey(name)) {
                rivmap.put(name,r);
                System.out.println("registration of river named: " + name);
                return true;
            }
            return false;
        }

        return false;
    }

    public void connect(String name1, String name2) throws RemoteException {
        String key = "";
        Remote value = null;
        Map<String, Remote>[] maps = new Map[] {rbmap, rivmap, ccmap, envmap};

        for (Map<String, Remote> map : maps) {
            for (Map.Entry<String, Remote> entry : map.entrySet()) {
                if (entry.getKey().equals(name1)) {
                    key = entry.getKey();
                    value = entry.getValue();
                    break;
                }
            }

            if (!key.isEmpty()) {
                System.out.println("Connecting to Key: " + key + " Value: " + value + " to " + name2);

                if(value instanceof IControlCenter) {
                    for (Map.Entry<String, Remote> entry : rbmap.entrySet()) {
                        if (entry.getKey().equals(name2)) {
                            String keyConn = entry.getKey();
                            IRetensionBasin valueConn = (IRetensionBasin) entry.getValue();
                            ((IControlCenter) value).assignRetensionBasin(valueConn, keyConn);
                        }
                    }
                } else if (value instanceof IEnvironment) {
                    for (Map.Entry<String, Remote> entry : rivmap.entrySet()) {
                        if (entry.getKey().equals(name2)) {
                            String keyConn = entry.getKey();
                            IRiverSection valueConn = (IRiverSection) entry.getValue();
                            ((IEnvironment) value).assignRiverSection(valueConn, keyConn);
                        }
                    }
                } else if (value instanceof IRetensionBasin) {
                    for (Map.Entry<String, Remote> entry : rivmap.entrySet()) {
                        if (entry.getKey().equals(name2)) {
                            String keyConn = entry.getKey();
                            IRiverSection valueConn = (IRiverSection) entry.getValue();
                            ((IRetensionBasin) value).assignRiverSection(valueConn, keyConn);
                        }
                    }
                } else if (value instanceof IRiverSection) {
                    for (Map.Entry<String, Remote> entry : rbmap.entrySet()) {
                        if (entry.getKey().equals(name2)) {
                            String keyConn = entry.getKey();
                            IRetensionBasin valueConn = (IRetensionBasin) entry.getValue();
                            ((IRiverSection) value).assignRetensionBasin(valueConn, keyConn);
                        }
                    }
                }
                break;
            }
        }

        if (key.isEmpty()) {
            System.out.println("Name1 not found");
        }
    }

    @Override
    public boolean unregister(Remote r) throws RemoteException {
        return false;
    }

    public Tailor(int port) {
        try {
            ITailor it = (ITailor) UnicastRemoteObject.exportObject(this,port);
            Registry r = LocateRegistry.createRegistry(port);
            r.rebind("Tailor", it);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Remote> getCcmap() {
        return ccmap;
    }

    public Map<String, Remote> getEnvmap() {
        return envmap;
    }

    public Map<String, Remote> getRbmap() {
        return rbmap;
    }

    public Map<String, Remote> getRivmap() {
        return rivmap;
    }
}
