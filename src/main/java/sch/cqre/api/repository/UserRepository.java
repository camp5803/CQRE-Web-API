package sch.cqre.api.repository;

import org.springframework.stereotype.Repository;
import sch.cqre.api.domain.UserEntity;
import sch.cqre.api.exception.CustomException;
import sch.cqre.api.exception.ErrorCode;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepository{ //extends JpaRepository<UserEntity,Long>{

    @PersistenceContext
    private EntityManager em;

    public void save(UserEntity userEntity){
        em.persist(userEntity);
    }

    public UserEntity findOne(Long userId){
        try {
            return em.find(UserEntity.class, userId);
        } catch(Exception e){
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public UserEntity findOneByEmail(String email){
        try {
            return (em.createQuery("select m from UserEntity m where m.email = :email", UserEntity.class)
                    .setParameter("email", email).getSingleResult());
        } catch (Exception e){
            return null;
        }
    }

    public boolean existByEmail(String email){
        try {
            return (em.createQuery("select m from UserEntity m where m.email = :email", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult() != null);
        } catch (Exception e){
            return false;
        }
    }

    public boolean existByNickname(String nickname){
        try {
            return (em.createQuery("select m from UserEntity m where m.nickname = :nickname", UserEntity.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult() != null);
        } catch (Exception e){
            return false;
        }
    }

    public boolean existByStudentId(int studentId){
        try {
            return (em.createQuery("select m from UserEntity m where m.studentId = :studentId", UserEntity.class)
                    .setParameter("studentId", studentId)
                    .getSingleResult() != null);
        } catch (Exception e){
            return false;
        }
    }

    public void deleteOne(Long postId){
        em.remove(findOne(postId));
    }
}
