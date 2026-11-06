package ru.practicum.main.compilations;

import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;

import java.util.Collection;

public interface CompilationsService {

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    Collection<CompilationDto> getAllCompilations(Boolean pinned, int offset, int size);

    CompilationDto getCompilationById(Long id);

    CompilationDto updateCompilation(Long id, UpdateCompilationRequest compilationDto);

    void deleteCompilation(Long id);
}
