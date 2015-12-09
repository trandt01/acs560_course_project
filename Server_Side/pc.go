package protocol

//protocol
import (
	"bytes"
	"encoding/binary"
)

const (
	ConstHeader       = "Headers"
	ConstHeaderLength = 7
	ConstMLength      = 4
)

func Enpack(message []byte) []byte {
	return append(append([]byte(ConstHeader), IntToBytes(len(message))...), message...)
}

func Depack(buffer []byte, readerChannel chan []byte) []byte {
	length := len(buffer)

	var i int
	for i = 0; i < length; i = i + 1 {
		if length < i+ConstHeaderLength+ConstMLength {
			break
		}
		if string(buffer[i:i+ConstHeaderLength]) == ConstHeader {
			messageLength := BytesToInt(buffer[i+ConstHeaderLength : i+ConstHeaderLength+ConstMLength])
			if length < i+ConstHeaderLength+ConstLength+messageLength {
				break
			}
			data := buffer[i+ConstHeaderLength+ConstMLength : i+ConstHeaderLength+ConstMLength+messageLength]
			readerChannel <- data

		}
	}

	if i == length {
		return make([]byte, 0)
	}
	return buffer[i:]
}

func IntToBytes(n int) []byte {
	x := int32(n)

	bytesBuffer := bytes.NewBuffer([]byte{})
	binary.Write(bytesBuffer, binary.BigEndian, x)
	return bytesBuffer.Bytes()
}

func BytesToInt(b []byte) int {
	bytesBuffer := bytes.NewBuffer(b)

	var x int32
	binary.Read(bytesBuffer, binary.BigEndian, &x)

	return int(x)
}
