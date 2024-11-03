package org.example.construconectaapisql.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.construconectaapisql.model.Categoria;
import org.example.construconectaapisql.service.CategoriaService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoriaController {
    private final CategoriaService categoriaService;
    private final Validator validator;

    @Autowired
    public CategoriaController(
            Validator validator,
            CategoriaService categoriaService
    ) {
        this.categoriaService = categoriaService;
        this.validator = validator;
    }

    @GetMapping("/categories")
    @Operation(summary = "Show all categories", description = "Returns a list of all available categories")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public List<Categoria> findAllCategories() { return categoriaService.findAllCategories(); }

    @PostMapping("/add")
    @Operation(summary = "Add a new category", description = "Create a new category and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "201", description = "Categories created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or category already exists",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Data integrity violation",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> addCategory(@Valid @RequestBody Categoria categoria, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n ");
            result.getAllErrors().forEach(error -> {
                sb.append(" |\n|");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        try {
            // Verificar se o nome da categoria já existe (ignorando maiúsculas/minúsculas)
            if (categoriaService.existsByNameIgnoreCase(categoria.getNome())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Categoria já existe.");
            }

            // Salvar a nova categoria
            Categoria savedCategoria = categoriaService.saveCategories(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoria);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar categoria: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    @Operation(summary = "Delete a category", description = "Deletes the categories with the specified categoryId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> deleteCategoryByCategoriaId ( @PathVariable Long categoryId ) {
        try {
            categoriaService.deleteCategory(categoryId);
            return ResponseEntity.ok("Categoria excluída com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar categoria: \n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{categoryId}")
    @Operation(summary = "Update a category", description = "Updates the category data with the specified categoryId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> updateCategory(@Valid @PathVariable Long categoryId,
                                            @RequestBody Map<String, Object> updates) {
        try {
            Categoria categoria = categoriaService.findCategoriesById(categoryId);
            if (categoria == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada.");
            }

            // Atualizando os campos da categoria se presentes no body
            if (updates.containsKey("nome")) {
                categoria.setNome((String) updates.get("nome"));
            }

            // Validação dos dados atualizados
            DataBinder binder = new DataBinder(categoria);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if (result.hasErrors()) {
                Map<String, String> errors = validate(result);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            // Salvando a categoria atualizada
            categoriaService.saveCategories(categoria);
            return ResponseEntity.ok("A categoria com ID " + categoryId + " foi atualizada com sucesso.");

        } catch (DataIntegrityViolationException e) {
            String message = e.getRootCause().getMessage();
            if (message.contains("nome")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Categoria com este nome já existe.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: " + e.getMessage());
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar a categoria: \n" + e.getMessage());
        }
    }

    @GetMapping("/findById/{categoryId}")
    @Operation(summary = "Find category by categoryId", description = "Returns the category with the specified categoryId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> findCategoriesById (@PathVariable Long categoryId) {
        try {
            Categoria categoria = categoriaService.findCategoriesById(categoryId);
            if (categoria == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada.");
            }
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/findByNome/{nome}")
    @Operation(summary = "Search categories by name", description = "Returns a list of categories with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> searchByNomeCompleto ( @PathVariable String nome ) {
        List<Categoria> lCategoria = categoriaService.findByNome(nome);
        if(!lCategoria.isEmpty()) {
            return ResponseEntity.ok(lCategoria);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada.");
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
