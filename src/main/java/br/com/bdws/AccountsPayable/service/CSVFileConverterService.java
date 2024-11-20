package br.com.bdws.AccountsPayable.service;

import br.com.bdws.AccountsPayable.dto.AccountPayableFromCsvDto;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class CSVFileConverterService {

    public List<AccountPayableFromCsvDto> convertToAccountPayableFromCsvDto(MultipartFile csv) throws IOException {
        InputStreamReader reader = new InputStreamReader(csv.getInputStream());
        CsvToBean<AccountPayableFromCsvDto> csvToBean = new CsvToBeanBuilder<AccountPayableFromCsvDto>(reader)
                .withType(AccountPayableFromCsvDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withThrowExceptions(true)
                .build();
        return csvToBean.parse();
    }
}
