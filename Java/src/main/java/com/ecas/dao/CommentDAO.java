package com.ecas.dao;

import com.ecas.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CommentDAO extends JpaRepository<Comment,Long> {

}
