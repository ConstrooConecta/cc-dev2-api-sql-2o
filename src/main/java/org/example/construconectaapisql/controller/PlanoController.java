package org.example.construconectaapisql.controller;

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
import java.util.Arrays;
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
    @Operation(summary = "Add a new plan", description = "Create a new plan and saves it to the database")
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
            StringBuilder sb = new StringBuilder("Validation errors:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n| ");
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

    @DeleteMapping("/delete/{planId}")
    @Operation(summary = "Delete a plan", description = "Deletes the plan with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Plan deleted successfully",
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
    public ResponseEntity<?> deletePlanById(@PathVariable Long planId) {
        try {
            planoService.deletePlan(planId);
            return ResponseEntity.ok("Plano excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar plano: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{planId}")
    @Operation(summary = "Update a plan", description = "Updates the plan data with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Plano.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Plan not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updatePlan(@Valid @PathVariable Long planId, @RequestBody Map<String, Object> updates) {
        try {
            Plano plano = planoService.findPlanById(planId);

            // Lista de campos válidos que podem ser atualizados
            List<String> validFields = Arrays.asList("nome", "descricao", "valor");

            // Itera sobre as atualizações e só aplica as que são válidas
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "nome":
                        plano.setNome((String) entry.getValue());
                        break;
                    case "descricao":
                        plano.setDescricao((String) entry.getValue());
                        break;
                    case "valor":
                        plano.setValor((BigDecimal) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            // Validação do plano atualizado
            DataBinder binder = new DataBinder(plano);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            planoService.savePlan(plano);
            return ResponseEntity.ok("O plano com ID " + planId + " foi atualizado com sucesso.");
        } catch (DataIntegrityViolationException e) {
            String message = e.getRootCause().getMessage();
            if (message.contains("nome")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Este nome de plano já está em uso.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/findById/{planId}")
    @Operation(summary = "Find plan by ID", description = "Returns the plan with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Plano.class))),
            @ApiResponse(responseCode = "404", description = "Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findPlanById(@PathVariable Long planId) {
        return ResponseEntity.ok(planoService.findPlanById(planId));
    }

    @GetMapping("/findByName/{nane}")
    @Operation(summary = "Find plan by name", description = "Returns the plan with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Plano.class))),
            @ApiResponse(responseCode = "404", description = "Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findPlanByName(@PathVariable String name) {
        return ResponseEntity.ok(planoService.findByNomeCompletoLikeIgnoreCase(name));
    }

    public Map<String, String> validate(BindingResult resultado) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
