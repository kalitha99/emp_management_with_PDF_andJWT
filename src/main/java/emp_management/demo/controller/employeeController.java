package emp_management.demo.controller;


import emp_management.demo.model.employee;
import emp_management.demo.repository.employeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class employeeController {

    @Autowired
    private employeeRepo EmployeeRepo;

    @GetMapping("/all")
    public List<employee> getAllEmployees() {
        return EmployeeRepo.findAll();
    }

    @PostMapping("/add")
    public employee createemployee(@RequestBody employee Employee){
        return EmployeeRepo.save(Employee);
    }
}
