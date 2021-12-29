package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelCreationDto;
import hexlet.code.app.exceptions.NotFoundException;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.app.controller.UsersController.ID;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";

    private LabelService labelService;
    private LabelRepository labelRepository;

    @PostMapping
    public Label createNewLabel(@Valid @RequestBody LabelCreationDto labelCreationDto) {
        return labelService.createNewLabel(labelCreationDto);
    }

    @PutMapping(ID)
    public Label changeLabel(@Valid @RequestBody LabelCreationDto labelCreationDto, @PathVariable(name = "id") Long id) {
        return labelService.changeLabel(id, labelCreationDto);
    }

    @GetMapping(ID)
    public Label getLabelById(@PathVariable(name = "id") Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Label with such ID not found"));
    }

    @GetMapping
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @DeleteMapping(ID)
    public String deleteLabel(@PathVariable(name = "id") Long id) {
        labelRepository.deleteById(id);
        return "OK";
    }
}
