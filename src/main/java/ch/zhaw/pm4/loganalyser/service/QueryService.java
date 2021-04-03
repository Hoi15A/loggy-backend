package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.exception.FileNotFoundException;
import ch.zhaw.pm4.loganalyser.exception.RecordNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.ColumnDTO;
import ch.zhaw.pm4.loganalyser.model.dto.HeaderDTO;
import ch.zhaw.pm4.loganalyser.model.dto.TableDTO;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.parser.LogParser;
import ch.zhaw.pm4.loganalyser.repository.LogServiceRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class QueryService {

    @Setter
    @NonNull
    private LogServiceRepository logServiceRepository;

    private final Logger logger = Logger.getLogger(QueryService.class.getName());
    @Setter
    private LogParser logParser = new LogParser();

     public TableDTO getSampleLogsByQuery() {

         List<ColumnDTO> tableData = new ArrayList<>();
         List<HeaderDTO> headers = new ArrayList<>();
         SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
         try {
            tableData.add(new ColumnDTO(sdf.parse("01.03.2021"), "info", "Service started"));
            tableData.add(new ColumnDTO(sdf.parse("02.03.2021"), "warning", "Could not interpret xyz"));
            tableData.add(new ColumnDTO(sdf.parse("03.03.2021"), "error", "FileNotFound test.txt"));
         } catch (ParseException e) {
             logger.log(Level.WARNING, "Unable to parse the given date", e);
         }

         headers.add(new HeaderDTO("Date", "date"));
         headers.add(new HeaderDTO("Type", "type"));
         headers.add(new HeaderDTO("Message", "message"));

        return new TableDTO(headers, tableData);

     }

    /**
     * Runs a query on a service and returns all matched lines
     * @param serviceId log service id
     * @param query TODO
     * @return rows
     */
    public List<String[]> runQueryForService(long serviceId, String query) {
        Optional<LogService> logService = logServiceRepository.findById(serviceId);
        if(logService.isEmpty()) throw new RecordNotFoundException(String.valueOf(serviceId));

        try {
            return logParser.read(null, logService.get());
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException("Logservice file not found");
        }
    }
}
