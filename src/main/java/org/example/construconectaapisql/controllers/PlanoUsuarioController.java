package org.example.construconectaapisql.controllers;

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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user-plan")
public class PlanoUsuarioController {
    private final PlanoUsuarioService planoUsuarioService;
    private final Validator validator;

    @Autowired
    public PlanoUsuarioController(Validator validator, PlanoUsuarioService planoUsuarioService) {
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
    public List<PlanoUsuario> findAllUserPlans() { return planoUsuarioService.findAllUserPlans(); }

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
            StringBuilder sb = new StringBuilder("Erros de validação: ");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
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

    @DeleteMapping("/delete/{planoUsuarioId}")
    @Operation(summary = "Delete a user plan", description = "Deletes the user plan with the specified planoUsuarioId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User Plan deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "User Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteUserPlanByPlanoUsuarioId ( @PathVariable Long planoUsuarioId ) {
        planoUsuarioService.deleteUserPlan(planoUsuarioId);
        return ResponseEntity.ok("Plano do usuário excluído com sucesso");
    }

    @PatchMapping("/update/{planoUsuarioId}")
    @Operation(summary = "Update a user plan", description = "Updates the user plan data with the specified planoUsuarioId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User Plan updated successfully",
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
    public ResponseEntity<?> updateUserPlan( @Valid @PathVariable Long planoUsuarioId,
                                          @RequestBody Map<String, Object> updates ) {
        try {
            PlanoUsuario planoUsuario = planoUsuarioService.findUserPlanById(planoUsuarioId);
            if (updates.containsKey("cpf") ) { planoUsuario.setPlano((Integer) updates.get("plano")); }
            DataBinder binder = new DataBinder(planoUsuario);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            planoUsuarioService.saveUserPlan(planoUsuario);
            return ResponseEntity.ok("O plano do usuário com planoUsuarioId " + planoUsuarioId + " foi atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findById/{planoUsuarioId}")
    @Operation(summary = "Find user plan by planoUsuarioId", description = "Returns the user plan with the specified planoUsuarioId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User plan found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoUsuario.class))),
            @ApiResponse(responseCode = "404", description = "User plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findUserPlanById ( @PathVariable Long planoUsuarioId ) {
        return ResponseEntity.ok(planoUsuarioService.findUserPlanById(planoUsuarioId));
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
    public ResponseEntity<?> searchByUserId ( @PathVariable String usuario ) {
        List<PlanoUsuario> lPlanoUsuario = planoUsuarioService.findUserPlansByUserId(usuario);
        if(!lPlanoUsuario.isEmpty()) {
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
    public ResponseEntity<?> searchByPlanId ( @PathVariable Integer plano ) {
        List<PlanoUsuario> lPlanoUsuario = planoUsuarioService.findUserPlanByPlanoId(plano);
        if(!lPlanoUsuario.isEmpty()) {
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
    public ResponseEntity<?> searchByDataAssinatura ( @PathVariable Date dataAssinatura ) {
        List<PlanoUsuario> lPlanoUsuario = planoUsuarioService.findUserPlanByDataAssinatura(dataAssinatura);
        if(!lPlanoUsuario.isEmpty()) {
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
    public ResponseEntity<?> searchByDataFinal ( @PathVariable Date dataFinal ) {
        List<PlanoUsuario> lPlanoUsuario = planoUsuarioService.findUserPlanByDataFinal(dataFinal);
        if(!lPlanoUsuario.isEmpty()) {
            return ResponseEntity.ok(lPlanoUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano do Usuário não encontrado.");
        }
    }
    
    public Map<String, String> validate(BindingResult resultado ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}
