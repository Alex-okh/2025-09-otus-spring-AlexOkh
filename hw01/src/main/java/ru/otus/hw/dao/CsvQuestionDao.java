package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;
    private final ResourceLoader resourceLoader;

    @Override
    public List<Question> findAll() {
        Resource resource = resourceLoader.getResource(fileNameProvider.getTestFileName());
        try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(reader).withType(QuestionDto.class)
                                                                                        .withSeparator(';')
                                                                                        .withSkipLines(1)
                                                                                        .withIgnoreLeadingWhiteSpace(
                                                                                                true)
                                                                                        .build();
            return csvToBean.stream()
                            .map(QuestionDto::toDomainObject)
                            .toList();
        } catch (IOException e) {

            throw new QuestionReadException("Could not read file", e);
        }
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/


    }
}
