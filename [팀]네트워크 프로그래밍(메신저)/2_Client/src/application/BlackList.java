package application;

import java.io.*;
import java.util.Iterator;
import java.util.Vector;
import java.util.*;

public class BlackList extends Thread{
	
	
	ListKickOut lko = new ListKickOut();
	public String[] wordArray;

	public String blackList(String msg) {
			msg.trim();
			wordArray = msg.split(": ", 2);
			String sp1_msg, sp2_msg;
			sp1_msg = wordArray[0];
			sp2_msg = wordArray[1];
			
			lko.addArrayList(sp1_msg);
			
			boolean check = true;
			
			while(check) {
				if(sp2_msg.contains("김동석")) {
					sp2_msg = sp2_msg.replaceAll("김동석", "###");
				}
				else if(sp2_msg.contains("컴퓨터공학과")) {
					sp2_msg = sp2_msg.replaceAll("컴퓨터공학과", "#####");
				}
				else if(sp2_msg.contains("대전")) {
					sp2_msg = sp2_msg.replaceAll("대전", "##");
				}
				else if(sp2_msg.contains("공부")) {
					sp2_msg = sp2_msg.replaceAll("공부", "##");
				}
				else if(sp2_msg.contains("네트워크")) {
					sp2_msg = sp2_msg.replaceAll("네트워크", "####");
				}
				else if(sp2_msg.contains("프로그래밍")) {
					sp2_msg = sp2_msg.replaceAll("프로그래밍", "#####");
				}
				else if(sp2_msg.contains("소켓")) {
					sp2_msg = sp2_msg.replaceAll("소켓", "##");
				}
				else if(sp2_msg.contains("통신")) {
					sp2_msg = sp2_msg.replaceAll("통신", "##");
				}
				else if(sp2_msg.contains("과제")) {
					sp2_msg = sp2_msg.replaceAll("과제", "##");
				}
				else if(sp2_msg.contains("프로젝트")) {
					sp2_msg = sp2_msg.replaceAll("프로젝트", "####");
				}
				else if(sp2_msg.contains("ㅁㅊ")) {
					sp2_msg = sp2_msg.replaceAll("ㅁㅊ", "##");
				}
				else if(sp2_msg.contains("안녕하세요 대전대 김동석입니다")) {
					sp2_msg = sp2_msg.replaceAll("안녕하세요 대전대 김동석입니다", "안녕하세요 ##대 ###입니다");
				}
				
				if(!sp2_msg.contains("ㅄ")) {
					check = false;
				}
			}

						
			return sp1_msg + ": "+ sp2_msg;
		
	}
	


}
