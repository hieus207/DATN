package com.example.qldatn.Helper

import android.content.Context
import android.widget.Toast
import com.example.qldatn.MyApplication

class Message {
    companion object {
        public fun msg(msg: String) {
            Toast.makeText(MyApplication._context, msg, Toast.LENGTH_SHORT).show()
        }

        public fun faildMsg() {
            Toast.makeText(MyApplication._context, "Thực thi thất bại, kiểm tra đường truyền mạng", Toast.LENGTH_SHORT).show()
        }
    }
}