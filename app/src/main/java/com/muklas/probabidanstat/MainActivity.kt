package com.muklas.probabidanstat

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log10
import kotlin.math.pow

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var dataList = ArrayList<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0f

        btnAdd.setOnClickListener(this)
        btnAddStem.setOnClickListener(this)
        btnScanData.setOnClickListener(this)
        btnScanLeaf.setOnClickListener(this)
    }

    private fun loadData() {
        dataList.sort()
        with(CalculateData) {
            tvResult.text = showData(dataList) + "\n" +
                    "N = " + dataList.size + "\n" +
                    countDataString(dataList) + "\n" +
                    getRangeString(dataList) + "\n" +
                    getMeanString(dataList) + "\n" +
                    getMedianString(dataList) + "\n" +
                    getClassCountString(dataList) + "\n" +
                    getIntervalString(dataList) + "\n" +
                    getVariationString(dataList)

        }
    }

    private fun addData() {
        val numberStr = etNumber.text.toString()
        if (numberStr != "") {
            val splitStr =
                numberStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (i in splitStr) {
                val number = i.toDouble()
                dataList.add(number)
            }

            etNumber.setText("")
            loadData()
        }
    }

    private fun addStemLeaf() {
        val leafStr = etLeaf.text.toString()
        val stemStr = etStem.text.toString()
        if (leafStr != "") {
            val splitstr =
                leafStr.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var iterator = 0
            for (i in splitstr) {
                if (iterator > 0) {
                    val numberStr = stemStr + i
                    val number = numberStr.toDouble()
                    dataList.add(number)
                }
                iterator++
            }

            etStem.setText("")
            etLeaf.setText("")
            loadData()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> addData()
            R.id.btnAddStem -> addStemLeaf()
            R.id.btnScanData -> {
                val intent = Intent(this, OCRActivity::class.java)
                intent.putExtra("data", "")
                startActivityForResult(intent, 100)
            }
            R.id.btnScanLeaf -> {
                val intent = Intent(this, OCRActivity::class.java)
                intent.putExtra("leaf", "")
                startActivityForResult(intent, 100)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        etNumber.setText(data?.getStringExtra("data")?.trimEnd()?.replace("\n", " "))
        etLeaf.setText(data?.getStringExtra("leaf")?.trimEnd())
    }

}
