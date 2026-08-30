package ru.practicum.main.compilations;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;

import java.util.Collection;

@RestController
public class CompilationsController {

    private final CompilationsService compilationsService;

    public CompilationsController(CompilationsService compilationsService) {
        this.compilationsService = compilationsService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/compilations")
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {
        return compilationsService.createCompilation(compilationDto);
    }

    @GetMapping("/compilations")
    public Collection<CompilationDto> getAllCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return compilationsService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationsService.getCompilationById(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    CompilationDto updateCompilation(
            @RequestBody @Valid UpdateCompilationRequest compilationDto,
            @PathVariable Long compId
    ) {
        return compilationsService.updateCompilation(compId, compilationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationsService.deleteCompilation(compId);
    }

}
