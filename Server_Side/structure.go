package main

import (
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"time"
)

type JSONFloat64 float64

func (t JSONFloat64) MarshalJSON() ([]byte, error) {
	return []byte(fmt.Sprintf("%.2f", float64(t))), nil
}

//models
type Account struct {
	DeviceID string `xorm:"index(ID)"`
	ID       uint64 `xorm:"index(ID)"`
	Weight   JSONFloat64
	Height   JSONFloat64
	BodyFat  JSONFloat64
}

const TIME_LAYOUT = "2006-01-02"

type JSONTime time.Time

func (t JSONTime) MarshalJSON() ([]byte, error) {
	str := fmt.Sprintf("\"%s\"", time.Time(t).Format(TIME_LAYOUT))
	return []byte(str), nil
}

type LiftHistory struct {
	DeviceID   string `xorm:"index(ID)"`
	ID         uint64 `xorm:"index(ID)"`
	Lift       string
	BodyPart   string
	Weight     JSONFloat64
	Reps       uint64
	InsertDate JSONTime
	LiftDate   JSONTime
	Active     bool
}

type SyncTime struct {
	DeviceID string   `xorm:"index"`
	Date     JSONTime `xorm:"updated"`
}

type SqliteFile struct {
	DeviceID string `xorm:"index"`
	Name     string
	Mtime    time.Time `xorm:"updated"`
	Context  []uint8
}
