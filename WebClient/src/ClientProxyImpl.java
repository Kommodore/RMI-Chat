import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientProxyImpl extends UnicastRemoteObject implements ClientProxy{
	ChatServer stub;
	
	ClientProxyImpl() throws RemoteException {
	}
	
	void connect(String server_ip){
		try {
			stub = (ChatServer)Naming.lookup("rmi://"+server_ip+"/ChatServer");
		} catch (NotBoundException | MalformedURLException | RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void receiveMessage(String username, String message) throws RemoteException {
	
	}
}
