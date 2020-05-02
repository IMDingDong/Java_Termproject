/*
 * 2015년 06월 20일 토요일
 * 대구가톨릭대학교 정보보호학과 14107050 임대동
 * 애플릿 기반의 채팅 프로그램
 * ChatClient.java
 */
import java.net.*;
import java.io.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient extends Applet implements ActionListener, Runnable{
	Socket mySocket = null;
	PrintWriter out = null;
	BufferedReader in = null;

	Thread clock;
	TextArea memo;
	TextField name;
	TextField input;
	Panel myPanel;

	// 애플릿이 생성되고 바로 해야하는 동작이 있는 init 메소드
	public void init(){
		try{
			mySocket = new Socket(getCodeBase().getHost(), 2357);
			out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream(), "KSC5601"), true);
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream(),"KSC5601"), 1024);
		}
		catch(UnknownHostException e){
			System.out.println(e.toString());
		}
		catch(IOException e){
			System.out.println(e.toString());
		}

		//GUI
		setLayout(new BorderLayout());
		memo = new TextArea(10, 55);
		add("Center", memo);

		myPanel = new Panel();
		name = new TextField(8);
		name.setText("대화명");
		myPanel.add(name);
		input = new TextField(40);
		input.addActionListener(this);
		myPanel.add(input);
		add("South", myPanel);
	}

	// 서버에서 지속적으로 받은 서버의 데이터를 화면에 출력하는 start 메소드
	public void start(){
		if(clock == null){
			clock = new Thread(this);
			clock.start();
		}
	}

	// 서버로부터 받은 데이터가 null이 아니라면 화면에 출력하는 동작을 반복하는 run 메소드
	public void run(){
		out.println("LOGIN| " + mySocket);
		memo.append("[접속] " + getCodeBase().toString() + "\n");

		try{
			while(true){
				String msg = in.readLine();
				if(!msg.equals("") && !msg.equals(null)){
					memo.append(msg + "\n");
				}
			}
		}
		catch(IOException e){
			memo.append(e.toString() + "\n");
		}
	}

	// 입력한 문장을 보낼 때의 actionPerformed 메소드
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == input){
			String data = input.getText();
			input.setText("");
			out.println("TALK| " + name.getText() + ": " + data);
			out.flush();
		}
	}

	// 종료할 때 stop 메소드
	public void stop(){
		if((clock != null) && (clock.isAlive())){
			clock = null;
		}

		out.println("LOGOUT|" + name.getText());
		out.flush();

		try{
			out.close();
			in.close();
			mySocket.close();
		}
		catch(IOException e){
			memo.append(e.toString() + "\n");
		}
	}
}