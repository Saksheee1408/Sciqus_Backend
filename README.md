# Sciqus Backend

A robust Student and Course Management System backend built with Spring Boot, featuring Firebase Authentication and PostgreSQL database.

## 🚀 Features

- **Student Management**: Complete CRUD operations for student records
- **Course Management**: Comprehensive course administration system
- **Firebase Authentication**: Secure user authentication and authorization
- **RESTful API**: Well-structured REST endpoints
- **PostgreSQL Database**: Reliable data persistence with Hibernate ORM
- **JWT Token Support**: Secure API access with Firebase tokens

## 🛠️ Technology Stack

- **Framework**: Spring Boot
- **Language**: Java
- **ORM**: Hibernate
- **Database**: PostgreSQL
- **Authentication**: Firebase Authentication
- **Build Tool**: Gradle
- **API Architecture**: RESTful

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- Java JDK 11 or higher
- PostgreSQL 12 or higher
- Gradle 7.x or higher
- Firebase Project with Authentication enabled
- IntelliJ IDEA (recommended) or any Java IDE

## ⚙️ Configuration

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE sciqus_db;
```

### 2. Application Properties

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/sciqus_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=8080
```

### 3. Firebase Configuration

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Navigate to Project Settings → Service Accounts
4. Click "Generate New Private Key"
5. Save the JSON file as `firebase-service-account.json`
6. Place it in `src/main/resources/` directory

**Note**: The `firebase-service-account.json` file is gitignored for security reasons. Never commit this file to version control.

## 🚀 Installation & Running

### Using Gradle Wrapper (Recommended)

```bash
# Clone the repository
git clone https://github.com/Saksheee1408/Sciqus_Backend.git
cd Sciqus_Backend

# Make gradlew executable (Linux/Mac)
chmod +x gradlew

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

### Using Gradle (Windows)

```powershell
# Build the project
.\gradlew.bat build

# Run the application
.\gradlew.bat bootRun
```

The application will start on `http://localhost:8080`

## 📚 API Endpoints

### Student Endpoints

| Method | Endpoint | Description | Access Level |
|--------|----------|-------------|--------------|
| GET | `/api/students` | Get all students | Admin only |
| GET | `/api/students/me` | Get current user details | Authenticated users |
| GET | `/api/students/{id}` | Get student by ID | Admin only |
| GET | `/api/students/with-courses` | Get all students with course details | Admin only |
| GET | `/api/students/course/{courseId}` | Get students by course ID | Admin only |
| POST | `/api/students` | Create new student with course assignment | Admin only |
| PUT | `/api/students/{id}` | Update student | Admin only |
| DELETE | `/api/students/{id}` | Delete student | Admin only |

#### Student Request Examples

**Create Student:**
```json
POST /api/students
{
  "student": {
    "studentName": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "firebaseUid": "firebase_uid_here",
    "role": "STUDENT"
  },
  "courseId": 1
}
```

**Update Student:**
```json
PUT /api/students/{id}
{
  "student": {
    "studentName": "John Doe Updated",
    "email": "john.updated@example.com",
    "phone": "0987654321"
  },
  "courseId": 2
}
```

### Course Endpoints

| Method | Endpoint | Description | Access Level |
|--------|----------|-------------|--------------|
| GET | `/api/courses` | Get all courses | Authenticated users |
| GET | `/api/courses/{id}` | Get course by ID | Authenticated users |
| POST | `/api/courses` | Create new course | Admin only |
| PUT | `/api/courses/{id}` | Update course | Admin only |
| DELETE | `/api/courses/{id}` | Delete course | Admin only |

#### Course Request Examples

**Create Course:**
```json
POST /api/courses
{
  "courseName": "Introduction to Java",
  "courseCode": "CS101",
  "description": "Learn Java programming basics",
  "credits": 3
}
```

## 🔐 Authentication

All API endpoints require Firebase Authentication. Include the Firebase ID token in the request header:

```
Authorization: Bearer <firebase_id_token>
```

### Test Credentials

For testing purposes, use these pre-configured Firebase accounts:

| Role | Email | Password | Permissions |
|------|-------|----------|-------------|
| Admin/Teacher | `teacher@gmail.com` | `Teacher123` | Full access to all endpoints |
| Student | `student@gmail.com` | `Student123` | Limited access (view own details, view courses) |

### Role-Based Access Control

- **Admin/Teacher Role**: Can perform all CRUD operations on students and courses
- **Student Role**: Can view their own details (`/api/students/me`) and view all courses (`/api/courses`)

### Getting a Firebase Token

1. Use Firebase Client SDK in your frontend application
2. Authenticate with the test credentials above
3. Obtain the ID token from Firebase Auth
4. Include the token in API requests: `Authorization: Bearer <token>`

**Example using curl:**
```bash
curl -X GET http://localhost:8080/api/courses \
  -H "Authorization: Bearer YOUR_FIREBASE_TOKEN"
```

## 📁 Project Structure

```
Sciqus_Backend/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── config/
│   │   │   │   └── FirebaseConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── StudentController.java
│   │   │   │   └── CourseController.java
│   │   │   ├── entity/
│   │   │   │   ├── Student.java
│   │   │   │   └── Course.java
│   │   │   ├── repository/
│   │   │   │   ├── StudentRepository.java
│   │   │   │   └── CourseRepository.java
│   │   │   ├── service/
│   │   │   │   ├── StudentService.java
│   │   │   │   ├── CourseService.java
│   │   │   │   └── FirebaseAuthService.java
│   │   │   └── Main.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── firebase-service-account.json (gitignored)
│   └── test/
├── gradle/
├── build.gradle
├── settings.gradle
├── .gitignore
└── README.md
```

## 🔒 Security Notes

- Firebase service account credentials are stored locally and not committed to version control
- All API endpoints are protected with Firebase Authentication
- Database credentials should be managed using environment variables in production
- Always use HTTPS in production environments

## 🧪 Testing

Run the tests using:

```bash
./gradlew test
```

## 🐛 Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running: `sudo service postgresql status`
- Check database credentials in `application.properties`
- Ensure the database exists

### Firebase Authentication Errors
- Verify `firebase-service-account.json` is in the correct location
- Check Firebase project configuration
- Ensure Firebase Authentication is enabled in Firebase Console

### Port Already in Use
- Change the port in `application.properties`: `server.port=8081`
- Or stop the process using port 8080

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👤 Author

**Saksheee**
- GitHub: [@Saksheee1408](https://github.com/Saksheee1408)

## 🙏 Acknowledgments

- Spring Boot Documentation
- Firebase Documentation
- PostgreSQL Documentation
- Hibernate Documentation

---

**Note**: Remember to rotate your Firebase credentials if they were ever exposed in version control.
