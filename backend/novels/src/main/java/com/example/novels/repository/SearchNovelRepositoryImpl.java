package com.example.novels.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.novels.entity.Novel;
import com.example.novels.entity.QGenre;
import com.example.novels.entity.QGrade;
import com.example.novels.entity.QNovel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

public class SearchNovelRepositoryImpl extends QuerydslRepositorySupport implements SearchNovelRepository {

    public SearchNovelRepositoryImpl() {
        super(Novel.class);
    }

    // @Override
    // public List<Object[]> list() {
    // QNovel novel = QNovel.novel;
    // QGenre genre = QGenre.genre;
    // QGrade grade = QGrade.grade;

    // JPQLQuery<Novel> query = from(novel);

    // // 장르
    // JPQLQuery<String> genreName = JPAExpressions.select(genre.name)
    // .from(genre)
    // .where(novel.genre.eq(genre));

    // // 도서 id 별 평점 평균
    // JPQLQuery<Double> ratingAvg = JPAExpressions.select(grade.rating.avg())
    // .from(grade)
    // .where(grade.novel.eq(novel))
    // .groupBy(grade.novel);

    // JPQLQuery<Tuple> tuple = query.select(novel, genreName, ratingAvg);
    // List<Tuple> result = tuple.fetch();
    // List<Object[]> list = result.stream().map(t ->
    // t.toArray()).collect(Collectors.toList());
    // return list;
    // }

    @Override
    public Object[] getNovelById(Long id) {
        QNovel novel = QNovel.novel;
        QGenre genre = QGenre.genre;
        QGrade grade = QGrade.grade;

        JPQLQuery<Novel> query = from(novel);
        query.leftJoin(novel.genre, genre);
        query.where(novel.id.eq(id));

        // 도서 id 별 평점 평균
        JPQLQuery<Double> ratingAvg = JPAExpressions.select(grade.rating.avg())
                .from(grade)
                .where(grade.novel.eq(novel))
                .groupBy(grade.novel);

        JPQLQuery<Tuple> tuple = query.select(novel, genre, ratingAvg);
        Tuple result = tuple.fetchFirst();
        System.out.println("================ Tuple");
        System.out.println(result);

        return result.toArray();
    }

    @Override
    public Page<Object[]> list(Long genreId, String keyword, Pageable pageable) {
        QNovel novel = QNovel.novel;
        QGenre genre = QGenre.genre;
        QGrade grade = QGrade.grade;

        JPQLQuery<Novel> query = from(novel);
        query.leftJoin(novel.genre, genre);

        // 도서 id 별 평점 평균
        JPQLQuery<Double> ratingAvg = JPAExpressions.select(grade.rating.avg())
                .from(grade)
                .where(grade.novel.eq(novel))
                .groupBy(grade.novel);

        JPQLQuery<Tuple> tuple = query.select(novel, genre, ratingAvg);

        // pk 지정
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(novel.id.gt(0L));

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        // ------ 장르 아이디(0으로 오는 경우는 전체)
        if (genreId != null && genreId != 0) {
            conditionBuilder.and(novel.genre.id.eq(genreId));
        }

        // ------ 검색
        if (keyword != null && !keyword.isEmpty()) {
            conditionBuilder.and(novel.title.contains(keyword));
            conditionBuilder.or(novel.author.contains(keyword));
        }

        builder.and(conditionBuilder);
        tuple.where(builder);

        // order by
        Sort sort = pageable.getSort();
        // sort 로 설정한 값이 여러개 있을 수 있으니
        sort.stream().forEach(order -> {
            // pageable 에서 설정한 Sort 방향 가져와서 설정
            // com.querydsl.core.types.Order
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            // order 컬럼 가져오기 : Sort 객체 속성 - bno or title 이런 것들 지정
            String prop = order.getProperty();

            // PathBuilder : order 를 어느 엔티티에 적용할 것인가? / Entity 변수명
            PathBuilder<Novel> orderByExpression = new PathBuilder<>(Novel.class, "novel");
            // Sort 객체 사용 불가로 OrderSpecifier() 사용
            // com.querydsl.core.types.OrderSpecifier.OrderSpecifier(Order order, Expression
            // target)
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        // page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();

        return new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }

}
