package sch.cqre.api.service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sch.cqre.api.exception.CustomException;
import sch.cqre.api.service.AccountService;
import sch.cqre.api.service.NotificationService;
import sch.cqre.api.service.PostService;
import sch.cqre.api.service.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MyPageControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private AccountService accountService;
	@Autowired
	private PostService postService;
	@Autowired
	private NotificationService noticeService;
	@Autowired
	private UserService userService;

	private static final String BASE_URL = "/api/v1/mypage";

	@Test
	void getMyInfo() throws Exception {
		mvc.perform(get(BASE_URL))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void withdrawMe() throws Exception {
		mvc.perform(delete(BASE_URL))
			.andExpect(status().isOk());

		mvc.perform((get(BASE_URL)))
			// .andExpect(status().isNotFound())
			// 삭제 후 유저 정보 로드시 "유저 없음" 예외가 발생하는지 확인
			.andExpect(
				(rslt) -> assertTrue(rslt.getResolvedException().getClass().isAssignableFrom(CustomException.class))
			)
			.andDo(print());
	}

	@Test
	void checkPassword() throws Exception{
		mvc.perform(get(BASE_URL + "/pw")
				.param("pw", "khb2439"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void changePassword() throws Exception {
		mvc.perform(patch(BASE_URL + "/pw")
				.param("pwOne", "khb2439")
				.param("pwTwo", "khb2439"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void getMyPost() throws Exception {
		mvc.perform(get(BASE_URL + "/post"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void getMyScrap() throws Exception {
		mvc.perform(get(BASE_URL + "/scrap"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void getMyNotice() throws Exception {
		mvc.perform(get(BASE_URL + "/notice"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void readOneNotice() throws Exception {
		mvc.perform(patch(BASE_URL + "/notice")
				.param("notiId", "1"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void deleteOneNotice() throws Exception {
		mvc.perform(delete(BASE_URL + "/notice")
				.param("notiId", "1"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void readAllNotice() throws Exception {
		mvc.perform(patch(BASE_URL + "/notice/all"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void deleteReadNotice() throws Exception {
		mvc.perform(delete(BASE_URL + "/notice/read"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void deleteAllNotice() throws Exception {
		mvc.perform(delete(BASE_URL + "/notice/all"))
			.andExpect(status().isOk())
			.andDo(print());
	}
}