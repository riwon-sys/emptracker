<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>사원 관리 시스템</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .navbar {
            background-color: #343a40 !important;
        }
        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .table-dark {
            background-color: #343a40 !important;
            color: white;
        }
    </style>
</head>
<body>

<!-- 네비게이션 바 -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container d-flex align-items-center">
        <a class="navbar-brand d-flex align-items-center" href="#">
            <span class="text-white">사원 관리 시스템</span>
        </a>
        <button class="btn btn-primary ms-2" data-bs-toggle="modal" data-bs-target="#employeeModal">사원 추가</button>
    </div>
</nav>

<!-- 콘텐츠 영역 -->
<div class="container mt-4" style="height: 582px; overflow-y: scroll;">
    <h2 class="mb-4 text-center">사원 목록</h2>
    <table class="table table-striped text-center">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>이름</th>
                <th>전화번호</th>
                <th>직급</th>
                <th>입사일</th>
                <th>관리</th>
            </tr>
        </thead>
        <tbody id="employeeTableBody">
            <!-- JavaScript에서 데이터 로드 -->
        </tbody>
    </table>
</div>

<!-- 사원 등록 모달 -->
<div class="modal fade" id="employeeModal" tabindex="-1" aria-labelledby="employeeModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="employeeModalLabel">사원 추가</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="employeeForm">
                    <div class="mb-3">
                        <label class="form-label">이름</label>
                        <input type="text" class="form-control" id="name" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">전화번호</label>
                        <input type="text" class="form-control" id="phone" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">직급</label>
                        <input type="text" class="form-control" id="position" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">입사일</label>
                        <input type="date" class="form-control" id="hireDate" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">등록</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 푸터 -->
<footer class="bg-dark text-white text-center py-3 mt-4">
    <div class="container">
        <p class="mb-0">&copy; 2025 K-D-T ㏇ </p>
        <p>인천시 부평구 경원대로 1366, (부평동, 스테이션타워 7F) | 전화: 032-521-8889</p>
        <p>사업자번호: 123-45-56789 | 통신판매신고번호: 2025-서울서초-1234</p>
        <p>HR 문의: <a href="mailto:partner@megaclab.com" class="text-white">lelabo7317@gmail.com</a></p>
        <p>
            <a href="#" class="text-white me-3">사용약관</a>
            <a href="#" class="text-white me-3">개인정보취급방침</a>
            <a href="#" class="text-white me-3">기타 문의사항</a>
        </p>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- JavaScript 파일 연결 -->
<script src="index.js"></script>
</body>
</html>