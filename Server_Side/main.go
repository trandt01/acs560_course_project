package main

import (
	"flag"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"github.com/go-xorm/core"
	"github.com/go-xorm/xorm"
	"github.com/labstack/echo"
	mw "github.com/labstack/echo/middleware"
	"net/http"
	"strconv"
	"time"
)


type Account struct {
	DeviceID uint64 `xorm:"index"`
	Weight   float64
	Height   float64
	BodyFat  float64
}

const TIME_LAYOUT = "2006-01-02"

type JSONTime time.Time

func (t JSONTime) MarshalJSON() ([]byte, error) {
	str := fmt.Sprintf("\"%s\"", time.Time(t).Format(TIME_LAYOUT))
	return []byte(str), nil
}

type LiftHistory struct {
	DeviceID uint64 `xorm:"index"`
	Lift     string
	BodyPart string
	Weight   float64
	Reps     uint64
	Date     JSONTime `xorm:"updated"`
	Active   bool
}

type SyncTime struct {
	DeviceID uint64   `xorm:"index"`
	Date     JSONTime `xorm:"updated"`
}

var g_engine *xorm.Engine

func jsonResp(c *echo.Context, reason string) error {
	return c.JSON(
		http.StatusOK,
		map[string]string{"msg": reason},
	)
}

func updateOrInsert(ptrobj interface{}) error {
	has, err := g_engine.Get(ptrobj)
	if err != nil {
		return err
	}

	if has {
		_, err = g_engine.Update(ptrobj)

	} else {
		_, err = g_engine.Insert(ptrobj)
	}
	return err
}

func setAccount(c *echo.Context) error {
	deviceid, err := strconv.ParseUint(c.Param("DeviceID"), 10, 64)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	acc := Account{DeviceID: deviceid}
	has, err := g_engine.Get(&acc)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	for _, field := range []string{"Weight", "Height", "BodyFat"} {
		switch field {
		case "Weight":
			acc.Weight, err = strconv.ParseFloat(c.Query(field), 32)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "Height":
			acc.Height, err = strconv.ParseFloat(c.Query(field), 32)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "BodyFat":
			acc.BodyFat, err = strconv.ParseFloat(c.Query(field), 32)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		}
	}

	if has {
		_, err = g_engine.Update(&acc)

	} else {
		_, err = g_engine.Insert(&acc)
	}

	if err != nil {
		return jsonResp(c, err.Error())
	}

	if err = updateOrInsert(&SyncTime{DeviceID: deviceid}); err != nil {
		return jsonResp(c, err.Error())
	}

	return jsonResp(c, "success")
}

func getAccount(c *echo.Context) error {
	deviceid, err := strconv.ParseUint(c.Param("DeviceID"), 10, 64)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	acc := Account{DeviceID: deviceid}
	has, err := g_engine.Get(&acc)
	if err != nil {
		return jsonResp(c, err.Error())
	}
	if !has {
		return jsonResp(c, "not exists")
	}

	return c.JSON(http.StatusOK, acc)
}

func setLiftHistory(c *echo.Context) error {
	deviceid, err := strconv.ParseUint(c.Param("DeviceID"), 10, 64)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	lift := LiftHistory{DeviceID: deviceid}
	has, err := g_engine.Get(&lift)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	for _, field := range []string{"Lift", "BodyPart", "Weight", "Reps", "Date", "Active"} {
		switch field {
		case "Lift":
			lift.Lift = c.Query(field)
		case "BodyPart":
			lift.BodyPart = c.Query(field)
		case "Weight":
			lift.Weight, err = strconv.ParseFloat(c.Query(field), 32)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "Reps":
			lift.Reps, err = strconv.ParseUint(c.Query(field), 10, 64)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "Date":
			date, err := time.Parse(TIME_LAYOUT, c.Query(field))
			lift.Date = JSONTime(date)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "Active":
			lift.Active, err = strconv.ParseBool(c.Query(field))
			if err != nil {
				return jsonResp(c, err.Error())
			}
		}
	}

	if has {
		_, err = g_engine.Update(&lift)

	} else {
		_, err = g_engine.Insert(&lift)
	}

	if err != nil {
		return jsonResp(c, err.Error())
	}

	if err = updateOrInsert(&SyncTime{DeviceID: deviceid}); err != nil {
		return jsonResp(c, err.Error())
	}

	return jsonResp(c, "success")
}

func getLiftHistory(c *echo.Context) error {
	deviceid, err := strconv.ParseUint(c.Param("DeviceID"), 10, 64)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	lift := LiftHistory{DeviceID: deviceid}
	has, err := g_engine.Get(&lift)
	if err != nil {
		return jsonResp(c, err.Error())
	}
	if !has {
		return jsonResp(c, "not exists")
	}

	return c.JSON(http.StatusOK, lift)
}

func main() {
	var user, pwd, port string

	flag.StringVar(&user, "user", "root", "mysql user default is root")

	flag.StringVar(&pwd, "pwd", "", "mysql pwd default is \"\"")

	flag.StringVar(&port, "port", ":8080", "http port default is :8080")

	flag.Parse()

	var err error
	g_engine, err = xorm.NewEngine("mysql", user+":"+pwd+"@/bodybuild?charset=utf8")
	if err != nil {
		panic(err)
	}

	g_engine.Logger.SetLevel(core.LOG_WARNING)

	err = g_engine.Sync2(&Account{}, &LiftHistory{}, &SyncTime{})
	if err != nil {
		panic(err)
	}

	e := echo.New()

	e.Use(mw.Recover())

	e.Get("/set/account/:DeviceID", setAccount)
	e.Get("/get/account/:DeviceID", getAccount)

	e.Get("/set/lifthistory/:DeviceID", setLiftHistory)
	e.Get("/get/lifthistory/:DeviceID", getLiftHistory)

	e.Run(port)
}
