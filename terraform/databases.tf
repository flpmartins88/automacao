
resource "aws_db_instance" "dev_database" {
  identifier = "dev-database"

  allocated_storage    = 20

  db_name              = "dev_database"
  engine               = "mysql"
  engine_version       = "8.0.30"
  instance_class       = "db.t3.micro"
  username             = "admin"
  password             = "password"
  parameter_group_name = "default.mysql8.0"
  skip_final_snapshot  = true
}
