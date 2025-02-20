package emptracker.controller;

import emptracker.dto.EmpDto;
import emptracker.service.EmpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/employees/*")
public class EmpController extends HttpServlet {
    private EmpService employeeService;

    @Override
    public void init() throws ServletException {
        employeeService = new EmpService();
    }

    // 사원 조회 (전체 또는 특정 ID)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<EmpDto> employees = employeeService.getAllEmployees();
            out.print(convertListToJson(employees));
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                EmpDto employee = employeeService.getEmployeeById(id);
                if (employee != null) {
                    out.print(convertEmployeeToJson(employee));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"message\": \"Employee not found\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"message\": \"Invalid employee ID\"}");
            }
        }
        out.flush();
    }

    // 사원 추가
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        EmpDto employee = parseEmployeeFromJson(readRequestBody(req));
        if (employee == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Invalid JSON format\"}");
            return;
        }

        boolean isAdded = employeeService.addEmployee(employee);
        resp.setStatus(isAdded ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(isAdded ? "{\"message\": \"Employee added successfully\"}" : "{\"message\": \"Failed to add employee\"}");
    }

    // 사원 수정
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Employee ID required\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            EmpDto employee = parseEmployeeFromJson(readRequestBody(req));
            if (employee == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\": \"Invalid JSON format\"}");
                return;
            }
            employee.setId(id);

            boolean isUpdated = employeeService.updateEmployee(employee);
            resp.setStatus(isUpdated ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(isUpdated ? "{\"message\": \"Employee updated successfully\"}" : "{\"message\": \"Failed to update employee\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Invalid employee ID\"}");
        }
    }

    // 사원 삭제
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Employee ID required\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean isDeleted = employeeService.deleteEmployee(id);
            resp.setStatus(isDeleted ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(isDeleted ? "{\"message\": \"Employee deleted successfully\"}" : "{\"message\": \"Employee not found\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Invalid employee ID\"}");
        }
    }

    // JSON 변환 메서드 (단일 객체)
    private String convertEmployeeToJson(EmpDto emp) {
        return "{ \"id\": " + emp.getId() + 
               ", \"name\": \"" + emp.getName() + 
               "\", \"phone\": \"" + emp.getPhone() + 
               "\", \"position\": \"" + emp.getPosition() + 
               "\", \"hireDate\": \"" + emp.getHireDate() + "\" }";
    }

    // JSON 변환 메서드 (리스트 변환)
    private String convertListToJson(List<EmpDto> employees) {
        StringBuilder json = new StringBuilder("[");
        for (EmpDto emp : employees) {
            json.append(convertEmployeeToJson(emp)).append(",");
        }
        if (!employees.isEmpty()) json.deleteCharAt(json.length() - 1);
        json.append("]");
        return json.toString();
    }

    // JSON 문자열을 EmpDto 객체로 변환
    private EmpDto parseEmployeeFromJson(String json) {
        if (json == null || json.isEmpty()) return null;
        
        EmpDto emp = new EmpDto();
        json = json.replace("{", "").replace("}", "").replace("\"", "").trim();

        String[] fields = json.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                switch (key) {
                    case "id":
                        emp.setId(Integer.parseInt(value));
                        break;
                    case "name":
                        emp.setName(value);
                        break;
                    case "phone":
                        emp.setPhone(value);
                        break;
                    case "position":
                        emp.setPosition(value);
                        break;
                    case "hireDate":
                        emp.setHireDate(value);
                        break;
                }
            }
        }
        return emp;
    }

    // 요청 바디 읽기
    private String readRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
