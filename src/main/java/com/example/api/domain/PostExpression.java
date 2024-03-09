package com.example.api.domain;

import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.types.dsl.BooleanExpression;

@QueryEntity
public class PostExpression {
    @QueryDelegate(Post.class)
    public static BooleanExpression matchSearchKeyword(QPost post,String searchStr) {
        if (searchStr == null || searchStr.isEmpty()) {
            return null;
        }
        return post.title.contains(searchStr).or(post.content.contains(searchStr)).or(post.writer.contains(searchStr));
    }
}
