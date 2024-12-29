package com.example.shoppingapp

data class Data(
    var productname:String?=null,
    var brand:String?=null,
    var img:String?=null
    ,var id:String?=null
    ,var price:String?=null,
    var quantity:String?=null
)
{
    constructor() : this ("","","","","","")
}


