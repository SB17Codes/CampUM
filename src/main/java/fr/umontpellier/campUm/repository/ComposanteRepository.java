package fr.umontpellier.campUm.repository;

import fr.umontpellier.campUm.entity.Composante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComposanteRepository extends JpaRepository<Composante, String> {
}