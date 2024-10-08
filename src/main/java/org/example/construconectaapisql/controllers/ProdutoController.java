package org.example.construconectaapisql.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.Produto;
import org.example.construconectaapisql.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.Validator;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProdutoController {

    private ProdutoService produtoService;
    private final Validator validator;

    @Autowired
    public ProdutoController(Validator validator, ProdutoService produtoService) {
        this.produtoService = produtoService;
        this.validator = validator;
    }

    @GetMapping("/products")
    @Operation(summary = "Show all products", description = "Returns a list of all available products")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<Produto> findAllProducts() { return produtoService.findAllProducts(); }

    @PostMapping("/add")
    @Operation(summary = "Add a new product", description = "Creates a new product and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or product already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addProduct(@RequestBody Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação: ");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            Produto savedProduct = produtoService.saveProducts(produto);
            if (savedProduct != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Produto já existe.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar produto: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/drop/{produtoId}")
    @Operation(summary = "Delete a product", description = "Deletes the product with the specified productId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> dropProductByProdutoId ( @PathVariable Long produtoId ) {
        produtoService.deleteProduct(produtoId);
        return ResponseEntity.ok("Produto excluído com sucesso");
    }

    @PatchMapping("/update/{produtoId}")
    @Operation(summary = "Update a product", description = "Updates the product data with the specified produtoId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateProduct ( @Valid @PathVariable Long produtoId,
                                             @RequestBody Map<Long, Object> updates ) {
        try {
            Produto produto = produtoService.findProductsById(produtoId);
            if (updates.containsKey("nomeProduto")) { produto.setNomeProduto((String) updates.get("nomeProduto")); }
            if (updates.containsKey("estoque")) { produto.setEstoque((Integer) updates.get("estoque")); }
            if (updates.containsKey("descricao")) { produto.setDescricao((String) updates.get("descricao")); }
            if (updates.containsKey("preco")) { produto.setPreco((BigDecimal) updates.get("preco")); }
            if (updates.containsKey("condicao")) { produto.setCondicao((Boolean) updates.get("condicao")); }
            if (updates.containsKey("desconto")) { produto.setDesconto((BigDecimal) updates.get("desconto")); }
            if (updates.containsKey("imagem")) { produto.setImagem((String) updates.get("imagem")); }
            DataBinder binder = new DataBinder(produto);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            produtoService.saveProducts(produto);
            return ResponseEntity.ok("O produto com id " + produtoId + " foi atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findById/{produtoId}")
    @Operation(summary = "Find product by produtoId", description = "Returns the product with the specified produtoId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findProductById ( @PathVariable Long produtoId ) {
        return ResponseEntity.ok(produtoService.findByProdutoId(produtoId));
    }

    @GetMapping("/findByNomeProduto/{nomeProduto}")
    @Operation(summary = "Search products by name", description = "Returns a list of products with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByNomeProduto( @PathVariable String nomeProduto ) {
        List<Produto> lProduto = produtoService.findByNomeProduto(nomeProduto);
        if(!lProduto.isEmpty()) {
            return ResponseEntity.ok(lProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }

    @GetMapping("/findByCondicao/{condicao}")
    @Operation(summary = "Search products by condicao", description = "Returns a list of products with the specified condicao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByCondicao( @PathVariable Boolean condicao ) {
        List<Produto> lProduto = produtoService.findByCondicao(condicao);
        if(!lProduto.isEmpty()) {
            return ResponseEntity.ok(lProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }


    @GetMapping("/findByUserId/{usuarioId}")
    @Operation(summary = "Search products by usuarioId", description = "Returns a list of products with the specified usuarioId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUsuarioId( @PathVariable String usuarioId ) {
        List<Produto> lProduto = produtoService.findByUserId(usuarioId);
        if(!lProduto.isEmpty()) {
            return ResponseEntity.ok(lProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
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
