package dotsandboxes.repository.edge;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dotsandboxes.model.Edge;

public interface EdgeRepository extends JpaRepository<Edge, String>{

	public List<Edge> findAllByGameId(String id);

}
