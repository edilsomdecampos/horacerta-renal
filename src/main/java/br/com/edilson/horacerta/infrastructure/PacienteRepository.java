package br.com.edilson.horacerta.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.edilson.horacerta.domain.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
