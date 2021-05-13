package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GroupRepository {

    private final EntityManager em;

    public void save(Group group){

        em.persist(group);
    }

    public Group findOne(Long id){
        return em.find(Group.class, id);
    }

    public List<Group> findAll(){
        return em.createQuery("select g from Group as g", Group.class)
                .getResultList();
    }

    public List<Group> findByCode(String code){
        return em.createQuery("select g from Group g where g.code = :code", Group.class)
                .setParameter("code", code)
                .getResultList();
    }

    public List<Group> findByHost(Long hostId){
        return em.createQuery("select g from Group g where g.hostId = :hostId", Group.class)
                .setParameter("hostId", hostId)
                .getResultList();
    }

    public void delete(Group group){
        System.out.println("===============");
        em.remove(group);
    }
}
