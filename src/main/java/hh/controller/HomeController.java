package hh.controller;

import hh.exception.CustomsException;
import hh.model.dto.request.EmployeeRequest;
import hh.model.dto.response.EmployeeResponse;
import hh.model.entity.Employee;
import hh.service.iml.DepartmentService;
import hh.service.iml.IEmployeeServiceIml;
import hh.service.iml.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/employee")
public class HomeController {

    @Autowired
    private IEmployeeServiceIml employeeServiceIml;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RoleService roleService;
    @GetMapping
    public String getName() {
        return "/pages/ChoseTable";
    }
    @GetMapping("/search")
    public String searchEmployee(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            Model model,
            @RequestParam(value = "search", defaultValue = "") String search) {
        Page<EmployeeResponse> employeePage = employeeServiceIml.getAll(pageable, search);
        model.addAttribute("employee", employeePage);
        return "/employee/ListEmployee";
    }

    @GetMapping("/get-all")
    public String getHome(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {

        int pageSize = 5; // Số bản ghi hiển thị trên mỗi trang
        List<EmployeeResponse> employeeList = employeeServiceIml.getAll();

        int totalRecords = employeeList.size();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        List<EmployeeResponse> paginatedEmployeeList = new ArrayList<>();
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalRecords);

        for (int i = startIndex; i < endIndex; i++) {
            paginatedEmployeeList.add(employeeList.get(i));
        }

        List<Integer> pages = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        model.addAttribute("employee", paginatedEmployeeList);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);
        return "/employee/ListEmployee";
    }

    @GetMapping("/add-employee")
    public ModelAndView addEmployee(Model model) {
        model.addAttribute("department", departmentService.getAll());
        return new ModelAndView("/employee/AddEmployee", "employee", new Employee());
    }

    @PostMapping("/add-employee")
    public String saveEmployee(@Valid @ModelAttribute("employee") EmployeeRequest employeeRequest,
                               BindingResult bindingResult, Model model) throws CustomsException {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            model.addAttribute("validationErrors", errorMessages);
            return "/employee/AddEmployee";
        }

        employeeServiceIml.save(employeeRequest);
        return "redirect:/employee/get-all";
    }


    @GetMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) throws CustomsException {
        employeeServiceIml.remove(id);
        return "redirect:/employee/get-all";
    }

    @GetMapping("/update-employee/{id}")
    public ModelAndView updateUser(@PathVariable("id") Long id, Model model) throws CustomsException {
        model.addAttribute("department", departmentService.getAll());
        model.addAttribute("roles", roleService.getAll());
        return new ModelAndView("/employee/UpdateEmployee", "EmployeeUpdate", employeeServiceIml.findById(id));
    }

    @PostMapping("/update-employee")
    public String updateUser(@ModelAttribute("UserUpdate") EmployeeRequest employeeRequest) throws CustomsException {
        employeeServiceIml.update(employeeRequest, employeeRequest.getId());
        return "redirect:/employee/get-all";
    }

}
