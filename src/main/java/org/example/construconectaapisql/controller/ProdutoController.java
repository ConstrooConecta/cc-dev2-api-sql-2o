package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.Categoria;
import org.example.construconectaapisql.model.Produto;
import org.example.construconectaapisql.repository.CategoriaRepository;
import org.example.construconectaapisql.repository.ProdutoRepository;
import org.example.construconectaapisql.service.ProdutoService;
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
@RequestMapping("/product")
public class ProdutoController {
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    private final ProdutoService produtoService;
    private final Validator validator;

    @Autowired
    public ProdutoController(
            Validator validator,
            ProdutoService produtoService,
            ProdutoRepository produtoRepository,
            CategoriaRepository categoriaRepository
    ) {
        this.produtoService = produtoService;
        this.validator = validator;
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
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
    @Operation(summary = "Add a new product", description = "Create a new product and saves it to the database")
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
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n| ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        // Gera um número aleatório entre 1 e 4 se o tópico for nulo
        if (produto.getTopico() == null) {
            Random random = new Random();
            produto.setTopico(random.nextInt(4) + 1); // Gera um número entre 1 e 4
        }

        // Verifica se o desconto e o frete foram enviados. Se não, atribui 0.
        if (produto.getDesconto() == null) {
            produto.setDesconto(BigDecimal.ZERO);
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

    @DeleteMapping("/delete/{produtoId}")
    @Operation(summary = "Delete a product", description = "Deletes the product with the specified productId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product deleted successfully",
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
    public ResponseEntity<?> deleteProductByProdutoId ( @PathVariable Long produtoId ) {
        try {
            produtoService.deleteProduct(produtoId);
            return ResponseEntity.ok("Produto excluído com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar produto: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{produtoId}")
    @Operation(summary = "Update a product", description = "Updates the product data with the specified produtoId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateProduct(@Valid @PathVariable Long produtoId, @RequestBody Map<String, Object> updates) {
        try {
            Produto produto = produtoService.findProductsById(produtoId);

            // Lista de campos válidos que podem ser atualizados
            List<String> validFields = Arrays.asList("nomeProduto", "estoque", "descricao", "preco", "condicao", "desconto", "imagem", "topico");

            // Itera sobre as atualizações e só aplica as que são válidas
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                if (!validFields.contains(field)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo '" + field + "' não é válido para atualização.");
                }

                switch (field) {
                    case "nomeProduto":
                        produto.setNomeProduto((String) entry.getValue());
                        break;
                    case "estoque":
                        produto.setEstoque((Integer) entry.getValue());
                        break;
                    case "descricao":
                        produto.setDescricao((String) entry.getValue());
                        break;
                    case "preco":
                        produto.setPreco((BigDecimal) entry.getValue());
                        break;
                    case "condicao":
                        produto.setCondicao((Boolean) entry.getValue());
                        break;
                    case "desconto":
                        produto.setDesconto((BigDecimal) entry.getValue());
                        break;
                    case "imagem":
                        produto.setImagem((String) entry.getValue());
                        break;
                    case "topico":
                        produto.setTopico((Integer) entry.getValue());
                        break;
                    default:
                        // Este default nunca será alcançado devido à verificação da lista `validFields`
                        break;
                }
            }

            // Validação do produto atualizado
            DataBinder binder = new DataBinder(produto);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            produtoService.saveProducts(produto);
            return ResponseEntity.ok("O produto com id " + produtoId + " foi atualizado com sucesso.");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
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
        try {
            Produto produto = produtoService.findProductsById(produtoId);
            if (produto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
            }
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByProduct/productName/{productName}")
    @Operation(summary = "Search products by name", description = "Returns a list of products with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByNomeProduto( @PathVariable String productName ) {
        List<Produto> lProduto = produtoService.findByNomeProduto(productName);
        if(!lProduto.isEmpty()) {
            return ResponseEntity.ok(lProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }

    @GetMapping("/findByCondition/{condicao}")
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

    @GetMapping("/findByUser/userId/{userId}")
    @Operation(summary = "Search products by userId", description = "Returns a list of products with the specified userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByUsuarioId( @PathVariable String userId ) {
        List<Produto> lProduto = produtoService.findByUserId(userId);
        if(!lProduto.isEmpty()) {
            return ResponseEntity.ok(lProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }

    @GetMapping("/findByTopic/{topico}")
    @Operation(summary = "Search products by topico", description = "Returns a list of products with the specified topico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByTopic( @PathVariable Integer topico ) {
        List<Produto> lProduto = produtoService.findByTopico(topico);
        if(!lProduto.isEmpty()) {
            return ResponseEntity.ok(lProduto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }

    @GetMapping("/findByCategory/categoryName/{nomeCategoria}")
    @Operation(summary = "Search products by category name", description = "Returns a list of products within the specified category name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<List<Produto>> searchByNomeCategoria(@PathVariable String nomeCategoria) {
        List<Categoria> categorias = produtoService.findByCategoryName(nomeCategoria);

        if (!categorias.isEmpty()) {
            List<Produto> produtos = produtoService.findByCategorias(categorias);
            return ResponseEntity.ok(produtos); // Retorna lista de produtos encontrados
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna 404 se a categoria não for encontrada ou estiver vazia
        }
    }

    @GetMapping("/findByCategory/categoryId/{categoriaId}")
    @Operation(summary = "Search products by category id", description = "Returns a list of products within the specified category id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<List<Produto>> searchByCategoryId(@PathVariable Long categoriaId) {
        // Buscar a categoria pelo ID
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);

        if (categoria.isPresent()) {
            // Buscar produtos associados à categoria encontrada
            List<Produto> produtos = produtoRepository.findByCategorias(Set.of(categoria.get()));
            return ResponseEntity.ok(produtos);
        } else {
            // Se a categoria não for encontrada, retornar 404
            return ResponseEntity.notFound().build();
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
