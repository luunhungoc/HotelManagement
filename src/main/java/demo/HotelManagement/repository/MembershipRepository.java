package demo.HotelManagement.repository;

import demo.HotelManagement.entities.Membership;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  MembershipRepository extends CrudRepository<Membership,Long>{

}
