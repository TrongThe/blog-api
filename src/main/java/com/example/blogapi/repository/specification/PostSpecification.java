package com.example.blogapi.repository.specification;

import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.PostStatus;
import org.hibernate.dialect.function.SpannerConcatFunction;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class PostSpecification {

    public static Specification<Post> hasTitle(String title){
        return ((root, query, criteriaBuilder) -> {

            if (title == null || title.isBlank()){
               return criteriaBuilder.conjunction();
           }

           return criteriaBuilder.like(
                   criteriaBuilder.lower(root.get("title")),
                   "%" + title.toLowerCase() + "%"
           );
        });
    }

    public static Specification<Post> hasStatus(PostStatus status){
        return ((root, query, criteriaBuilder) -> {

            if (status == null){
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("status"), status);
        });
    }

    public static Specification<Post> hasAuthor(String username) {

        return (root, query, criteriaBuilder) -> {

            if (username == null || username.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("author").get("username"),
                    username
            );
        };
    }

    public static Specification<Post> hasCategory(Long categoryId) {

        return (root, query, criteriaBuilder) -> {

            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.join("categories").get("id"),
                    categoryId
            );
        };
    }

    public static Specification<Post> visibleToUser(String username){

        return ((root, query, criteriaBuilder) -> {

           return criteriaBuilder.or(

                   criteriaBuilder.equal(
                           root.get("status"),
                           PostStatus.PUBLISHED
                   ),

                   criteriaBuilder.and(

                           criteriaBuilder.equal(
                                   root.get("status"),
                                   PostStatus.DRAFT
                           ),

                           criteriaBuilder.equal(
                                   root.get("author").get("username"),
                                   username
                           )
                   )
           );
        });
    }
}
