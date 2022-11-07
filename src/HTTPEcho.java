
import java.net.*;
import java.io.*;

public class HTTPEcho {

	public static void main(String[] args) throws IOException{
		
		String clientMsg = "";
		String ln = "";

		ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("Open");
		
		while(true) {
			Socket connectionSocket = welcomeSocket.accept();
			System.out.println("Connected");
		
			// inbound
			BufferedReader fromClient = 
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			ln = fromClient.readLine();
			clientMsg += ln + '\n';
			while((ln != null) && (ln.length() != 0)) {
				ln = fromClient.readLine();
				clientMsg += ln + '\n';
			}
		
			System.out.println("***HTTP request:***");
			System.out.println(clientMsg);
			
			
			// outbound
			DataOutputStream toClient = 
				new DataOutputStream(connectionSocket.getOutputStream());
			
			toClient.writeBytes("HTTP/1.1 200 OK\n\n");
			toClient.writeBytes(clientMsg + '\r' + '\n' + '\r' + '\n');
			System.out.println("Sent back");
			
			
			// close
			clientMsg = "";
			connectionSocket.close();
			fromClient.close();
			toClient.flush();
			toClient.close();
			System.out.println("closed");
			
			
		}
		
	}

}
