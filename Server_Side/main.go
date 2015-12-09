package main

import (
	"flag"
	_ "github.com/go-sql-driver/mysql"
	"github.com/go-xorm/core"
	"github.com/go-xorm/xorm"
	"github.com/labstack/echo"
	mw "github.com/labstack/echo/middleware"
)

func main() {
	var user, pwd, port string

	flag.StringVar(&user, "user", "root", "mysql user default is root")

	flag.StringVar(&pwd, "pwd", "", "mysql pwd default is \"\"")

	flag.StringVar(&port, "port", ":8080", "http port default is :8080")

	flag.Parse()

	var err error
	//[username[:password]@][protocol[(address)]]/dbname[?param1=value1&...&paramN=valueN]
	g_engine, err = xorm.NewEngine("mysql", user+":"+pwd+"@/bodybuild?charset=utf8")
	if err != nil {
		panic(err)
	}

	g_engine.Logger.SetLevel(core.LOG_WARNING)

	err = g_engine.Sync2(&Account{}, &LiftHistory{}, &SyncTime{}, &SqliteFile{})
	if err != nil {
		panic(err)
	}

	e := echo.New()

	e.Use(mw.Recover())

	// Routes
	e.Index("index.html")
	e.Favicon("favicon.ico")

	e.Get("/insert/account/:DeviceID/:ID", func(c *echo.Context) error { return setAccount(c, true) })
	e.Get("/update/account/:DeviceID/:ID", func(c *echo.Context) error { return setAccount(c, false) })
	e.Get("/get/account/:DeviceID/:ID", getAccount)

	e.Get("/insert/lifthistory/:DeviceID/:ID", func(c *echo.Context) error { return setLiftHistory(c, true) })
	e.Get("/update/lifthistory/:DeviceID/:ID", func(c *echo.Context) error { return setLiftHistory(c, false) })
	e.Get("/get/lifthistory/:DeviceID/:ID", getLiftHistory)

	e.Get("/set/sqlitefile/:DeviceID", htmlSqliteFile)
	e.Post("/set/sqlitefile/:DeviceID", setSqliteFile)
	e.Get("/get/sqlitefile/:DeviceID", getSqliteFile)
	e.Get("/get/sqlitefileattach/:DeviceID", getSqliteFileAttach)

	// Start server,
	e.Run(port)
}
