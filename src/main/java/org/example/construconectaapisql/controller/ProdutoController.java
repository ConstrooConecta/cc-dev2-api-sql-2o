package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.Categoria;
import org.example.construconectaapisql.model.Produto;
import org.example.construconectaapisql.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Random;

@RestController
@RequestMapping("/product")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final Validator validator;

    @Autowired
    public ProdutoController(Validator validator, ProdutoService produtoService) {
        this.produtoService = produtoService;
        this.validator = validator;
    }

    @GetMapping("/products")
    @Operation(summary = "Show all products", description = "Returns a list of all available products")
    public List<Produto> findAllProducts() {
        return produtoService.findAllProducts();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new product", description = "Create a new product and saves it to the database")
    public ResponseEntity<?> addProduct(@RequestBody Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        if (produto.getTopico() == null) {
            produto.setTopico(new Random().nextInt(4) + 1); // Gera número entre 1 e 4
        }

        if (produto.getDesconto() == null) {
            produto.setDesconto(BigDecimal.ZERO);
        }

        try {
            Produto savedProduct = produtoService.saveProducts(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar produto: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{produtoId}")
    @Operation(summary = "Delete a product", description = "Deletes the product with the specified productId")
    public ResponseEntity<?> deleteProductByProdutoId(@PathVariable Long produtoId) {
        try {
            produtoService.deleteProduct(produtoId);
            return ResponseEntity.ok("Produto excluído com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/update/{produtoId}")
    @Operation(summary = "Update a product", description = "Updates the product data with the specified produtoId")
    public ResponseEntity<?> updateProduct(@Valid @PathVariable Long produtoId, @RequestBody Map<String, Object> updates) {
        try {
            Produto produto = produtoService.findProductsById(produtoId);
            if (updates.containsKey("nomeProduto")) produto.setNomeProduto((String) updates.get("nomeProduto"));
            if (updates.containsKey("estoque")) produto.setEstoque((Integer) updates.get("estoque"));
            if (updates.containsKey("descricao")) produto.setDescricao((String) updates.get("descricao"));
            if (updates.containsKey("preco")) produto.setPreco(new BigDecimal((String) updates.get("preco")));
            if (updates.containsKey("condicao")) produto.setCondicao((Boolean) updates.get("condicao"));
            if (updates.containsKey("desconto")) produto.setDesconto(new BigDecimal((String) updates.get("desconto")));
            if (updates.containsKey("imagem")) produto.setImagem((String) updates.get("imagem"));
            if (updates.containsKey("topico")) produto.setTopico((Integer) updates.get("topico"));

            DataBinder binder = new DataBinder(produto);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();

            if (result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validate(result));
            }

            produtoService.saveProducts(produto);
            return ResponseEntity.ok("O produto com id " + produtoId + " foi atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findByCategoryName/{nomeCategoria}")
    @Operation(summary = "Search products by category name", description = "Returns a list of products within the specified category name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<List<Produto>> searchByNomeCategoria(@PathVariable String nomeCategoria) {
        List<Categoria> categorias = produtoService.findCategoriasByNome(nomeCategoria);

        if (!categorias.isEmpty()) {
            List<Produto> produtos = produtoService.findByCategorias(categorias);
            return ResponseEntity.ok(produtos); // Retorna lista de produtos encontrados
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna 404 se a categoria não for encontrada ou estiver vazia
        }
    }



    private Map<String, String> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
