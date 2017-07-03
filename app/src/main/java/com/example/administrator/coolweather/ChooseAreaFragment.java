package com.example.administrator.coolweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.coolweather.db.City;
import com.example.administrator.coolweather.db.County;
import com.example.administrator.coolweather.db.Province;
import com.example.administrator.coolweather.util.HttpUtil;
import com.example.administrator.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseAreaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseAreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseAreaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTRY=2;
    private ProgressDialog progressDialog;
    private TextView titletext;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> datalist=new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChooseAreaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseAreaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseAreaFragment newInstance(String param1, String param2) {
        ChooseAreaFragment fragment = new ChooseAreaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.choose_area,container,false);
        titletext=(TextView)view.findViewById(R.id.title_text);
        backButton=(Button)view.findViewById(R.id.back_button);
        listView=(ListView)view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        return view;
        //return inflater.inflate(R.layout.fragment_choose_area, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCountries();
                }else if(currentLevel==LEVEL_COUNTRY){
                    String weatherId=countyList.get(position).getWeatherId();
                    Intent intent=new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVEL_COUNTRY){
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    queryProvince();
                }
            }
        });
        queryProvince();
    }

    private void queryProvince() {
        titletext.setText("china");
        backButton.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            datalist.clear();
            for(Province province:provinceList){
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else{
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryCountries() {
        titletext.setText(selectedCity.getcityName());
        backButton.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            datalist.clear();
            for(County county:countyList){
                datalist.add(county.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTRY;
        }else {
            int provinceCode=selectedProvince.getProvinceCode();
            int cityCode=selectedCity.getcityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"country");
        }
    }

    private void queryCities() {
        titletext.setText(selectedCity.getcityName());
        backButton.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            datalist.clear();
            for(City city:cityList){
                datalist.add(city.getcityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }
    }
    private void queryFromServer(String address,final String type) {
        showProgressDialog();
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"load failure",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                if("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);
                 }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("country".equals(type)){
                    result=Utility.handleCountryResponse(responseText,selectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvince();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("country".equals(type)){
                                queryCountries();
                            }
                        }
                    });
                }
            }
        });

    }

    private void showProgressDialog() {
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("on loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
