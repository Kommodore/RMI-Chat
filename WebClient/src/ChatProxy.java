import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatProxy extends Remote {
	void sendMessage(String message) throws RemoteException;
}
