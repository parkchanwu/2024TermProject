CREATE TABLE member (
    id VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255),
    name VARCHAR(255),
    age INT,
    tel VARCHAR(20),
    ssn VARCHAR(20)
);



CREATE TABLE flight_info (
    flight_id INT PRIMARY KEY AUTO_INCREMENT,
    departure_airport VARCHAR(255),
    arrival_airport VARCHAR(255),
    airline_name VARCHAR(255),
    flight_number VARCHAR(20),
    arrival_time DATETIME,
    gate VARCHAR(100)
);



CREATE TABLE reservation (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(255),
    flight_id INT,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES member(id),
    FOREIGN KEY (flight_id) REFERENCES flight_info(flight_id)
);


