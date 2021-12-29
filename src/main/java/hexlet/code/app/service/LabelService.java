package hexlet.code.app.service;

import hexlet.code.app.dto.LabelCreationDto;
import hexlet.code.app.model.Label;

public interface LabelService {
    Label createNewLabel(LabelCreationDto labelCreationDto);
    Label changeLabel(Long id, LabelCreationDto labelCreationDto);
}
