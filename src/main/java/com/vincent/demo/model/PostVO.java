package com.vincent.demo.model;

import java.util.List;

public class PostVO {
    private String id;
    private String creatorId;
    private String title;
    private String creatorName;
    private List<String> likerNames;

    public static PostVO of(PostPO postPO, String creatorName, List<String> likerNames) {
        var postVO = new PostVO();
        postVO.id = postPO.getId();
        postVO.creatorId = postPO.getCreatorId();
        postVO.title = postPO.getTitle();
        postVO.creatorName = creatorName;
        postVO.likerNames = likerNames;

        return postVO;
    }

    public String getId() {
        return id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public List<String> getLikerNames() {
        return likerNames;
    }
}
