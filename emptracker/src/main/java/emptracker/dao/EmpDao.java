package emptracker.dao;

import emptracker.dto.EmpDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpDao {
    // 데이터베이스 연결 관련 변수
    protected Connection conn;
    
    private String dburl = "jdbc:mysql://localhost:3306/AssetKeeper"; // 연동할 DB 서버 URL
    private String dbuser = "root"; // DB 사용자 계정명
    private String dbpwd = "1234"; // DB 비밀번호

    // 생성자: 데이터베이스 연결 초기화
    public EmpDao() {
        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 데이터베이스 연결
            conn = DriverManager.getConnection(dburl, dbuser, dbpwd);
            System.out.println("[DB 연결 성공]");
        } catch (Exception e) {
            System.out.println("[DB 연동 실패]: " + e.getMessage());
        }
    }

    // 싱글톤 패턴
    private static EmpDao instance = new EmpDao();
    public static EmpDao getInstance() {
        return instance;
    }

    // 사원 추가 (Create)
    public boolean addEmployee(EmpDto employee) {
        String sql = "INSERT INTO employees (name, phone, position, hire_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPhone());
            pstmt.setString(3, employee.getPosition());
            pstmt.setDate(4, Date.valueOf(employee.getHireDate()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 모든 사원 조회 (Read)
    public List<EmpDto> getAllEmployees() {
        List<EmpDto> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                employees.add(new EmpDto(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("position"),
                        rs.getDate("hire_date").toString()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // 특정 사원 조회 (Read by ID)
    public EmpDto getEmployeeById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new EmpDto(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("position"),
                            rs.getDate("hire_date").toString()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 사원 정보 수정 (Update)
    public boolean updateEmployee(EmpDto employee) {
        String sql = "UPDATE employees SET name = ?, phone = ?, position = ?, hire_date = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPhone());
            pstmt.setString(3, employee.getPosition());
            pstmt.setDate(4, Date.valueOf(employee.getHireDate()));
            pstmt.setInt(5, employee.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 사원 삭제 (Delete)
    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
