package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user){

        String encryptedPassword = Encrypt(user.getPassword());
        user.setPassword(encryptedPassword);
        em.persist(user);
    }

    public User findOne(Long id){
        User findUser = em.find(User.class, id);
        if(findUser == null)
            throw new IllegalStateException("유효하지 않은 유저입니다.");
        return findUser;
    }

    public List<User> findAll(){
        return em.createQuery("select u from User as u", User.class)
                .getResultList();
    }

    public List<User> findByName(String name){
        return em.createQuery("select u from User as u where u.name = :name", User.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<User> findByEmail(String email){
        return em.createQuery("select u from User as u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
    }

    /**
     * 비밀번호 해시 변경(SHA256)
     */
    public String Encrypt(String password){
        try{
            //MessageDigest: 해시 알고리즘 사용 라이브러리
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //password 문자열을 SHA-256로 암호화하여 byte배열에 저장
            byte[]hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            //StringBuffer: append 메소드로 문자 추가(mutable)
            StringBuffer hexString = new StringBuffer();

            //byte를 String으로 변환
            for(int i = 0; i < hash.length; i++){
                //16진수 전환
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

