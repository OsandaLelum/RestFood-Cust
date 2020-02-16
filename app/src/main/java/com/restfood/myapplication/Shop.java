package com.restfood.myapplication;

public class Shop {
    private  String shopName ;
    private  String shopType;
    private  String shopAddress;
    private String  shopEmail;


    public Shop()
    {

    }



    public Shop(String name,String type,String address,String email)
    {
        shopAddress=address;

        shopName=name;
        shopEmail=email;
        shopType=type;
    }


public void setShopName(String name)
{
    shopName=name ;
}

public String getShopName()
{
    return shopName;
}
/*
    public void  setShopId(String Id) {
    shopId = Id;
}*/
     public void setShopType(String type){
       shopType =type;
  }
     public String getShopType(){
        return shopType;
 }
// public String getShopId()
// {
//     return shopId;
// }

     public void setAddress(String add)
      {
     shopAddress=add;
      }

    public String getShopAddress()
     {
     return shopAddress;
     }
    public void setShopEmail(String email){
    shopEmail =email;
    }
    public String getShopemail(){
    return shopEmail;
    }
}
