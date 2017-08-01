package com.example.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.algorithm.InterestLodge;
import com.example.algorithm.InterestRestaurant;
import com.example.algorithm.InterestTour;
import com.example.domain.Friend;
import com.example.domain.SearchWriter;
import com.example.domain.TweetData;
import com.example.json.SocialData;
import com.example.service.FriendService;
import com.example.service.JsonTypeService;
import com.example.service.TweetDataService;
import com.example.service.UserService;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;

import java.util.ArrayList;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

/*
 * AccessToken 수집 후 회원가입 페이지로 이동한다. 
 * 한번 인증을 거친 사용자는 그 이후 AccessToken을 받아오는 과정을 거칠 필요 없이 설계한다. 
 * 회원가입시 사용자는 site에서 가입한 ID, PW를 사용해서 로그인 할 수 있다. 
 */
@Controller
@SessionAttributes("socialData")
@RequestMapping("twitter")
public class TwitterController {

	@Autowired
	TweetDataService tweetDataService;

	private static final String API_KEY = "rS4oxbojVLoFFBKX6nSzXlsMu";
	private static final String API_SECRET = "og9KWknVT1nnb2xcnNAiLkf5YQq8syXJk7Yt85OL83gft0EcqY";
	private static final String HOST = "http://localhost:9000"; // port가 변경될때마다
																// 변경해줘야 한다.
	private static final String CALLBACK_URL = "/twitter/callback";

	// Twitter 4j
	Twitter twitter;
	twitter4j.User user;
	String writer;
	AccessToken accesstoken; // accessToken 얻어오기 (token,token_secret)
	String key1;
	String key2;
	String extracted_name = ""; // Screen User Name.
	Paging page = new Paging(1, 40); // 글을 40개 가져올 수 있게 한다.

	List<String> list = new ArrayList<>(); // 트윗 명사 추출
	List<String> list2 = new ArrayList<>(); // 친구 리스트

	// Algorithm variable.
	ArrayList<int[]> cntListTour = new ArrayList<int[]>(); // 관광지 category group
															// score를 저장할 수 있는
															// 리스트.
	ArrayList<int[]> cntListRestaurant = new ArrayList<int[]>(); // 맛집 category
																	// group
																	// score를
																	// 저장할 수 있는
																	// 리스트.
	ArrayList<int[]> cntListLodge = new ArrayList<int[]>(); // 숙박 category group
															// score를 저장할 수 있는
															// 리스트.

	int userCntTour[] = { 0, 0, 0, 0, 0, 0, 0 }; // 관광지에 대한 사용자 category score.
	int userCntRestaurant[] = { 0, 0, 0, 0, 0, 0, 0 }; // 맛집에 대한 사용자 category
														// score.
	int userCntLodge[] = { 0, 0, 0 }; // 숙박에 대한 사용자 category score.

	/*
	 * 프로젝트 시작을 확인하는 변수. Consumer key를 처음에만 설정하고 다음부터는 설정하지 않는다.
	 */
	int cnt = 0;

	@ModelAttribute("socialData")
	public SocialData socialData() {
		return new SocialData();
	}

	@ModelAttribute
	SearchWriterForm setUpSearchWriterForm() {
		return new SearchWriterForm();
	}

	@Autowired
	UserService userService;

	@Autowired
	FriendService friendService;

	@Autowired
	JsonTypeService jsonTypeService;

	@ModelAttribute
	UserForm setUpUserForm() {
		return new UserForm();
	}

	@ModelAttribute
	FriendForm setUpFriendForm() {
		return new FriendForm();
	}

	@ModelAttribute
	JsonTypeForm setUpJsonTypeForm() {
		return new JsonTypeForm();
	}

	@RequestMapping(value = "signin", params = "form", method = RequestMethod.GET)
	public void signin(HttpServletRequest request, HttpServletResponse response) throws IOException {

		OAuthService service = new ServiceBuilder().provider(TwitterApi.Authenticate.class).apiKey(API_KEY)
				.apiSecret(API_SECRET).callback(HOST + CALLBACK_URL).build();

		Token requestToken = service.getRequestToken();
		String redirectURL = service.getAuthorizationUrl(requestToken);

		response.sendRedirect(redirectURL);
	}

