package emptracker.service;

import emptracker.dao.EmpDao;
import emptracker.dto.EmpDto;

import java.util.List;

public class EmpService {
    private final EmpDao empDao;

    public EmpService() {
        this.empDao = EmpDao.getInstance();
    }

    // 사원 추가
    public boolean addEmployee(EmpDto employee) {
        return empDao.addEmployee(employee);
    }

    // 모든 사원 조회
    public List<EmpDto> getAllEmployees() {
        return empDao.getAllEmployees();
    }

    // 특정 사원 조회
    public EmpDto getEmployeeById(int id) {
        return empDao.getEmployeeById(id);
    }

    // 사원 정보 수정
    public boolean updateEmployee(EmpDto employee) {
        return empDao.updateEmployee(employee);
    }

    // 사원 삭제
    public boolean deleteEmployee(int id) {
        return empDao.deleteEmployee(id);
    }
}