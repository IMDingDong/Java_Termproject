/*
 * 2015�� 06�� 20�� �����
 * �뱸���縯���б� ������ȣ�а� 14107050 �Ӵ뵿
 * ���ø� ����� ä�� ���α׷�
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

	// ���ø��� �����ǰ� �ٷ� �ؾ��ϴ� ������ �ִ� init �޼ҵ�
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
		name.setText("��ȭ��");
		myPanel.add(name);
		input = new TextField(40);
		input.addActionListener(this);
		myPanel.add(input);
		add("South", myPanel);
	}

	// �������� ���������� ���� ������ �����͸� ȭ�鿡 ����ϴ� start �޼ҵ�
	public void start(){
		if(clock == null){
			clock = new Thread(this);
			clock.start();
		}
	}

	// �����κ��� ���� �����Ͱ� null�� �ƴ϶�� ȭ�鿡 ����ϴ� ������ �ݺ��ϴ� run �޼ҵ�
	public void run(){
		out.println("LOGIN| " + mySocket);
		memo.append("[����] " + getCodeBase().toString() + "\n");

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

	// �Է��� ������ ���� ���� actionPerformed �޼ҵ�
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == input){
			String data = input.getText();
			input.setText("");
			out.println("TALK| " + name.getText() + ": " + data);
			out.flush();
		}
	}

	// ������ �� stop �޼ҵ�
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