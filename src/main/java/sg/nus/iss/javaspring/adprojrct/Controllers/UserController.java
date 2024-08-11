package sg.nus.iss.javaspring.adprojrct.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;
import sg.nus.iss.javaspring.adprojrct.Services.CategoryService;
import sg.nus.iss.javaspring.adprojrct.Services.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("http://3.227.89.83")
@RestController
@RequestMapping(value = "/User")
public class UserController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/dashboard")
    public String dashboard(Model model) {
        return "userDashboard";
    }

    @GetMapping(value = "/budget/{userId}")
    public ResponseEntity<List<Category>> getBudgetsById(@PathVariable Integer userId) {
        return ResponseEntity.ok(categoryService.getCategoriesByUserId(userId));
    }

    @PostMapping("/budget/add/{userId}")
    public ResponseEntity<?> createCategory(@RequestBody Category category, @PathVariable Integer userId) {
        try {
            category.setType(1);
            Category newCategory = categoryService.addCategory(category, userId);
            return ResponseEntity.ok(newCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/budget/update/{catId}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable Integer catId) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, catId);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/budget/delete/{catId}")
    public void deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategory(catId);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> optionalCategory = categoryService.getCategoryById(id);
        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(optionalCategory.get());
        }
    }



    @GetMapping("/transaction/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsById(@PathVariable Integer userId) {
        return ResponseEntity.ok(transactionService.findTransactionsByOrderDateAtDesc(userId));
    }

    @PostMapping("/transaction/add/{userId}")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction, @PathVariable Integer userId) {
        try {
            System.out.println(userId);
            Transaction newTransaction = transactionService.addTransaction(transaction, userId);
            return ResponseEntity.ok(newTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/transaction/update/{transId}")
    public ResponseEntity<?> updateTransaction(@RequestBody Transaction transaction, @PathVariable Integer transId) {
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(transaction, transId);
            return ResponseEntity.ok(updatedTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/transaction/delete/{transId}")
    public void deleteTransaction(@PathVariable Integer transId) {
        transactionService.deleteTransaction(transId);
    }


    // DASHBOARD API   ( つ•̀ω•́)つ



    @GetMapping("/total-spending-today/{userId}")
    public ResponseEntity<Double> getTotalSpendingToday(@PathVariable int userId) {
        double totalSpending = transactionService.getTotalSpendingToday(userId);
        return ResponseEntity.ok(totalSpending);
    }

    @GetMapping("/total-spending-last-week/{userId}")
    public ResponseEntity<Double> getTotalSpendingLastWeek(@PathVariable int userId) {
        double totalSpending = transactionService.getTotalSpendingLastWeek(userId);
        return ResponseEntity.ok(totalSpending);
    }

    @GetMapping("/total-spending-this-month/{userId}")
    public ResponseEntity<Double> getTotalSpendingLastMonth(@PathVariable int userId) {
        double totalSpending = transactionService.getTotalSpendingThisMonth(userId);
        return ResponseEntity.ok(totalSpending);
    }

    @GetMapping("/total-spending-previous-month/{userId}")
    public ResponseEntity<Double> getTotalSpendingPreviousMonth(@PathVariable int userId) {
        double totalSpending = transactionService.getTotalSpendingPreviousMonth(userId);
        return ResponseEntity.ok(totalSpending);
    }

    @GetMapping("/total-spending-last-year/{userId}")
    public ResponseEntity<Double> getTotalSpendingLastYear(@PathVariable int userId) {
        double totalSpending = transactionService.getTotalSpendingLastYear(userId);
        return ResponseEntity.ok(totalSpending);
    }

    //当前用户总预算  ʕ•̀ω•́ʔ✧
    @GetMapping("/categories/total-budget/{userId}")
    public ResponseEntity<Double> getTotalBudgetByUserId(@PathVariable int userId) {
        double totalBudget = categoryService.getTotalBudgetByUserId(userId);
        return ResponseEntity.ok(totalBudget);
    }

    @GetMapping("/total-spending-current-mouth/{userId}")
    public ResponseEntity<Double> getTotalSpendingCurrentMouth(@PathVariable int userId) {
        double totalSpending = transactionService.getTotalSpendingCurrentMonth(userId);
        return ResponseEntity.ok(totalSpending);
    }

    @GetMapping("/total-spending-current-mouth-by-category/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getTotalSpendingCurrentByCategory(@PathVariable int userId) {
        List<Map<String, Object>> totalSpending = transactionService.getTotalSpendingByCategoryForCurrentMonth(userId);
        //System.out.println(totalSpending);
        return ResponseEntity.ok(totalSpending);
    }

//    @GetMapping("/category-spending/{userId}")
//    public ResponseEntity<List<Object[]>> getTotalSpendingByCategoryForCurrentMonth(@PathVariable int userId) {
//        List<Object[]> categorySpendings = transactionService.getTotalSpendingByCategoryForCurrentMonth(userId);
//        return ResponseEntity.ok(categorySpendings);
//    }

    @GetMapping("/total-spending-current/{userId}")
    public ResponseEntity<Double> getTotalSpendingCurrent(@PathVariable int userId) {
        double totalSpending = transactionService.getTotalSpendingCurrentMonth(userId);
        return ResponseEntity.ok(totalSpending);
    }
}