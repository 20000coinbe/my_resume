package application;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;

public class Client { //1���� Ŭ���̾�Ʈ�� ����� �ϱ�����
	Socket socket;

	
	public Client(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	//Ŭ���̾�Ʈ�κ��� �޽����� ���޹޴� �޼ҵ�
	public void receive() {
		Runnable thread = new Runnable() {	
			@Override
			public void run() {
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						
						int length = in.read(buffer); //��� �޽����� ũ��
						while(length == -1) throw new IOException();
						System.out.println("[�޽��� ��� ����]" + socket.getRemoteSocketAddress()
												+" : " + Thread.currentThread().getName());
						
						String msg = new String(buffer, 0, length, "UTF-8");
						String temp = msg;
						ChatingBot chatbot = new ChatingBot();
						String msg2 = chatbot.setting(temp);
						System.out.println(msg2);
						for(Client client : Main.clients) {
							client.send(msg2); 
							////////////////����
							
						}//�޾ƿ� �޽ý����� Ŭ���̾�Ʈ���� ����
					}
				} catch (Exception e) {
					// TODO: handle exception
					try {
						System.out.println("[�޽��� ���� ����]" + socket.getRemoteSocketAddress()
												+ " : " + Thread.currentThread().getName());
						Main.clients.remove(Client.this);
						socket.close();
					} catch (Exception e2) {
						// TODO: handle exception
						System.out.println(e2);
					}
				}
			}
		};
		Main.threadPool.submit(thread);
		 
		
	}
	
	//Ŭ���̾�Ʈ���� �޽����� �����ϴ� �޼ҵ�
	public void send(String msg) {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					OutputStream os = socket.getOutputStream(); //������ �޴� ���̰� �ݴ�� �޽����� ��������
					byte[] buffer = msg.getBytes("UTF-8");
					os.write(buffer);
					os.flush();
				} catch (Exception e) {
					// TODO: handle exception
					try {
						System.out.println("�޽��� �۽� ����" + socket.getRemoteSocketAddress()
												+ " : " + Thread.currentThread().getName());
						Main.clients.remove(Client.this); //������ �������� ����Ŭ���̾�Ʈ ����
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
			}
		};
		Main.threadPool.submit(thread); //������Ǯ�� �߰�
	}
}
