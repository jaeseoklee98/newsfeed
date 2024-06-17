package com.sparta.newsfeed.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeed.config.WebSecurityConfig;
import com.sparta.newsfeed.controller.CommentController;
import com.sparta.newsfeed.controller.NewsfeedController;
import com.sparta.newsfeed.dto.*;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.entity.UserRoleEnum;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {NewsfeedController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)

class NewsfeedCommentMvcTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    NewsfeedService newsfeedService;

    @MockBean
    CommentService commentService;

    @MockBean
    LikeService likeService;

    @MockBean
    EmailService emailService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
        mockUserSetup();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String username = "sollertia4351";
        String password = "robbie1234";
        String email = "sollertia@sparta.com";
        String name = "robbie";
        String nickname = "rob";
        String introduce = "한줄 소개";
        String user_status = "정상";
        String refreshToken = "리프레쉬토큰";
        String accessToken = "엑세스토큰";
        String authKey = "인증키";
        LocalDateTime verifyTime = LocalDateTime.now();
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(username, password , name, nickname, email, introduce, user_status, refreshToken, accessToken, authKey, verifyTime);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("게시물 생성")
    void test1() throws Exception {
        // given
        this.mockUserSetup();
        String username = "사용자명";
        String title = "게시물 제목";
        String content = "게시물 내용";
        int like = 1;
        NewsfeedRequestDto requestDto = new NewsfeedRequestDto(
                username,
                title,
                content,
                like
        );
        String postInfo = objectMapper.writeValueAsString(requestDto);
        // when - then
        mvc.perform(post("/api/newsfeed")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 전체 조회")
    void test2() throws Exception {
        // given
        this.mockUserSetup();
        Long id = 1L;
        String title = "게시물 제목";
        String content = "게시물 내용";
        NewsfeedResponseDto responseDto = new NewsfeedResponseDto(
                id,
                title,
                content
        );
        String postInfo = objectMapper.writeValueAsString(responseDto);
        // when - then
        mvc.perform(get("/api/newsfeed")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 단건 조회")
    void test3() throws Exception {
        // given
        this.mockUserSetup();
        Long id = 1L;
        String title = "게시물 제목";
        String content = "게시물 내용";
        NewsfeedResponseDto responseDto = new NewsfeedResponseDto(
                id,
                title,
                content
        );
        String postInfo = objectMapper.writeValueAsString(responseDto);
        // when - then
        mvc.perform(get("/api/newsfeed/1")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 수정")
    void test4() throws Exception {
        // given
        this.mockUserSetup();
        Long id = 1L;
        String title = "게시물 제목";
        String content = "게시물 내용 수정";
        NewsfeedResponseDto responseDto = new NewsfeedResponseDto(
                id,
                title,
                content
        );
        String postInfo = objectMapper.writeValueAsString(responseDto);
        // when - then
        mvc.perform(put("/api/newsfeed/1")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 삭제")
    void test5() throws Exception {
        // given
        this.mockUserSetup();
        Long id = 1L;
        String title = "게시물 제목";
        String content = "게시물 내용 수정";
        NewsfeedResponseDto responseDto = new NewsfeedResponseDto(
                id,
                title,
                content
        );
        String postInfo = objectMapper.writeValueAsString(responseDto);
        // when - then
        mvc.perform(delete("/api/newsfeed/1")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 10건 씩 페이지 조회")
    void test6() throws Exception {
        // given
        this.mockUserSetup();
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now();
        String sortBy = "writeDate";
        PagingRequestDto requestDto = new PagingRequestDto(
                startDate,
                endDate,
                sortBy
        );
        String postInfo = objectMapper.writeValueAsString(requestDto);
        // when - then
        mvc.perform(get("/api/newsfeed/page/1")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 생성")
    void test7() throws Exception {
        // given
        this.mockUserSetup();
        String username = "사용자명";
        String comment = "댓글";
        String nickname = "별명";

        CommentCreateRequest requestDto = new CommentCreateRequest(
                comment,
                username,
                nickname
        );
        String postInfo = objectMapper.writeValueAsString(requestDto);
        // when - then
        mvc.perform(post("/api/1/comments")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 조회")
    void test8() throws Exception {
        // given
        this.mockUserSetup();
        Long id = 1L;
        String title = "제목";
        String comment = "내용";
        Long good_counting = 1L;

        CommentResponse response = new CommentResponse(
                id,
                title,
                comment,
                good_counting
        );
        String postInfo = objectMapper.writeValueAsString(response);
        // when - then
        mvc.perform(get("/api/1/comments/1")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정")
    void test9() throws Exception {
        // given
        this.mockUserSetup();
        Long id = 1L;
        String comment = "내용";

        CommentUpdateRequest updateRequest = new CommentUpdateRequest(
                id,
                comment
        );
        String postInfo = objectMapper.writeValueAsString(updateRequest);
        // when - then
        mvc.perform(put("/api/1/comments/1")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제")
    void test10() throws Exception {
        // given
        this.mockUserSetup();

        // when - then
        mvc.perform(delete("/api/1/comments/1")
                        //.content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
