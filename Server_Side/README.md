For using this server
==========================
build
-----------------------------------
* create local mysql databace;
* install the following go package
  * go get github.com/go-sql-driver/mysql
  * go get github.com/go-xorm/xorm
  * go get github.com/labstack/echo (check go version, this package supports go 1.3+)
* go build -o xxxx(your server name) -ldflags "-s -w" -i .

running
------------------------------------
* ./xxxx(your server name) -user="(mysql username)" -pwd="(mysql password") -port="(port you like or 8080 as default)"
* or ./xxxx -help 
