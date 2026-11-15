package br.com.edilson.horacerta.infrastructure;

import br.com.edilson.horacerta.domain.LocalAdministracao;
import br.com.edilson.horacerta.domain.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, UUID> {

    List<Medicamento> findByLocalAdministracao(LocalAdministracao localAdministracao);

    List<Medicamento> findByNomeContainingIgnoreCase(String nome);
}