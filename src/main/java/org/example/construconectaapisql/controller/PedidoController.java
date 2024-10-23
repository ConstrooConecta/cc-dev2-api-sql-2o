package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.Pedido;
import org.example.construconectaapisql.service.PedidoService;
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
@RequestMapping("/order")
public class PedidoController {
    private final PedidoService pedidoService;
    private final Validator validator;

    @Autowired
    public PedidoController(
            PedidoService pedidoService,
            Validator validator
    ) {
        this.pedidoService = pedidoService;
        this.validator = validator;
    }

    @GetMapping("/orders")
    @Operation(summary = "Show all orders", description = "Returns a list of all available orders")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<Pedido> findAllOrders() { return pedidoService.findAllOrders(); }

    @PostMapping("/add")
    @Operation(summary = "Add a new order", description = "Create a new order and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or order already exists",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addOrder(@Valid @RequestBody Pedido order,
                                      BindingResult result
    ) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }
        try {
            Pedido savedOrder = pedidoService.saveOrders(order);
            if (savedOrder != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pedido já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar pedido: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{orderId}")
    @Operation(summary = "Delete an order", description = "Deletes the order with the specified orderId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteOrderByOrderId(@PathVariable Long orderId) {
        try {
            pedidoService.deleteOrders(orderId);
            return ResponseEntity.status(HttpStatus.OK).body("Order deleted successfully");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar pedido: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{orderId}")
    @Operation(summary = "Update a order", description = "Updates the order data with the specified orderId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateOrder( @Valid @PathVariable Long orderId,
                                         @RequestBody Map<String, Object> updates ) {
        try {
            Pedido order = pedidoService.findOrdersBydId(orderId);

            // Lista de campos válidos que podem ser atualizados
            List<String> validFields = Arrays.asList("dataPedido", "dataEntrega");

            // Itera sobre as atualizações e só aplica as que são válidas
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "dataPedido":
                        order.setDataPedido((Date) entry.getValue());
                        break;
                    case "dataEntrega":
                        order.setDataEntrega((Date) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            // Validação do usuário atualizado
            DataBinder binder = new DataBinder(order);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            pedidoService.saveOrders(order);
            return ResponseEntity.ok("O pedido com orderId " + orderId + " foi atualizado com sucesso.");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findByOrderId/{orderId}")
    @Operation(summary = "Find order by orderId", description = "Returns the order with the specified orderId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findOrdersByOrderId (@PathVariable Long orderId) {
        try {
            Pedido order = pedidoService.findOrdersBydId(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
            }
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByVoucher/{voucher}")
    @Operation(summary = "Search orders by voucher", description = "Returns a list of orders with the specified voucher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByVoucher ( @PathVariable String voucher ) {
        List<Pedido> lOrder = pedidoService.findByCupom(voucher);
        if(!lOrder.isEmpty()) {
            return ResponseEntity.ok(lOrder);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }
    }

    @GetMapping("/findByOrderDate/{orderDate}")
    @Operation(summary = "Search orders by orderDate", description = "Returns a list of orders with the specified orderDate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByOrderDate ( @PathVariable Date orderDate ) {
        List<Pedido> lOrder = pedidoService.findByDataPedido(orderDate);
        if(!lOrder.isEmpty()) {
            return ResponseEntity.ok(lOrder);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }
    }

    @GetMapping("/findByDeliveryDate/{deliveryDate}")
    @Operation(summary = "Search orders by deliveryDate", description = "Returns a list of orders with the specified deliveryDate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByDeliveryDate ( @PathVariable Date deliveryDate ) {
        List<Pedido> lOrder = pedidoService.findByDataEntrega(deliveryDate);
        if(!lOrder.isEmpty()) {
            return ResponseEntity.ok(lOrder);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
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
