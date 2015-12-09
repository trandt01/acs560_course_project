package main

//database part

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	//"strconv"
	//"time"
)

func main() {
	db, err := sql.Open("mysql", "user:password@(localhost:port)/dbname")
	checkErr(err)
	fmt.Println("Connection success!")

	/*stmt, err := db.Prepare("delete from account where account = ?")
	checkErr(err)
	res, err := stmt.Exec("")
	checkErr(err)

	affect, err := res.RowsAffected()
	checkErr(err)

	fmt.Println("Rows Affacted:", affect)
	fmt.Println("Table account cleaned")
	*/
	/*	stmtIn, err := db.Prepare("INSERT INTO account VAlUES(?,?)")
		checkErr(err)
		res, err = stmtIn.Exec("3.321.222.4", "")
		checkErr(err)
		fmt.Println("Insert success!")
	*/
	stmtOut, err := db.Query("SELECT * FROM account")
	checkErr(err)

	for stmtOut.Next() {
		var account string
		var phoneID int
		err = stmtOut.Scan(&account, &phoneID)
		checkErr(err)

		if account == "" {
			break
		}
		fmt.Println(account)
		fmt.Println(phoneID)
	}

	stmtCal, err := db.Query("SELECT account, IFNULL(weight,0), IFNULL(height,0),IFNULL(bodyFat,0),IFNULL(BMI,0),IFNULL(FFMI,0) FROM user_info")
	checkErr(err)

	for stmtCal.Next() {
		var accout int
		/*
			var origWeight string
			var origHeight string
			var origBodyFat string
		*/
		var weight float32
		var height float32
		var bodyFat float32
		var BMI float32
		var FFMI float32

		//err = stmtCal.Scan(&accout, &origWeight, &origHeight, &origBodyFat, &BMI, &FFMI)
		err = stmtCal.Scan(&accout, &weight, &height, &bodyFat, &BMI, &FFMI)
		checkErr(err)

		//weight, height, bodyFat := origConv(origWeight, origHeight, origBodyFat)

		if weight == 0.0 || height == 0.0 || FFMI != 0.0 { // already calculated or missing height or weight
			//stmtCal.Next()

		} else {
			if bodyFat != 0.0 { //can calculate FFMI
				FFMI = CalFFMI(weight, height, bodyFat)
			}
			/*else if FFMI != CalFFMI(weight, height, bodyFat) { //FFMI number error recovery
				FFMI = CalFFMI(weight, height, bodyFat)
			}
			*/
			BMI = CalBMI(weight, height)
		}

		stmt, err := db.Prepare("update user_info set weight = ?,height = ?, bodyFat = ?, BMI = ?, FFMI = ? where account = ?")
		checkErr(err)

		res, err := stmt.Exec(weight, height, bodyFat, BMI, FFMI, accout)
		checkErr(err)

		affect, err := res.RowsAffected()
		checkErr(err)

		fmt.Println("Rows Affacted:", affect)
		fmt.Println("Table user_info updated")
	}

	db.Close()

}

func checkErr(err error) {
	if err != nil {
		panic(err)
	}
}

func CalBMI(w float32, h float32) float32 { //based on m and kg
	return w / (h * h)
}

func CalFFMI(w float32, h float32, bF float32) float32 { //based on m and kg
	return (w * (1 - (bF / 100)) / (h * h)) + (6.3 * (1.8 - h))
}

/*
func origConv(ow string, oh string, obF string) (float32, float32, float32) {
	var wC, hC, bFc float32

	if ow != "" {
		wC := strconv.ParseFloat(w, 32)
	} else {
		wC := 0.0
	}
	fmt.Println("weith convert into:", wC)
	if oh != "" {
		hC := strconv.ParseFloat(h, 32)
	} else {
		hC := 0.0
	}
	fmt.Println("height convert into:", hC)
	if obF != "" {
		bFC := strconv.ParseFloat(bF, 32)
	} else {
		bFC := 0.0
	}
	fmt.Println("bodyFat convert into:", bFC)

	return wC, hC, bFC
}
*/
