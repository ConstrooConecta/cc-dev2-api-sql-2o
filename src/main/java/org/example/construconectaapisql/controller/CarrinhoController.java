package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.Carrinho;
import org.example.construconectaapisql.service.CarrinhoService;
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
@RequestMapping("/shopping-cart")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;
    private final Validator validator;

    @Autowired
    public CarrinhoController(CarrinhoService carrinhoService, Validator validator) {
        this.carrinhoService = carrinhoService;
        this.validator = validator;
    }

    @GetMapping("/shopping-carts")
    @Operation(summary = "Show all shopping carts", description = "Returns a list of all available shopping carts")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<Carrinho> findAllShoppingCarts() {
        return carrinhoService.findAllShoppingCarts();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new shopping cart", description = "Create a new shopping cart and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shopping Cart created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or shopping cart already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addShoppingCart(@RequestBody Carrinho carrinho, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro de validação.");
        }

        // Remover qualquer ID fornecido pelo cliente
        carrinho.setCarrinhoId(null); // Se ainda estiver no modelo

        try {
            Carrinho savedCarrinho = carrinhoService.saveShoppingCart(carrinho);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCarrinho);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro ao adicionar carrinho: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{shoppingCartId}")
    @Operation(summary = "Delete a shopping cart", description = "Deletes the shopping cart with the specified shoppingCartId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shopping Cart deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteShoppingCartById(@PathVariable Long shoppingCartId) {
        try {
            carrinhoService.deleteShoppingCart(shoppingCartId);
            return ResponseEntity.ok("Carrinho de compras excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar carrinho: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/deleteByUsuarioId/{userId}")
    @Operation(summary = "Delete a shopping cart by userId", description = "Deletes the shopping cart with the specified userId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shopping Cart deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<String> deleteShoppingCartByUsuarioId(@PathVariable String userId) {
        try {
            carrinhoService.deleteShoppingCartByUserId(userId);
            return ResponseEntity.ok("Carrinho deletado com sucesso para o usuário: " + userId);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar carrinho: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/deleteByIdentifier/{identifier}")
    @Operation(summary = "Delete a shopping cart by identifier", description = "Deletes the shopping cart with the specified identifier")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shopping Cart deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<String> deleteShoppingCartByIdentifier(@PathVariable Integer identifier) {
        try {
            carrinhoService.deleteShoppingCartByIdentifier(identifier);
            return ResponseEntity.ok("Carrinho deletado com sucesso para o identifier: " + identifier);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar carrinho: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{shoppingCartId}")
    @Operation(summary = "Update a shopping cart", description = "Updates the shopping cart data with the specified shoppingCartId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shopping Cart updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateShoppingCart(@Valid @PathVariable Long shoppingCartId,
                                                @RequestBody Map<String, Object> updates) {
        try {
            Carrinho carrinho = carrinhoService.findShoppingCartById(shoppingCartId);
            if (updates.containsKey("produto")) {
                carrinho.setProduto((Integer) updates.get("produto"));
            }
            if (updates.containsKey("quantidade")) {
                carrinho.setQuantidade((Integer) updates.get("quantidade"));
            }
            if (updates.containsKey("valorTotal")) {
                carrinho.setValorTotal((BigDecimal) updates.get("valorTotal"));
            }

            DataBinder binder = new DataBinder(carrinho);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            carrinhoService.saveShoppingCart(carrinho);
            return ResponseEntity.ok("O carrinho de compras com id " + shoppingCartId + " foi atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findById/{shoppingCartId}")
    @Operation(summary = "Find shopping cart by shoppingCartId", description = "Returns the shopping cart with the specified shoppingCartId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping Cart found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findShoppingCartById(@PathVariable Long shoppingCartId) {
        return ResponseEntity.ok(carrinhoService.findShoppingCartById(shoppingCartId));
    }

    @GetMapping("/findByUserId/{userId}")
    @Operation(summary = "Find shopping cart by userId", description = "Returns the shopping cart with the specified userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping Cart found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUserId(@PathVariable String userId) {
        List<Carrinho> lCarrinho = carrinhoService.findByUserId(userId);
        if (!lCarrinho.isEmpty()) {
            return ResponseEntity.ok(lCarrinho);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrinho de compras não encontrado.");
        }
    }

    @GetMapping("/findByIdentifier/{identifier}")
    @Operation(summary = "Find shopping cart by identifier", description = "Returns the shopping cart with the specified identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping Cart found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByIdentifier(@PathVariable Integer identifier) {
        List<Carrinho> lCarrinho = carrinhoService.findByIdentifier(identifier);
        if (!lCarrinho.isEmpty()) {
            return ResponseEntity.ok(lCarrinho);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrinho de compras não encontrado.");
        }
    }

    @GetMapping("/findByProductId/{productId}")
    @Operation(summary = "Find shopping cart by productId", description = "Returns the shopping cart with the specified productId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping Cart found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByProductId(@PathVariable Integer productId) {
        List<Carrinho> lCarrinho = carrinhoService.findByProductId(productId);
        if (!lCarrinho.isEmpty()) {
            return ResponseEntity.ok(lCarrinho);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrinho de compras não encontrado.");
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
