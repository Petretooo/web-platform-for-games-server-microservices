package dotsandboxes.repository.dot;

import org.springframework.data.jpa.repository.JpaRepository;

import dotsandboxes.model.Dot;

public interface DotRepository extends JpaRepository<Dot, String>{

}
