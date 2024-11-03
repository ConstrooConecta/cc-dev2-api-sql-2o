package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.PagamentoServico;
import org.example.construconectaapisql.service.PagamentoServicoService;
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
@RequestMapping("/payment-service")
public class PagamentoServicoController {
    private final PagamentoServicoService pagamentoServicoService;
    private final Validator validator;

    @Autowired
    public PagamentoServicoController(
            Validator validator, PagamentoServicoService pagamentoServicoService
    ) {
        this.validator = validator;
        this.pagamentoServicoService = pagamentoServicoService;
    }

    @GetMapping("/payments-services")
    @Operation(summary = "Show all payments services", description = "Returns a list of all available payments services")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoServico.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<PagamentoServico> findAllPaymentsServices() {
        return pagamentoServicoService.findAllPaymentsServices();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new payment service", description = "Create a new payment service and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Service created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoServico.class))),
            @ApiResponse(responseCode = "201", description = "Payment Service created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or payment service already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addPaymentService(@Valid @RequestBody PagamentoServico paymentService, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            PagamentoServico savedPagamentoServico = pagamentoServicoService.savePaymentService(paymentService);
            if (savedPagamentoServico != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedPagamentoServico);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados de pagamento de serviço já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar dados de pagamento do serviço: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{paymentServiceId}")
    @Operation(summary = "Delete a payment service", description = "Deletes the payment service with the specified paymentServiceId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment Service deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoServico.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Payment Service not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deletePaymentServiceById(@PathVariable Long paymentServiceId) {
        try {
            pagamentoServicoService.deletePaymentService(paymentServiceId);
            return ResponseEntity.ok("Dados de pagamento de serviço excluídos com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar dados de pagamento douten: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{paymentServiceId}")
    @Operation(summary = "Update a payment service", description = "Updates the payment service data with the specified paymentServiceId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment Service updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoServico.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Payment Service not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updatePaymentService(@Valid @PathVariable Long paymentServiceId,
                                                  @RequestBody Map<String, Object> updates) {
        try {
            PagamentoServico paymentService = pagamentoServicoService.findPaymentsServicesById(paymentServiceId);

            // Lista de campos válidos que podem ser atualizados
            List<String> validFields = Arrays.asList("valorServico", "tipoPagamento", "dataPagamento");

            // Itera sobre as atualizações e só aplica as que são válidas
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "valorServico":
                        paymentService.setValorServico((BigDecimal) entry.getValue());
                        break;
                    case "tipoPagamento":
                        paymentService.setTipoPagamento((String) entry.getValue());
                        break;
                    case "dataPagamento":
                        paymentService.setDataPagamento((Date) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            // Validação do usuário atualizado
            DataBinder binder = new DataBinder(paymentService);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            pagamentoServicoService.savePaymentService(paymentService);
            return ResponseEntity.ok("O(s) dado(s) de Pagamento de Serviço com paymentServiceId " + paymentServiceId + " foi atualizado com sucesso.");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findByServicePaymentId/{servicePaymentId}")
    @Operation(summary = "Find service payment by servicePaymentId", description = "Returns the service payment with the specified servicePaymentId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service Payment found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoServico.class))),
            @ApiResponse(responseCode = "404", description = "Service Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findServicePaymentsByServicePaymentId(@PathVariable Long servicePaymentId) {
        try {
            PagamentoServico paymentService = pagamentoServicoService.findPaymentsServicesById(servicePaymentId);
            if (paymentService == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de pagamento de serviço não encontrados.");
            }
            return ResponseEntity.ok(paymentService);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByServiceId/{serviceId}")
    @Operation(summary = "Search services payments by serviceId", description = "Returns a list of services payments with the specified serviceId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services Payments found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoServico.class))),
            @ApiResponse(responseCode = "404", description = "Service Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByServiceId(@PathVariable Integer serviceId) {
        List<PagamentoServico> lServicePayment = pagamentoServicoService.findByServico(serviceId);
        if (!lServicePayment.isEmpty()) {
            return ResponseEntity.ok(lServicePayment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de pagamento de serviço não encontrados.");
        }
    }

    @GetMapping("/findByPaymentMethod/{paymentMethod}")
    @Operation(summary = "Search services payments by paymentMethod", description = "Returns a list of services payments with the specified paymentMethod")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services Payments found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoServico.class))),
            @ApiResponse(responseCode = "404", description = "Service Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByPaymentMethod(@PathVariable String paymentMethod) {
        List<PagamentoServico> lServicePayment = pagamentoServicoService.findByTipoPagamento(paymentMethod);
        if (!lServicePayment.isEmpty()) {
            return ResponseEntity.ok(lServicePayment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de pagamento de serviço não encontrados.");
        }
    }

    @GetMapping("/findByPaymentDate/{paymentDate}")
    @Operation(summary = "Search services payments by paymentDate", description = "Returns a list of services payments with the specified paymentDate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services Payments found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoServico.class))),
            @ApiResponse(responseCode = "404", description = "Service Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByPaymentMethod(@PathVariable Date paymentDate) {
        List<PagamentoServico> lServicePayment = pagamentoServicoService.findByDataPagamento(paymentDate);
        if (!lServicePayment.isEmpty()) {
            return ResponseEntity.ok(lServicePayment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de pagamento de serviço não encontrados.");
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
