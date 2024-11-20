package br.com.bdws.AccountsPayable.dto;

import br.com.bdws.AccountsPayable.entity.enums.AccountStatus;
import com.opencsv.bean.AbstractBeanField;

public class AccountStatusConverter extends AbstractBeanField<AccountStatus, String> {
    @Override
    protected AccountStatus convert(String value) {
        return AccountStatus.valueOf(value.toUpperCase());
    }
}
