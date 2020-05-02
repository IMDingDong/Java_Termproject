/*
 * 2015�� 06�� 20�� �����
 * �뱸���縯���б� ������ȣ�а� 14107050 �Ӵ뵿
 * ���ø� ����� ä�� ���α׷�
 * ChatServer.java
 */
import java.net.*;
import java.io.*;
import java.util.*;
public class ChatServer {
	Vector clientVector = new Vector();
	int clientNum = 0;

	// ����� ��� ä�� Ŭ���̾�Ʈ���� �����͸� ������ broadcast
	public void broadcast(String msg) throws IOException{
		synchronized(clientVector){
			for(int i=0; i < clientVector.size(); i++){
				ChatThread client = (ChatThread) clientVector.elementAt(i);
				synchronized(client){
					client.sendMessage(msg);
				}
			}
		}
	}

	// Client�� �����ϴ� removeClient
	public void removeClient(ChatThread client){
		synchronized(clientVector){
			clientVector.removeElement(client);
			client = null;
			System.gc();
		}
	}

	// Client�� �߰��ϴ� addClient
	public void addClient(ChatThread client){
		synchronized(clientVector){
			clientVector.addElement(client);
		}
	}

	// main �޼ҵ�
	public static void main(String[] args){
		ServerSocket myServerSocket = null;	// myServerSocket�� �������ְ� �� ������ �ʱ�ȭ
		ChatServer myServer = new ChatServer();

		try{
			myServerSocket = new ServerSocket(2357);
		}
		catch(IOException e){
			System.out.println(e.toString());
			System.exit(-1);
		}

		System.out.println("[���� ��� ����]" + myServerSocket);

		try{
			while(true){
				ChatThread client = new ChatThread(myServer, myServerSocket.accept());
				client.start();
				myServer.addClient(client);

				myServer.clientNum++;
				System.out.println("[���� �����ڼ�]" + myServer.clientNum + "��");	// �����ڼ� ���
			}
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
	}
}