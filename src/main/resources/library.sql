DROP DATABASE library;
CREATE DATABASE library;
USE library;

-- Création de la table categories
CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Création de la table books
CREATE TABLE `books` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `author` varchar(100) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Création de la table users
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `role` ENUM('ADMIN', 'STUDENT') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Création de la table students
CREATE TABLE `students` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL UNIQUE,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `students_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Création de la table loans
CREATE TABLE `loans` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL,
  `loan_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `student_id` (`student_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `loans_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  CONSTRAINT `loans_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Insertion de données dans la table categories
INSERT INTO `categories` (`id`, `name`) VALUES
(1, 'Grow'),
(2, 'Until'),
(3, 'Necessary'),
(4, 'Dream'),
(5, 'Law'),
(6, 'Chance'),
(7, 'Set'),
(8, 'Know'),
(9, 'While'),
(10, 'Police');

-- Insertion de données dans la table books
INSERT INTO `books` (`id`, `title`, `author`, `category_id`) VALUES
(1, 'Player young white amount.', 'Kim Robinson', 7),
(2, 'Me American.', 'Jessica Sanchez', 7),
(3, 'Second serious together half.', 'Heather Mueller', 7),
(4, 'Behind day.', 'Stephanie Tran', 7),
(5, 'Media board.', 'Amy Poole', 9),
(6, 'Their dream American.', 'Deanna Knight', 2),
(7, 'Republican each.', 'Jerome Sandoval', 3),
(8, 'Task whom large.', 'David Huang', 5),
(9, 'Its choice.', 'Rebecca Green', 10),
(10, 'The example stock.', 'Lisa Martin', 4);

-- Insertion de données dans la table users
INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
(1, 'Achraf', 'A@ch#arf)', 'STUDENT'),
(2, 'Hatim', 'sLd8CpOv^Y', 'STUDENT'),
(3, 'Sara', '**0jYFxjUH', 'STUDENT'),
(4, 'Soufian', ')s)&@yQhi2', 'STUDENT'),
(5, 'Karim', '#G9IK$EqG5', 'STUDENT'),
(6, 'Asmae', ')z$Y4UMkdT', 'STUDENT'),
(7, 'Maria', 'h6hI7qVzs_', 'STUDENT'),
(8, 'Anas', 'oituX!*c%1', 'ADMIN'),
(9, 'Assia', '@^1H^_dZ%K', 'ADMIN'),
(10, 'Hamid', '5Jk46rNj&L', 'STUDENT');

-- Insertion de données dans la table students
INSERT INTO `students` (`id`, `name`, `email`, `user_id`) VALUES
(1, 'Anas ER-RAKIBI', 'errakibianasdev@gmail.com', 8),
(2, 'Hamid KAMALI', 'hamidkamali@gmail.com', 10),
(3, 'Maria LAADIMI', 'laadimimaria@miller.org', 7),
(4, 'Achraf BENNIS', 'bennis@hotmail.com', 1),
(5, 'Asmae DOUKKALI', 'asmaedoukkali@gmail.com', 6),
(6, 'Karim KARIM', 'karimkarim@rodriguez.biz', 5),
(7, 'Soufian HAMMIDA', 'soufianhamida@hotmail.com', 4),
(8, 'Aroua NAIIMI', 'arouanaiimi@gmail.com', NULL);

-- Insertion de données dans la table loans
INSERT INTO `loans` (`id`, `book_id`, `student_id`, `loan_date`, `return_date`) VALUES
(1, 8, 3, '2025-01-20', '2025-05-26'),
(2, 9, 6, '2025-03-15', '2025-05-01'),
(3, 6, 2, '2025-03-27', '2025-05-26'),
(4, 1, 1, '2025-04-01', NULL),
(5, 4, 5, '2025-04-10', NULL),
(6, 5, 3, '2025-04-20', NULL);
