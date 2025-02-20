document.addEventListener("DOMContentLoaded", function () {
    loadEmployees();
    document.getElementById("employeeForm").addEventListener("submit", handleInsertFormSubmit);
    document.getElementById("editEmployeeForm").addEventListener("submit", handleEditFormSubmit);
});

// 🚀 [1] 사원 전체 조회 (GET 요청)
function loadEmployees() {
    fetch('/employees')
        .then(response => response.json())
        .then(data => {
            let tableBody = document.getElementById("employeeTableBody");
            tableBody.innerHTML = "";
            data.forEach(emp => {
                tableBody.innerHTML += `
                    <tr>
                        <td>${emp.id}</td>
                        <td>${emp.name}</td>
                        <td>${emp.phone}</td>
                        <td>${emp.position}</td>
                        <td>${emp.hireDate}</td>
                        <td>
                            <button class="btn btn-info btn-sm view-btn" data-id="${emp.id}">조회</button>
                            <button class="btn btn-warning btn-sm edit-btn" data-id="${emp.id}">수정</button>
                            <button class="btn btn-danger btn-sm delete-btn" data-id="${emp.id}">삭제</button>
                        </td>
                    </tr>`;
            });
        })
        .catch(error => console.error("❌ 데이터 불러오기 실패: ", error));
}

// 🚀 [2] 사원 개별 조회 (GET 요청)
function getEmployee(id) {
    return fetch(`/employees/${id}`)
        .then(response => response.json());
}

// 🚀 [3] 사원 등록 처리 (POST 요청)
function insertEmployee() {
    let employeeData = getEmployeeFormData();
    console.log("📌 사원 등록 데이터:", employeeData);

    fetch('/employees', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(employeeData)
    })
    .then(response => response.json())
    .then(data => {
        console.log("✅ 서버 응답:", data);
        loadEmployees();
        bootstrap.Modal.getInstance(document.getElementById("employeeModal")).hide();
    })
    .catch(error => console.error("❌ 사원 등록 요청 실패:", error));
}

// 🚀 [4] 사원 수정 처리 (PUT 요청)
function updateEmployee(id) {
    let employeeData = getEditEmployeeFormData();
    employeeData.id = id;

    fetch(`/employees/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(employeeData)
    })
    .then(response => response.json())
    .then(() => {
        loadEmployees();
        bootstrap.Modal.getInstance(document.getElementById("editEmployeeModal")).hide();
    })
    .catch(error => console.error("❌ 사원 수정 요청 실패:", error));
}

// 🚀 [5] 사원 삭제 처리 (DELETE 요청)
function deleteEmployee(id) {
    fetch(`/employees/${id}`, { method: "DELETE" })
        .then(() => loadEmployees());
}

// 🚀 [6] 버튼 이벤트 처리 (조회, 수정, 삭제)
document.addEventListener("click", function (event) {
    let employeeId = event.target.getAttribute("data-id");

    if (event.target.classList.contains("delete-btn")) {
        if (confirm("정말 삭제하시겠습니까?")) deleteEmployee(employeeId);
    } else if (event.target.classList.contains("view-btn")) {
        viewEmployee(employeeId);
    } else if (event.target.classList.contains("edit-btn")) {
        editEmployee(employeeId);
    }
});

// 🚀 [7] 사원 상세 조회 모달 표시
function viewEmployee(id) {
    getEmployee(id).then(data => {
        document.getElementById("viewEmployeeName").innerText = data.name;
        document.getElementById("viewEmployeePhone").innerText = data.phone;
        document.getElementById("viewEmployeePosition").innerText = data.position;
        document.getElementById("viewEmployeeHireDate").innerText = data.hireDate;
        new bootstrap.Modal(document.getElementById("viewEmployeeModal")).show();
    });
}

// 🚀 [8] 사원 수정 모달 표시
function editEmployee(id) {
    getEmployee(id).then(data => {
        document.getElementById("editEmployeeId").value = data.id;
        document.getElementById("editEmployeeName").value = data.name;
        document.getElementById("editEmployeePhone").value = data.phone;
        document.getElementById("editEmployeePosition").value = data.position;
        document.getElementById("editEmployeeHireDate").value = data.hireDate;
        new bootstrap.Modal(document.getElementById("editEmployeeModal")).show();
    });
}

// 🚀 [9] 등록 폼 제출 핸들러
function handleInsertFormSubmit(event) {
    event.preventDefault();
    insertEmployee();
}

// 🚀 [10] 수정 폼 제출 핸들러
function handleEditFormSubmit(event) {
    event.preventDefault();
    let employeeId = document.getElementById("editEmployeeId").value;
    updateEmployee(employeeId);
}

// 🚀 [11] 공통: 폼 데이터 가져오기
function getEmployeeFormData() {
    return {
        name: document.getElementById("name").value,
        phone: document.getElementById("phone").value,
        position: document.getElementById("position").value,
        hireDate: document.getElementById("hireDate").value
    };
}

function getEditEmployeeFormData() {
    return {
        name: document.getElementById("editEmployeeName").value,
        phone: document.getElementById("editEmployeePhone").value,
        position: document.getElementById("editEmployeePosition").value,
        hireDate: document.getElementById("editEmployeeHireDate").value
    };
}

