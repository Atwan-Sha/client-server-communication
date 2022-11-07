
import java.net.*;
import java.io.*;

public class HTTPAsk {

	public static class TCPClient {

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
	}

	public static void main(String[] args) throws IOException {
		// message strings
		String clientMsg = "";
		String ln = "";
		String serverMsg = "200 OK";
		String toBrowser = "";
		// parameters
		String hostname = "";
		String port = "";
		String toServer = "";

		ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("Open");

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			System.out.println("Connected");

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
						toBrowser = TCPClient.askServer(hostname, prt);
					} else {
						toBrowser = TCPClient.askServer(hostname, prt, toServer);
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

		}

	}

}
