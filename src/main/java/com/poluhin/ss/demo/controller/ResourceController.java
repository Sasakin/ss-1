package com.poluhin.ss.demo.controller;

import com.poluhin.ss.demo.domain.model.*;
import com.poluhin.ss.demo.service.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceObjectService service;

    @PostMapping
    public ResponseEntity<Integer> createResourceObject(@RequestBody ResourceObject object) {
        val result = service.save(object);
        return ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResourceObject>> getResources() {
        val result = service.getAll();
        return ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceObject> getResourceObject(@PathVariable Integer id) {
        return ok(service.get(id));
    }

}
