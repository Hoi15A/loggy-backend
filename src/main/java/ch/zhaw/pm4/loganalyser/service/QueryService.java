package ch.zhaw.pm4.loganalyser.service;

import ch.zhaw.pm4.loganalyser.model.dto.ColumnDTO;
import ch.zhaw.pm4.loganalyser.model.dto.HeaderDTO;
import ch.zhaw.pm4.loganalyser.model.dto.TableDTO;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class QueryService {

    private final Logger logger = Logger.getLogger(QueryService.class.getName());

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
}
