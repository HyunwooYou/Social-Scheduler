package com.example.algorithm;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class InterestRestaurant {
   
	int name; // 카테고리 이름.  
	ArrayList<String> textList = new ArrayList<String>();
	int weight = 0;    
   
	// 맛집
	 String category[][] = { 
	         {"비빔밥","갈비","떡국","김치","찌게","파전"},
	         {"자장면","짬뽕","탕수육","양장피","깐풍기","마파두부","유산슬"}, 
	         {"초밥","덮밥","튀김","스키야키","나베","메밀국수","미소" },
	         {"스테이크","파스타","피자","수프","치즈","바베큐","베이컨","버터","크로켓"},
	         {"떡볶이","오뎅","튀김","순대" },
	         {"빵","슈크림","소보로","고로케" },
	         {"커피","아메리카노","카페","분위기" }};
	 String[] categoryName = {"한식","중식","일식","양식","분식","베이커리","카페"}; //
      
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