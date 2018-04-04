DROP DATABASE IF EXISTS albums;

CREATE DATABASE albums;

CREATE USER IF NOT EXISTS 'albums'@'localhost'
  IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON albums.* TO 'albums' @'localhost';

