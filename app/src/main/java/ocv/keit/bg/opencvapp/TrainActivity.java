package ocv.keit.bg.opencvapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class TrainActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);

        ListView DynamicListView = new ListView(this);

        final String[] DynamicListElements = new String[] {
                "Android",
                "PHP",
                "Android Studio",
                "Android Stfsdaio",
                "Android Studfsadio",
                "Android Studfsado",
                "Android Stufasddio",
                "Android Stufdsadio",
                "Android Studidsf",
                "PhpMyAdmin"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (TrainActivity.this, android.R.layout.simple_list_item_1, DynamicListElements);

        DynamicListView.setAdapter(adapter);

        linearLayout.addView(DynamicListView);

        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        DynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Toast.makeText(TrainActivity.this, DynamicListElements[position], Toast.LENGTH_SHORT).show();
            }


        });

    }

}
