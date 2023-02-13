package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {      //  title, hashtag, createdAt, createdBy에 빠르게 서칭이 가능한 인덱싱을 건다.
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
} )
@Entity
public class Article extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //domain 에 관련된 내용
    private Long id;


    @Setter
    @Column(nullable = false)
    private String title; // 제목
    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문

    @Setter
    private String hashtag; // 해시태그
    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();    // 이 Article에 연동되어있는 코멘트는 중복을 허용하지 않고 다 여기서 모아서 컬렉션으로 보겠다.

    protected Article() {
    }     // 모든 JPA Entity들은 hibernate 구현체를 사용하는 기준으로 설명하자면 기본 생성자를 가지고 있어야한다.


    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    //factory 메소드
    public static Article of(String title, String content, String hashtag) {    //의도를 전달, domain Article 생성하려면 title, content, hashtag를 넣어야한다.
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {       // 동등성 검사
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id); // 새로 만든 Entity들이 아직 영속화 되지 않았다면 id는 모두 동등성 검사를 탈락한다.( 각각 다른 값으로 취급하겠다.)
    }

    @Override
    public int hashCode() {     // 동등성 검사에선는 id만 가지고 hash 하면 된다.
        return Objects.hash(id);
    }
}
