package ch.zhaw.pm4.loganalyser.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService {

     public List<String[]> getSampleLogsByQuery() {
         List<String[]> log = new ArrayList<>();
         log.add(new String[] {"Date", "Type", "Message"});
         log.add(new String[] {"01.03.2021", "info", "Service started"});
         log.add(new String[] {"02.03.2021", "warning", "Could not interpret xyz"});
         log.add(new String[] {"03.03.2021", "error", "FileNotFound test.txt"});
         return log;
     }
}
