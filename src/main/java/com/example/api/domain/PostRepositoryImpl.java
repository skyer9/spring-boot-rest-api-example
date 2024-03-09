package com.example.api.domain;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.api.domain.QPost.post;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Post> search(Integer pageNo, Integer pageSize, String searchStr) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<Post> list = jpaQueryFactory
                .selectFrom(post)
                .where(
                        post.matchSearchKeyword(searchStr)
                )
                .orderBy(post.idx.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> count = jpaQueryFactory
                .selectFrom(post)
                .where(
                        post.matchSearchKeyword(searchStr)
                );

        return PageableExecutionUtils.getPage(list, pageable, () -> count.fetch().size());
    }
}
