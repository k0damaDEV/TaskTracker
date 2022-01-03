package hexlet.code.controller;

import hexlet.code.dto.LabelCreationDto;
import hexlet.code.exceptions.NotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;

import java.util.List;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";

    private LabelService labelService;
    private LabelRepository labelRepository;

    @Operation(summary = "Create new label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label was created"),
            @ApiResponse(responseCode = "422", description = "Invalid arguments"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public Label createNewLabel(@Valid @RequestBody LabelCreationDto labelCreationDto) {
        return labelService.createNewLabel(labelCreationDto);
    }

    @Operation(summary = "Change label data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label has been changed"),
            @ApiResponse(responseCode = "422", description = "Invalid arguments"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Label with such ID not found")
    })
    @PutMapping(UsersController.ID)
    public Label changeLabel(@Valid @RequestBody LabelCreationDto labelCreationDto,
                             @PathVariable(name = "id") Long id) {
        return labelService.changeLabel(id, labelCreationDto);
    }

    @Operation(summary = "Get label by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get label"),
            @ApiResponse(responseCode = "404", description = "Label with such ID not found"),
    })
    @GetMapping(UsersController.ID)
    public Label getLabelById(@PathVariable(name = "id") Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Label with such ID not found"));
    }

    @Operation(summary = "Get all labels")
    @ApiResponse(responseCode = "200", description = "Get all labels")
    @GetMapping
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Operation(summary = "Delete label by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label has been deleted"),
            @ApiResponse(responseCode = "404", description = "Label withc such ID not found"),
            @ApiResponse(responseCode = "422", description = "Invalid arguments"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping(UsersController.ID)
    public String deleteLabel(@PathVariable(name = "id") Long id) {
        labelRepository.deleteById(id);
        return "OK";
    }
}
