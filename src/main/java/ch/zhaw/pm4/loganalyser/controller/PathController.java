package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.exception.PathNotFoundException;
import ch.zhaw.pm4.loganalyser.model.dto.FileTreeDTO;
import ch.zhaw.pm4.loganalyser.service.PathService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API controller for {@link FileTreeDTO}
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService service;

    /**
     * Returns a transformed list of {@link FileTreeDTO} either containing the root folders of the disk
     * or sub folders from a specific folder.
     * @param folder Optional folder to list its subfolder.
     * @return {@link ResponseEntity} with status 200 and a list of {@link FileTreeDTO} inside the body.
     * @throws PathNotFoundException when the directory does not exist.
     */
    @GetMapping
    public ResponseEntity<List<FileTreeDTO>> getFoldersOfPath(@RequestParam(required = false) String folder) {
        return ResponseEntity.ok(folder == null ? service.getRootFolder() : service.getContentOfFolder(folder));
    }

}
