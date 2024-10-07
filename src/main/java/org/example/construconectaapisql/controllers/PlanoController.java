package org.example.construconectaapisql.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.Plano;
import org.example.construconectaapisql.service.PlanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
public class PlanoController {

    private final PlanoService planoService;
    private final Validator validator;

    @Autowired
    public PlanoController(Validator validator, PlanoService planoService) {
        this.planoService = planoService;
        this.validator = validator;
    }

    @GetMapping("/plans")
    @Operation(summary = "Show all plans", description = "Returns a list of all available plans")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Plano.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<Plano> findAllPlans() {
        return planoService.findAllPlans();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new plan", description = "Creates a new plan and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Plan created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Plano.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or plan already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addPlan(@RequestBody Plano plano, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Validation errors: ");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            Plano savedPlano = planoService.savePlan(plano);
            if (savedPlano != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedPlano);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Plano já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data integrity error: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database access error: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding plan: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/drop/{planoId}")
    @Operation(summary = "Delete a plan", description = "Deletes the plan with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Plan deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Plano.class))),
            @ApiResponse(responseCode = "404", description = "Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> dropPlanById(@PathVariable Long planoId) {
        planoService.deletePlan(planoId);
        return ResponseEntity.ok("Plano excluído com sucesso");
    }

    @PatchMapping("/update/{planoId}")
    @Operation(summary = "Update a plan", description = "Updates the plan data with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Plan updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Plano.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updatePlan(@Valid @PathVariable Long planoId, @RequestBody Map<String, Object> updates) {
        try {
            Plano plano = planoService.findPlanById(planoId);
            if (updates.containsKey("nome")) {
                plano.setNome((String) updates.get("nome"));
            }
            if (updates.containsKey("descricao")) {
                plano.setDescricao((String) updates.get("descricao"));
            }
            if (updates.containsKey("valor")) {
                plano.setValor((BigDecimal) updates.get("valor"));
            }

            DataBinder binder = new DataBinder(plano);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            planoService.savePlan(plano);
            return ResponseEntity.ok("O plano com ID " + planoId + " foi atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findById/{planoId}")
    @Operation(summary = "Find plan by ID", description = "Returns the plan with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Plano.class))),
            @ApiResponse(responseCode = "404", description = "Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(planoService.findPlanById(id));
    }

    public Map<String, String> validate(BindingResult resultado) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
