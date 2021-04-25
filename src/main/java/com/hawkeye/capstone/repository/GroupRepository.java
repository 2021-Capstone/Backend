package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.Group;
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

    public void delete(Group group){
        System.out.println("===============");
        em.remove(group);
    }
}
