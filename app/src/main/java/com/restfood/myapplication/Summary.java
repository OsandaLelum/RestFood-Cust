package com.restfood.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class Summary extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<OrderData> orderList = new ArrayList<>();
    ArrayList<String> docIdList = new ArrayList<>();
    ArrayList<Integer> totalList=new ArrayList<>();

    //ui components
    private TextView dailyTotalTextView;


    DataPoint[] dataPointArray;     //this is to assign values





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summaryx);
        dailyTotalTextView=findViewById(R.id.daily_total);


        orderList.add(new OrderData(true, "Pizza", "AA", "gfnufng", "gjfng", "gbuyfg", 522, "gjhbg"));

        getTotal();

    }


    //this is to get total from db
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getTotal() {
        orderList.clear();

        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();


        //this part is to store order
        final String yea=String.valueOf(currentYear);
        final String mon=currentMonth.toString();
        final String day=String.valueOf(currentDate.getDayOfMonth());
        final String dat=yea+"-"+mon+"-"+day;

        db.collection("shop")
                .document(new Auth().getUId())
                .collection("orders")
                .document(new Auth().getUId())
                .collection(dat)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //this is temp list to get from database
                            //orderList.clear();
                            int total=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("Doc Id Are:", document.getId());

                                try {
                                    int temp=Integer.parseInt(document.get("total").toString());
                                    totalList.add(temp);
                                    total=total+Integer.parseInt(document.get("total").toString());
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }
                                //Toast.makeText(getContext(),.((List<String>) document.get("Food_Names")).get(0),Toast.LENGTH_LONG).show();
                            }
                             setTotal(total);       //this call to private method
                             setGraph();

                        } else {
                            Toast.makeText(getApplicationContext(),"Error in getting data",Toast.LENGTH_LONG).show();
                            setTotal(0);        //if error set is as 0



                        }
                    }
                });
    }


    //this method handle with ui part
    private void setTotal(int total)
    {
        String temp=String.valueOf(total);
        String stringTotal="Rs."+temp;
        dailyTotalTextView.setText(stringTotal);

    }

    //this is to set the graph
    private void setGraph()
    {
        dataPointArray=new DataPoint[totalList.size()];

        int i;
        for(i=0;i<totalList.size();i++)
        {
            dataPointArray[i]=new DataPoint(i+1,totalList.get(i));
        }

        try
        {

            GraphView graph = (GraphView) findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPointArray);
            series.setBackgroundColor(R.color.button_blue);
            series.setDataPointsRadius(2);
            graph.addSeries(series);

        }


        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

    }



    //this is to start a new activity
    public void clickHistory(View view)
    {
        Intent inta=new Intent(getApplicationContext(),OrderHistory.class);
//                //inta.putExtra("Demo",obj.getFoodName());
        //inta.putExtra("DocId",docIdList.get(position));
        startActivity(inta);

    }
}
