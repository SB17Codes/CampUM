package fr.umontpellier.campUm.repository;

import fr.umontpellier.campUm.entity.Batiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface BatimentRepository extends JpaRepository<Batiment, String> {
    List<Batiment> getBatimentsByCampusC_nomC(String nomc);
    void deleteByCodeB(String codeB);
    Optional<Batiment> findByCodeB(String codeB);
}