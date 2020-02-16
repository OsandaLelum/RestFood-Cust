package com.restfood.myapplication;

public class FoodData {
    private String foodName;
    private String category;
    private int price;
    private int minDuration;
    private int maxDuration;
    private boolean isVeg;
    private String description;

    //this true for test
    private boolean isAvailable;



    //this is constructor part
    public FoodData()
    {
        foodName=null;
        category=null;
    }


    public FoodData(String foodname,String cat,int pri,int min,int max)
    {
        foodName=foodname;
        category=cat;
        price=pri;
        minDuration=min;
        maxDuration=max;
    }

    public FoodData(String foodname,String cat,int pri,int min,int max,boolean bol)
    {
        foodName=foodname;
        category=cat;
        price=pri;
        minDuration=min;
        maxDuration=max;
        this.isVeg=bol;
    }

    public FoodData(String foodname,String cat,int pri,int min,int max,boolean bol,boolean avail,String d)
    {
        foodName=foodname;
        category=cat;
        price=pri;
        minDuration=min;
        maxDuration=max;
        this.isVeg=bol;
        this.isAvailable=avail;
        this.description=d;
    }

    public FoodData(String foodname,String cat,int pri,int min,int max,boolean bol,boolean avail)
    {
        foodName=foodname;
        category=cat;
        price=pri;
        minDuration=min;
        maxDuration=max;
        this.isVeg=bol;
        this.isAvailable=avail;
    }


    //setter
    public void setFoodName(String name)
    {
        foodName=name;
    }

    public void setCategory(String cat)
    {
        category=cat;
    }

    public void setPrice(int pri)
    {
        price=pri;
    }

    public void setMinDuration(int min)
    {
        minDuration=min;
    }

    public void setMaxDuration(int max)
    {
        maxDuration=max;
    }

    public void setIsAvailable(boolean click)
    {
        isAvailable=click;
    }

    public void setDescription(String dis)
    {
        this.description=dis;
    }




    //access values from
    public String getFoodName()
    {
        return foodName;
    }

    public String getCategory()
    {
        return category;
    }
    public int getPrice()
    {
        return price;
    }

    public int getMinDuration()
    {
        return minDuration;
    }

    public int getMaxDuration()
    {
        return maxDuration;
    }
    public boolean getIsVeg()
    {
        return isVeg;
    }

    public boolean getIsAvailable()
    {
        return isAvailable;
    }

    public String getDescription()
    {
        return description;
    }


}
