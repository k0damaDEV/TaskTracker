package hexlet.code.service;

import hexlet.code.dto.LabelCreationDto;
import hexlet.code.model.Label;

public interface LabelService {
    Label createNewLabel(LabelCreationDto labelCreationDto);
    Label changeLabel(Long id, LabelCreationDto labelCreationDto);
}