	/**
	 * signin이후에 callback method에서 타임라인을 가져온다.
	 * 
	 * @param oauth_token : 인증토큰. oauth_verifier : 인증을 확인하기 위한 확인자. model : 브라우저
	 * 상에서 사용할 수 있는 변수를 설정한다.
	 * 
	 * @return h
	 */
	@RequestMapping(value = "callback", method = RequestMethod.GET)
	public String callback(
			@RequestParam(value = "oauth_token", required = false) String oauth_token,
			@RequestParam(value = "oauth_verifier", required = false) String oauth_verifier,
			@ModelAttribute SocialData socialData,
			Model model			
			) {

		OAuthService service = new ServiceBuilder().provider(TwitterApi.Authenticate.class).apiKey(API_KEY)
				.apiSecret(API_SECRET).callback(HOST + CALLBACK_URL).build();

		final Verifier verifier = new Verifier(oauth_verifier);
		final Token requestToken = new Token(oauth_token, oauth_verifier);
		final Token accessToken = service.getAccessToken(requestToken, verifier);

		/*
		 * accessTokken과 Consumer api-key, api-secret을 적용시킨다.
		 */
		try {
			key1 = accessToken.getToken();
			key2 = accessToken.getSecret();

			accesstoken = new AccessToken(key1, key2);
			twitter = TwitterFactory.getSingleton();

			if (cnt == 0) {
				twitter.setOAuthConsumer(API_KEY, API_SECRET);
				cnt = 1;
			}
			twitter.setOAuthAccessToken(accesstoken);

			user = twitter.verifyCredentials();
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		// H2 DB에 타임라인 저장.
		putTimeLineToH2();

		/*
		 * * * * * * * * * * 그룹 관심사를 적용한다. * * * * * * * * *
		 */
		adaptGroupInterest();

		/*
		 * 결과 값 테스트
		 */
		System.out.println("[관광지]");
		for (int i = 0; i < userCntTour.length; i++) {
			System.out.print(userCntTour[i] + " ");
		}
		System.out.println();

		System.out.println("[맛집]");
		for (int i = 0; i < userCntRestaurant.length; i++) {
			System.out.print(userCntRestaurant[i] + " ");
		}
		System.out.println();

		System.out.println("[숙박]");
		for (int i = 0; i < userCntLodge.length; i++) {
			System.out.print(userCntLodge[i] + " ");
		}
		System.out.println("\n");

		/*
		 * [[ 결 과 ]] JsonType result를 생성하는 부분.
		 */

		socialData.setWriter(extracted_name);
		socialData.setTour(userCntTour);
		socialData.setRestaurant(userCntRestaurant);
		socialData.setLodge(userCntLodge);
		socialData.setJsonResult();
		
		//JSONObject result = socialData.getJsonResult();

		System.out.println(socialData.getJsonResult().toString());
		// jsonTypeService.

		// screen name 값을 "writer" 모델로 view상에 보여지게 한다.
		model.addAttribute("writer", extracted_name);
		
		model.addAttribute("socialData", socialData);

		return "users/userInfo";
	}
	
	@RequestMapping(value="exit", method=RequestMethod.POST)
	public String indexEXIT(Model model, @ModelAttribute SocialData socialData, SessionStatus session) {
		System.out.println("indexEXIT");
		
		model.addAttribute("socialData", socialData);
		// session 종료
		session.setComplete();
		
		return "loginForm";
	}

	/**
	 * 특정 카테고리에 대한 weight.
	 * 
	 * @param 특정 카테고리에 대한 counting list.
	 * 
	 * @return Weight를 적용시킨 Following user들 각각의 counting list를 userCntTour에 모두
	 * 추가.
	 */
	public void setMainUserCnt(int userCnt[], ArrayList<int[]> cntList) {
		for (int i = 0; i < userCnt.length; i++) {
			for (int j = 0; j < cntList.size(); j++) {
				userCnt[i] += cntList.get(j)[i];
			}
		}
	}

	/**
	 * 리트윗 수를 확인하기 위해 현재 접속자에 대한 트윗 수를 카운팅 한다.
	 * 
	 * @param 접속자 아이디, Following user의 타임라인.
	 * 
	 * @return 접속자에 대한 리트윗 수.
	 */
	public int countWriter(String writer, List<String> list) {
		int count = 0;

		int index = 0;
		for (@SuppressWarnings("unused")
		String str : list) {
			if (list.get(index).contains("@" + writer)) {
				count++;
			}
			index++;
		}

		return count;
	}

	/**
	 * TimeLine 글을 {auto_id, writer, tweetData} 형식으로 H2 DB에 저장.
	 * 
	 * @param (Global)extracted_name : ScreenUserName.
	 * 
	 * @return 사용자 한명의 TimeLine을 H2에 저장.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void putTimeLineToH2() {

		String message = "";
		List list_name = new ArrayList();

		try {
			List<Status> list = twitter.getUserTimeline(page);

			for (Status status : list) {

				writer = status.getUser().getScreenName();
				extracted_name = writer;

				// 타임라인 내용을 message에 저장시킨다.
				message = status.getText();

				list_name.add(message);
			}

			/* * * * * * * * * * * * * * * * * * * * *
			 * 
			 * 
			 * 
			 *	 << 지우면 안됨 >> DB에 insert하는 과정.
			 * 
			 * 
			 * 
			 *  * * * * * * * * * * * * * * * * * * * *
			 */			
			/*for (Object object : list_name) {
				String element = (String) object;
				tweetDataService.create(new TweetData(0, extracted_name, element));

				System.out.println(element);
			}*/
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1. 친구 목록을 가져온다. 2. 친구 목록을 DB에서 확인한 뒤 내장 DB에 존재할 경우 following하는 대상의
	 * timeline을 가져온다.
	 * 
	 * @param (Global)extracted_name : ScreenUserName.
	 * 
	 * @return following목록을 H2에 저장.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void adaptGroupInterest() {

		long cursor = -1;
		IDs ids;
		List list_friends = new ArrayList();

		int weightUserTour = 0;
		int weightUserRestaurant = 0;
		int weightUserLodge = 0;

		try {
			do {
				ids = twitter.getFriendsIDs(user.getScreenName(), cursor);

				for (long id : ids.getIDs()) {
					User user = twitter.showUser(id); // Follow하고 있는 대상

					list_friends.add(user.getScreenName());
				}

				/*
				 * <<<< 없애면 안됌 >>>>
				 * * * * * * * * * * * * * * * * * * * * * 
				 * 
				 * 
				 * 
				 * 		친구들의 리스트를 받아온다.
				 * 
				 *
				 * 
				 * * * * * * * * * * * * * * * * * * * * *  
				 */
				//putFriendsListToH2(list_friends);

				/*
				 * * * * * * * * * * * * 그룹 관심사 적용을 위한 객체. * * * * * * * * * * *
				 */
				InterestTour interestTour = new InterestTour();
				InterestRestaurant interestRestaurant = new InterestRestaurant();
				InterestLodge interestLodge = new InterestLodge();

				/*
				 * Following 목록에 있는 ScreenUserName하나씩을 tweetData table의 writer와
				 * 일치하는지 확인한다. 일치할 경우 getUserPublicTimeLine(SearchWriter
				 * search)을 통해서 timeline을 가져온다.
				 */
				for (Object object : list_friends) {
					String writer = (String) object;

					// writer가 친구 목록에 존재할 경우.
					if (isScreenUserName(writer)) { // writer = 친구 아이디.
						// 특정 user에 대한 글을 읽어온다.
						SearchWriter search = new SearchWriter();
						search.setWriter(writer);
						List<String> listForFriends = getUserPublicTimeLine(search);

						// [관광지]
						interestTour.setWeight(countWriter(extracted_name, listForFriends));
						cntListTour.add(interestTour.getCategoryScore(interestTour.setTextList(listForFriends)));
						weightUserTour += interestTour.getWeight();

						// [맛집]
						interestRestaurant.setWeight(countWriter(extracted_name, listForFriends));
						cntListRestaurant.add(
								interestRestaurant.getCategoryScore(interestRestaurant.setTextList(listForFriends)));
						weightUserRestaurant += interestRestaurant.getWeight();

						// [숙박]
						interestLodge.setWeight(countWriter(extracted_name, listForFriends));
						cntListLodge.add(interestLodge.getCategoryScore(interestLodge.setTextList(listForFriends)));
						weightUserLodge += interestLodge.getWeight();
					} else {
						System.out.println("Following하는 user가 DB상에 존재하지 않습니다.");
					}
				}

				System.out.println("\nPresent user : " + extracted_name + "\n");

				SearchWriter searchNowUser = new SearchWriter();
				searchNowUser.setWriter(extracted_name);
				List<String> listForMainUser = getUserPublicTimeLine(searchNowUser);

				// [관광지]
				interestTour.setWeight(weightUserTour);
				cntListTour.add(interestTour.getCategoryScore(interestTour.setTextList(listForMainUser)));
				setMainUserCnt(userCntTour, cntListTour);

				// [맛집]
				interestRestaurant.setWeight(weightUserRestaurant);
				cntListRestaurant
						.add(interestRestaurant.getCategoryScore(interestRestaurant.setTextList(listForMainUser)));
				setMainUserCnt(userCntRestaurant, cntListRestaurant);

				// [숙박]
				interestLodge.setWeight(weightUserLodge);
				cntListLodge.add(interestLodge.getCategoryScore(interestLodge.setTextList(listForMainUser)));
				setMainUserCnt(userCntLodge, cntListLodge);

				// Testing for 관광지
				showScoreResult(cntListTour, "관광지");
				// Testing for 맛집
				showScoreResult(cntListRestaurant, "맛집");
				// Testing for 숙박
				showScoreResult(cntListLodge, "숙박");
				System.out.println();
			} while ((cursor = ids.getNextCursor()) != 0);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * For testing.
	 */
	public void showScoreResult(ArrayList<int[]> cntList, String category) {
		System.out.println("\n" + category);
		for (int i = 0; i < cntList.size(); i++) {
			int[] tem = cntList.get(i);
			for (int j = 0; j < cntList.get(i).length; j++) {
				System.out.print(tem[j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Friend List를 H2 DB에 {writer, friend} 값으로 저장. 친구가 다수일 경우 {writer,
	 * friend0}, {writer, friend1}..방식으로 DB에 저장.
	 * 
	 * @param Follwing user list.
	 * 
	 * @return H2 DB 친구 리스트 저장.
	 */
	public void putFriendsListToH2(@SuppressWarnings("rawtypes") List list_friends) {
		for (Object object : list_friends) {
			String element = (String) object;
			friendService.create(new Friend(0, extracted_name, element));
		}
	}

	/**
	 * DB에서 user id에 따라 트윗 내용을 가져와서 리턴한다.
	 * 
	 * @param ScreenUserName. (객체 내부 변수로 표현된다.)
	 * 
	 * @return List<String>.
	 */
	public List<String> getUserPublicTimeLine(SearchWriter search) {
		List<TweetData> list = tweetDataService.findUserTweetData(search);
		List<String> result = new ArrayList<>();

		/*
		 * <TweetData> list의 내용 중에서 tweetdata만 추출해서 <String> result에 tweetdata를
		 * 저장한다.
		 */
		int c = 0;
		for (@SuppressWarnings("unused")
		TweetData tweetData : list) {
			result.add(list.get(c).getTweet_Data().toString());
			c++;
		}

		return result;
	}

	/**
	 * Screen user name이 존재하는지 확인.
	 * 
	 * @param writer : ScreenUseName.
	 * 
	 * @return true : user 존재 . false : 존재하지 않음.
	 */
	public boolean isScreenUserName(String writer) {

		if (tweetDataService.findWriter("gachonhyun") != null) {
			return true;
		} else {
			return false;
		}
	}

}
