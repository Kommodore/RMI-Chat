import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
	ChatProxy subscribeUser(String username, ClientProxy handle) throws RemoteException;
	
	ChatProxy unsubscribeUser(String username) throws RemoteException;
}
