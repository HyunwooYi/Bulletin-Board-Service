package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {      //  content, createdAt, createdBy에 빠르게 서칭이 가능한 인덱싱을 건다.
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
} )
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //domain 에 관련된 내용
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article; // 게시글 (ID)
    @Setter @Column(nullable = false, length = 500) private String content; // 본문

    //메타 데이터
    @CreatedDate @Column(nullable =false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy @Column(nullable =false, length = 100) private String createdBy; // 생성자
    @LastModifiedDate @Column(nullable =false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy @Column(nullable =false, length = 100) private String modifiedBy; // 수정자


    protected ArticleComment() {}
    // 모든 JPA Entity들은 hibernate 구현체를 사용하는 기준으로 설명하자면 기본 생성자를 가지고 있어야한다.


    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
