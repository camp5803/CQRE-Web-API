package sch.cqre.api.repository;

import org.springframework.stereotype.Repository;
import sch.cqre.api.domain.CommentEntity;
import sch.cqre.api.exception.CustomExeption;
import sch.cqre.api.exception.ErrorCode;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(CommentEntity commentEntity){
        em.persist(commentEntity);
    }

    public CommentEntity findid(String name){
        return em.find(CommentEntity.class, name);
    }

    public CommentEntity getMyComment(long userId, long commentId){
        try {
            return em.createQuery("select m from CommentEntity m where m.commentId = :commentId And m.userId = :userId", CommentEntity.class)
                    .setParameter("userId", userId)
                    .setParameter("commentId", commentId)
                    .getSingleResult();
        } catch (Exception e){
            throw new CustomExeption(ErrorCode.INVALID_INPUT);
        }
    }

    public List<CommentEntity> findCommentList(long postId){
        try {
            return em.createQuery("select m from CommentEntity m where m.postId =: postId", CommentEntity.class)
                    .setParameter("postId", postId).getResultList();
        } catch (Exception e){
            return null;
        }
    }


}
