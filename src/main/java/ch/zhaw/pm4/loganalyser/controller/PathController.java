package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.service.PathService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/path")
@AllArgsConstructor
public class PathController {

    private final PathService service;

    @GetMapping("")
    public ResponseEntity<File[]> getFoldersOfPath(@RequestParam String folder) {
        return ResponseEntity.ok(service.getContentOfFolder(folder));
    }

    public ResponseEntity<String> getRootFolder() {
        return ResponseEntity.ok(service.getRootFolder());
    }
}
