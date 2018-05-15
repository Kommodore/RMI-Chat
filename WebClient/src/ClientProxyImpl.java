import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientProxyImpl extends UnicastRemoteObject implements ClientProxy {
	ChatServer stub;
	
	private PropertyChangeSupport pcs;
	
	ClientProxyImpl() throws RemoteException {
		pcs = new PropertyChangeSupport(this);
	}
	
	void connect(String server_ip){
		try {
			stub = (ChatServer)Naming.lookup("rmi://"+server_ip+"/ChatServer");
		} catch (NotBoundException | MalformedURLException | RemoteException e) {
			e.printStackTrace();
		}
	}
	
	void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
	
	@Override
	public void receiveMessage(String username, String message) {
		pcs.firePropertyChange("msg", "", message);
	}

}
