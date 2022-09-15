package com.brilcom.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Score {

	public static void main(String[] args) {
		
		HashMap<String, Integer> grade = new HashMap<>();
		grade.put("a", 5);
        grade.put("b", 4);
        grade.put("c", 3);
        grade.put("d", 2);
        grade.put("e", 1);
        
       // System.out.println(grade);
		
		String select = "a,a,b,b,b";
	
		int score = 0;
		
		ArrayList<String> integers1  = new ArrayList<>();
				
		StringTokenizer st = new StringTokenizer(select, ",");
		try {
			String token = null;
			int sum = 0;
			int result = 0;
			while (st.hasMoreElements())
			{
				token = st.nextToken();
				
				/*
				if(token.equals("a")) {
					result = Integer.parseInt(grade.get("a").toString());
					System.out.println(grade.get("a"));
				}else if(token.equals("b")) {
					//grade.get("b");
					result = Integer.parseInt(grade.get("b").toString());
					System.out.println(grade.get("b"));
				}else if(token.equals("c")) {
					//grade.get("c");
					System.out.println(grade.get("c"));
				} else if(token.equals("d")) {
					//grade.get("d");
					System.out.println(grade.get("d"));
				}else if(token.equals("e")) {
					//grade.get("e");
					System.out.println(grade.get("e"));
				}//*/
				
				result = grade.get(token);
				sum += result;
			
			}
			System.out.println(sum);
			if(sum >=25) {			
				System.out.println("잘 관리됨");
			}else if(21<= sum && sum <25) {
				System.out.println("부분적으로 관리됨");
			}else {
				System.out.println("잘 관리되지 않음");
			}
		
		}catch(NumberFormatException nume) {
			nume.printStackTrace();
		}
		/*
		// 25 21~24 0~20
		if(score >=25) {			
			System.out.println("잘 관리됨");
		}else if(21<= score && score <25) {
			System.out.println("부분적으로 관리됨");
		}else {
			System.out.println("잘 관리되지 않음");
		}
		
		//*/
		
	}

}
