
import java.net.*;
import java.io.*;
//import HTTPAsk.TCPClient;

public class ConcHTTPAsk {

	
	public static String askServer(String hostname, int port, String ToServer) throws IOException {
		if (ToServer == null) {
			return (askServer(hostname, port));
		}
		System.out.println("1st function...");
		// Connection
		Socket clientSocket = new Socket(hostname, port);
		clientSocket.setSoTimeout(5000);

		// outbound
		DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
		toServer.writeBytes(ToServer + '\n');

		// inbound
		String serverResponse = "";
		int responseValue = 0;

		try {
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			long start = System.currentTimeMillis();
			while ((responseValue = fromServer.read()) != -1 && ((System.currentTimeMillis() - start) < 4000)) {
				serverResponse += (char) responseValue;
			}

		} catch (SocketTimeoutException e) {
			clientSocket.close();
			return serverResponse;
		}

		clientSocket.close();
		return serverResponse;
	}

	public static String askServer(String hostname, int port) throws IOException {
		System.out.println("2nd function...");
		// Connection
		Socket clientSocket = new Socket(hostname, port);
		clientSocket.setSoTimeout(5000);

		// inbound
		String serverResponse = "";
		int responseValue = 0;

		BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		long start = System.currentTimeMillis();
		while ((responseValue = fromServer.read()) != -1 && ((System.currentTimeMillis() - start) < 4000)) {
			serverResponse += (char) responseValue;
		}

		clientSocket.close();
		return serverResponse;
	}
	
	

	public static void main(String[] args) throws IOException {

		ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("Open");

		while (true) {
			Socket connectionSocket;
			// try connection, if error try again
			try {
				connectionSocket = welcomeSocket.accept();
				System.out.println("Connected");

			} catch (Exception e) {
				continue;
			}
			// create new thread with existing connection
			(new Thread(new MyRunnable(connectionSocket))).start();

		}

	}
	
}
