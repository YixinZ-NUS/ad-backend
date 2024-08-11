package sg.nus.iss.javaspring.adprojrct.Controllers;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.Category;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;
import sg.nus.iss.javaspring.adprojrct.Models.User;
import sg.nus.iss.javaspring.adprojrct.Services.CategoryService;
import sg.nus.iss.javaspring.adprojrct.Services.TransactionService;
import sg.nus.iss.javaspring.adprojrct.Services.UserService;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://3.227.89.83:3000")
@RestController
@RequestMapping(value = "/Admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/dashboard")
    public String dashboard() {
        return "adminDashboard";
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{type}")  //0 -> System  1 -> User
    public ResponseEntity<List<Category>> getCategoriesByType(@PathVariable int type) {
        List<Category> categories = categoryService.getCategoriesByType(type);
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<Category> createCategory(@RequestBody Category category, @PathVariable Integer userId) {
        Category newCategory = categoryService.addCategory(category, userId);
        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/update/{catId}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable Integer catId) {
        Optional<Category> optionalCategory = categoryService.getCategoryById(catId);
        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Category updatedCategory = categoryService.updateCategory(category, catId);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }

        // 检查是否有相关联的 transactions
        List<Transaction> transactions = transactionService.getTransactionsByCategoryId(id).get();
        if (!transactions.isEmpty()) {
            return new ResponseEntity<>("Cannot delete category: There are transactions associated with this category.", HttpStatus.CONFLICT);
        }

        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Cannot delete category due to data integrity violation", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> optionalCategory = categoryService.getCategoryById(id);
        if (optionalCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Category category = optionalCategory.get();
            return ResponseEntity.ok(category);
        }
    }

    @GetMapping("/transaction/{catId}")
    public ResponseEntity<List<Transaction>> getTransactionBycatId(@PathVariable Integer catId) {
        Optional<List<Transaction>> Transactions = transactionService.getTransactionsByCategoryId(catId);
        if (Transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            List<Transaction> transactions = Transactions.get();
            return ResponseEntity.ok(transactions);
        }
    }

    @GetMapping("/transaction_user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionBycuserId(@PathVariable Integer userId) {
        Optional<List<Transaction>> Transactions = transactionService.getTransactionsByUserId(userId);
        if (Transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            List<Transaction> transactions = Transactions.get();
            return ResponseEntity.ok(transactions);
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transaction_detail/{transcationId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Integer transcationId) {
        Optional<Transaction> transcation = transactionService.getTransactionById(transcationId);
        if (transcation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return ResponseEntity.ok(transcation.get());
        }
    }

    @DeleteMapping("deleteTranscation/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("editTransaction/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Integer transactionId, @RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(transaction, transactionId);
        return ResponseEntity.ok(updatedTransaction);
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        Optional<User> inuser = userService.findUserById(userId);
        if (inuser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(inuser.get());
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Integer userId) {
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User updatedUser = userService.updateUser(user, userId);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/deleteuser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        Optional<User> optionalUser = userService.findUserById(userId);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        if (user.getCategories() != null && !user.getCategories().isEmpty()) {
            return new ResponseEntity<>("Cannot delete user: Categories associated with user", HttpStatus.CONFLICT);
        }

        if (user.getTransactions() != null && !user.getTransactions().isEmpty()) {
            return new ResponseEntity<>("Cannot delete user: Transactions associated with user", HttpStatus.CONFLICT);
        }

        userService.deleteUserById(userId);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }


    @GetMapping("/average-amount-per-category")
    public List<Object[]> getAverageAmountPerCategory() {
        return transactionService.getAverageAmountPerCategory();
    }

    @GetMapping("/top-categories")
    public List<Object[]> getTopCategories() {
        return categoryService.getTopCategoriesWithMostTransactions();
    }
}

