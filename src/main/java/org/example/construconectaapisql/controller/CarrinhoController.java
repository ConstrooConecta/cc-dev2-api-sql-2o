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
    public List<Carrinho> findAllShoppingCarts() { return carrinhoService.findAllShoppingCarts(); }

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

        try {
            // Salva o carrinho
            Carrinho savedCarrinho = carrinhoService.saveShoppingCart(carrinho);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCarrinho);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: " + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{carrinhoId}")
    @Operation(summary = "Delete a shopping cart", description = "Deletes the shopping cart with the specified carrinhoId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shopping Cart deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteShoppingCartById ( @PathVariable Integer carrinhoId ) {
        carrinhoService.deleteShoppingCart(carrinhoId);
        return ResponseEntity.ok("Carrinho de compras excluído com sucesso");
    }

    @DeleteMapping("/deleteByUsuarioId/{usuario}")
    @Operation(summary = "Delete a shopping cart by userId", description = "Deletes the shopping cart with the specified userId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shopping Cart deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<String> deleteCategoryByUsuarioId(@PathVariable String usuario) {
        try {
            carrinhoService.deleteShoppingCartByUsuarioId(usuario);
            return ResponseEntity.ok("Carrinho deletado com sucesso para o usuario: " + usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar carrinho: " + e.getMessage());
        }
    }


    @PatchMapping("/update/{carrinhoId}")
    @Operation(summary = "Update a shopping cart", description = "Updates the shopping cart data with the specified UID")
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
    public ResponseEntity<?> updateShoppingCart ( @Valid @PathVariable Integer carrinhoId,
                                                  @RequestBody Map<String, Object> updates ) {
        try {
            Carrinho carrinho = carrinhoService.findShoppingCartById(carrinhoId);
            if (updates.containsKey("produto") ) { carrinho.setProduto((Integer) updates.get("produto")); }
            if (updates.containsKey("quantidade") ) { carrinho.setQuantidade((Integer) updates.get("quantidade")); }
            if (updates.containsKey("valorTotal") ) { carrinho.setValorTotal((BigDecimal) updates.get("valorTotal")); }

            DataBinder binder = new DataBinder(carrinho);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            carrinhoService.saveShoppingCart(carrinho);
            return ResponseEntity.ok("O carrinho de compras com id " + carrinhoId + " foi atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findById/{carrinhoId}")
    @Operation(summary = "Find shopping cart by carrinhoId", description = "Returns the shopping cart with the specified carrinhoId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping Cart found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findShoppingCartById ( @PathVariable Integer carrinhoId ) {
        return ResponseEntity.ok(carrinhoService.findShoppingCartById(carrinhoId));
    }

    @GetMapping("/findByUserId/{usuario}")
    @Operation(summary = "Find shopping cart by usuarioId", description = "Returns the shopping cart with the specified usuarioId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping Cart found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUserId ( @PathVariable String usuario ) {
        List<Carrinho> lCarrinho = carrinhoService.findByUserId(usuario);
        if(!lCarrinho.isEmpty()) {
            return ResponseEntity.ok(lCarrinho);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrinho de compras não encontrado.");
        }
    }

    @GetMapping("/findByProductId/{produto}")
    @Operation(summary = "Find shopping cart by produtoId", description = "Returns the shopping cart with the specified produtoId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping Cart found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrinho.class))),
            @ApiResponse(responseCode = "404", description = "Shopping Cart not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByProductId ( @PathVariable Integer produto ) {
        List<Carrinho> lCarrinho = carrinhoService.findByProductId(produto);
        if(!lCarrinho.isEmpty()) {
            return ResponseEntity.ok(lCarrinho);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrinho de compras não encontrado.");
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
