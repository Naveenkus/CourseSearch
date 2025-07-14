# Course Search Application

A Spring Boot application that provides course search functionality using Elasticsearch.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose

## Setup Instructions

### 1. Start Elasticsearch

```bash
docker-compose up -d
```

Verify Elasticsearch is running:
```bash
curl http://localhost:9200
```

### 2. Build and Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080` and automatically:
- Create the Elasticsearch index with proper mappings
- Load sample course data from `sample-courses.json`

## API Endpoints

### Search Courses

**Endpoint:** `GET /api/search`

**Query Parameters:**
- `q` - Search keyword (searches in title and description)
- `minAge` - Minimum age for the course
- `maxAge` - Maximum age for the course
- `category` - Course category (exact match)
- `type` - Course type (ONE_TIME, COURSE, or CLUB)
- `minPrice` - Minimum price
- `maxPrice` - Maximum price
- `startDate` - Start date (ISO-8601 format)
- `sort` - Sort order (priceAsc, priceDesc, default: nextSessionDate ascending)
- `page` - Page number (default: 0)
- `size` - Page size (default: 10)

### Example API Calls

#### 1. Basic search
```bash
curl "http://localhost:8080/api/search?q=math"
```

#### 2. Filter by age range
```bash
curl "http://localhost:8080/api/search?minAge=6&maxAge=10"
```

#### 3. Filter by category and type
```bash
curl "http://localhost:8080/api/search?category=Science&type=COURSE"
```

#### 4. Filter by price range
```bash
curl "http://localhost:8080/api/search?minPrice=50&maxPrice=100"
```

#### 5. Sort by price (ascending)
```bash
curl "http://localhost:8080/api/search?sort=priceAsc"
```

#### 6. Sort by price (descending)
```bash
curl "http://localhost:8080/api/search?sort=priceDesc"
```

#### 7. Filter by start date
```bash
curl "http://localhost:8080/api/search?startDate=2025-07-20T00:00:00Z"
```

#### 8. Combined filters with pagination
```bash
curl "http://localhost:8080/api/search?q=science&category=Science&minAge=7&maxAge=12&minPrice=60&maxPrice=120&sort=priceAsc&page=0&size=5"
```

## Response Format

```json
{
  "total": 25,
  "courses": [
    {
      "id": "1",
      "title": "Math Explorers",
      "description": "Description for Math Explorers",
      "category": "Math",
      "type": "COURSE",
      "gradeRange": "1th–3th",
      "minAge": 5,
      "maxAge": 7,
      "price": 50.0,
      "nextSessionDate": "2025-07-15T10:00:00Z"
    }
  ]
}
```

## Sample Data

The application includes 50 sample courses with varied:
- Categories: Math, Science, Art, Technology, Language, History, Coding, Health
- Types: ONE_TIME, COURSE, CLUB
- Age ranges: 5-12 years
- Prices: $50-$172.5
- Session dates: July 15 - September 2, 2025

## Testing

Run the tests:
```bash
mvn test
```

## Troubleshooting

1. **Elasticsearch connection issues:** Ensure Docker is running and Elasticsearch is accessible on port 9200
2. **Data not loading:** Check application logs for indexing errors
3. **Search not working:** Verify the index was created and data was indexed successfully

## Project Structure

```
src/
├── main/
│   ├── java/com/undoschool/course_search/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── document/        # Elasticsearch document models
│   │   ├── dto/             # Data transfer objects
│   │   ├── repository/      # Data access layer
│   │   └── service/         # Business logic
│   └── resources/
│       ├── sample-courses.json
│       └── application.properties
└── test/                    # Test files
```