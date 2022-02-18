package hexlet.code.service.impl;

import hexlet.code.dto.LabelCreationDto;
import hexlet.code.exceptions.NotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private LabelRepository labelRepository;

    @Override
    public Label createNewLabel(LabelCreationDto labelCreationDto) {
        return labelRepository.save(new Label(
                labelCreationDto.name()
        ));
    }

    @Override
    public Label changeLabel(Long id, LabelCreationDto labelCreationDto) {
        return labelRepository.save(labelRepository.findById(id)
                .map(l -> {
                    l.setName(labelCreationDto.name());
                    return l;
                })
                .orElseThrow(() -> new NotFoundException("Label with such ID not found.")));
    }
}
