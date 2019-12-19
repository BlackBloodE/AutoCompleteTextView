package com.example.autocompletetextview

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var nearHistory = 3 //显示记录条数
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initHistory()
        auto_complete_text.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            val view = v as AutoCompleteTextView
            if (hasFocus) {
                val sp =
                    getSharedPreferences("auto_text", Context.MODE_PRIVATE)
                val history = sp.getString("history", "")
                //history不为空才显示
                if (!TextUtils.isEmpty(history)) view.showDropDown()
            }
        })
        btn_main_save.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                saveHistory(auto_complete_text)
            }
        })
    }
    private fun initHistory() {
        val sp =
            getSharedPreferences("auto_text", Context.MODE_PRIVATE)
        val history = sp.getString("history", "")
        val hisArrays: Array<String?> = history!!.split(",").toTypedArray()
        var hisAdapter: ArrayAdapter<String?>? = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            hisArrays
        )
        //TODO:这个字符串会越来越长,开发中最好提供清理历史记录的功能
        if (hisArrays.size > nearHistory) {
            val newArrays = arrayOfNulls<String>(nearHistory)
            System.arraycopy(hisArrays, 0, newArrays, 0, nearHistory)
            //布局可以自己创建,用android自带的也行
            hisAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                newArrays
            )
        }
        auto_complete_text.setAdapter(hisAdapter) //这里要传递的adapter参数必须是继承ListAdapter和Filterable的
        //        mAutoTv.setDropDownHeight(350);//设置提示下拉框的高度，注意，这只是限制了提示下拉框的高度，提示数据集的个数并没有变化
        auto_complete_text.setThreshold(1) //当输入一个字符时就开始搜索历史,默认是2个
        auto_complete_text.setCompletionHint("最近紀錄")
        //        TODO:一些AutoCompleteTextView的方法解释
//        clearListSelection，去除selector样式，只是暂时的去除，当用户再输入时又重新出现
//        dismissDropDown，关闭下拉提示框
//        enoughToFilter，这是一个是否满足过滤条件的方法，sdk建议我们可以重写这个方法
//        getAdapter，得到一个可过滤的列表适配器
//        getDropDownAnchor，得到下拉框的锚计的view的id
//        getDropDownBackground，得到下拉框的背景色
//        setDropDownBackgroundDrawable，设置下拉框的背景色
//        setDropDownBackgroundResource，设置下拉框的背景资源
//        setDropDownVerticalOffset，设置下拉表垂直偏移量，即是list里包含的数据项数目
//        getDropDownVerticalOffset ，得到下拉表垂直偏移量
//        setDropDownHorizontalOffset，设置水平偏移量
//        setDropDownAnimationStyle，设置下拉框的弹出动画
//        getThreshold，得到过滤字符个数
//        setOnItemClickListener，设置下拉框点击事件
//        getListSelection，得到下拉框选中为位置
//        getOnItemClickListener。得到单项点击事件
//        getOnItemSelectedListener得到单项选中事件
//        getAdapter，得到那个设置的适配器
    }
    private fun saveHistory(autoTv: AutoCompleteTextView) {
        val text = autoTv.text.toString()
        val sp =
            getSharedPreferences("auto_text", Context.MODE_PRIVATE)
        val history = sp.getString("history", "")
        //先判断是否已经存过一样的文字
        if (!TextUtils.isEmpty(text) && !history!!.contains("$text,")) {
            val sb = StringBuilder(history)
            sb.insert(0, "$text,") //插入到第一个,代表最新
            sp.edit().putString("history", sb.toString()).apply()
            Toast.makeText(this@MainActivity, "存好了", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "存过了", Toast.LENGTH_SHORT).show()
        }
    }
}
