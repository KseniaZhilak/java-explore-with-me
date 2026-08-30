package ru.practicum.main.compilations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;
import ru.practicum.main.compilations.repository.CompilationEntity;
import ru.practicum.main.compilations.repository.CompilationRepository;
import ru.practicum.main.compilations.repository.mapper.CompilationsMapper;
import ru.practicum.main.events.repository.EventsEntity;
import ru.practicum.main.events.repository.EventsRepository;
import ru.practicum.main.exception.NotFoundException;

import java.util.*;

@Service
public class CompilationsServiceImpl implements CompilationsService {

    private final CompilationsMapper compilationsMapper;
    private final CompilationRepository compilationRepository;
    private final EventsRepository eventsRepository;

    public CompilationsServiceImpl(CompilationsMapper compilationsMapper, CompilationRepository compilationRepository, EventsRepository eventsRepository) {
        this.compilationsMapper = compilationsMapper;
        this.compilationRepository = compilationRepository;
        this.eventsRepository = eventsRepository;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        List<EventsEntity> eventsEntities = eventsRepository
                .findAllById(compilationDto.getEvents());
        if (eventsEntities.size() != compilationDto.getEvents().size()) {
            throw new NotFoundException("Some events were not found");
        }
        CompilationEntity compilationEntity = compilationsMapper.toEntity(compilationDto);
        compilationEntity.setEvents(new HashSet<>(eventsEntities));

        CompilationEntity saved = compilationRepository.save(compilationEntity);
        return compilationsMapper.toDto(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CompilationDto> getAllCompilations(Boolean pinned, int offset, int size) {
        boolean filterByPinned = pinned != null;
        List<CompilationEntity> compilations = compilationRepository
                .findCompilationsByFilters(filterByPinned, Boolean.TRUE.equals(pinned), offset, size);
        return compilations.stream().map(compilationsMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long id) {
        CompilationEntity compilationEntity = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Compilation not found")
        );
        return compilationsMapper.toDto(compilationEntity);
    }

    @Override
    public CompilationDto updateCompilation(Long id, UpdateCompilationRequest compilationDto) {
        CompilationEntity compilationEntity = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Compilation not found")
        );

        if(compilationDto.getEvents() != null) {
            List<EventsEntity> eventsEntities = eventsRepository
                    .findAllById(compilationDto.getEvents());
            if (eventsEntities.size() != compilationDto.getEvents().size()) {
                throw new NotFoundException("Some events were not found");
            }
            compilationEntity.setEvents(new HashSet<>(eventsEntities));
        }

        CompilationEntity updated = compilationsMapper
                .toEntity(compilationEntity, compilationDto);
        CompilationEntity saved = compilationRepository.save(updated);

        return compilationsMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new NotFoundException("Compilation not found");
        }
        compilationRepository.deleteById(id);
    }
}
