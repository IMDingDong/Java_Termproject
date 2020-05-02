/*
 * 2015년 06월 20일 토요일
 * 대구가톨릭대학교 정보보호학과 14107050 임대동
 * 애플릿 기반의 채팅 프로그램
 * ChatServer.java
 */
import java.net.*;
import java.io.*;
import java.util.*;
public class ChatServer {
	Vector clientVector = new Vector();
	int clientNum = 0;

	// 연결된 모든 채팅 클라이언트에게 데이터를 보내는 broadcast
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

	// Client를 제거하는 removeClient
	public void removeClient(ChatThread client){
		synchronized(clientVector){
			clientVector.removeElement(client);
			client = null;
			System.gc();
		}
	}

	// Client를 추가하는 addClient
	public void addClient(ChatThread client){
		synchronized(clientVector){
			clientVector.addElement(client);
		}
	}

	// main 메소드
	public static void main(String[] args){
		ServerSocket myServerSocket = null;	// myServerSocket을 생성해주고 널 값으로 초기화
		ChatServer myServer = new ChatServer();

		try{
			myServerSocket = new ServerSocket(2357);
		}
		catch(IOException e){
			System.out.println(e.toString());
			System.exit(-1);
		}

		System.out.println("[서버 대기 상태]" + myServerSocket);

		try{
			while(true){
				ChatThread client = new ChatThread(myServer, myServerSocket.accept());
				client.start();
				myServer.addClient(client);

				myServer.clientNum++;
				System.out.println("[현재 접속자수]" + myServer.clientNum + "명");	// 접속자수 출력
			}
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
	}
}