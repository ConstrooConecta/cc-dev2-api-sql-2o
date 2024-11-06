package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.ItemPedido;
import org.example.construconectaapisql.service.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderItem")
public class ItemPedidoController {
    private final ItemPedidoService itemPedidoService;
    private final Validator validator;

    @Autowired
    public ItemPedidoController(
            Validator validator,
            ItemPedidoService itemPedidoService
    ) {
        this.validator = validator;
        this.itemPedidoService = itemPedidoService;
    }

    @GetMapping("/orderItems")
    @Operation(summary = "Show all orderItems", description = "Returns a list of all available orderItems")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemPedido.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<ItemPedido> findAllOrderItems() {
        return itemPedidoService.findAllOrderItems();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new order item", description = "Create a new order item and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order Item created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemPedido.class))),
            @ApiResponse(responseCode = "201", description = "Order Item created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or order item already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addOrderItem(@Valid @RequestBody ItemPedido orderItem,
                                          BindingResult result
    ) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            ItemPedido savedItemPedido = itemPedidoService.saveOrderItems(orderItem);
            if (savedItemPedido != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedItemPedido);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item Pedido já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar usuário: \n" + e.getMessage());
        }
    }

    @GetMapping("/findOrderItem/{orderId}")
    @Operation(summary = "Find order item by orderId", description = "Returns the order item with the specified orderId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order Item found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemPedido.class))),
            @ApiResponse(responseCode = "404", description = "Order Item not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findOrderItemById(@PathVariable Long orderId) {
        try {
            ItemPedido orderItem = itemPedidoService.findOrderItemsById(orderId);
            if (orderItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item Pedido não encontrado.");
            }
            return ResponseEntity.ok(orderItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{orderItemId}")
    @Operation(summary = "Delete a order item", description = "Deletes the order item with the specified orderItemId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order Item deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemPedido.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Order Item not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteOrderItemById(@PathVariable Long orderItemId) {
        try {
            itemPedidoService.deleteOrderItems(orderItemId);
            return ResponseEntity.ok("Item Pedido excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar item pedido: \n" + e.getMessage());
        }
    }

    @GetMapping("/findByProduct/{productId}")
    @Operation(summary = "Search order item by productId", description = "Returns a list of order item with the specified productId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order Items found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemPedido.class))),
            @ApiResponse(responseCode = "404", description = "Order Item not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByProduct(@PathVariable Integer productId) {
        List<ItemPedido> lOrderItem = itemPedidoService.findByProduct(productId);
        if (!lOrderItem.isEmpty()) {
            return ResponseEntity.ok(lOrderItem);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item Pedido não encontrado.");
        }
    }

    @GetMapping("/findByOrder/{orderId}")
    @Operation(summary = "Search order item by orderId", description = "Returns a list of order item with the specified orderId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order Items found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemPedido.class))),
            @ApiResponse(responseCode = "404", description = "Order Item not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByOrder(@PathVariable Integer orderId) {
        List<ItemPedido> lOrderItem = itemPedidoService.findByOrder(orderId);
        if (!lOrderItem.isEmpty()) {
            return ResponseEntity.ok(lOrderItem);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item Pedido não encontrado.");
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
