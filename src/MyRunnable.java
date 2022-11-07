
import java.net.*;
import java.io.*;

public class MyRunnable implements Runnable {

	private Socket connectionSocket;

	public MyRunnable(Socket param) {
		this.connectionSocket = param;
	}

	public void run() {
		
		// message strings
		String clientMsg = "";
		String ln = "";
		String serverMsg = "200 OK";
		String toBrowser = "";
		// parameters
		String hostname = "";
		String port = "";
		String toServer = "";

		try {
			// inbound
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			// read all lines
			ln = fromClient.readLine();
			clientMsg += ln + '\n';
			while ((ln != null) && (ln.length() != 0)) {
				ln = fromClient.readLine();
				clientMsg += ln + '\n';
			}

			// string processing!!
			try {
				// GET, only first line this time
				String[] splitStr = clientMsg.split("\n");
				// split into relevant parts (GET + param + HTTP)
				splitStr = splitStr[0].split(" ");
				// check for "GET"
				if (!splitStr[0].equals("GET")) {
					serverMsg = "400 Bad Request";
				}
				// check for "HTTP/1.1"
				if (!splitStr[2].equals("HTTP/1.1")) {
					serverMsg = "400 Bad Request";
				}
				// split param part
				splitStr = splitStr[1].split("hostname=");
				// check for "/ask?"
				if (!splitStr[0].equals("/ask?")) {
					serverMsg = "400 Bad Request";
				}
				// split to hostname
				splitStr = splitStr[1].split("&");
				hostname = splitStr[0];
				// divide into port and (optional)string
				String[] portArr = splitStr[1].split("port=");
				port = portArr[1];
				// check for string, extract if exists
				String[] strArr;
				toServer = "";
				if (splitStr.length > 2) {
					strArr = splitStr[2].split("string=");
					toServer = strArr[1];
				}

			} catch (Exception e) {
				serverMsg = "400 Bad Request";
			}

			// skip askServer-call if bad request
			if (!serverMsg.equals("400 Bad Request")) {
				// call askServer!!
				try {
					int prt = Integer.parseInt(port);

					if (toServer.equals("")) {
						toBrowser = ConcHTTPAsk.askServer(hostname, prt);
					} else {
						toBrowser = ConcHTTPAsk.askServer(hostname, prt, toServer);
					}

				} catch (Exception e) {
					serverMsg = "404 Not Found";
				}
			}

			// outbound
			DataOutputStream toClient = new DataOutputStream(connectionSocket.getOutputStream());

			// return answer to browser
			toClient.writeBytes("HTTP/1.1 " + serverMsg + "\n\n");
			toClient.writeBytes(toBrowser + '\r' + '\n' + '\r' + '\n');
			System.out.println("Sent back");

			// close and reset
			clientMsg = "";
			serverMsg = "200 OK";
			toBrowser = "";
			hostname = "";
			port = "";
			toServer = "";

			connectionSocket.close();
			fromClient.close();
			toClient.flush();
			toClient.close();
			System.out.println("closed");

		} catch (IOException e) {
			System.out.println("Error!!!!");
		}

		
		
	}

}
