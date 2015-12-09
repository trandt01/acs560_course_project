package main

import (
	"bytes"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"github.com/go-xorm/xorm"
	"github.com/labstack/echo"
	"io/ioutil"
	"net/http"
	"strconv"
	"time"
)

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

// path /insert/account/:DeviceID/:ID?field1=value1&&field2=value2
// example /insert/account/device10/12?Weight=17.3&&Height=20.69&&BodyFat=100.44

// path /update/account/:DeviceID/:ID?field1=value1&&field2=value2
// example /update/account/device10/12?Weight=17.3&&Height=20.69&&BodyFat=100.44
func setAccount(c *echo.Context, isInsert bool) error {
	deviceid := c.Param("DeviceID")
	id, err := strconv.ParseUint(c.Param("ID"), 10, 64)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	acc := Account{DeviceID: deviceid, ID: id}
	has, err := g_engine.Get(&acc)
	if err != nil {
		return jsonResp(c, err.Error())
	}
	if isInsert && has {
		return jsonResp(c, "exists")
	}

	if !isInsert && !has {
		return jsonResp(c, "not exists")
	}

	for _, field := range []string{"Weight", "Height", "BodyFat"} {
		switch field {
		case "Weight":
			tmp, err := strconv.ParseFloat(c.Query(field), 32)
			acc.Weight = JSONFloat64(tmp)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "Height":
			tmp, err := strconv.ParseFloat(c.Query(field), 32)
			acc.Height = JSONFloat64(tmp)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "BodyFat":
			tmp, err := strconv.ParseFloat(c.Query(field), 32)
			acc.BodyFat = JSONFloat64(tmp)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		}
	}

	if isInsert {
		_, err = g_engine.Insert(&acc)
	} else {
		_, err = g_engine.Update(&acc)
	}

	if err != nil {
		return jsonResp(c, err.Error())
	}

	if err = updateOrInsert(&SyncTime{DeviceID: deviceid}); err != nil {
		return jsonResp(c, err.Error())
	}

	return jsonResp(c, "success")
}

// path /get/account/:DeviceID/:ID
// example /get/account/device10/12
func getAccount(c *echo.Context) error {
	id, err := strconv.ParseUint(c.Param("ID"), 10, 64)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	acc := Account{DeviceID: c.Param("DeviceID"), ID: id}
	has, err := g_engine.Get(&acc)
	if err != nil {
		return jsonResp(c, err.Error())
	}
	if !has {
		return jsonResp(c, "not exists")
	}

	return c.JSON(http.StatusOK, acc)
}

// path /insert/lifthistory/:DeviceID/:ID?field1=value1&&field2=value2
// example /insert/lifthistory/device10/15?Lift=sssssd&&BodyPart=leg&&Weight=30.1&&Reps=30&&Date=2015-04-04&&Active=true

