package com.example.algorithm;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class InterestTour {
   
	int name; // 카테고리 이름.  
	ArrayList<String> textList = new ArrayList<String>();
	int weight = 0; 
   
   // 관광지 
   String category[][] = { 
		   // 카테고리에 대한 하위 키워드  ex) "산"의 연관 단어 "등반, 등정, 산행 ..."
         { "산", "등반", "등정", "산행", "등산로", "등산", "경치", "계곡", "풍경", "야생동물", "운동", "메아리" },
         { "섬", "등대", "해안", "낚시", "무인도", "남이섬", "제부도" }, 
         { "수영", "해수욕장", "해안", "동해", "리조트", "갈매기", "해변가", "파라솔" },
         { "레포츠", "캠프파이어", "번지점프", "수상스키", "암벽등반", "패러글라이딩" }, 
         { "역사", "고궁", "시대", "탐방", "경복궁", "조선", "과거" },
         { "문화", "박물관", "기념관", "미술관", "영화", "음악" }, 
         { "문화", "이벤트", "잔치", "체험", "댄스", "세계" } };
   String[] categoryName = {"산","섬","바다","레포츠","역사","문화","축제"}; // 하위 카테고리 [관광지]
      
   /*
    * tweetData를 testList 값에 add한다. 
    * @Param	String type list. 
    * @Return	testList에 tweetData를 추가. 
    */
   public int[] setTextList(List<String> list){	   
	   int cnt[]={0,0,0,0,0,0,0};
	   int index = 0;
	   for(@SuppressWarnings("unused") String string : list){
    	  textList.add(list.get(index));
    	  index ++;
	   }      
	   
	   AdaptInterest(cnt);         
      
      /*
       * 초기화 과정
       * list = 모든 값 제거 .
       * Array = 모든 값 0으로 초기화. 
       */
       textList.clear();
      
       return cnt;
   }
   
   /*
    * User tweet목록에서 각각의 카테고리 별로 존재하는 단어의 수를 카운팅한다.
    * @Param	int type array.
    * @Return	category score.  
    */
   public void AdaptInterest(int cnt[]){
      for(int i=0; i<textList.size(); i++){
         for(int j=0; j<category.length; j++){
            for(int k=0; k< category[j].length; k++){
               if(textList.get(i).contains(category[j][k])){
                  cnt[j]++;
               }
            }
         }
      }
   }      
   /*
    * int type인 Array를  리던.
    * @Param	int type array.
    * @Return	weight를 곱한. int type array.  
    */
   public int[] getCategoryScore(int[] cnt){
	  // weight를 적용한다. 
      for(int i=0; i<cnt.length; i++){
    	  cnt[i] = cnt[i]*weight;         
      }     
      return cnt;
   }
}