package application;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;

public class Client { //1명의 클라이언트와 통신을 하기위한
	Socket socket;

	
	public Client(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	//클라이언트로부터 메시지를 전달받는 메소드
	public void receive() {
		Runnable thread = new Runnable() {	
			@Override
			public void run() {
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						
						int length = in.read(buffer); //담긴 메시지의 크기
						while(length == -1) throw new IOException();
						System.out.println("[메시지 출력 성공]" + socket.getRemoteSocketAddress()
												+" : " + Thread.currentThread().getName());
						
						String msg = new String(buffer, 0, length, "UTF-8");
						String temp = msg;
						ChatingBot chatbot = new ChatingBot();
						String msg2 = chatbot.setting(temp);
						System.out.println(msg2);
						for(Client client : Main.clients) {
							client.send(msg2); 
							////////////////보냄
							
						}//받아온 메시시지를 클라이언트에게 전달
					}
				} catch (Exception e) {
					// TODO: handle exception
					try {
						System.out.println("[메시지 수신 오류]" + socket.getRemoteSocketAddress()
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
	
	//클라이언트에게 메시지를 전송하는 메소드
	public void send(String msg) {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					OutputStream os = socket.getOutputStream(); //위에서 받는 것이고 반대로 메시지를 보내주자
					byte[] buffer = msg.getBytes("UTF-8");
					os.write(buffer);
					os.flush();
				} catch (Exception e) {
					// TODO: handle exception
					try {
						System.out.println("메시지 송신 오류" + socket.getRemoteSocketAddress()
												+ " : " + Thread.currentThread().getName());
						Main.clients.remove(Client.this); //오류가 생겼으니 오류클라이언트 제거
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
			}
		};
		Main.threadPool.submit(thread); //쓰레드풀에 추가
	}
}
