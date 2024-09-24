package fr.umontpellier.campUm.repository;

import fr.umontpellier.campUm.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {

    List<Campus> findByVille(String name);

    Campus findByNomC(String name);
}