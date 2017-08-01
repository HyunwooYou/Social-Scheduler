package com.example.algorithm;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class InterestLodge {
   
	int name; // 카테고리 이름.  
	ArrayList<String> textList = new ArrayList<String>();
	int weight = 0;    
   
	// 숙박 
	String category[][] = { 
	         {"데스크","룸","와인","서비스","객실" },
	         {"휴양지","레져","콘도" }, 
	         {"수영장","계곡","가평","양평","사장님","민박"}};
	 String[] categoryName = {"호텔","리조트","펜션"}; // [숙박]
      
   /*
    * tweetData를 testList 값에 add한다. 
    * @Param	String type list. 
    * @Return	testList에 tweetData를 추가. 
    */
   public int[] setTextList(List<String> list){	   
	   int cnt[]={0,0,0};
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