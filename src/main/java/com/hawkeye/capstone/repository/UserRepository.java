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
        //비밀번호 해시 저장(SHA256)
        String encryptedPassword = Encrypt(user.getPassword());
        user.setPassword(encryptedPassword);

        em.persist(user);
    }

    public User findOne(Long id){
        return em.find(User.class, id);
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

    private String Encrypt(String password){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[]hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();

            for(int i = 0; i < hash.length; i++){
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

