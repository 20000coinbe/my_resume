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
				if(sp2_msg.contains("�赿��")) {
					sp2_msg = sp2_msg.replaceAll("�赿��", "###");
				}
				else if(sp2_msg.contains("��ǻ�Ͱ��а�")) {
					sp2_msg = sp2_msg.replaceAll("��ǻ�Ͱ��а�", "#####");
				}
				else if(sp2_msg.contains("����")) {
					sp2_msg = sp2_msg.replaceAll("����", "##");
				}
				else if(sp2_msg.contains("����")) {
					sp2_msg = sp2_msg.replaceAll("����", "##");
				}
				else if(sp2_msg.contains("��Ʈ��ũ")) {
					sp2_msg = sp2_msg.replaceAll("��Ʈ��ũ", "####");
				}
				else if(sp2_msg.contains("���α׷���")) {
					sp2_msg = sp2_msg.replaceAll("���α׷���", "#####");
				}
				else if(sp2_msg.contains("����")) {
					sp2_msg = sp2_msg.replaceAll("����", "##");
				}
				else if(sp2_msg.contains("���")) {
					sp2_msg = sp2_msg.replaceAll("���", "##");
				}
				else if(sp2_msg.contains("����")) {
					sp2_msg = sp2_msg.replaceAll("����", "##");
				}
				else if(sp2_msg.contains("������Ʈ")) {
					sp2_msg = sp2_msg.replaceAll("������Ʈ", "####");
				}
				else if(sp2_msg.contains("����")) {
					sp2_msg = sp2_msg.replaceAll("����", "##");
				}
				else if(sp2_msg.contains("�ȳ��ϼ��� ������ �赿���Դϴ�")) {
					sp2_msg = sp2_msg.replaceAll("�ȳ��ϼ��� ������ �赿���Դϴ�", "�ȳ��ϼ��� ##�� ###�Դϴ�");
				}
				
				if(!sp2_msg.contains("��")) {
					check = false;
				}
			}

						
			return sp1_msg + ": "+ sp2_msg;
		
	}
	


}
