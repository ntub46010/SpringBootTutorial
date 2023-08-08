package com.vincent.demo;

import com.vincent.demo.log.QueryHistoryService;
import com.vincent.demo.model.UserPO;
import com.vincent.demo.service.LikeService;
import com.vincent.demo.service.PostService;
import com.vincent.demo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CacheTests {
    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostService postService;

    @Autowired
    private QueryHistoryService historyService;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setup() {
        userService.deleteAll();
        likeService.deleteAll();
        postService.deleteAll();
        historyService.deleteAll();
        Stream.of("user", "likeUserNames", "post").forEach(cacheName ->
                Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }

    @Test
    public void testCreateUserThenCache() {
        var createdUser = userService.createUser("U1", "Vincent", true);
        var resultUser = userService.getUser(createdUser.getId());

        assertEquals(createdUser.getId(), resultUser.getId());
        assertEquals(createdUser.getName(), resultUser.getName());
        assertFalse(historyService.contains("user", createdUser.getId()));
    }

    @Test
    public void testCreateUserWithoutCache() {
        var createdUser = userService.createUser("U1", "Vincent", false);

        var resultUser = userService.getUser(createdUser.getId());
        assertTrue(historyService.contains("user", createdUser.getId()));
        historyService.deleteAll();

        resultUser = userService.getUser(createdUser.getId());
        assertEquals(createdUser.getId(), resultUser.getId());
        assertEquals(createdUser.getName(), resultUser.getName());
        assertFalse(historyService.contains("user", createdUser.getId()));
    }

    @Test
    public void testConditionalCacheable() {
        var user = userService.createUser("test-U1", "Andy", false);

        // the prefix "test-" of id make this data always non cacheable
        for (var i = 0; i < 2; i++) {
            userService.getUser(user.getId());
            assertTrue(historyService.contains("user", user.getId()));
            historyService.deleteAll();
        }
    }

    @Test
    public void testUpdateUser() {
        var createdUser = userService.createUser("U1", "Vincent", true);
        var updatedUser = UserPO.of(createdUser.getId(), "Vincent Zheng");
        userService.updateUser(updatedUser);

        userService.getUser(createdUser.getId());
        assertTrue(historyService.contains("user", createdUser.getId()));
    }

    @Test
    public void testGetPostCreatorName() {
        var creator = userService.createUser("U1", "Vincent", false);
        var postPO = postService.createPost("P1", creator.getId(), "Cache Tutorial");

        var postVO = postService.getPost(postPO.getId());
        assertEquals(creator.getName(), postVO.getCreatorName());
        assertTrue(historyService.contains("user", creator.getId()));
        historyService.deleteAll();

        postVO = postService.getPost(postPO.getId());
        assertEquals(creator.getName(), postVO.getCreatorName());
        assertFalse(historyService.contains("user", creator.getId()));
    }

    @Test
    public void testGetPostLikeUserNames() {
        var creatorUser = userService.createUser("U1", "Vincent", false);
        var postPO = postService.createPost("P1", creatorUser.getId(), "Post_1");

        // create first like
        var likeUser1 = userService.createUser("U2", "Ivy", false);
        likeService.createLike(likeUser1.getId(), postPO.getId());

        var postVO = postService.getPost(postPO.getId());
        assertEquals(1, postVO.getLikerNames().size());
        assertTrue(postVO.getLikerNames().contains(likeUser1.getName()));
        assertTrue(historyService.contains("likeUserNames", postPO.getId()));
        assertTrue(historyService.contains("user", likeUser1.getId()));
        historyService.deleteAll();

        postService.getPost(postPO.getId());
        assertFalse(historyService.contains("likeUserNames", postPO.getId()));
        assertFalse(historyService.contains("user", likeUser1.getId()));

        // create second like
        var likeUser2 = userService.createUser("U3", "Dora", false);
        likeService.createLike(likeUser2.getId(), postPO.getId());

        postVO = postService.getPost(postPO.getId());
        assertEquals(2, postVO.getLikerNames().size());
        assertTrue(postVO.getLikerNames().containsAll(
                List.of(likeUser1.getName(), likeUser2.getName())));
        assertTrue(historyService.contains("likeUserNames", postPO.getId()));
        assertFalse(historyService.contains("user", likeUser1.getId()));
        assertTrue(historyService.contains("user", likeUser2.getId()));
    }

    @Test
    public void testGetMostLikedPosts() {
        var creatorUser1 = userService.createUser("U1", "Vincent", true);
        var creatorUser2 = userService.createUser("U2", "Ivy", true);
        var creatorUser3 = userService.createUser("U3", "Dora", true);

        var postPO1 = postService.createPost("P1", creatorUser1.getId(), "Title_1");
        var postPO2 = postService.createPost("P2", creatorUser2.getId(), "Title_2");
        var postPO3 = postService.createPost("P3", creatorUser3.getId(), "Title_3");

        likeService.createLike(creatorUser1.getId(), postPO1.getId());
        likeService.createLike(creatorUser2.getId(), postPO1.getId());
        likeService.createLike(creatorUser3.getId(), postPO1.getId());
        likeService.createLike(creatorUser2.getId(), postPO2.getId());
        likeService.createLike(creatorUser3.getId(), postPO2.getId());
        likeService.createLike(creatorUser3.getId(), postPO3.getId());

        var postVOs = postService.getMostLikedPosts();
        assertEquals(3, postVOs.size());
        assertEquals(postPO1.getId(), postVOs.get(0).getId());
        assertEquals(postPO2.getId(), postVOs.get(1).getId());
        assertEquals(postPO3.getId(), postVOs.get(2).getId());
        assertTrue(historyService.contains("post", "mostLikedPosts"));
        historyService.deleteAll();

        postVOs = postService.getMostLikedPosts();
        assertEquals(3, postVOs.size());
        assertEquals(postPO1.getId(), postVOs.get(0).getId());
        assertEquals(postPO2.getId(), postVOs.get(1).getId());
        assertEquals(postPO3.getId(), postVOs.get(2).getId());
        assertFalse(historyService.contains("post", "mostLikedPosts"));
    }
}