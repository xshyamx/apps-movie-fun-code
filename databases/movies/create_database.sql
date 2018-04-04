DROP DATABASE IF EXISTS movies;

CREATE DATABASE movies;

CREATE USER IF NOT EXISTS 'movies'@'localhost'
  IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON movies.* TO 'movies' @'localhost';