// path /update/lifthistory/:DeviceID/:ID?field1=value1&&field2=value2
// example /update/lifthistory/device10/15?Lift=kkk&&BodyPart=leg&&Weight=30.44&&Reps=300&&Date=2015-04-04&&Active=false
func setLiftHistory(c *echo.Context, isInsert bool) error {
	deviceid := c.Param("DeviceID")
	id, err := strconv.ParseUint(c.Param("ID"), 10, 64)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	lift := LiftHistory{DeviceID: deviceid, ID: id}
	has, err := g_engine.Get(&lift)
	if err != nil {
		return jsonResp(c, err.Error())
	}
	if isInsert && has {
		return jsonResp(c, "exists")
	}

	if !isInsert && !has {
		return jsonResp(c, "not exists")
	}

	for _, field := range []string{"Lift", "BodyPart", "Weight", "Reps", "Date", "Active"} {
		switch field {
		case "Lift":
			lift.Lift = c.Query(field)
		case "BodyPart":
			lift.BodyPart = c.Query(field)
		case "Weight":
			tmp, err := strconv.ParseFloat(c.Query(field), 32)
			lift.Weight = JSONFloat64(tmp)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "Reps":
			lift.Reps, err = strconv.ParseUint(c.Query(field), 10, 64)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "InsertDate":
			date, err := time.Parse(TIME_LAYOUT, c.Query(field))
			lift.InsertDate = JSONTime(date)
			if err != nil {
				return jsonResp(c, err.Error())
			}
		case "LiftDate":
			date, err := time.Parse(TIME_LAYOUT, c.Query(field))
			lift.LiftDate = JSONTime(date)
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

	if isInsert {
		_, err = g_engine.Insert(&lift)
	} else {
		_, err = g_engine.Update(&lift)
	}

	if err != nil {
		return jsonResp(c, err.Error())
	}

	if err = updateOrInsert(&SyncTime{DeviceID: deviceid}); err != nil {
		return jsonResp(c, err.Error())
	}

	return jsonResp(c, "success")
}

// path /get/lifthistory/:DeviceID/:ID
// example /get/lifthistory/device10/15
func getLiftHistory(c *echo.Context) error {
	id, err := strconv.ParseUint(c.Param("ID"), 10, 64)
	if err != nil {
		return jsonResp(c, err.Error())
	}
	lift := LiftHistory{DeviceID: c.Param("DeviceID"), ID: id}

	has, err := g_engine.Get(&lift)
	if err != nil {
		return jsonResp(c, err.Error())
	}
	if !has {
		return jsonResp(c, "not exists")
	}

	return c.JSON(http.StatusOK, lift)
}

var g_html_str = `
<html>
	<head><title>upload sqlite dbfile</title></head>
	<body>
		<form enctype="multipart/form-data" action="/set/sqlitefile/%s" method="post">
			<input type="file" name="sqlitefile" />
			<input type="submit" value="upload" />
		</form>
	</body>
</html>
`

// path /set/sqlitefile/:DeviceID
// example /set/sqlitefile/18333
func htmlSqliteFile(c *echo.Context) error {
	return c.HTML(http.StatusOK, fmt.Sprintf(g_html_str, c.Param("DeviceID")))
}

// post /set/sqlitefile/:DeviceID
func setSqliteFile(c *echo.Context) error {
	deviceid := c.Param("DeviceID")

	sf := SqliteFile{DeviceID: deviceid}
	has, err := g_engine.Get(&sf)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	file, head, err := c.Request().FormFile("sqlitefile")
	if err != nil {
		return jsonResp(c, err.Error())
	}
	defer file.Close()

	sf.Name = head.Filename
	sf.Context, err = ioutil.ReadAll(file)
	if err != nil {
		return jsonResp(c, err.Error())
	}

	if has {
		_, err = g_engine.Update(&sf)

	} else {
		_, err = g_engine.Insert(&sf)
	}

	if err != nil {
		return jsonResp(c, err.Error())
	}

	if err = updateOrInsert(&SyncTime{DeviceID: deviceid}); err != nil {
		return jsonResp(c, err.Error())
	}

	return jsonResp(c, "success")
}

// path /get/sqlitefile/:DeviceID
// example /get/sqlitefile/30404
func getSqliteFile(c *echo.Context) error {
	sf := SqliteFile{DeviceID: c.Param("DeviceID")}
	has, err := g_engine.Get(&sf)
	if err != nil {
		return jsonResp(c, err.Error())
	}
	if !has {
		return jsonResp(c, "not exists")
	}

	return c.String(http.StatusOK, string(sf.Context))
}

// path /get/sqlitefileattach/:DeviceID
// example /get/sqlitefile/30404
func getSqliteFileAttach(c *echo.Context) error {
	sf := SqliteFile{DeviceID: c.Param("DeviceID")}
	has, err := g_engine.Get(&sf)
	if err != nil {
		return jsonResp(c, err.Error())
	}
	if !has {
		return jsonResp(c, "not exists")
	}

	c.Response().Header().Set(echo.ContentDisposition, "attachment; filename="+sf.Name)

	http.ServeContent(c.Response(), c.Request(), sf.Name, sf.Mtime, bytes.NewReader(sf.Context))

	return nil
}
