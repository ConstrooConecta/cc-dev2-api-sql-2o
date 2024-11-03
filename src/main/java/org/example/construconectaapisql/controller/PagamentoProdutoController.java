package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.PagamentoProduto;
import org.example.construconectaapisql.model.Usuario;
import org.example.construconectaapisql.service.PagamentoProdutoService;
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
@RequestMapping("/product-payment")
public class PagamentoProdutoController {
    private final PagamentoProdutoService pagamentoProdutoService;
    private final Validator validator;

    @Autowired
    public PagamentoProdutoController(
            PagamentoProdutoService pagamentoProdutoService,
            Validator validator
    ) {
        this.pagamentoProdutoService = pagamentoProdutoService;
        this.validator = validator;
    }

    @GetMapping("/products-payments")
    @Operation(summary = "Show all products payments", description = "Returns a list of all available products payments")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoProduto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<PagamentoProduto> findAllProductPayments() { return pagamentoProdutoService.findAllProductPayment(); }

    @PostMapping("/add")
    @Operation(summary = "Add a new product payment", description = "Create a new product payment and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product Payment created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoProduto.class))),
            @ApiResponse(responseCode = "201", description = "Product Payment created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or product payment already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addProductPayment(@Valid @RequestBody PagamentoProduto productPayment, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            PagamentoProduto savedProductPayment = pagamentoProdutoService.saveProductPayment(productPayment);
            if (savedProductPayment != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedProductPayment);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados de Pagamento de Produto já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar usuário: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{productPaymentId}")
    @Operation(summary = "Delete a product payment", description = "Deletes the product payment with the specified productPaymentId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product Payment deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoProduto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteProductPaymentByProductPaymentId ( @PathVariable Long productPaymentId ) {
        try {
            pagamentoProdutoService.deleteProductPayment(productPaymentId);
            return ResponseEntity.ok("Dados de Pagamento de produto excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar dados de pagamento: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{productPaymentId}")
    @Operation(summary = "Update a product payment", description = "Updates the product payment data with the specified UID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product Payment updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoProduto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product Payment not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateProductPayment( @Valid @PathVariable Long productPaymentId,
                                         @RequestBody Map<String, Object> updates ) {
        try {
            PagamentoProduto pagamentoProduto = pagamentoProdutoService.findProductPaymentById(productPaymentId);

            // Lista de campos válidos que podem ser atualizados
            List<String> validFields = Arrays.asList("dataPagamento", "tipoPagamento", "valorTotal", "valorFrete");

            // Itera sobre as atualizações e só aplica as que são válidas
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "dataPagamento":
                        pagamentoProduto.setDataPagamento((Date) entry.getValue());
                        break;
                    case "tipoPagamento":
                        pagamentoProduto.setTipoPagamento((String) entry.getValue());
                        break;
                    case "valorTotal":
                        pagamentoProduto.setValorTotal((BigDecimal) entry.getValue());
                        break;
                    case "valorFrete":
                        pagamentoProduto.setValorFrete((BigDecimal) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            // Validação do usuário atualizado
            DataBinder binder = new DataBinder(pagamentoProduto);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            pagamentoProdutoService.saveProductPayment(pagamentoProduto);
            return ResponseEntity.ok("O(s) dado(s) de pagamento do produto com id " + productPaymentId + " foi atualizado com sucesso.");
        } catch (DataIntegrityViolationException e) {
            // Identifica qual campo violou a restrição UNIQUE
            String message = e.getRootCause().getMessage();
            if (message.contains("telefone")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Telefone já está em uso.");
            } else if (message.contains("email")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Email já está em uso.");
            } else if (message.contains("nomeUsuario")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Nome de usuário já está em uso.");
            } else if (message.contains("cpf")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: CPF já está em uso.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findById/{productPaymentId}")
    @Operation(summary = "Find product payment by productPaymentId", description = "Returns the product payment with the specified productPaymentId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product Payment found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoProduto.class))),
            @ApiResponse(responseCode = "404", description = "Product Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findProductsPaymentsById (@PathVariable Long productPaymentId) {
        try {
            PagamentoProduto pagamentoProduto = pagamentoProdutoService.findProductPaymentById(productPaymentId);
            if (pagamentoProduto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de Pagamento de Produto não encontrado.");
            }
            return ResponseEntity.ok(pagamentoProduto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByOrder/{orderId}")
    @Operation(summary = "Search product payment by orderId", description = "Returns a list of product payment with the specified orderId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products Payments found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoProduto.class))),
            @ApiResponse(responseCode = "404", description = "Product Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByOrderId ( @PathVariable Integer orderId ) {
        List<PagamentoProduto> lPagamentoProduto = pagamentoProdutoService.findProductPaymentByPedido(orderId);
        if (!lPagamentoProduto.isEmpty()) {
            return ResponseEntity.ok(lPagamentoProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de pagamento de produto não encontrado.");
        }
    }

    @GetMapping("/findByUser/{userId}")
    @Operation(summary = "Search product payment by userId", description = "Returns a list of product payment with the specified userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products Payments found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoProduto.class))),
            @ApiResponse(responseCode = "404", description = "Product Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUserId ( @PathVariable String userId ) {
        List<PagamentoProduto> lPagamentoProduto = pagamentoProdutoService.findProductPaymentByUsuario(userId);
        if (!lPagamentoProduto.isEmpty()) {
            return ResponseEntity.ok(lPagamentoProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de pagamento de produto não encontrado.");
        }
    }

    @GetMapping("/findByPaymentDate/{paymentDate}")
    @Operation(summary = "Search product payment by paymentDate", description = "Returns a list of product payment with the specified paymentDate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products Payments found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoProduto.class))),
            @ApiResponse(responseCode = "404", description = "Product Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByPaymentDate ( @PathVariable Date paymentDate ) {
        List<PagamentoProduto> lPagamentoProduto = pagamentoProdutoService.findProductPaymentByDataPagamento(paymentDate);
        if (!lPagamentoProduto.isEmpty()) {
            return ResponseEntity.ok(lPagamentoProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de pagamento de produto não encontrado.");
        }
    }

    @GetMapping("/findByPaymentMethod/{type}")
    @Operation(summary = "Search product payment by type", description = "Returns a list of product payment with the specified type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products Payments found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagamentoProduto.class))),
            @ApiResponse(responseCode = "404", description = "Product Payment not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByPaymentMethod ( @PathVariable String paymentMethod ) {
        List<PagamentoProduto> lPagamentoProduto = pagamentoProdutoService.findProductPaymentByTipoPagamento(paymentMethod);
        if (!lPagamentoProduto.isEmpty()) {
            return ResponseEntity.ok(lPagamentoProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados de pagamento de produto não encontrado.");
        }
    }

    public Map<String, String> validate( BindingResult resultado ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
