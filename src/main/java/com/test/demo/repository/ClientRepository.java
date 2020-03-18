package com.test.demo.repository;


import com.test.demo.model.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query("select c from Client c where c.username like ?1")
    Client findByUsername(String username);

    @Query("select c from Client c where c.id = ?1")
    Client getById(int id);

    @Query("select c from Client c where c.cnp = ?1")
    Client findClientByCnp(String cnp);


}
