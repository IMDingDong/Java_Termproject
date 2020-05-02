/*
 * 2015년 06월 20일 토요일
 * 대구가톨릭대학교 정보보호학과 14107050 임대동
 * 애플릿 기반의 채팅 프로그램
 * ChatThread.java
 */
import java.net.*;
import java.io.*;
import java.util.*;
public class ChatThread extends Thread{
	// 인수로 전달받은 서버 소켓과 소켓을 저장
	ChatServer myServer;
	Socket mySocket;

	PrintWriter out;
	BufferedReader in;

	public ChatThread(ChatServer server, Socket socket){
		super("ChatThread");

		myServer = server;
		mySocket = socket;

		out = null;
		in = null;
	}

	// 메시지를 보내는 메소드
	public void sendMessage(String msg) throws IOException{
		out.println(msg);
		out.flush();
	}

	// 연결을 끊어주는 메소드
	public void disconnect(){
		try{
			out.flush();
			in.close();
			out.close();
			mySocket.close();
			myServer.removeClient(this);
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
	}

	// 클라이언틀 데이터를 보냄
	public void run(){
		try{
			out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream(), "KSC5601"), true);
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream(), " KSC5601"), 1024);

			while(true){
				String inLine = in.readLine();
				if(!inLine.equals("") && !inLine.equals(null)){
					messageProcess(inLine);
				}
			}
		}
		catch(Exception e){
			disconnect();
		}
	}

	// 문자열에서 명령과 내용을 구분해주는 messageProcess 메소드
	public void messageProcess(String msg){
		System.out.println(msg);

		StringTokenizer st = new StringTokenizer(msg, "|");
		String command = st.nextToken();
		String talk = st.nextToken();

		if(command.equals("LOGIN")){
			System.out.println("[접속] " + mySocket);
			try{
				myServer.broadcast("[현재 접속자수] " + myServer.clientNum + "명");
			}
			catch(IOException e){
				System.out.println(e.toString());
			}
		}
		else if(command.equals("LOGOUT")){
			try{
				myServer.clientNum--;
				myServer.broadcast("[접속 종료] " + talk);
				myServer.broadcast("[현재 접속자수] " + myServer.clientNum + "명");
			}
			catch(IOException e){
				System.out.println(e.toString());
			}
			disconnect();
		}
		else{
			try{
				myServer.broadcast(talk);
			}
			catch(IOException e){
				System.out.println(e.toString());
			}
		}
	}
}