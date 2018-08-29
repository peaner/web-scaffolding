package web.scaffolding.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import web.scaffolding.domain.User;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    User findTopByUsernameOrderByIdDesc(String username);

}
