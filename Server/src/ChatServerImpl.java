import java.rmi.RemoteException;

public class ChatServerImpl implements ChatServer{
	
	@Override
	public ChatProxy subscribeUser(String username, ClientProxy handle) throws RemoteException {
		return null;
	}
	
	@Override
	public ChatProxy unsubscribeUser(String username) throws RemoteException {
		return null;
	}
	
	public static void main(String[] args) {
	
	}
}
