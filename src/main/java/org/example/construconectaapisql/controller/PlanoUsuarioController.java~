package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.PlanoUsuario;
import org.example.construconectaapisql.service.PlanoUsuarioService;
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

import java.util.*;

@RestController
@RequestMapping("/user-plan")
public class PlanoUsuarioController {
    private final PlanoUsuarioService planoUsuarioService;
    private final Validator validator;

    @Autowired
    public PlanoUsuarioController(
            Validator validator, PlanoUsuarioService planoUsuarioService
    ) {
        this.validator = validator;
        this.planoUsuarioService = planoUsuarioService;
    }

    @GetMapping("/user-plans")
    @Operation(summary = "Show all user plans", description = "Returns a list of all available user plans")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<PlanoUsuario> findAllUserPlans() {
        return planoUsuarioService.findAllUserPlans();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new user plan", description = "Create a new user plan and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User Plan created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or user plan already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addUserPlan(@RequestBody PlanoUsuario planoUsuario, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            PlanoUsuario savedUserPlan = planoUsuarioService.saveUserPlan(planoUsuario);
            if (savedUserPlan != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedUserPlan);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Plano do usuário já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar plano do usuário: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{userPlanId}")
    @Operation(summary = "Delete a user plan", description = "Deletes the user plan with the specified userPlanId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User Plan deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteUserPlanByPlanoUsuarioId(@PathVariable Long userPlanId) {
        try {
            planoUsuarioService.deleteUserPlan(userPlanId);
            return ResponseEntity.ok("Plano do usuário excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar dados do plano do usuário: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{userPlanId}")
    @Operation(summary = "Update a user plan", description = "Updates the user plan data with the specified userPlanId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Plan updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User Plan not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateUserPlan(@Valid @PathVariable Long userPlanId, @RequestBody Map<String, Object> updates) {
        try {
            PlanoUsuario planoUsuario = planoUsuarioService.findUserPlanById(userPlanId);

            // Lista de campos válidos que podem ser atualizados
            List<String> validFields = Arrays.asList("plano");

            // Itera sobre as atualizações e só aplica as que são válidas
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "plano":
                        planoUsuario.setPlano((Integer) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            // Validação do plano do usuário atualizado
            DataBinder binder = new DataBinder(planoUsuario);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            planoUsuarioService.saveUserPlan(planoUsuario);
            return ResponseEntity.ok("O plano do usuário com userPlanId " + userPlanId + " foi atualizado com sucesso.");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findById/{userPlanId}")
    @Operation(summary = "Find user plan by userPlanId", description = "Returns the user plan with the specified userPlanId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User plan found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "User plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findUserPlanById(@PathVariable Long userPlanId) {
        try {
            PlanoUsuario userPlan = planoUsuarioService.findUserPlanById(userPlanId);
            if (userPlan == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano do usuário não encontrado.");
            }
            return ResponseEntity.ok(userPlan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByUserId/{usuario}")
    @Operation(summary = "Search user plan by usuarioId", description = "Returns a list of user plan with the specified usuarioId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Plan found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "User Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUserId(@PathVariable String usuario) {
        List<PlanoUsuario> lPlanoUsuario = planoUsuarioService.findUserPlansByUserId(usuario);
        if (!lPlanoUsuario.isEmpty()) {
            return ResponseEntity.ok(lPlanoUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano do Usuário não encontrado.");
        }
    }

    @GetMapping("/findByPlanId/{usuario}")
    @Operation(summary = "Search user plan by planoId", description = "Returns a list of user plan with the specified planoId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Plan found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "User Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByPlanId(@PathVariable Integer plano) {
        List<PlanoUsuario> lPlanoUsuario = planoUsuarioService.findUserPlanByPlanoId(plano);
        if (!lPlanoUsuario.isEmpty()) {
            return ResponseEntity.ok(lPlanoUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano do Usuário não encontrado.");
        }
    }

    @GetMapping("/findByDataAssinatura/{dataAssinatura}")
    @Operation(summary = "Search user plan by dataAssinatura", description = "Returns a list of user plan with the specified dataAssinatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Plan found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "User Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByDataAssinatura(@PathVariable Date dataAssinatura) {
        List<PlanoUsuario> lPlanoUsuario = planoUsuarioService.findUserPlanByDataAssinatura(dataAssinatura);
        if (!lPlanoUsuario.isEmpty()) {
            return ResponseEntity.ok(lPlanoUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano do Usuário não encontrado.");
        }
    }

    @GetMapping("/findByDataAssinatura/{dataFinal}")
    @Operation(summary = "Search user plan by dataFinal", description = "Returns a list of user plan with the specified dataFinal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Plan found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "User Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByDataFinal(@PathVariable Date dataFinal) {
        List<PlanoUsuario> lPlanoUsuario = planoUsuarioService.findUserPlanByDataFinal(dataFinal);
        if (!lPlanoUsuario.isEmpty()) {
            return ResponseEntity.ok(lPlanoUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano do Usuário não encontrado.");
        }
    }

    public Map<String, String> validate(BindingResult resultado) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}
