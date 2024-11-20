package br.com.bdws.AccountsPayable.repository;

import br.com.bdws.AccountsPayable.entity.AccountPayable;
import br.com.bdws.AccountsPayable.entity.enums.AccountStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Lazy
public interface IAccountPayableRepository extends JpaRepository<AccountPayable, Long> {

    Page<AccountPayable> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    Page<AccountPayable> findByDueDateAndDescriptionContainingIgnoreCase(LocalDate dueDate, String description, Pageable pageable);

    List<AccountPayable> findByStatusAndPaymentDateBetween(AccountStatus status, LocalDate start, LocalDate end);
}
