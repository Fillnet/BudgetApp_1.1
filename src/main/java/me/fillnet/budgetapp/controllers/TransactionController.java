package me.fillnet.budgetapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.fillnet.budgetapp.model.Category;
import me.fillnet.budgetapp.model.Transaction;
import me.fillnet.budgetapp.services.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;


//@Tag(value = (name = "Transaction", description = "Crud"))
@RestController
@RequestMapping("/transaction")
@Tag(name = ("Transaction"), description = "Crud")
public class TransactionController {
    private final BudgetService budgetService;

    public TransactionController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Long> addTransaction(@RequestBody Transaction transaction) {
        long id = budgetService.addTransaction(transaction);
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTransactionById(@PathVariable long id) {
        Transaction transaction = budgetService.getTransaction(id);
        if (transaction == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction);
    }

    @GetMapping

    @Operation(
            summary = "Поиск транзакции по месяцу и/или категории",
            description = "Можно искать по одному параметру, обоим или вобще без параметров"
    )
    @ApiResponses(value= {
        @ApiResponse(
                responseCode = "200",
                description = "транзакции были найдены",
                content = {
                        @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = Transaction.class))
                        )
                }
        )
    }
    )

    public ResponseEntity<Transaction> getAllTransactions(@RequestParam(required = false) Month month,
                                                          @RequestParam(required = false) Category category) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> editTransaction(@PathVariable long id, @RequestBody Transaction transaction) {
        transaction = budgetService.editTransaction(id, transaction);
        if (transaction == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable long id) {
        budgetService.deleteTransaction(id);
        if (budgetService.deleteTransaction(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransaction() {
        budgetService.deleteAllTransaction();
        return ResponseEntity.ok().build();
    }
}
