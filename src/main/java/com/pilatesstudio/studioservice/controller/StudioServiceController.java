package com.pilatesstudio.studioservice.controller;
import com.pilatesstudio.studioservice.dto.StudioServiceDto;
import com.pilatesstudio.studioservice.service.StudioServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/services") @RequiredArgsConstructor @PreAuthorize("hasAuthority('PROFILE_ADMIN')")
public class StudioServiceController {
 private final StudioServiceService service;
 @GetMapping public List<StudioServiceDto> findAll(){return service.findAll();}
 @GetMapping("/{id}") public StudioServiceDto findById(@PathVariable Long id){return service.findById(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public StudioServiceDto create(@Valid @RequestBody StudioServiceDto dto){return service.create(dto);}
 @PutMapping public StudioServiceDto update(@Valid @RequestBody StudioServiceDto dto){return service.update(dto);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
