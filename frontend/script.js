const API_BASE_URL = "http://localhost:8080/api";

document.getElementById("loadStudents").addEventListener("click", async () => {
  const response = await fetch(`${API_BASE_URL}/students/with-courses`);
  if (response.ok) {
    const students = await response.json();
    const tbody = document.querySelector("#studentsTable tbody");
    tbody.innerHTML = "";

    students.forEach(s => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${s.studentId}</td>
        <td>${s.studentName}</td>
        <td>${s.email}</td>
        <td>${s.phone}</td>
        <td>${s.course ? s.course.courseName : "No Course"}</td>
      `;
      tbody.appendChild(tr);
    });
  } else {
    alert("Error fetching students");
  }
});

document.getElementById("addCourse").addEventListener("click", async () => {
  const name = document.getElementById("courseName").value;
  const code = document.getElementById("courseCode").value;
  const duration = document.getElementById("courseDuration").value;

  const payload = {
    courseName: name,
    courseCode: code,
    courseDuration: parseInt(duration)
  };

  const response = await fetch(`${API_BASE_URL}/courses`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  const msg = document.getElementById("courseMessage");
  if (response.status === 201) {
    msg.textContent = "✅ Course added successfully!";
  } else {
    msg.textContent = "❌ Error adding course.";
  }
});
