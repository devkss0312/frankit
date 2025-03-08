package com.frankit.task.controller;

import com.frankit.task.dto.OptionDTO;
import com.frankit.task.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;

    @PostMapping
    public ResponseEntity<OptionDTO> addOption(@RequestBody OptionDTO optionDTO) {
        return ResponseEntity.ok(optionService.addOption(optionDTO));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<OptionDTO>> getOptions(@PathVariable Long productId) {
        return ResponseEntity.ok(optionService.getOptionsByProduct(productId));
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<OptionDTO> updateOption(@PathVariable Long optionId, @RequestBody OptionDTO optionDTO) {
        return ResponseEntity.ok(optionService.updateOption(optionId, optionDTO));
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
