package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.search.SearchResult;
import com.healthyconsciousness.backend.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchResult>> globalSearch(@RequestParam String query) {
        return ResponseEntity.ok(searchService.globalSearch(query));
    }
}
