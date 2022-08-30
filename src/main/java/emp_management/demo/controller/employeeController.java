package emp_management.demo.controller;




import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import emp_management.demo.exception.resourceNotFoundException;
import emp_management.demo.model.employee;
import emp_management.demo.repository.employeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class employeeController {

    @Autowired
    private employeeRepo EmployeeRepo;

    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    public employeeController(TemplateEngine templateEngine) {

        this.templateEngine = templateEngine;
    }


    @GetMapping("/all")
    public List<employee> getAllEmployees() {
        return EmployeeRepo.findAll();
    }

    @PostMapping("/add")
    public employee createemployee(@RequestBody employee Employee) {

        return EmployeeRepo.save(Employee);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<employee> getEmployeeById(@PathVariable Long id) {
        employee Employee = EmployeeRepo.findById(id)
                .orElseThrow(() -> new resourceNotFoundException("Employee not exist with id :" + id));
        return ResponseEntity.ok(Employee);
    }


    @PutMapping("/employees/{id}")
    public ResponseEntity<employee> updateEmployee(@PathVariable Long id, @RequestBody employee employeeDetails) {
        employee Employee = EmployeeRepo.findById(id)
                .orElseThrow(() -> new resourceNotFoundException("Employee not exist with id :" + id));

        Employee.setFirstname(employeeDetails.getFirstname());
        Employee.setLastname(employeeDetails.getLastname());
        Employee.setEmail(employeeDetails.getEmail());

        employee updatedEmployee = EmployeeRepo.save(Employee);
        return ResponseEntity.ok(updatedEmployee);
    }


    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
        employee Employee = EmployeeRepo.findById(id)
                .orElseThrow(() -> new resourceNotFoundException("Employee not exist with id :" + id));

        EmployeeRepo.delete(Employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees/pdf/{id}")
    public ResponseEntity<byte[]> getPDF(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {

        /* Do Business Logic*/

        employee Employee = EmployeeRepo.findById(id)
                .orElseThrow(() -> new resourceNotFoundException("Employee not exist with id :" + id));

        /* Create HTML using Thymeleaf template Engine */

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("employe", Employee);
        String employeeHTML = templateEngine.process("employee", context);

        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        /*Setup converter properties. */
        ConverterProperties converterProperties = new ConverterProperties();
        //converterProperties.setCharset("UTF-8");
       // converterProperties.setBaseUri("http://localhost:8080");

        /* Call convert method */
        HtmlConverter.convertToPdf(employeeHTML, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

//        try {
//            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//            templateResolver.setSuffix(".html");
//            templateResolver.setTemplateMode("HTML");
//            TemplateEngine templateEngine = new TemplateEngine();
//            templateEngine.setTemplateResolver(templateResolver);
//            Context context = new Context();
//            context.setVariable("employe", Employee);
//            String html = templateEngine.process("employee", context);
//            String fileName = "employee.pdf";
//            response.addHeader("Content-disposition", "attachment;filename=" + fileName);
//            response.setContentType("application/pdf");
//            OutputStream outputStream = response.getOutputStream();
//            ITextRenderer renderer = new ITextRenderer();
//            renderer.setDocumentFromString(html);
//            renderer.layout();
//            renderer.createPDF(outputStream);
//            outputStream.flush();
//            outputStream.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


    }

//    @GetMapping("/employees/all/pdf")
//    public ResponseEntity<?> getallPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        /* Do Business Logic*/
//
//        List<employee> employeeslList =EmployeeRepo.findAll();
//
//        /* Create HTML using Thymeleaf template Engine */
//
//        WebContext context = new WebContext(request, response, servletContext);
//        context.setVariable("employees", employeeslList);
//        String employeesHTML = templateEngine.process("employees", context);
//
//        /* Setup Source and target I/O streams */
//
//        ByteArrayOutputStream target = new ByteArrayOutputStream();
//
//        /*Setup converter properties. */
//        ConverterProperties converterProperties = new ConverterProperties();
//        converterProperties.setBaseUri("http://localhost:8080");
//
//        /* Call convert method */
//        HtmlConverter.convertToPdf(employeesHTML, target, converterProperties);
//
//        /* extract output as bytes */
//        byte[] bytes = target.toByteArray();
//
//
//        /* Send the response as downloadable PDF */
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(bytes);
//
//    }
}
