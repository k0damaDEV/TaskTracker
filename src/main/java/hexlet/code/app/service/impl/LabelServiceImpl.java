package hexlet.code.app.service.impl;

import hexlet.code.app.dto.LabelCreationDto;
import hexlet.code.app.exceptions.NotFoundException;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private LabelRepository labelRepository;

    @Override
    public Label createNewLabel(LabelCreationDto labelCreationDto) {
        return labelRepository.save(new Label(
                labelCreationDto.getName()
        ));
    }

    @Override
    public Label changeLabel(Long id, LabelCreationDto labelCreationDto) {
        Label dbLabel = labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Label with such ID not found."));
        dbLabel.setName(labelCreationDto.getName());
        return labelRepository.save(dbLabel);
    }
}
