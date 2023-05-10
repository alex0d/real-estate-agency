package ru.alex0d.realestateagency.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.alex0d.realestateagency.model.RealEstate;
import ru.alex0d.realestateagency.service.RealEstateService;

import java.util.List;

@RestController
@RequestMapping("/api/real-estate")
@RequiredArgsConstructor
public class RealEstateApiController {
    private final RealEstateService realEstateService;

    @GetMapping
    public @ResponseBody List<RealEstate> search(@RequestParam(name = "q", required = false) String query) {
        if (query == null || query.isBlank()) {
            return realEstateService.findAll();
        }
        return realEstateService.search(query);
    }
}
