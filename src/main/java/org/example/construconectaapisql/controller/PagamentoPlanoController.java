package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.PagamentoPlano;
import org.example.construconectaapisql.service.PagamentoPlanoService;
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
import java.util.*;

@RestController
@RequestMapping("/payment-plan")
public class PagamentoPlanoController {
    private final PagamentoPlanoService pagamentoPlanoService;
    private final Validator validator;

    @Autowired
    public PagamentoPlanoController(PagamentoPlanoService pagamentoPlanoService, Validator validator) {
        this.pagamentoPlanoService = pagamentoPlanoService;
        this.validator = validator;
    }

    @GetMapping("/payments-plan")
    @Operation(summary = "Show all payments plan", description = "Returns a list of all available payments plan")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPlano.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<PagamentoPlano> findAllPaymentsPlan() {
        return pagamentoPlanoService.findAllPaymentsPlan();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new payment plan", description = "Create a new payment plan and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Plan created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPlano.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or payment plan already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addPaymentPlan(@RequestBody PagamentoPlano pagamentoPlano, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            PagamentoPlano savedPaymentPlan = pagamentoPlanoService.savePaymentPlan(pagamentoPlano);
            if (savedPaymentPlan != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentPlan);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados de Pagamento de Plano já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar dados de pagamento do plano: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{planPaymentId}")
    @Operation(summary = "Delete a payment plan", description = "Deletes the payment plan with the specified planPaymentId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Plan deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPlano.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Payment Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deletePaymentPlanByPagamentoPlanoId(@PathVariable Long planPaymentId) {
        try {
            pagamentoPlanoService.deletePaymentPlan(planPaymentId);
            return ResponseEntity.ok("Pagamento do Plano excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar dados de pagamento do plano: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{planPaymentId}")
    @Operation(summary = "Update a payment plan", description = "Updates the payment plan with the specified planPaymentId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Plan updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPlano.class))),
            @ApiResponse(responseCode = "404", description = "Payment Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updatePaymentPlan(@Valid @PathVariable Long planPaymentId,
                                               @RequestBody Map<String, Object> updates) {
        try {
            PagamentoPlano pagamentoPlano = pagamentoPlanoService.findPaymentPlanById(planPaymentId);

            List<String> validFields = Arrays.asList("plano", "valor", "tipoPagamento", "dataPagamento");

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "plano":
                        pagamentoPlano.setPlano((Integer) entry.getValue());
                        break;
                    case "valor":
                        pagamentoPlano.setValor((BigDecimal) entry.getValue());
                        break;
                    case "tipoPagamento":
                        pagamentoPlano.setTipoPagamento((String) entry.getValue());
                        break;
                    case "dataPagamento":
                        pagamentoPlano.setDataPagamento((String) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            DataBinder binder = new DataBinder(pagamentoPlano);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            pagamentoPlanoService.savePaymentPlan(pagamentoPlano);
            return ResponseEntity.ok("O plano do usuário com planPaymentId " + planPaymentId + " foi atualizado com sucesso.");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findById/{planPaymentId}")
    @Operation(summary = "Find payment plan", description = "Returns the user plan with the specified planPaymentId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Plan found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPlano.class))),
            @ApiResponse(responseCode = "404", description = "Payment Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findPaymentPlanById(@PathVariable Long planPaymentId) {
        try {
            PagamentoPlano pagamentoPlano = pagamentoPlanoService.findPaymentPlanById(planPaymentId);
            if (pagamentoPlano == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de Pagamento de Plano não encontrado.");
            }
            return ResponseEntity.ok(pagamentoPlano);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByPlanId/{plano}")
    @Operation(summary = "Find payment plan by planId", description = "Returns the user plan with the specified planId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Plan found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPlano.class))),
            @ApiResponse(responseCode = "404", description = "Payment Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByPlanId(@PathVariable Integer plano) {
        List<PagamentoPlano> lPagamentoPlano = pagamentoPlanoService.findByPlanoId(plano);
        if (!lPagamentoPlano.isEmpty()) {
            return ResponseEntity.ok(lPagamentoPlano);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de Pagamento de Plano não encontrado.");
        }
    }

    @GetMapping("/findByUserId/{usuario}")
    @Operation(summary = "Find payment plan by userId", description = "Returns the user plan with the specified userId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Plan found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPlano.class))),
            @ApiResponse(responseCode = "404", description = "Payment Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUserId(@PathVariable String usuario) {
        List<PagamentoPlano> lPagamentoPlano = pagamentoPlanoService.findByUserId(usuario);
        if (!lPagamentoPlano.isEmpty()) {
            return ResponseEntity.ok(lPagamentoPlano);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de Pagamento de Plano não encontrado.");
        }
    }

    @GetMapping("/findByDataPagamento/{dataPagamento}")
    @Operation(summary = "Find payment plan by dataPagamento", description = "Returns the user plan with the specified dataPagamento")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Plan found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPlano.class))),
            @ApiResponse(responseCode = "404", description = "Payment Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByDtPagamento(@PathVariable String dataPagamento) {
        List<PagamentoPlano> lPagamentoPlano = pagamentoPlanoService.findByDataPagamento(dataPagamento);
        if (!lPagamentoPlano.isEmpty()) {
            return ResponseEntity.ok(lPagamentoPlano);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de Pagamento de Plano não encontrado.");
        }
    }

    @GetMapping("/findByDataPagamento/{tipoPagamento}")
    @Operation(summary = "Find payment plan by tipoPagamento", description = "Returns the user plan with the specified tipoPagamento")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Plan found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPlano.class))),
            @ApiResponse(responseCode = "404", description = "Payment Plan not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByTipoPagamento(@PathVariable String tipoPagamento) {
        List<PagamentoPlano> lPagamentoPlano = pagamentoPlanoService.findByTipoPagamento(tipoPagamento);
        if (!lPagamentoPlano.isEmpty()) {
            return ResponseEntity.ok(lPagamentoPlano);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de Pagamento de Plano não encontrado.");
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
