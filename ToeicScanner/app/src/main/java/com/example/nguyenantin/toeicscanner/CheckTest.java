package com.example.nguyenantin.toeicscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import android.content.Intent;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class CheckTest extends AppCompatActivity {

    ListView lvResult;
    Button btn_ok;
    ArrayList<Result> dsResult;
    ApdaterResult adapterResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_test);


        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent( CheckTest.this, CameraToiec.class);
                CheckTest.this.startActivity(intentMain);
            }
        });

        lvResult = (ListView) findViewById(R.id.ls_result);
        dsResult=new ArrayList<>();
        adapterResult=new ApdaterResult(CheckTest.this,R.layout.itemt_result,dsResult);
        lvResult.setAdapter(adapterResult);

        danhsachKetQua();
        getResults();
    }

    private void getResults() {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Accept", "application/json"));

        ResultRestClient.get(CheckTest.this, "api/notes", headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        ArrayList<Result> noteArray = new ArrayList<Result>();
                        ApdaterResult noteAdapter = new ApdaterResult(CheckTest.this, noteArray);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                noteAdapter.add(new Result(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        lvResult = (ListView) findViewById(R.id.ls_result);
                        lvResult.setAdapter(noteAdapter);
                    }
                });
    }
    private void danhsachKetQua() {
        dsResult.add(new Result(1,"D","D"));
        dsResult.add(new Result(2,"C","C"));
        dsResult.add(new Result(3,"A","A"));
        dsResult.add(new Result(4,"B","B"));
        dsResult.add(new Result(5,"B","A"));
        dsResult.add(new Result(6,"A","A"));
        dsResult.add(new Result(7,"C","C"));
        dsResult.add(new Result(8,"C","C"));
        dsResult.add(new Result(9,"B","B"));
        dsResult.add(new Result(10,"A","x"));
        dsResult.add(new Result(11,"A","A"));
        dsResult.add(new Result(12,"C","C"));
        dsResult.add(new Result(13,"A","A"));
        dsResult.add(new Result(14,"B","B"));
        dsResult.add(new Result(15,"B","A"));
        dsResult.add(new Result(16,"A","A"));
        dsResult.add(new Result(17,"C","C"));
        dsResult.add(new Result(18,"C","C"));
        dsResult.add(new Result(19,"B","B"));
        dsResult.add(new Result(20,"A","x"));
        dsResult.add(new Result(21,"A","A"));
        dsResult.add(new Result(22,"C","C"));
        dsResult.add(new Result(23,"A","A"));
        dsResult.add(new Result(24,"B","B"));
        dsResult.add(new Result(25,"B","A"));
        dsResult.add(new Result(26,"A","A"));
        dsResult.add(new Result(27,"C","C"));
        dsResult.add(new Result(28,"C","C"));
        dsResult.add(new Result(29,"B","B"));
        dsResult.add(new Result(30,"A","x"));
        dsResult.add(new Result(31,"D","D"));
        dsResult.add(new Result(32,"C","C"));
        dsResult.add(new Result(33,"A","A"));
        dsResult.add(new Result(34,"B","B"));
        dsResult.add(new Result(35,"B","A"));
        dsResult.add(new Result(36,"A","A"));
        dsResult.add(new Result(37,"C","C"));
        dsResult.add(new Result(38,"C","C"));
        dsResult.add(new Result(39,"B","B"));
        dsResult.add(new Result(40,"A","x"));
        dsResult.add(new Result(41,"D","D"));
        dsResult.add(new Result(42,"C","C"));
        dsResult.add(new Result(43,"A","A"));
        dsResult.add(new Result(44,"B","B"));
        dsResult.add(new Result(45,"B","A"));
        dsResult.add(new Result(46,"A","A"));
        dsResult.add(new Result(47,"C","C"));
        dsResult.add(new Result(48,"C","C"));
        dsResult.add(new Result(49,"B","B"));
        dsResult.add(new Result(50,"A","x"));
        dsResult.add(new Result(51,"D","D"));
        dsResult.add(new Result(52,"C","C"));
        dsResult.add(new Result(53,"A","A"));
        dsResult.add(new Result(54,"B","B"));
        dsResult.add(new Result(55,"B","A"));
        dsResult.add(new Result(56,"A","A"));
        dsResult.add(new Result(57,"C","C"));
        dsResult.add(new Result(58,"C","C"));
        dsResult.add(new Result(59,"B","B"));
        dsResult.add(new Result(60,"A","x"));
        dsResult.add(new Result(61,"D","D"));
        dsResult.add(new Result(62,"C","C"));
        dsResult.add(new Result(63,"A","A"));
        dsResult.add(new Result(64,"B","B"));
        dsResult.add(new Result(65,"B","A"));
        dsResult.add(new Result(66,"A","A"));
        dsResult.add(new Result(67,"C","C"));
        dsResult.add(new Result(68,"C","C"));
        dsResult.add(new Result(69,"B","B"));
        dsResult.add(new Result(70,"A","x"));
        dsResult.add(new Result(71,"D","D"));
        dsResult.add(new Result(72,"C","C"));
        dsResult.add(new Result(73,"A","A"));
        dsResult.add(new Result(74,"B","B"));
        dsResult.add(new Result(75,"B","A"));
        dsResult.add(new Result(76,"A","A"));
        dsResult.add(new Result(77,"C","C"));
        dsResult.add(new Result(78,"C","C"));
        dsResult.add(new Result(79,"B","B"));
        dsResult.add(new Result(80,"A","x"));
        dsResult.add(new Result(81,"D","D"));
        dsResult.add(new Result(82,"C","C"));
        dsResult.add(new Result(83,"A","A"));
        dsResult.add(new Result(84,"B","B"));
        dsResult.add(new Result(85,"B","A"));
        dsResult.add(new Result(86,"A","A"));
        dsResult.add(new Result(87,"C","C"));
        dsResult.add(new Result(88,"C","C"));
        dsResult.add(new Result(89,"B","B"));
        dsResult.add(new Result(90,"A","x"));
        dsResult.add(new Result(91,"D","D"));
        dsResult.add(new Result(92,"C","C"));
        dsResult.add(new Result(93,"A","A"));
        dsResult.add(new Result(94,"B","B"));
        dsResult.add(new Result(95,"B","A"));
        dsResult.add(new Result(96,"A","A"));
        dsResult.add(new Result(97,"C","C"));
        dsResult.add(new Result(98,"C","C"));
        dsResult.add(new Result(99,"B","B"));
        dsResult.add(new Result(100,"A","x"));
        dsResult.add(new Result(101,"A","A"));
        dsResult.add(new Result(102,"C","C"));
        dsResult.add(new Result(103,"A","A"));
        dsResult.add(new Result(104,"B","B"));
        dsResult.add(new Result(105,"B","A"));
        dsResult.add(new Result(106,"A","A"));
        dsResult.add(new Result(107,"C","C"));
        dsResult.add(new Result(108,"C","C"));
        dsResult.add(new Result(109,"B","B"));
        dsResult.add(new Result(110,"A","x"));
        dsResult.add(new Result(111,"A","A"));
        dsResult.add(new Result(112,"C","C"));
        dsResult.add(new Result(113,"A","A"));
        dsResult.add(new Result(114,"B","B"));
        dsResult.add(new Result(115,"B","A"));
        dsResult.add(new Result(116,"A","A"));
        dsResult.add(new Result(117,"C","C"));
        dsResult.add(new Result(118,"C","C"));
        dsResult.add(new Result(119,"B","B"));
        dsResult.add(new Result(120,"A","x"));
        dsResult.add(new Result(121,"A","A"));
        dsResult.add(new Result(122,"C","C"));
        dsResult.add(new Result(123,"A","A"));
        dsResult.add(new Result(124,"B","B"));
        dsResult.add(new Result(125,"B","A"));
        dsResult.add(new Result(126,"A","A"));
        dsResult.add(new Result(127,"C","C"));
        dsResult.add(new Result(128,"C","C"));
        dsResult.add(new Result(129,"B","B"));
        dsResult.add(new Result(130,"A","x"));
        dsResult.add(new Result(131,"A","A"));
        dsResult.add(new Result(132,"C","C"));
        dsResult.add(new Result(133,"A","A"));
        dsResult.add(new Result(134,"B","B"));
        dsResult.add(new Result(135,"B","A"));
        dsResult.add(new Result(136,"A","A"));
        dsResult.add(new Result(137,"C","C"));
        dsResult.add(new Result(138,"C","C"));
        dsResult.add(new Result(139,"B","B"));
        dsResult.add(new Result(140,"A","x"));
        dsResult.add(new Result(141,"A","A"));
        dsResult.add(new Result(142,"D","D"));
        dsResult.add(new Result(143,"A","A"));
        dsResult.add(new Result(144,"B","B"));
        dsResult.add(new Result(145,"B","A"));
        dsResult.add(new Result(146,"A","A"));
        dsResult.add(new Result(147,"C","C"));
        dsResult.add(new Result(148,"C","C"));
        dsResult.add(new Result(149,"B","B"));
        dsResult.add(new Result(150,"A","x"));
        dsResult.add(new Result(151,"D","D"));
        dsResult.add(new Result(152,"C","C"));
        dsResult.add(new Result(153,"A","A"));
        dsResult.add(new Result(154,"B","B"));
        dsResult.add(new Result(155,"B","A"));
        dsResult.add(new Result(156,"A","A"));
        dsResult.add(new Result(157,"C","C"));
        dsResult.add(new Result(158,"C","C"));
        dsResult.add(new Result(159,"B","B"));
        dsResult.add(new Result(160,"A","x"));
        dsResult.add(new Result(161,"D","D"));
        dsResult.add(new Result(162,"C","C"));
        dsResult.add(new Result(163,"A","A"));
        dsResult.add(new Result(164,"B","B"));
        dsResult.add(new Result(165,"B","A"));
        dsResult.add(new Result(166,"A","A"));
        dsResult.add(new Result(167,"C","C"));
        dsResult.add(new Result(168,"C","C"));
        dsResult.add(new Result(169,"B","B"));
        dsResult.add(new Result(170,"A","x"));
        dsResult.add(new Result(171,"D","D"));
        dsResult.add(new Result(172,"C","C"));
        dsResult.add(new Result(173,"A","A"));
        dsResult.add(new Result(174,"B","B"));
        dsResult.add(new Result(175,"B","A"));
        dsResult.add(new Result(176,"A","A"));
        dsResult.add(new Result(177,"C","C"));
        dsResult.add(new Result(178,"C","C"));
        dsResult.add(new Result(179,"B","B"));
        dsResult.add(new Result(180,"A","x"));
        dsResult.add(new Result(181,"D","D"));
        dsResult.add(new Result(182,"C","C"));
        dsResult.add(new Result(183,"A","A"));
        dsResult.add(new Result(184,"B","B"));
        dsResult.add(new Result(185,"B","A"));
        dsResult.add(new Result(186,"A","A"));
        dsResult.add(new Result(187,"C","C"));
        dsResult.add(new Result(188,"C","C"));
        dsResult.add(new Result(189,"B","B"));
        dsResult.add(new Result(190,"A","x"));
        dsResult.add(new Result(191,"D","D"));
        dsResult.add(new Result(192,"C","C"));
        dsResult.add(new Result(193,"A","A"));
        dsResult.add(new Result(194,"B","B"));
        dsResult.add(new Result(195,"B","A"));
        dsResult.add(new Result(196,"A","A"));
        dsResult.add(new Result(197,"C","C"));
        dsResult.add(new Result(198,"C","C"));
        dsResult.add(new Result(199,"B","B"));
        dsResult.add(new Result(200,"A","x"));
        adapterResult.notifyDataSetChanged();
    }
}

