#For using this server

##build

* create local mysql databace;
* install the following go package
  * go get github.com/go-sql-driver/mysql
  * go get github.com/go-xorm/xorm
  * go get github.com/labstack/echo (check go version, this package supports go 1.3+)
* go build -o xxxx(the name you like) -ldflags "-s -w" -i .

##running

* ./xxxx(the name you like) -user="(mysql username)" -pwd="(mysql password") -port="(port you like or 8080 as default)"
* or ./xxxx -help 
* if you want to run it at backstage, in UNIX-like system please check `nohup` command.

##file explain

`main.go` `method.go` and `structure.go` are the server file.<br>
`index.html` can be created by your own, check the `github.com/labstack/echo' for more information of web framework.<br>
Other files are maded for previous design of server.
