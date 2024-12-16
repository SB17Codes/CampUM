package fr.umontpellier.campUm.repository;

import fr.umontpellier.campUm.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, String> {
    List<Salle> findByBatimentB_CodeB(String codeB);
}